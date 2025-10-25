package edu.utn.frsf.isi.dan.gestion.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.utn.frsf.isi.dan.gestion.dao.HabitacionRepository;
import edu.utn.frsf.isi.dan.gestion.dao.TarifaRepository;
import edu.utn.frsf.isi.dan.gestion.model.Habitacion;
import edu.utn.frsf.isi.dan.shared.HabitacionDTO;
import edu.utn.frsf.isi.dan.shared.HabitacionEvent;
import edu.utn.frsf.isi.dan.shared.HotelDTO;
import edu.utn.frsf.isi.dan.shared.TipoEvento;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private TarifaRepository tarifaRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${rabbitmq.exchange:habitacion.exchange}")
    private String exchange;
    @Value("${rabbitmq.routingkey:habitacion.key}")
    private String routingKey;

    @Transactional
    public Habitacion save(Habitacion habitacion) {
        log.info("Habltaicon {} ", habitacion);
        boolean isNew = Objects.isNull(habitacion.getId());
        Habitacion newHabitacion = habitacionRepository.save(habitacion);
        enviarHabitacionJms(newHabitacion, isNew);
        return newHabitacion;
    }

    @Transactional
    public void deleteById(Integer id) {
        enviarHabitacionJms(id);
        habitacionRepository.deleteById(id);
    }

    public Optional<Habitacion> findById(Integer id) {
        return habitacionRepository.findById(id);
    }

    public List<Habitacion> findAll() {
        return habitacionRepository.findAll();
    }

    public void enviarHabitacionJms(Habitacion habitacion,boolean isNew) {
        Double precioNoche = tarifaRepository
                .findFirstByTipoHabitacionIdAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
                        habitacion.getTipoHabitacion().getId(), LocalDate.now(), LocalDate.now())
                .map(tarifa -> tarifa.getPrecioNoche())
                .orElse(0.0);

        HotelDTO hotelDto = HotelDTO.builder()
                .id(habitacion.getHotel().getId())
                .nombre(habitacion.getHotel().getNombre())
                .build();

        HabitacionDTO dto = HabitacionDTO.builder()
                .habitacionId(habitacion.getId().longValue())
                .numero(habitacion.getNumero())
                .tipoHabitacionId(habitacion.getTipoHabitacion().getId())
                .tipoHabitacion(habitacion.getTipoHabitacion().getDescripcion())
                .capacidad(habitacion.getTipoHabitacion().getCapacidad())
                .precioNoche(precioNoche)
                .hotel(hotelDto)
                .build();
        // agregar la tarifa que le corresponde por el tipo de habitacion
        // agregar el hotel
        HabitacionEvent msgEvent = HabitacionEvent.builder()
                .tipoEvento(isNew? TipoEvento.CREAR : TipoEvento.ACTUALIZAR_DATOS)
                .habitacion(dto)
                .build();
        try {
            String msgToSend = objectMapper.writeValueAsString(msgEvent);
            log.debug("[RabbitMQ] Enviando mensaje: {}", msgToSend);    
            rabbitTemplate.convertAndSend(exchange, routingKey, msgToSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarHabitacionJms(Integer id) {  
        HabitacionDTO dto = HabitacionDTO.builder()
                .habitacionId(id.longValue()).build();      
        HabitacionEvent msgEvent = HabitacionEvent.builder()
                .tipoEvento(TipoEvento.ELIMINAR)
                .habitacion(dto)
                .build();
        try {
            String msgToSend = objectMapper.writeValueAsString(msgEvent);
            log.debug("[RabbitMQ] Enviando mensaje: {}", msgToSend);     
            rabbitTemplate.convertAndSend(exchange, routingKey, msgToSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Habitacion> search(Optional<Double> precioMin,
                                   Optional<Double> precioMax,
                                   Optional<Integer> tipoHabitacionId,
                                   Optional<Integer> capacidad) {
        // Obtenemos todas las habitaciones para luego filtrarlas en memoria.
        // Advertencia: Esto puede ser ineficiente con una gran cantidad de datos.
        List<Habitacion> habitaciones = habitacionRepository.findAll();

        // Filtrar por precio
        if (precioMin.isPresent() || precioMax.isPresent()) {
            final double min = precioMin.orElse(Double.MIN_VALUE);
            final double max = precioMax.orElse(Double.MAX_VALUE);
            LocalDate hoy = LocalDate.now();

            habitaciones = habitaciones.stream().filter(h -> {
                // Para cada habitación, verificamos si alguna de sus tarifas vigentes cumple con el rango de precios.
                return h.getTipoHabitacion().getTarifas().stream()
                        .anyMatch(tarifa ->
                                !tarifa.getFechaInicio().isAfter(hoy) && // fechaInicio <= hoy
                                !tarifa.getFechaFin().isBefore(hoy) &&   // fechaFin >= hoy
                                tarifa.getPrecioNoche() >= min &&
                                tarifa.getPrecioNoche() <= max
                        );
            }).toList();
        }

        // Filtrar por tipo de habitación
        if (tipoHabitacionId.isPresent()) {
            habitaciones = habitaciones.stream()
                    .filter(h -> h.getTipoHabitacion().getId().equals(tipoHabitacionId.get())).toList();
        }

        // Filtrar por capacidad
        if (capacidad.isPresent()) {
            habitaciones = habitaciones.stream()
                    .filter(h -> h.getTipoHabitacion().getCapacidad() >= capacidad.get()).toList();
        }

        return habitaciones;
    }
}

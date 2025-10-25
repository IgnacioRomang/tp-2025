package edu.utn.frsf.isi.dan.gestion.service;

import edu.utn.frsf.isi.dan.gestion.dao.HotelRepository;
import edu.utn.frsf.isi.dan.gestion.enums.HotelStatus;
import edu.utn.frsf.isi.dan.gestion.model.Hotel;
import edu.utn.frsf.isi.dan.shared.HabitacionDTO;
import edu.utn.frsf.isi.dan.shared.HabitacionEvent;
import edu.utn.frsf.isi.dan.shared.HotelDTO;
import edu.utn.frsf.isi.dan.shared.HotelEvent;
import edu.utn.frsf.isi.dan.shared.TipoEvento;
import lombok.extern.log4j.Log4j2;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.utn.frsf.isi.dan.gestion.model.AmenityHotel;
import edu.utn.frsf.isi.dan.gestion.model.Habitacion;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange:habitacion.exchange}")
    private String exchange;
    @Value("${rabbitmq.routingkey:habitacion.key}")
    private String routingKey;

    public Hotel save(Hotel hotel) {
        if (hotel.getNombre() == null || hotel.getNombre().isEmpty()) {
            return null;
        }
        if (hotel.getCuit() == null || hotel.getCuit().isEmpty()) {
            return null;
        }
        if (hotel.getDomicilio() == null || hotel.getDomicilio().isEmpty()) {
            return null;
        }
        if (hotel.getLatitud() == null || hotel.getLongitud() == null || hotel.getLatitud() == null
                || hotel.getLongitud() == null) {
            return null;
        }
        hotel.setHotel_status(HotelStatus.ABIERTO);
        this.enviarCreateHotelJms(hotel, true);
        return hotelRepository.save(hotel);
    }

    public void deleteById(Integer id) {
        Hotel hotel = hotelRepository.findById(id).get();
        hotel.setHotel_status(HotelStatus.CERRADO);
        hotel.setStatus_date(new java.util.Date());
        this.enviarCerrarHotel(id);
        // TODO: Implementar envio de evento a reservas serv
    }

    public Hotel update(Integer id, String correo, String telefono, Integer categoria) {
        Hotel hotel = hotelRepository.findById(id).get();
        hotel.setTelefono(telefono);
        hotel.setCategoria(categoria);
        hotel.setCorreoContacto(correo);
        this.enviarCreateHotelJms(hotel, false);
        ;
        return hotelRepository.save(hotel);
    }

    public Optional<Hotel> findById(Integer id) {
        return hotelRepository.findById(id);
    }

    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    public Hotel addAmenities(Integer id, List<AmenityHotel> amenities) {
        return hotelRepository.findById(id).map((hotel) -> {
            amenities.forEach((amenity) -> amenity.setHotel(hotel)

            );
            List<AmenityHotel> amenitysHotel = hotel.getAmenities();
            if (amenitysHotel != null) {
                amenitysHotel.addAll(amenities);
            } else {
                hotel.setAmenities(amenities);
            }
            Hotel updatedHotel = hotelRepository.save(hotel);
            return updatedHotel;
        }).orElse(null);
    }

    public Hotel deleteAmenityHotel(Integer id, Long amenityId) {
        return hotelRepository.findById(id).map((hotel) -> {
            List<AmenityHotel> amenities = hotel.getAmenities();
            amenities.removeIf(amenity -> amenity.getId() == amenityId);
            hotel.setAmenities(amenities);
            return hotelRepository.save(hotel);
        }).orElse(null);
    }

    public List<Hotel> findHotelsByCriteria(String nombre, Integer categoria, List<String> amenities) {
        List<Hotel> hotels = hotelRepository.findAll();

        if (nombre != null && !nombre.isEmpty()) {
            hotels = hotels.stream().filter(hotel -> hotel.getNombre().contains(nombre)).toList();
        }
        if (categoria != null) {
            hotels = hotels.stream().filter(hotel -> hotel.getCategoria() == categoria).toList();
        }
        if (amenities != null && !amenities.isEmpty()) {
            hotels = hotels.stream().filter(hotel -> hotel.getAmenities().stream()
                    .anyMatch(amenity -> amenity.getAmenity().toString().equals(amenities.get(0)))).toList();
        }
        if (hotels.isEmpty()) {
            return null;
        }
        return hotels;
    }

    public void enviarCreateHotelJms(Hotel hotel, boolean isNew) {
        HotelDTO dto = HotelDTO.builder()
                .categoria(hotel.getCategoria())
                .correoContacto(hotel.getCorreoContacto())
                .cuit(hotel.getCuit())
                .domicilio(hotel.getDomicilio())
                .latitud(hotel.getLatitud())
                .longitud(hotel.getLongitud())
                .nombre(hotel.getNombre())
                .telefono(hotel.getTelefono())
                .build();

        HotelEvent msgEvent = HotelEvent.builder()
                .tipoEvento(isNew ? TipoEvento.CREAR : TipoEvento.ACTUALIZAR_DATOS)
                .hotel(dto)
                .build();
        try {
            String msgToSend = objectMapper.writeValueAsString(msgEvent);
            log.debug("[RabbitMQ] Enviando mensaje: {}", msgToSend);
            rabbitTemplate.convertAndSend(exchange, routingKey, msgToSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarDeleteHotelJms(Integer id) {
        HotelDTO dto = HotelDTO.builder()
                .id(id).build();
        HotelEvent msgEvent = HotelEvent.builder()
                .tipoEvento(TipoEvento.ELIMINAR)
                .hotel(dto)
                .build();
        try {
            String msgToSend = objectMapper.writeValueAsString(msgEvent);
            log.debug("[RabbitMQ] Enviando mensaje: {}", msgToSend);
            rabbitTemplate.convertAndSend(exchange, routingKey, msgToSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarCerrarHotel(Integer id) {
        HotelDTO dto = HotelDTO.builder()
                .id(id)
                .statuString(HotelStatus.CERRADO.toString())
                .statusDate(new java.util.Date()).build();
        HotelEvent msgEvent = HotelEvent.builder()
                .tipoEvento(TipoEvento.ACTUALIZAR_DATOS)
                .hotel(dto)
                .build();
        try {
            String msgToSend = objectMapper.writeValueAsString(msgEvent);
            log.debug("[RabbitMQ] Enviando mensaje: {}", msgToSend);
            rabbitTemplate.convertAndSend(exchange, routingKey, msgToSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

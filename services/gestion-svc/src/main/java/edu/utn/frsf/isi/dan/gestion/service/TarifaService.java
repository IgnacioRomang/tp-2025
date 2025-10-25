package edu.utn.frsf.isi.dan.gestion.service;

import edu.utn.frsf.isi.dan.gestion.dao.TarifaRepository;
import edu.utn.frsf.isi.dan.gestion.model.Habitacion;
import edu.utn.frsf.isi.dan.gestion.model.Tarifa;
import edu.utn.frsf.isi.dan.gestion.model.TipoHabitacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TarifaService {
    @Autowired
    private TarifaRepository tarifaRepository;

    @Autowired
    private HabitacionService habitacionService;

    public Tarifa save(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    public void delete(Tarifa tarifaAEliminar) {
        TipoHabitacion tipoHabitacion = tarifaAEliminar.getTipoHabitacion();

        List<Tarifa> todasLasTarifas = tipoHabitacion.getTarifas();
        if (todasLasTarifas.size() <= 1) {
            return;
        }

        LocalDate hoy = LocalDate.now();
        boolean esVigente = !tarifaAEliminar.getFechaInicio().isAfter(hoy) &&
                (tarifaAEliminar.getFechaFin() == null || !tarifaAEliminar.getFechaFin().isBefore(hoy));

        if (esVigente) {
            // Es la tarifa vigente, buscar la anterior y extenderla.
            tarifaRepository
                    .findTopByTipoHabitacionIdAndFechaFinLessThanOrderByFechaFinDesc(tipoHabitacion.getId(),
                            tarifaAEliminar.getFechaInicio())
                    .ifPresent(tarifaAnterior -> {
                        tarifaAnterior.setFechaFin(null); // La hace vigente indefinidamente hacia el futuro
                        tarifaRepository.save(tarifaAnterior);
                        tarifaRepository.delete(tarifaAEliminar);
                    });
        } else {
            // No es la tarifa vigente, se borra directamente.
            tarifaRepository.delete(tarifaAEliminar);
        }
    }

    public Optional<Tarifa> findById(Integer id) {
        return tarifaRepository.findById(id);
    }

    public List<Tarifa> findAll() {
        return tarifaRepository.findAll();
    }

    public Optional<Tarifa> findByHabitacionId(Integer id) {
        return habitacionService.findById(id)
                .flatMap(habitacion -> {
                    Integer tipoHabitacionId = habitacion.getTipoHabitacion().getId();
                    LocalDate hoy = LocalDate.now();
                    return tarifaRepository
                            .findFirstByTipoHabitacionIdAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
                                    tipoHabitacionId, hoy, hoy);
                });
    }

    public Tarifa createNewTarifa(Tarifa tarifa, TipoHabitacion tipoHabitacion) {
        tarifa.setTipoHabitacion(tipoHabitacion);

        // Escenario 1: Es una tarifa especial con fechas de inicio y fin definidas.
        if (tarifa.getFechaInicio() != null && tarifa.getFechaFin() != null) {
            LocalDate inicioEspecial = tarifa.getFechaInicio();
            LocalDate finEspecial = tarifa.getFechaFin();

            // 1. Buscar la tarifa que se ve afectada por la nueva tarifa especial.
            Optional<Tarifa> tarifaAfectadaOpt = tarifaRepository
                    .findFirstByTipoHabitacionIdAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
                            tipoHabitacion.getId(), inicioEspecial, inicioEspecial);

            if (tarifaAfectadaOpt.isPresent()) {
                Tarifa tarifaAfectada = tarifaAfectadaOpt.get();
                Double precioOriginal = tarifaAfectada.getPrecioNoche();

                // 2. Acortar la tarifa afectada para que termine antes de la especial.
                tarifaAfectada.setFechaFin(inicioEspecial.minusDays(1));
                tarifaRepository.save(tarifaAfectada);

                // 3. Guardar la nueva tarifa especial.
                tarifaRepository.save(tarifa);

                // 4. Crear una nueva tarifa que retoma el precio original después de la especial.
                Tarifa tarifaContinuacion = new Tarifa();
                tarifaContinuacion.setTipoHabitacion(tipoHabitacion);
                tarifaContinuacion.setPrecioNoche(precioOriginal);
                tarifaContinuacion.setFechaInicio(finEspecial.plusDays(1));
                tarifaContinuacion.setFechaFin(null); // Vigente indefinidamente.
                return tarifaRepository.save(tarifaContinuacion);
            }
        } else { // Escenario 2: Actualización de precio simple (sin fechas).
            LocalDate hoy = LocalDate.now();
            tarifaRepository.findFirstByTipoHabitacionIdAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
                    tipoHabitacion.getId(), hoy, hoy)
                    .ifPresent(t -> t.setFechaFin(hoy.minusDays(1)));
            tarifa.setFechaInicio(hoy);
            tarifa.setFechaFin(null);
            return tarifaRepository.save(tarifa);
        }
        return null; // No se pudo crear la tarifa
    }
}

package edu.utn.frsf.isi.dan.gestion.dao;

import edu.utn.frsf.isi.dan.gestion.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Integer> {
    Optional<Tarifa> findFirstByTipoHabitacionIdAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
            Integer tipoHabitacionId,
            LocalDate fechaInicio,
            LocalDate fechaFin);

    Optional<Tarifa> findTopByTipoHabitacionIdAndFechaFinLessThanOrderByFechaFinDesc(Integer tipoHabitacionId,
            LocalDate fecha);

    List<Tarifa> findByPrecioNocheBetween(Double precioMin, Double precioMax);
}

package edu.utn.frsf.isi.dan.gestion.dao;

import edu.utn.frsf.isi.dan.gestion.model.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Integer>, JpaSpecificationExecutor<Habitacion> {
}

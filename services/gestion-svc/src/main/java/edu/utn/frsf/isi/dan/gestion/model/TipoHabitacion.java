package edu.utn.frsf.isi.dan.gestion.model;

import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Entity
@Table(name = "tipo_habitacion", schema = "tp_dan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoHabitacion {
    @Id
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer capacidad;
    @OneToMany(mappedBy = "tipoHabitacion", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Tarifa> tarifas;
}

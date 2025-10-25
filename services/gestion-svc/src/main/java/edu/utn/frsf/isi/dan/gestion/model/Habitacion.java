package edu.utn.frsf.isi.dan.gestion.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "habitacion", schema = "tp_dan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Min(1)
    private Integer numero;
    @NotNull
    @Min(1)
    private Integer piso;
    @ManyToOne
    @JoinColumn(name = "id_tipo")
    private TipoHabitacion tipoHabitacion;
    @ManyToOne
    @JoinColumn(name = "id_hotel")
    @JsonBackReference("hotel-habitacion")
    private Hotel hotel;
    
}

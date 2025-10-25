package edu.utn.frsf.isi.dan.gestion.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import edu.utn.frsf.isi.dan.gestion.enums.HotelStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.*;
import edu.utn.frsf.isi.dan.gestion.converter.AmenityConverter;


@Entity
@Table(name = "hotel", schema = "tp_dan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String cuit;
    private String domicilio;
    private Double latitud;
    private Double longitud;
    @Null
    @Enumerated(EnumType.STRING)
    private HotelStatus hotel_status;
    @Null
    private Date status_date;
    private String telefono;
    private String correoContacto;
    private Integer categoria;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "hotel")
    @JsonManagedReference("hotel-habitacion")
    private List<Habitacion> habitaciones;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "hotel")
    @Convert(converter = AmenityConverter.class)
    private List<AmenityHotel> amenities;

}

package edu.utn.frsf.isi.dan.user.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PROPIETARIO")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Propietario extends Usuario {

    @OneToOne
    @JoinColumn(name = "cuenta_bancaria_id")
    @JsonIgnoreProperties("propietario") 
    private CuentaBancaria cuentaBancaria;

    @Column(name = "hotel_id")
    private Long idHotel;
    // solo guardo el id del hotel, no la entidad Hotel 
    // porque se gestiona en otro microservicio
    // entonces la consistencia aquí será eventual
}

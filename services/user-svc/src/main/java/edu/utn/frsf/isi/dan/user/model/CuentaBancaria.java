package edu.utn.frsf.isi.dan.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "cuentas_bancarias")
@Data
@NoArgsConstructor
public class CuentaBancaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero_cuenta")
    private String numeroCuenta;
    private String cbu;
    private String alias;

    @ManyToOne
    @JoinColumn(name = "banco_id")
    private Banco banco;

    @OneToOne(mappedBy = "cuentaBancaria")
    @JsonIgnoreProperties("cuentaBancaria") 
    private Propietario propietario;

}

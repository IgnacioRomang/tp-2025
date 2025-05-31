package edu.utn.frsf.isi.dan.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.utn.frsf.isi.dan.user.model.TarjetaCredito;


public record TarjetaCreditoRecord(
    String numero,
    String nombreTitular,   
    String fechaVencimiento,
    String cvc,  
    Boolean esPrincipal,
    Long huesped,
    Long banco) {

    public TarjetaCredito toTarjetaCredito(){
        TarjetaCredito tarjetaCredito = new TarjetaCredito();
        tarjetaCredito.setCvc(this.cvc);
        tarjetaCredito.setEsPrincipal(this.esPrincipal);
        tarjetaCredito.setFechaVencimiento(this.fechaVencimiento);
        tarjetaCredito.setNombreTitular(this.nombreTitular);
        tarjetaCredito.setNumero(this.numero);
        return tarjetaCredito;
    }
}

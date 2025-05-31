package edu.utn.frsf.isi.dan.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utn.frsf.isi.dan.user.dao.BancoRepository;
import edu.utn.frsf.isi.dan.user.dao.HuespedRepository;
import edu.utn.frsf.isi.dan.user.dao.TarjetaCreditoRepository;
import edu.utn.frsf.isi.dan.user.dto.TarjetaCreditoRecord;
import edu.utn.frsf.isi.dan.user.model.Banco;
import edu.utn.frsf.isi.dan.user.model.Huesped;
import edu.utn.frsf.isi.dan.user.model.TarjetaCredito;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TarjetaService {
    @Autowired
    private BancoRepository bancoRepository;

    @Autowired
    private TarjetaCreditoRepository tarjetaCreditoRepository;

    @Autowired
    private HuespedRepository huespedRepository;

    public Long crearTarjetaCredito(TarjetaCreditoRecord tarjetaCreditoRecord){
        Huesped huesped = huespedRepository.findById(tarjetaCreditoRecord.huesped())
            .orElseThrow(() -> new EntityNotFoundException("Id de usuario no valida"));
        Banco hBanco = bancoRepository.findById(tarjetaCreditoRecord.banco())
            .orElseThrow(() -> new EntityNotFoundException("Banco no encontrado"));

        TarjetaCredito nuevaTarjeta = tarjetaCreditoRecord.toTarjetaCredito();
        nuevaTarjeta.setBanco(hBanco);
        nuevaTarjeta.setHuesped(huesped);

        if(tarjetaCreditoRecord.esPrincipal()){
            huesped.getTarjetaCredito().stream()
                        .filter(TarjetaCredito::getEsPrincipal)
                        .findFirst()
                        .ifPresent(t -> t.setEsPrincipal(false));
        }
        TarjetaCredito tarjetaGuardada = tarjetaCreditoRepository.save(nuevaTarjeta);

        return tarjetaGuardada.getId();
    }
    

    public Long borrarTarjetaCredito(Long id){
        TarjetaCredito tarjetaCredito = tarjetaCreditoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tarjeta no existe"));
        tarjetaCreditoRepository.delete(tarjetaCredito);
        return id;
    }

    public Long seleccionarComoPrincipal(Long id){
        TarjetaCredito tarjetaCredito = tarjetaCreditoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tarjeta no existe"));
        Huesped huesped = tarjetaCredito.getHuesped();

        huesped.getTarjetaCredito().stream()
                    .filter(TarjetaCredito::getEsPrincipal)
                    .findFirst()
                    .ifPresent(t -> t.setEsPrincipal(false));
                    
        tarjetaCredito.setEsPrincipal(true);
        return tarjetaCredito.getId();
    }
    
}

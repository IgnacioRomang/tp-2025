package edu.utn.frsf.isi.dan.user.service;

import edu.utn.frsf.isi.dan.user.dao.BancoRepository;
import edu.utn.frsf.isi.dan.user.dao.CuentaBancariaRepository;
import edu.utn.frsf.isi.dan.user.dao.HuespedRepository;
import edu.utn.frsf.isi.dan.user.dao.TarjetaCreditoRepository;
import edu.utn.frsf.isi.dan.user.dao.UsuarioRepository;
import edu.utn.frsf.isi.dan.user.dto.HuespedRecord;
import edu.utn.frsf.isi.dan.user.dto.PropietarioRecord;
import edu.utn.frsf.isi.dan.user.model.Banco;
import edu.utn.frsf.isi.dan.user.model.CuentaBancaria;
import edu.utn.frsf.isi.dan.user.model.Huesped;
import edu.utn.frsf.isi.dan.user.model.Propietario;
import edu.utn.frsf.isi.dan.user.model.TarjetaCredito;
import edu.utn.frsf.isi.dan.user.model.Usuario;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BancoRepository bancoRepository;

    @Autowired 
    private CuentaBancariaRepository cuentaBancariaRepository;

    @Autowired
    private TarjetaCreditoRepository tarjetaCreditoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HuespedRepository huespedRepository;

    public Huesped crearUsuarioHuesped(HuespedRecord huespedRecord) {
        // Buscar el banco por ID
        Optional<Banco> bancoOptional = bancoRepository.findById(huespedRecord.idBanco());
        if (bancoOptional.isEmpty()) {
            throw new IllegalArgumentException("Banco no encontrado con ID: " + huespedRecord.idBanco());
        }

        Banco banco = bancoOptional.get();

        // Crear y guardar el usuario
        Huesped usuario = huespedRecord.toHuesped();
        usuarioRepository.save(usuario);

        // Crear y guardar la tarjeta de crédito
        
        TarjetaCredito tarjetaCredito = huespedRecord.toTarjetaCredito();
        tarjetaCredito.setHuesped(usuario);
        tarjetaCredito.setBanco(banco);
        TarjetaCredito tarjetaCreditoSaved = tarjetaCreditoRepository.save(tarjetaCredito);
        if (usuario.getTarjetaCredito() == null) {
            usuario.setTarjetaCredito(new ArrayList<>());
        }
        usuario.getTarjetaCredito().add(tarjetaCreditoSaved);
        return usuario;
    }

    public Huesped updateUsuarioHuesped(Long id, HuespedRecord huespedRecord){
        Huesped huesped = huespedRepository.findById(id).orElseThrow(() -> 
                                                new EntityNotFoundException("No se encontró un huésped con ese DNI")
                                            );
        huesped.setNombre(huespedRecord.nombre());
        huesped.setEmail(huespedRecord.email());
        huesped.setTelefono(huespedRecord.telefono());
        huesped.setFechaNacimiento(huespedRecord.fechaNacimiento());
        usuarioRepository.save(huesped);
        return huesped;
    }

    public Long removeUsuarioHuesped(Long id){
        Huesped huesped = huespedRepository.findById(id).orElseThrow(() -> 
                                                new EntityNotFoundException("No se encontró un huésped con ese DNI")
                                            );
        usuarioRepository.delete(huesped);
        return id;
    }

    public Page<Usuario> getUsuarios(String field, String value, Pageable pageable) {
        switch (field.toLowerCase()) {
            case "dni":
                return usuarioRepository.findFirstByDniIgnoreCase(value)
                        .map(usuario -> new PageImpl<>(List.of(usuario), pageable, 1))
                        .orElseThrow(() ->
                                new EntityNotFoundException("No se encontró un huésped con ese DNI")
                        );
            case "nombre":
                Page<Usuario> page = usuarioRepository.findByNombreContainingIgnoreCase(value, pageable);
                if (page.isEmpty()) {
                    throw new EntityNotFoundException("No se encontró un huésped con ese nombre");
                }
                return page;
            default:
                throw new EntityNotFoundException("Campo de búsqueda no soportado: " + field);
        }
    }
    
    public void crearUsuarioPropietario(PropietarioRecord propietarioRecord) {
        Optional<Banco> bancoOptional = bancoRepository.findById(propietarioRecord.cuentaBancaria().idBanco());
        if (bancoOptional.isEmpty()) {
            throw new IllegalArgumentException("Banco no encontrado con ID: " + propietarioRecord.cuentaBancaria().idBanco());
        }

        Banco banco = bancoOptional.get();

        Propietario propietario = propietarioRecord.toPropietario();
        CuentaBancaria cuentaBancaria = propietarioRecord.cuentaBancaria().toCuentaBancaria();
        cuentaBancaria.setBanco(banco);
        propietario.setCuentaBancaria(cuentaBancariaRepository.save(cuentaBancaria));
        usuarioRepository.save(propietario);
    }
}
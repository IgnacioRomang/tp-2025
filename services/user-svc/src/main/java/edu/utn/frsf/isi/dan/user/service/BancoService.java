package edu.utn.frsf.isi.dan.user.service;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.utn.frsf.isi.dan.user.dao.BancoRepository;
import edu.utn.frsf.isi.dan.user.dto.BancoRecord;
import edu.utn.frsf.isi.dan.user.model.Banco;
import edu.utn.frsf.isi.dan.user.model.Usuario;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BancoService {
    @Autowired
    private BancoRepository bancoRepository;

    public Long agregarBanco(BancoRecord bancoRecord){
        Banco sBanco = bancoRepository.save(bancoRecord.toBanco());
        return sBanco.getId();
    }

    public Long eliminarBanco(Long id){
        Banco banco = bancoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Banco no encontrado"));
        bancoRepository.delete(banco);
        return id;
    }

    public Banco actualizarBanco(BancoRecord bancoRecord){
        Banco banco = bancoRepository.findById(bancoRecord.id()).orElseThrow(() -> new EntityNotFoundException("Banco no encontrado"));
        banco.setNombre(bancoRecord.nombre());
        return banco;
    }

    public List<Banco> getAllBancos(){
        return bancoRepository.findAll();
    }

    public Page<Banco> getBancos(String value, Pageable pageable) {
        return bancoRepository.findByNombreContainingIgnoreCase(value, pageable);
    }
}

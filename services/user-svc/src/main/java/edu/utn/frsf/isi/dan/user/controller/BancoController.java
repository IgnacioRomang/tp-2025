package edu.utn.frsf.isi.dan.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.utn.frsf.isi.dan.user.dto.BancoRecord;
import edu.utn.frsf.isi.dan.user.model.Banco;
import edu.utn.frsf.isi.dan.user.service.BancoService;
import edu.utn.frsf.isi.dan.user.service.TarjetaService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Banco Controller", description = "Operaciones para la gestión de bancos")
@RestController
@RequestMapping("/banco")
public class BancoController {
    
    @Autowired
    private BancoService bancoService;

    @PostMapping("/")
    public ResponseEntity<Long> agregarBanco(@RequestBody BancoRecord bancoRecord) {
        Long id = bancoService.agregarBanco(bancoRecord);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> agregarBanco(@RequestParam Long id) {
        Long delId = bancoService.eliminarBanco(id);
        return new ResponseEntity<>(delId, HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<Banco> actualizarBanco(@RequestBody BancoRecord bancoRecord) {
        Banco banco = bancoService.actualizarBanco(bancoRecord);
        return new ResponseEntity<>(banco, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Banco>> getALl() {
        List<Banco> listBancos = bancoService.getAllBancos();
        return new ResponseEntity<>(listBancos, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Page<Banco>> getMethodName(
        @Parameter(description = "nombre por el que buscar")
        @RequestParam String value,
        @ParameterObject Pageable pageable) {

        Page<Banco> bPage = bancoService.getBancos(value, pageable);
        return new ResponseEntity<>(bPage, HttpStatus.OK);
    }
    
    
}

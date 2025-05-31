package edu.utn.frsf.isi.dan.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.utn.frsf.isi.dan.user.dto.TarjetaCreditoRecord;
import edu.utn.frsf.isi.dan.user.service.TarjetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Tarjeta Controller", description = "Operaciones para la gestión de tarjetas")
@RestController
@RequestMapping("/tarjeta")
public class TarjetaController {
    @Autowired
    private TarjetaService tarjetaService;

    @Operation(summary = "Buscar usuario huésped y agrega una nueva tarjeta", 
                description = "Crea tarjeta para el usuario, Si la nueva tarjeta esta definida como principal entonces su antigua tarjeta principal sera actualizada",
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Crea tarjeta correctamente"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en la solicitud"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")}
    )
    @PostMapping("/")
    public ResponseEntity<Long> agregarTarjeta(@PathVariable Long id, @RequestBody TarjetaCreditoRecord tarjetaCreditoRecord) {
        Long tarjetaId = tarjetaService.crearTarjetaCredito(tarjetaCreditoRecord);
        return new ResponseEntity<>(tarjetaId,HttpStatus.OK);
    }

    @Operation(summary = "Borra una tarjeta", 
                description = "Busca una tarjeta por id, y la borra.",
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Crea tarjeta correctamente"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en la solicitud"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")}
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> borrarTarjeta(@PathVariable Long id) {
        Long tarjetaId = tarjetaService.borrarTarjetaCredito(id);
        return new ResponseEntity<>(tarjetaId,HttpStatus.OK);
    }


    @Operation(summary = "Selecciona una tarjeta como tarjeta principal", 
                description = "Busca una tarjeta por id, y la borra.",
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Crea tarjeta correctamente"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en la solicitud"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")}
    )
    @PatchMapping("/seleccionar/{id}")
    public ResponseEntity<Long> seleccionarComoPrincipal(@PathVariable Long id) {
        Long tarjetaId = tarjetaService.seleccionarComoPrincipal(id);
        return new ResponseEntity<>(tarjetaId,HttpStatus.OK);
    }

}

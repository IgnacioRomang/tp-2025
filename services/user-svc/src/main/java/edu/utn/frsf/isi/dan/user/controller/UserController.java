package edu.utn.frsf.isi.dan.user.controller;

import edu.utn.frsf.isi.dan.user.dto.HuespedRecord;
import edu.utn.frsf.isi.dan.user.dto.PropietarioRecord;
import edu.utn.frsf.isi.dan.user.model.Huesped;
import edu.utn.frsf.isi.dan.user.model.Usuario;
import edu.utn.frsf.isi.dan.user.service.UserService;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "User Controller", description = "Operaciones para la gestión de usuarios")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Crear usuario huesped", 
                description = "Crea un nuevo usuario de tipo huesped",
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuario huesped creado exitosamente"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en la solicitud"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")}
    )
    @PostMapping("/huesped")
    public ResponseEntity<Void> crearUsuarioHuesped(@RequestBody HuespedRecord huespedRecord) {
        userService.crearUsuarioHuesped(huespedRecord);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Actualiza un usuario huesped", 
                description = "Crea un nuevo usuario de tipo huesped",
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Actualizó huesped creado anteriormente"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en la solicitud"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")}
    )
    @PutMapping("/huesped/{id}")
    public ResponseEntity<Huesped> updateUsuarioHuesped(@PathVariable Long id , @RequestBody HuespedRecord huespedRecord){
        Huesped huesped = userService.updateUsuarioHuesped(id,huespedRecord);
        return new ResponseEntity<>(huesped,HttpStatus.OK);
    }

    @Operation(summary = "Elimina un usuario huesped", 
                description = "Crea un nuevo usuario de tipo huesped",
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Eliminó huesped creado anteriormente"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en la solicitud"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")}
    )
    @DeleteMapping("/huesped/{id}")
    public ResponseEntity<Long> removeUsuarioHuesped(@PathVariable Long id){
        Long id_remove = userService.removeUsuarioHuesped(id);
        return new ResponseEntity<>(id_remove,HttpStatus.OK);
    }


    @Operation(summary = "Buscar usuario huésped por campo y valor", 
                description = "Este endpoint permite buscar un usuario huésped dinámicamente utilizando el nombre del campo y su valor correspondiente. Por ejemplo: field=dni&value=435343434",
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Actualiza huesped creado anteriormente"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en la solicitud"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")}
    )
    @GetMapping("/huesped")
    public ResponseEntity<Page<Usuario>> getUsuarios(
        @Parameter(description = "Nombre del campo por el cual se desea buscar (ej: dni)")
        @RequestParam String field,

        @Parameter(description = "Valor que se desea buscar para el campo indicado")
        @RequestParam String value,

        @ParameterObject Pageable pageable
    ) {
        Page<Usuario> usuarios = userService.getUsuarios(field, value, pageable);
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }



    @Operation(summary = "Crear usuario propietario", description = "Crea un nuevo usuario de tipo propietario")
    @PostMapping("/propietario")
    public ResponseEntity<Void> crearUsuarioPropietario(@RequestBody @Valid PropietarioRecord propietarioRecord) {
        userService.crearUsuarioPropietario(propietarioRecord);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
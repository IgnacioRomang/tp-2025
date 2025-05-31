package edu.utn.frsf.isi.dan.user.dto;

import edu.utn.frsf.isi.dan.user.model.Banco;
import jakarta.validation.constraints.NotBlank;

public record BancoRecord(@NotBlank(message = "El nombre de banco no puede estar vacío") String nombre, Long id) {
    public Banco toBanco(){
        Banco banco = new Banco();
        banco.setNombre(this.nombre);
        banco.setId(this.id);
        return banco;
    }
}
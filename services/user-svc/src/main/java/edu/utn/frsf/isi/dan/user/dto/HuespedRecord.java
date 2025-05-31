package edu.utn.frsf.isi.dan.user.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.utn.frsf.isi.dan.user.model.Banco;
import edu.utn.frsf.isi.dan.user.model.Huesped;
import edu.utn.frsf.isi.dan.user.model.TarjetaCredito;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record HuespedRecord(
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    String nombre,
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser un email válido") 
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    String email,
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\+?[0-9]{7,15}", message = "El teléfono debe contener solo dígitos y puede incluir el prefijo internacional")
    String telefono,
    @Past(message = "La fecha de nacimiento debe ser una fecha en el pasado")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    LocalDate fechaNacimiento,
    @Pattern(regexp = "\\d{7,10}", message = "El DNI debe contener entre 7 y 10 dígitos")
    String dni,
    @Pattern(regexp = "\\d{16}", message = "El número de la tarjeta debe tener 16 dígitos")
    String numeroCC,
    @Size(min = 1, max = 100, message = "El nombre del titular debe tener entre 2 y 100 caracteres")
    String nombreTitular,
    @Future(message = "La fecha de vencimiento debe ser una fecha en el futuro")
    String fechaVencimientoCC,
    @Pattern(regexp = "\\d{3,4}", message = "El CVC debe tener 3 o 4 dígitos")
    String cvcCC,
    @NotNull(message = "Debe indicar si esta tarjeta es la principal")
    Boolean esPrincipalCC,
    @NotNull(message = "El ID del banco es obligatorio")
    @Min(value = 1, message = "El ID del banco debe ser un número positivo")
    Long idBanco
) {

    public Huesped toHuesped() {
        Huesped huesped = new Huesped();
        huesped.setNombre(this.nombre);
        huesped.setEmail(this.email);
        huesped.setDni(this.dni);
        huesped.setTelefono(this.telefono);
        huesped.setFechaNacimiento(this.fechaNacimiento);
        huesped.setTarjetaCredito(new ArrayList<>(List.of(
            TarjetaCredito.builder()
            .numero(this.numeroCC)
            .nombreTitular(this.nombreTitular)
            .fechaVencimiento(this.fechaVencimientoCC)
            .cvc(this.cvcCC)
            .esPrincipal(this.esPrincipalCC)
            .banco(Banco.builder()
                .id(this.idBanco)
                .build()).build()
        )));
        return huesped;
    }

    public TarjetaCredito toTarjetaCredito() {
        return TarjetaCredito.builder()
            .numero(this.numeroCC)
            .fechaVencimiento(this.fechaVencimientoCC)
            .nombreTitular(this.nombreTitular)
            .cvc(this.cvcCC)
            .esPrincipal(this.esPrincipalCC)
            .build();
    }

}
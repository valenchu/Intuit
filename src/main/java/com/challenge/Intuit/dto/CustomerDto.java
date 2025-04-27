package com.challenge.Intuit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    @NotBlank(message = "Name is required")
    @Column(name = "nombre")
    private String nombre;

    @NotBlank(message = "Last name is required")
    @Column(name = "apellido")
    private String apellido;

    @Column(name = "fecha_nacimiento")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "CUIT is required")
    @Column(name = "cuit", unique = true)
    private String cuit;

    @Column(name = "domicilio")
    private String domicilio;

    @NotBlank(message = "Mobile phone is required")
    @Column(name = "telefono_celular")
    private String telefonoCelular;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(name = "email", unique = true)
    private String email;
}

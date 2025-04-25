package com.challenge.Intuit.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "cuit", unique = true)
    private String cuit;

    @Column(name = "domicilio")
    private String domicilio;

    @Column(name = "telefono_celular")
    private String telefonoCelular;

    @Column(name = "email", unique = true)
    private String email;
}

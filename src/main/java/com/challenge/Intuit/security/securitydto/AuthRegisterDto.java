package com.challenge.Intuit.security.securitydto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterDto {
    private String email;
    private String password;
    private String rol;
}

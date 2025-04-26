package com.challenge.Intuit.security.securitydto;

public record AuthRegisterDto(
		String email,
		String password,
		String rol
) {
}

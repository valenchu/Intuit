package com.challenge.Intuit.service;

import com.challenge.Intuit.entity.User;
import com.challenge.Intuit.security.securitydto.AuthLoginDto;
import com.challenge.Intuit.security.securitydto.AuthRegisterDto;
import com.challenge.Intuit.security.securitydto.TokenResponseDto;

import java.util.Optional;

public interface AuthService {
	Optional<User> findByEmail(String email);

	TokenResponseDto createToken(AuthRegisterDto authRegisterDto);

	TokenResponseDto authenticate(AuthLoginDto authLoginDto);

	TokenResponseDto refresToken(String string);
}

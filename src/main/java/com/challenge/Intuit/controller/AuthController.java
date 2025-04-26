package com.challenge.Intuit.controller;

import com.challenge.Intuit.security.securitydto.AuthLoginDto;
import com.challenge.Intuit.security.securitydto.AuthRegisterDto;
import com.challenge.Intuit.security.securitydto.TokenResponseDto;
import com.challenge.Intuit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
	private final AuthService authService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> registerUser(@RequestBody final AuthRegisterDto userRegister) {
		if (authService.findByEmail(userRegister.email()).isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The email is already registered.");
		}
		TokenResponseDto token = authService.createToken(userRegister);

		return ResponseEntity.status(HttpStatus.CREATED).body(token);
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody final AuthLoginDto user) {
		TokenResponseDto token = authService.authenticate(user);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refresToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader ) {
		TokenResponseDto token = authService.refresToken(authHeader);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
	}

	@PostMapping("/testLoki")
	public void testLoki() {
		LOG.info("PROBANDO LA CONFIGlOKI");
	}
}

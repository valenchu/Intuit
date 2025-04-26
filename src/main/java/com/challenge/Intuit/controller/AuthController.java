package com.challenge.Intuit.controller;

import com.challenge.Intuit.entity.User;
import com.challenge.Intuit.security.securitydto.AuthLoginDto;
import com.challenge.Intuit.security.securitydto.AuthRegisterDto;
import com.challenge.Intuit.security.securitydto.TokenResponseDto;
import com.challenge.Intuit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userServiceImpl;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> registerUser(@RequestBody final AuthRegisterDto userRegister) {
		if (userServiceImpl.findByEmail(userRegister.getEmail()).isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El email ya está registrado.");
		}
		userRegister.setPassword(passwordEncoder.encode(userRegister.getPassword()));
//		TokenResponseDto token = userServiceImpl.createToken(userRegister);

		return ResponseEntity.status(HttpStatus.CREATED).body("token");
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody AuthLoginDto user) {


		return null;
	}

	@PostMapping("/testLoki")
	public void testLoki() {
		LOG.info("PROBANDO LA CONFIGlOKI");
	}
}

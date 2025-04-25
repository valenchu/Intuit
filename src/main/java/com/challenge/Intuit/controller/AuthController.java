package com.challenge.Intuit.controller;

import com.challenge.Intuit.entity.User;
import com.challenge.Intuit.repository.UserRepository;
import com.challenge.Intuit.security.securitydto.AuthRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private UserRepository userRepository; // Para guardar nuevos usuarios

//	@Autowired
//	private PasswordEncoder passwordEncoder; // Para cifrar contraseñas
//
//	@Autowired
//	private AuthenticationManager authenticationManager; // Para autenticar usuarios

	// Necesitarás un servicio/utilidad para generar tokens (descomentar cuando lo tengas)
	// @Autowired
	// private TokenService tokenService;

	/**
	 * Endpoint para registrar un nuevo usuario.
	 * Recibe email y password en el cuerpo de la solicitud.
	 * @param user El objeto User con email y password.
	 * @return El usuario creado (sin la contraseña) o un error si el email ya existe.
	 */
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED) // Retorna 201 Created en caso de éxito
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		// Verifica si el email ya existe
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El email ya está registrado.");
		}

		// Cifra la contraseña antes de guardarla (¡CRUCIAL para la seguridad!)
		//user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Guarda el nuevo usuario en la base de datos
		User savedUser = userRepository.save(user);

		// En una aplicación real, no deberías retornar la contraseña cifrada.
		// Podrías retornar un DTO con solo el email y el ID.
		savedUser.setPassword(null); // Elimina la contraseña antes de retornar

		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}

	/**
	 * Endpoint para autenticar un usuario y obtener un token.
	 * Recibe email y password en el cuerpo de la solicitud (usando un DTO AuthRequest).
	 * @param authRequest Objeto con email y password para autenticación.
	 * @return Un objeto AuthResponse con el token si la autenticación es exitosa.
	 */
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody AuthRequestDto authRequest) {
		try {
			// Crea un objeto de autenticación con el email y password proporcionados.
//			Authentication authentication = authenticationManager.authenticate(
//					new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
//			);

			// Establece la autenticación en el contexto de seguridad.
//			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Genera el token (necesitas implementar esta lógica en un servicio de token)
			// String token = tokenService.generateToken(authentication); // Descomentar cuando tengas el servicio

			// Retorna el token en la respuesta.
			// return ResponseEntity.ok(new AuthResponse(token)); // Descomentar y ajustar con tu clase AuthResponse

			// Placeholder response until token generation is implemented
			return ResponseEntity.ok("Autenticación exitosa. Aquí iría el token."); // Respuesta temporal
		} catch (Exception e) {
			// Maneja errores de autenticación (ej. credenciales incorrectas)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas.");
		}
	}
	// Nota: Necesitarás crear las clases AuthRequest y AuthResponse (DTOs).
	// AuthRequest: Contendrá campos para email y password.
	// AuthResponse: Contendrá un campo para el token.
	// También necesitarás implementar la lógica de generación de tokens (ej. JWT) en un servicio separado.
	// Además, necesitarás un filtro para interceptar las solicitudes y validar el token en la cabecera.
}

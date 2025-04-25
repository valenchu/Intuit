package com.challenge.Intuit.configuration;

public class SecurityConfig {
/*	// Inyecta el filtro de token que crearemos (descomentar cuando lo tengas)
	// private final JwtRequestFilter jwtRequestFilter;

	// @Autowired // Inyecta el filtro (descomentar cuando lo tengas)
	// public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
	//     this.jwtRequestFilter = jwtRequestFilter;
	// }

	// Define el Bean para el codificador de contraseñas.
	// BCrypt es un algoritmo de hashing fuerte y recomendado.
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Define el Bean para el AuthenticationManager.
	// Es necesario para autenticar usuarios programáticamente (ej. en el controlador de login).
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	// Configura la cadena de filtros de seguridad HTTP.
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// Deshabilita la protección CSRF (común para APIs REST sin estado)
				.csrf(AbstractHttpConfigurer::disable)
				// Configura las reglas de autorización para las solicitudes HTTP
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								// Permite el acceso sin autenticación a los endpoints de autenticación y Swagger UI
								.requestMatchers("/api/auth/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
								// Todas las demás solicitudes requieren autenticación
								.anyRequest().authenticated()
				)
				// Configura la gestión de sesiones como sin estado (STATELESS).
				// Esto es crucial para la autenticación basada en tokens, ya que no se usan sesiones HTTP.
				.sessionManagement(sessionManagement ->
						sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);
		// Agrega un filtro personalizado (el filtro de token) antes del filtro estándar de autenticación de nombre de usuario/contraseña.
		// Este filtro interceptará las solicitudes para validar el token en la cabecera.
		// http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Descomentar cuando tengas el filtro

		return http.build();
	}

	// Nota: Necesitarás crear una implementación de UserDetailsService si no usas la configuración por defecto
	// o si necesitas cargar detalles de usuario de una fuente específica (como tu base de datos).
	// Spring Boot a menudo puede inferir esto si tienes un UserRepository y una entidad User.*/
}

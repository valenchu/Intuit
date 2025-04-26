package com.challenge.Intuit.configuration;

import com.challenge.Intuit.entity.Token;
import com.challenge.Intuit.entity.User;
import com.challenge.Intuit.repository.TokenRepository;
import com.challenge.Intuit.repository.UserRepository;
import com.challenge.Intuit.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Objects;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService detailsService() {
		return email -> {
			final User user = userRepository.findByEmail(email)
											.orElseThrow(() -> new UsernameNotFoundException("User not found"));

				return org.springframework.security.core.userdetails.User.builder()
																		 .username(user.getEmail())
																		 .password(user.getPassword())
																		 .build();

		};
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(this.detailsService());
		authProvider.setPasswordEncoder(this.passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	// Configura la cadena de filtros de seguridad HTTP.
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(
					authorizeRequests -> authorizeRequests
							.requestMatchers("/api/auth/**",
									"/swagger-ui.html",
									"/swagger-ui/**", "/v3/**",
									"/h2-console/**",
									"/swagger-resources/**",
									"/configuration/security",
									"/configuration/ui",
									"/webjars/**")
							.permitAll()
							.anyRequest()
							.authenticated())
			.formLogin(AbstractHttpConfigurer::disable)
			.headers(httpSecurityHeadersConfigurer -> {
				httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
			})
			.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authenticationProvider(this.authenticationProvider())
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.logout(logout -> {
				logout.logoutUrl("auth/logout")
					  .addLogoutHandler((request, response, authentication) -> {
						  final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
						  logout(authHeader);
					  })
					  .logoutSuccessHandler((request, response, authentication) ->{
						  SecurityContextHolder.clearContext();
					  });
			});
		return http.build();
	}

	private void logout(final String token){
		if(Objects.isNull(token) || !token.startsWith("Bearer ")){
			throw new IllegalArgumentException("Invalid token");
		}

		final String jwtToken = token.substring(7);
		final Token foundToken = tokenRepository.findByToken(jwtToken)
												.orElseThrow(() -> new IllegalArgumentException("Invalid token"));
		foundToken.setExpired(true);
		foundToken.setRevoke(true);
		tokenRepository.save(foundToken);
	}

}

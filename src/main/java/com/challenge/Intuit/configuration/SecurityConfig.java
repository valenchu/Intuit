package com.challenge.Intuit.configuration;

import com.challenge.Intuit.entity.User;
import com.challenge.Intuit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserRepository userRepository;

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
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//		http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).
		http.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(authorizeRequests ->
					authorizeRequests
							.requestMatchers("/api/auth/**", "/swagger-ui.html", "/swagger-ui/**",
									"/v3/**", "/h2-console/**", "/swagger-resources/**", "/configuration/security",
									"/configuration/ui", "/webjars/**").permitAll()
							.anyRequest().authenticated()
			).formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement(sessionManagement ->
					sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			);
		return http.build();
	}

}

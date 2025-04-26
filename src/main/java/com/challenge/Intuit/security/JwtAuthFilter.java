package com.challenge.Intuit.security;

import com.challenge.Intuit.entity.Token;
import com.challenge.Intuit.entity.User;
import com.challenge.Intuit.repository.TokenRepository;
import com.challenge.Intuit.repository.UserRepository;
import com.challenge.Intuit.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final TokenRepository tokenRepository;
	private final UserRepository userRepository;
	private final AntPathMatcher pathMatcher = new AntPathMatcher();
	private final List<String> publicPaths = Arrays.asList(
			"/api/auth/**",
			"/swagger-ui.html",
			"/swagger-ui/**",
			"/v3/**",
			"/h2-console/**",
			"/swagger-resources/**",
			"/configuration/security",
			"/configuration/ui",
			"/webjars/**"
	);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		final String pathServlet = request.getServletPath();
		for (String publicPathPattern : publicPaths) {
			if (pathMatcher.match(publicPathPattern, pathServlet)) {
				filterChain.doFilter(request, response);
				return;
			}
		}

		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		final String jwtToken = authHeader.substring(7);
		final String userEmail = jwtService.extractEmail(jwtToken);

		if(Objects.isNull(userEmail) || SecurityContextHolder.getContext().getAuthentication() != null){
			filterChain.doFilter(request, response);
			return;
		}

		final Token token = tokenRepository.findByToken(jwtToken)
										   .orElse(null);
		if(Objects.isNull(token) || token.isExpired() || token.isRevoke()){
			filterChain.doFilter(request, response);
			return;
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
		final Optional<User> user = userRepository.findByEmail(userDetails.getUsername());

		if(user.isEmpty()){
			filterChain.doFilter(request,response);
			return;
		}

		final boolean tokenValid = jwtService.isValidToken(jwtToken, user.get());

		if(!tokenValid){
			return;
		}

		final var authToken = new UsernamePasswordAuthenticationToken(
				userDetails,
				null,
				userDetails.getAuthorities()
		);

		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);

	}
}

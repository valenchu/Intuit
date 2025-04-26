package com.challenge.Intuit.service;

import com.challenge.Intuit.entity.Token;
import com.challenge.Intuit.entity.User;
import com.challenge.Intuit.repository.TokenRepository;
import com.challenge.Intuit.repository.UserRepository;
import com.challenge.Intuit.security.securitydto.AuthLoginDto;
import com.challenge.Intuit.security.securitydto.AuthRegisterDto;
import com.challenge.Intuit.security.securitydto.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;


	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public TokenResponseDto createToken(AuthRegisterDto authRegisterDto) {
		var user = User.builder()
					   .email(authRegisterDto.email())
					   .rol(authRegisterDto.rol())
					   .password(passwordEncoder.encode(authRegisterDto.password()))
					   .build();
		var saveUser = userRepository.save(user);
		var jwtToken = jwtService.generatedToken(user);
		var refreshToken = jwtService.generatedRefreshToken(user);
		saveUserToken(saveUser, jwtToken);
		return new TokenResponseDto(jwtToken, refreshToken);
	}

	private void saveUserToken(User user, String jwtToken) {
		var tokenToSave = Token.builder()
							   .user(user)
							   .token(jwtToken)
							   .tokenType(Token.TokenType.BEARER)
							   .expired(false)
							   .revoke(false)
							   .build();
		tokenRepository.save(tokenToSave);
	}

	@Override
	public TokenResponseDto authenticate(AuthLoginDto authLoginDto) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authLoginDto.email(),
				authLoginDto.password()));
		var user = userRepository.findByEmail(authLoginDto.email())
								 .orElseThrow(() -> new RuntimeException(
										 "Error to find email in UserServiceImpl authenticate()"));
		var jwtToken = jwtService.generatedToken(user);
		var refreshToken = jwtService.generatedRefreshToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);
		return new TokenResponseDto(jwtToken, refreshToken);
	}

	private void revokeAllUserTokens(final User user) {
		final List<Token> validateUserTokens =
				tokenRepository.findAllValidIsFalseOrRevokedIsFalseByUserId(user.getId());
		if (!validateUserTokens.isEmpty()) {
			for (final Token token : validateUserTokens) {
				token.setExpired(true);
				token.setRevoke(true);
			}
			tokenRepository.saveAll(validateUserTokens);
		}
	}

	@Override
	public TokenResponseDto refresToken(String string) {
		return null;
	}
}

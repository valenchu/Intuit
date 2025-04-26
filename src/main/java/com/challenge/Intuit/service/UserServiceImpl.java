package com.challenge.Intuit.service;

import com.challenge.Intuit.entity.Token;
import com.challenge.Intuit.entity.User;
import com.challenge.Intuit.repository.TokenRepository;
import com.challenge.Intuit.repository.UserRepository;
import com.challenge.Intuit.security.securitydto.AuthLoginDto;
import com.challenge.Intuit.security.securitydto.AuthRegisterDto;
import com.challenge.Intuit.security.securitydto.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final JwtService jwtService;


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
		return null;
	}

	@Override
	public TokenResponseDto refresToken(String string) {
		return null;
	}
}

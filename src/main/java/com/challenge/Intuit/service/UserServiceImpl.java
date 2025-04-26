package com.challenge.Intuit.service;

import com.challenge.Intuit.entity.User;
import com.challenge.Intuit.repository.UserRepository;
import com.challenge.Intuit.security.securitydto.AuthRegisterDto;
import com.challenge.Intuit.security.securitydto.TokenResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;


	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public TokenResponseDto createToken(AuthRegisterDto user) {
		return null;
	}
}

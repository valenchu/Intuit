package com.challenge.Intuit.service;

import com.challenge.Intuit.entity.User;
import com.challenge.Intuit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepositor;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = this.userRepositor.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRol().toUpperCase())));

	}
}

package com.challenge.Intuit.repository;

import com.challenge.Intuit.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	List<Token> findAllValidIsFalseOrRevokedIsFalseByUserId(Long userId);

	Optional<Token> findByToken(String jwtToken);
}

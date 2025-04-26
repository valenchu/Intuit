package com.challenge.Intuit.repository;

import com.challenge.Intuit.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	List<Token> findAllValidIsFalseOrRevokedIsFalseByUserId(Long userId);
}

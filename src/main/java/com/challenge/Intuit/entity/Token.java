package com.challenge.Intuit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String token;

	@Enumerated(EnumType.STRING)
	private TokenType tokenType = TokenType.BEARER;

	private boolean revoke;

	private boolean expired;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public enum TokenType {
		BEARER
	}
}

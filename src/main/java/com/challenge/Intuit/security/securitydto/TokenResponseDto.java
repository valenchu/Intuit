package com.challenge.Intuit.security.securitydto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponseDto(
		@JsonProperty("access_token")
		String accessToken,
		@JsonProperty("refresh_token")
		String refreshToken
) {

}

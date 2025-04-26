package com.challenge.Intuit.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
//	private static final String SECURITY_SCHEME_NAME = "bearerAuth";
//
//	@Bean
//	public OpenAPI customOpenAPI() {
//		return new OpenAPI()
//				.info(new Info()
//						.title("Customer Management API")
//						.version("1.0")
//						.description("REST API for customer management with Spring Boot and JWT.")
//				).components(new Components()
//						.addSecuritySchemes(SECURITY_SCHEME_NAME,
//								new SecurityScheme()
//										.name(SECURITY_SCHEME_NAME)
//										.type(SecurityScheme.Type.HTTP)
//										.scheme("bearer")
//										.bearerFormat("JWT")
//										.description("Enter the JWT token with the Bearer prefix. Example: 'Bearer " +
//												"abcde12345'")
//						)
//				).addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
//
//	}
}

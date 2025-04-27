package com.challenge.Intuit.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.media.Schema;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
@OpenAPIDefinition(
		info = @Info(
				title = "API Documentation",
				version = "1.0",
				description = "REST API for customer management with Spring Boot and JWT"
		)
)
public class OpenApiConfig {
	private static final String SECURITY_SCHEME_NAME = "bearerAuth";

	@Bean
	public OpenApiCustomizer dynamicDateExampleCustomizer() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String currentDate = LocalDate.now().format(formatter);
		return openApi -> {
				// --- Lógica para añadir la configuración de seguridad (proveniente de tu customOpenAPI anterior) ---
				Components components = openApi.getComponents();
				if (components == null) {
					components = new Components();
					openApi.setComponents(components);
				}
				components.addSecuritySchemes(SECURITY_SCHEME_NAME,
						new SecurityScheme()
								.name(SECURITY_SCHEME_NAME)
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")
								.description("Enter the JWT token with the Bearer prefix. Example: 'Bearer " +
										"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
										"eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ." +
										"SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c'")
				);
				openApi.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
				openApi.getComponents().getSchemas().forEach((schemaName, schema) -> {
				if (schemaName.equals("CustomerDto") || schemaName.equals("Customer")) {
					Schema<?> fechaNacimientoSchema = (Schema<?>) schema.getProperties().get("fechaNacimiento");
					fechaNacimientoSchema.setExample(currentDate);
					fechaNacimientoSchema.pattern(currentDate);
					fechaNacimientoSchema.set$schema(currentDate);
					fechaNacimientoSchema.setFormat(currentDate);
				}
			});
		};
	}
}

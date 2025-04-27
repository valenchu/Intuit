package com.challenge.Intuit.utilValidate;

import com.challenge.Intuit.dto.CustomerDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UtilValidate {
	private static final DateTimeFormatter LATIN_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final int[] COEFICIENTES = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};

	/**
	 * Validates a date string in "dd/MM/yyyy" format and converts it to LocalDate.
	 */
	public static LocalDate parseLatinDateToLocalDate(String dateString) {

		if (dateString == null || dateString.trim().isEmpty()) {
			throw new IllegalArgumentException("Date string cannot be null or empty.");
		}
		try {
			return LocalDate.parse(dateString, LATIN_DATE_FORMATTER);
		} catch (DateTimeParseException e) {
			throw new DateTimeParseException(
					"Invalid date format or value. Expected dd/MM/yyyy. Input: " + dateString,
					e.getParsedString(),
					e.getErrorIndex(),
					e
			);
		}
	}


	public static boolean isValid(String cuit) {
		if (cuit == null || cuit.isEmpty()) return false;

		// Validar formato: 11 dígitos numéricos
		if (!cuit.matches("^\\d{11}$")) return false;

		// Convertir a array de dígitos
		int[] digitos = new int[11];
		try {
			for (int i = 0; i < 11; i++) {
				digitos[i] = Integer.parseInt(cuit.substring(i, i + 1));
			}
		} catch (NumberFormatException e) {
			return false;
		}

		// Calcular dígito verificador
		int suma = 0;
		for (int i = 0; i < 10; i++) {
			suma += digitos[i] * COEFICIENTES[i];
		}

		int resto = suma % 11;
		int digitoVerificadorCalculado = 11 - resto;

		if (digitoVerificadorCalculado == 11) digitoVerificadorCalculado = 0;
		if (digitoVerificadorCalculado == 10) return false; // CUIT inválido

		return digitoVerificadorCalculado == digitos[10];
	}


	public static boolean validateData(CustomerDto customerDto) {
		parseLatinDateToLocalDate(customerDto.getFechaNacimiento().toString());
		if (!isValid(customerDto.getCuit())) {
			throw new IllegalArgumentException("Invalid CUIT");
		}
		return true;
	}
}

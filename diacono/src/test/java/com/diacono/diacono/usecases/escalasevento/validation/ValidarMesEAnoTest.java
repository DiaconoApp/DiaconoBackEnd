package com.diacono.diacono.usecases.escalasevento.validation;

import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidarMesEAnoTest {

	private final ValidarMesEAno validator = new ValidarMesEAno();

	@Test
	void deveAceitarMesEAnoValidos() {
		validator.validarMesEAno(5, 2026);
	}

	@Test
	void deveLancarExcecaoQuandoMesForInvalido() {
		FieldInvalidException ex = assertThrows(FieldInvalidException.class, () -> validator.validarMesEAno(0, 2026));

		assertEquals("O mês precisa estar entre 1 e 12", ex.getMessage());
	}

	@Test
	void deveLancarExcecaoQuandoAnoForInvalido() {
		FieldInvalidException ex = assertThrows(FieldInvalidException.class, () -> validator.validarMesEAno(5, 0));

		assertEquals("O ano precisa ser maior que 0", ex.getMessage());
	}
}


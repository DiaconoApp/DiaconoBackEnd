package com.diacono.diacono.applications.dtos.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
		@NotBlank(message = "Email é obrigatório")
		@Email(message = "Email Inválido. ")
		@Size(max = 255, message = "Email deve ter no máximo {max} caracteres")
		String email,

		@NotBlank(message = "Senha é obrigatória")
		@Size(min = 1, max = 72, message = "Senha deve ter entre {min} e {max} caracteres")
		String senha
) {
}

package com.diacono.diacono.presentation.error;

import com.diacono.diacono.presentation.exception.RestErrorMessage;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                content = @Content(mediaType = APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = RestErrorMessage.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida (dados incorretos, ausentes, ou conflito de validação)",
                content = @Content(mediaType = APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = RestErrorMessage.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor (falha não esperada)")
})
public @interface ApiErrorsComuns {
}
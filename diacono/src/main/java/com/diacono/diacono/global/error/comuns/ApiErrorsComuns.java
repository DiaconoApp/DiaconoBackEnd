package com.diacono.diacono.global.error.comuns;

import com.diacono.diacono.global.error.RestErrorMessage;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;
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
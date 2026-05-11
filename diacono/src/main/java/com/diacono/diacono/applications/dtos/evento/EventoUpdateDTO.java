package com.diacono.diacono.applications.dtos.evento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EventoUpdateDTO(

        List<UUID> fkMinisterios,

        @Valid
        EnderecoEventoDTO endereco,

        @Size(max = 255, message = "O nome do evento deve conter no máximo {max} caracteres.")
        String nome,

        @Size(max = 2000, message = "A descrição do evento deve conter no máximo {max} caracteres.")
        String descricao,

        @Size(max = 255, message = "O público alvo deve conter no máximo {max} caracteres.")
        String publicoAlvo,

        @FutureOrPresent(message = "Não é possível cadastrar eventos com datas passadas.")
        LocalDateTime dataHoraInicio,

        @FutureOrPresent(message = "Não é possível cadastrar eventos com datas passadas.")
        LocalDateTime dataHoraFim,

        @DecimalMin(value = "0.0", inclusive = true, message = "O custo não pode ser negativo.")
        @Digits(integer = 10, fraction = 2, message = "O custo possui formato inválido.")
        BigDecimal custo
) {
}

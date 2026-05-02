package com.diacono.diacono.applications.dtos.evento;

import com.diacono.diacono.applications.dtos.igreja.IgrejaSimplificadoDTO;
import com.diacono.diacono.applications.dtos.membro.MembroSimplificadoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSimplificadoDTO;
import com.diacono.diacono.applications.dtos.recorrencia.RecorrenciaSimplificadaDTO;
import com.diacono.diacono.domain.enums.EnumStatusEvento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record EventoCompletoDTO(
        String nome,
        String descricao,
        String publicoAlvo,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        BigDecimal custo,
        MembroSimplificadoDTO organizador,
        List<MinisterioSimplificadoDTO> ministerios,
        EnderecoEventoSimplificadoDTO enderecoEvento,
        IgrejaSimplificadoDTO igreja,
        RecorrenciaSimplificadaDTO recorrencia,
        EnumStatusEvento status
) {
}

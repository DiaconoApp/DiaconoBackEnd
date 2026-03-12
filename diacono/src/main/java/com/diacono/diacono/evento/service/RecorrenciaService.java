package com.diacono.diacono.evento.service;

import com.diacono.diacono.evento.mapper.RecorrenciaMapper;
import com.diacono.diacono.evento.model.dto.request.RecorrenciaCreateDTO;
import com.diacono.diacono.evento.model.entity.Recorrencia;
import com.diacono.diacono.evento.model.entity.TipoRecorrencia;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RecorrenciaService {

    // OWASP A05/A07: limita o periodo maximo da recorrencia para reduzir payload inconsistente.
    private static final int MAX_INTERVALO_RECORRENCIA_DIAS = 365;

    private final RecorrenciaMapper recorrenciaMapper;

    public RecorrenciaService(RecorrenciaMapper recorrenciaMapper) {
        this.recorrenciaMapper = recorrenciaMapper;
    }

    // OWASP A05: validacao defensiva antes da conversao do DTO.
    public Recorrencia converterDtoToRecorrencia(@Valid RecorrenciaCreateDTO recorrenciaCreateDTO){

        return recorrenciaMapper.paraRecorrencia(recorrenciaCreateDTO);
    }

    // OWASP A05/A07: falha de forma segura para payload nulo e combinacoes inconsistentes.
    public void validarRecorrencia(@Valid RecorrenciaCreateDTO recorrencia, LocalDateTime comparativoEvento){

        if(!recorrencia.tipoRecorrencia().equals(TipoRecorrencia.NAO_REPETE)){

            if(recorrencia.dataTerminoRecorrencia() == null || recorrencia.dataInicioRecorrencia() == null){
                throw  new FieldInvalidException("É necessário preencher os campos de inicío e término da recorrência");
            }

            if(!recorrencia.dataTerminoRecorrencia().isAfter(recorrencia.dataInicioRecorrencia())){
                throw new FieldInvalidException("A data final da recorrência precisa ser maior que a data de início, para eventos com recorrência.");
            }

            if(recorrencia.dataInicioRecorrencia().plusDays(365).isBefore(recorrencia.dataTerminoRecorrencia())){
                throw  new FieldInvalidException("A data final da recorrência precisar estar dentro do período de um ano");
            }

            if(!recorrencia.dataInicioRecorrencia().isEqual(comparativoEvento.toLocalDate())){
                throw new FieldInvalidException("A data de início da recorrência precisa ser igual à data de início do evento.");
            }

        }
    }

}

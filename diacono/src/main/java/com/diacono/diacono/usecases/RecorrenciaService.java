package com.diacono.diacono.usecases;

import com.diacono.diacono.applications.mappers.recorrencia.RecorrenciaMapper;
import com.diacono.diacono.applications.dtos.recorrencia.RecorrenciaCreateDTO;
import com.diacono.diacono.domain.entity.Recorrencia;
import com.diacono.diacono.domain.enums.TipoRecorrencia;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RecorrenciaService {

    private final RecorrenciaMapper recorrenciaMapper;

    public RecorrenciaService(RecorrenciaMapper recorrenciaMapper) {
        this.recorrenciaMapper = recorrenciaMapper;
    }

    //metodo para conversão

    public Recorrencia converterDtoToRecorrencia(RecorrenciaCreateDTO recorrenciaCreateDTO){
        return recorrenciaMapper.paraRecorrencia(recorrenciaCreateDTO);
    }

    // metodos para validacoes
    public void validarRecorrencia(RecorrenciaCreateDTO recorrencia, LocalDateTime comparativoEvento){

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

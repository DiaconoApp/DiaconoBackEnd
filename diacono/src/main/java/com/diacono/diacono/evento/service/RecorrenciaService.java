package com.diacono.diacono.evento.service;

import com.diacono.diacono.evento.mapper.RecorrenciaMapper;
import com.diacono.diacono.evento.model.dto.request.EnderecoEventoDTO;
import com.diacono.diacono.evento.model.dto.request.RecorrenciaCreateDTO;
import com.diacono.diacono.evento.model.entity.EnderecoEvento;
import com.diacono.diacono.evento.model.entity.Recorrencia;
import com.diacono.diacono.evento.model.entity.TipoRecorrencia;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;

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
    public void validarRecorrencia(RecorrenciaCreateDTO recorrencia){

        if(!recorrencia.tipoRecorrencia().equals(TipoRecorrencia.NAO_REPETE)){

            if(recorrencia.dataTerminoRecorrencia() == null || recorrencia.dataInicioRecorrencia() == null){
                throw  new FieldInvalidException("É necessário preencher os campos de inicío e término da recorrência");
            }

            if(!recorrencia.dataTerminoRecorrencia().isAfter(recorrencia.dataInicioRecorrencia())){
                throw new DateTimeException("A data final da recorrência precisa ser maior ou igual à data de início.");
            }

            if(recorrencia.dataInicioRecorrencia().plusDays(365).isBefore(recorrencia.dataInicioRecorrencia())){
                throw  new FieldInvalidException("A data final da recorrência precisar estar dentro do período de um ano");
            }

        }
    }

}

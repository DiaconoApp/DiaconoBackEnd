package com.diacono.diacono.evento.service;

import com.diacono.diacono.Igreja.model.entity.Igreja;
import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.evento.mapper.EnderecoEventoMapper;
import com.diacono.diacono.evento.model.dto.request.EnderecoEventoDTO;
import com.diacono.diacono.evento.model.dto.response.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.evento.model.entity.EnderecoEvento;
import com.diacono.diacono.evento.repository.EnderecoEventoRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EnderecoEventoService {

    private final EnderecoEventoRepository enderecoEventoRepository;
    private final EnderecoEventoMapper enderecoEventoMapper;
    private final JwtUtils jwtUtils;
    private final IgrejaService igrejaService;

    public EnderecoEventoService(EnderecoEventoRepository enderecoEventoRepository, EnderecoEventoMapper enderecoEventoMapper, JwtUtils jwtUtils, IgrejaService igrejaService) {
        this.enderecoEventoRepository = enderecoEventoRepository;
        this.enderecoEventoMapper = enderecoEventoMapper;
        this.jwtUtils = jwtUtils;
        this.igrejaService = igrejaService;
    }

    public EnderecoEvento buscarPorUUID(UUID idExterno){
        EnderecoEvento enderecoEvento = enderecoEventoRepository.findByIdExterno(idExterno);

        if(enderecoEvento == null){
            throw new ObjectNotFoundException("Endereço do evento não encontrado");
        }

        return enderecoEvento;
    }

    public void salvarEnderecoEvento(EnderecoEvento endereco){
        EnderecoEvento salvo = enderecoEventoRepository.save(endereco);
    }

    public EnderecoEventoSimplificadoDTO buscarEnderecoIgreja(){

        Igreja igreja = igrejaService.buscarUUID(jwtUtils.getIgrejaId());

        EnderecoEvento enderecoEvento = enderecoEventoRepository.findByCep(igreja.getEnderecoIgreja().getCep(), igreja.getEnderecoIgreja().getNumero());

        if(enderecoEvento == null){
            throw new ObjectNotFoundException("Endereço do evento não registrado");
        }

        return enderecoEventoMapper.paraEnderecoEventoSimplificadoDTO(enderecoEvento);
    }

    //metodo para conversão

    public EnderecoEvento converterDtoToEndereco(EnderecoEventoDTO enderecoEventoDTO){
        return enderecoEventoMapper.paraEndereco(enderecoEventoDTO);
    }

    //metodos para validacoes

    public void validarEnderecoEvento(EnderecoEventoDTO endereco){

        if (endereco.idExterno() != null) {
            return;
        }

        if(endereco.idExterno() == null){
            if(endereco.cep().isBlank()){
                throw  new FieldInvalidException("Você precisa preencher o CEP");
            }
        }

        if (endereco.cep().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o CEP");
        }

        if (endereco.estado().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o estado");
        }

        if (endereco.cidade().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher a cidade");
        }

        if (endereco.bairro().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o bairro");
        }

        if (endereco.rua().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher a rua");
        }

        if (endereco.numero().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o número");
        }

        if (endereco.apelido().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o apelido");
        }

    }

}

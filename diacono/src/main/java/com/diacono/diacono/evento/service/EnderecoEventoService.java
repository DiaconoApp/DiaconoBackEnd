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
import jakarta.validation.Valid;
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
        // OWASP A01/A05: evita consultar recurso com identificador ausente ou invalido.
        if (idExterno == null) {
            throw new FieldInvalidException("O id do endereço do evento precisa ser informado");
        }

        EnderecoEvento enderecoEvento = enderecoEventoRepository.findByIdExterno(idExterno);

        if(enderecoEvento == null){
            throw new ObjectNotFoundException("Endereço do evento não encontrado");
        }

        return enderecoEvento;
    }

    public void salvarEnderecoEvento(EnderecoEvento endereco){
        // OWASP A05: falha de forma segura antes de persistir payload nulo.
        if (endereco == null) {
            throw new FieldInvalidException("O endereço do evento precisa ser informado");
        }

        enderecoEventoRepository.save(endereco);
    }

    public EnderecoEventoSimplificadoDTO buscarEnderecoIgreja(){

        Igreja igreja = igrejaService.buscarUUID(jwtUtils.getIgrejaId());

        EnderecoEvento enderecoEvento = enderecoEventoRepository.findByCep(igreja.getEnderecoIgreja().getCep(), igreja.getEnderecoIgreja().getNumero());

        if(enderecoEvento == null){
            throw new ObjectNotFoundException("Endereço do evento não registrado");
        }

        return enderecoEventoMapper.paraEnderecoEventoSimplificadoDTO(enderecoEvento);
    }

    // OWASP A01/A05: só permite conversão para nova entidade quando o payload representa um novo endereco.
    public EnderecoEvento converterDtoToEndereco(@Valid EnderecoEventoDTO enderecoEventoDTO){
        return enderecoEventoMapper.paraEndereco(enderecoEventoDTO);
    }

    // OWASP A01/A05: remove bypass por idExterno e rejeita payload hibrido com id + campos de endereco.
    public void validarEnderecoEvento(@Valid EnderecoEventoDTO endereco){

        if (endereco.idExterno() != null) {
            return;
        }

        if(endereco.idExterno() == null){
            if(endereco.cep() == null || endereco.cep().isBlank()){
                throw  new FieldInvalidException("Você precisa preencher o CEP");
            }
        }

        if (endereco.cep() == null || endereco.cep().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o CEP");
        }

        if (endereco.estado() == null  || endereco.estado().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o estado");
        }

        if (endereco.cidade() == null  ||  endereco.cidade().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher a cidade");
        }

        if (endereco.bairro() == null  ||  endereco.bairro().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o bairro");
        }

        if (endereco.rua() == null  || endereco.rua().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher a rua");
        }

        if (endereco.numero() == null  || endereco.numero().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o número");
        }

        if (endereco.apelido() == null  ||  endereco.apelido().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o apelido");
        }

    }

}

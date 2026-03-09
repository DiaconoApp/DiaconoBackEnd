package com.diacono.diacono.use_cases;

import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.applications.mappers.endereco.EnderecoEventoMapper;
import com.diacono.diacono.applications.dtos.evento.EnderecoEventoDTO;
import com.diacono.diacono.applications.dtos.evento.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.repository.EnderecoEventoRepository;
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

    public EnderecoEventoService(
            EnderecoEventoRepository enderecoEventoRepository,
            EnderecoEventoMapper enderecoEventoMapper,
            JwtUtils jwtUtils,
            IgrejaService igrejaService) {
        this.enderecoEventoRepository = enderecoEventoRepository;
        this.enderecoEventoMapper = enderecoEventoMapper;
        this.jwtUtils = jwtUtils;
        this.igrejaService = igrejaService;
    }

    public EnderecoEvento buscarPorUUID(UUID idExterno){
        return enderecoEventoRepository.findByIdExterno(idExterno)
            .orElseThrow(() -> new ObjectNotFoundException("Endereço do evento não encontrado"));
    }

    public EnderecoEvento salvarEnderecoEvento(EnderecoEvento endereco){
        return enderecoEventoRepository.save(endereco);
    }

    public EnderecoEventoSimplificadoDTO buscarEnderecoIgreja(){
        Igreja igreja = igrejaService.buscarUUID(jwtUtils.getIgrejaId());

        EnderecoEvento enderecoEvento = enderecoEventoRepository
            .findByCep(igreja.getEnderecoIgreja().getCep(), igreja.getEnderecoIgreja().getNumero())
            .orElseThrow(() -> new ObjectNotFoundException("Endereço do evento não registrado"));

        return enderecoEventoMapper.paraEnderecoEventoSimplificadoDTO(enderecoEvento);
    }

    public EnderecoEvento converterDtoToEndereco(EnderecoEventoDTO enderecoEventoDTO){
        return enderecoEventoMapper.paraEndereco(enderecoEventoDTO);
    }

    public void validarEnderecoEvento(EnderecoEventoDTO endereco){
        if (endereco.idExterno() != null) {
            return;
        }

        if (endereco.cep() == null || endereco.cep().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o CEP");
        }

        if (endereco.estado() == null || endereco.estado().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o estado");
        }

        if (endereco.cidade() == null || endereco.cidade().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher a cidade");
        }

        if (endereco.bairro() == null || endereco.bairro().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o bairro");
        }

        if (endereco.rua() == null || endereco.rua().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher a rua");
        }

        if (endereco.numero() == null || endereco.numero().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o número");
        }

        if (endereco.apelido() == null || endereco.apelido().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o apelido");
        }
    }
}
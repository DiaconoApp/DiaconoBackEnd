package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.evento.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.applications.mappers.endereco.EnderecoEventoMapper;
import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.entity.EnderecoIgreja;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.repository.EnderecoEventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.igreja.BuscarIgrejaPorUUIDUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarEnderecoEventoUseCase {

    private final BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase;
    private final EnderecoEventoRepository enderecoEventoRepository;
    private final EnderecoEventoMapper enderecoEventoMapper;
    private static final Logger logger = LoggerFactory.getLogger(BuscarEnderecoEventoUseCase.class);

    public BuscarEnderecoEventoUseCase(BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase, EnderecoEventoRepository enderecoEventoRepository, EnderecoEventoMapper enderecoEventoMapper) {
        this.buscarIgrejaPorUUIDUseCase = buscarIgrejaPorUUIDUseCase;
        this.enderecoEventoRepository = enderecoEventoRepository;
        this.enderecoEventoMapper = enderecoEventoMapper;
    }

    // NOVO: execute com igrejaId para que o controller obtenha o id do JWT e passe como parâmetro
    public EnderecoEventoSimplificadoDTO execute(UUID igrejaId){
        Igreja igreja = buscarIgrejaPorUUIDUseCase.execute(igrejaId);

        EnderecoIgreja enderecoIgreja = igreja.getEnderecoIgreja();
        if (enderecoIgreja == null) {
            logger.warn("Igreja sem endereço cadastrado. igrejaId={}", igrejaId);
            throw new ObjectNotFoundException("Endereço do evento não registrado");
        }

        EnderecoEvento enderecoEvento = enderecoEventoRepository
                .findByCep(enderecoIgreja.getCep(), enderecoIgreja.getNumero())
                .orElseThrow(() -> new ObjectNotFoundException("Endereço do evento não registrado"));

        return enderecoEventoMapper.paraEnderecoEventoSimplificadoDTO(enderecoEvento);
    }
}
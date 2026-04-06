package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.evento.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.applications.mappers.endereco.EnderecoEventoMapper;
import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.repository.EnderecoEventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.igreja.BuscarIgrejaPorUUIDUseCase;
import org.springframework.stereotype.Service;

@Service
public class BuscarEnderecoEventoUseCase {

    private final BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase;
    private final JwtUtils jwtUtils;
    private final EnderecoEventoRepository enderecoEventoRepository;
    private final EnderecoEventoMapper enderecoEventoMapper;

    public BuscarEnderecoEventoUseCase(BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase, JwtUtils jwtUtils, EnderecoEventoRepository enderecoEventoRepository, EnderecoEventoMapper enderecoEventoMapper) {
        this.buscarIgrejaPorUUIDUseCase = buscarIgrejaPorUUIDUseCase;
        this.jwtUtils = jwtUtils;
        this.enderecoEventoRepository = enderecoEventoRepository;
        this.enderecoEventoMapper = enderecoEventoMapper;
    }

    public EnderecoEventoSimplificadoDTO execute(){
        Igreja igreja = buscarIgrejaPorUUIDUseCase.execute(jwtUtils.getIgrejaId());

        EnderecoEvento enderecoEvento = enderecoEventoRepository
                .findByCep(igreja.getEnderecoIgreja().getCep(), igreja.getEnderecoIgreja().getNumero())
                .orElseThrow(() -> new ObjectNotFoundException("Endereço do evento não registrado"));

        return enderecoEventoMapper.paraEnderecoEventoSimplificadoDTO(enderecoEvento);
    }
}
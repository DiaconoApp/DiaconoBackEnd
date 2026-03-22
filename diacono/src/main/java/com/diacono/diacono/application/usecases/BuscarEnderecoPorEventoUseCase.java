package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.mappers.EnderecoEventoMapper;
import com.diacono.diacono.application.service.IgrejaFetcher;
import com.diacono.diacono.domain.entities.EnderecoEvento;
import com.diacono.diacono.domain.entities.Igreja;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.EnderecoEventoRepository;
import com.diacono.diacono.presentation.dto.response.EnderecoEventoSimplificadoDTO;
import org.springframework.stereotype.Service;

@Service
public class BuscarEnderecoPorEventoUseCase {

    private final IgrejaFetcher igrejaFetcher;
    private final EnderecoEventoRepository repository;
    private final EnderecoEventoMapper mapper;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public BuscarEnderecoPorEventoUseCase(IgrejaFetcher igrejaFetcher, EnderecoEventoRepository repository, EnderecoEventoMapper mapper, JwtClaimsExtractor jwtClaimsExtractor) {
        this.igrejaFetcher = igrejaFetcher;
        this.repository = repository;
        this.mapper = mapper;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    public EnderecoEventoSimplificadoDTO buscarEnderecoIgreja(){
        Igreja igreja = igrejaFetcher.buscarUUID(jwtClaimsExtractor.getIgrejaId());

        EnderecoEvento enderecoEvento = repository.findByCep(igreja.getEnderecoIgreja().getCep(), igreja.getEnderecoIgreja().getNumero());

        if(enderecoEvento == null){
            throw new ObjectNotFoundException("Endereço do evento não registrado");
        }

        return mapper.paraEnderecoEventoSimplificadoDTO(enderecoEvento);
    }


}

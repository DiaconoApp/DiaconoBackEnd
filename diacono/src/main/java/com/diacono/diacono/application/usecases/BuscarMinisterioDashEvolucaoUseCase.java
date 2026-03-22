package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.MembroMinisterioRepository;
import com.diacono.diacono.presentation.dto.response.MinisterioDashEvolucaoDTO;

import java.util.List;
import java.util.UUID;

import static com.diacono.diacono.application.validators.DateValidator.validarAnoInicioEFim;

@Service
public class BuscarMinisterioDashEvolucaoUseCase {

    private final JwtClaimsExtractor jwtClaimsExtractor;
    private final MembroMinisterioRepository membroMinisterioRepository;

    public BuscarMinisterioDashEvolucaoUseCase(JwtClaimsExtractor jwtClaimsExtractor, MembroMinisterioRepository membroMinisterioRepository) {
        this.jwtClaimsExtractor = jwtClaimsExtractor;
        this.membroMinisterioRepository = membroMinisterioRepository;
    }

    public List<MinisterioDashEvolucaoDTO> ministerioBuscarDashEvolucao(int anoInicio, int anoFim, UUID idMinisterio) {

        if (idMinisterio == null) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        validarAnoInicioEFim(anoInicio, anoFim);

        UUID idIgreja = jwtClaimsExtractor.getIgrejaId();

        List<MinisterioDashEvolucaoDTO> response;

        if(anoInicio == anoFim){
            response = membroMinisterioRepository.buscarDashEvolucaoUmAno(anoFim, idMinisterio, idIgreja);
            return response;
        }else{
            response = membroMinisterioRepository.buscarDashEvolucaoPeriodo(anoInicio,anoFim, idMinisterio, idIgreja);
        }

        if (response == null || response.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        return response;

    }
}

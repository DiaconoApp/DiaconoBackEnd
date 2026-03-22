package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.MembroRepository;
import com.diacono.diacono.presentation.dto.response.DashboardGeneroMembroDTO;
import com.diacono.diacono.presentation.dto.response.MembroDashGeneroDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.diacono.diacono.application.validators.DateValidator.validarAnoInicioEFim;

@Service
public class BuscarDashGeneroUseCase {

    private final MembroRepository repository;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public BuscarDashGeneroUseCase(MembroRepository repository, JwtClaimsExtractor jwtClaimsExtractor) {
        this.repository = repository;
        this.jwtClaimsExtractor = jwtClaimsExtractor;
    }

    public DashboardGeneroMembroDTO buscarDashGenero(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);

        UUID idExternoIgreja = jwtClaimsExtractor.getIgrejaId();

        MembroDashGeneroDTO genero = repository.buscarMembrosPorGenero(idExternoIgreja, anoFim);

        if (genero == null){
            throw new ObjectNotFoundException("Nenhum dado encontrado para o período informado.");
        }

        long total = genero.masculino() + genero.feminino();


        if (total == 0) {
            return new DashboardGeneroMembroDTO(0, 0);
        }

        double masculinoPercent = (double) genero.masculino() / total * 100;
        double femininoPercent = (double) genero.feminino() / total * 100;

        DashboardGeneroMembroDTO response = new DashboardGeneroMembroDTO(
                masculinoPercent,
                femininoPercent
        );

        return response;
    }

}

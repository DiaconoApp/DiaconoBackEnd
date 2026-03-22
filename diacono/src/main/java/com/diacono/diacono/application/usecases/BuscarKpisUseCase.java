package com.diacono.diacono.application.usecases;

import com.diacono.diacono.application.service.TokenService;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.infrastructure.persistence.MembroRepository;
import com.diacono.diacono.presentation.dto.response.KpisMembrosDTO;
import com.diacono.diacono.presentation.dto.response.MembroKpiResponseDTO;

import java.util.UUID;

import static com.diacono.diacono.application.validators.DateValidator.validarAnoInicioEFim;

@Service
public class BuscarKpisUseCase {

    private final MembroRepository repository;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    public BuscarKpisUseCase(MembroRepository repository) {
        this.repository = repository;
    }

    public KpisMembrosDTO buscarKpisMembros(int anoInicio, int anoFim) {

        validarAnoInicioEFim(anoInicio, anoFim);

        UUID idExternoIgreja = jwtClaimsExtractor.getIgrejaId();

        MembroKpiResponseDTO kpis = repository.buscarKpisMembros(idExternoIgreja, anoInicio, anoFim);

        return buildKpisMembrosDto(kpis, anoInicio, anoFim);

    }

    private KpisMembrosDTO buildKpisMembrosDto(MembroKpiResponseDTO kpis,
                                               int anoInicio,
                                               int anoFim){
        long membrosAtivos = kpis.membrosAtivos();
        long membrosInativos = kpis.membrosInativos();
        long totalMembrosAnoInicio = kpis.totalAnoInicio();
        long totalMembros = membrosAtivos + membrosInativos;

        long membrosNovos = totalMembros;


        if (anoInicio == anoFim) {
            membrosNovos = totalMembrosAnoInicio;
        }

        double resultado = Math.round((membrosAtivos - membrosInativos) * 100.0 / totalMembros);


        double retencao = membrosInativos == 0 ? 100 : resultado;


        return new KpisMembrosDTO(
                membrosAtivos,
                membrosNovos,
                retencao
        );
    }

}

package com.diacono.diacono.usecases.dashboard.membros;

import com.diacono.diacono.applications.dtos.ministerio.KpisMembrosDTO;
import com.diacono.diacono.applications.dtos.membro.MembroKpiResponseDTO;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarKpiMembrosDashUseCaseTest {

	@Mock
	private DashboardPeriodoValidator periodoValidator;

	@Mock
	private MembroRepository membroRepository;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private BuscarKpiMembrosDashUseCase useCase;

	@Test
	void deveRetornarKpisQuandoPeriodoForMesmoAno() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		MembroKpiResponseDTO kpis = new MembroKpiResponseDTO(80, 20, 15, 100);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroRepository.buscarKpisMembros(idIgreja, 2026, 2026)).thenReturn(kpis);

		KpisMembrosDTO response = useCase.execute(2026, 2026);

		assertEquals(80, response.membrosAtivos());
		assertEquals(15, response.membrosNovos());
		assertEquals(60.0, response.retencao());
		verify(periodoValidator).validarAnoInicioEFim(2026, 2026);
	}

	@Test
	void deveRetornarKpisQuandoPeriodoForAnosDiferentes() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		MembroKpiResponseDTO kpis = new MembroKpiResponseDTO(50, 0, 20, 50);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroRepository.buscarKpisMembros(idIgreja, 2025, 2026)).thenReturn(kpis);

		KpisMembrosDTO response = useCase.execute(2025, 2026);

		assertEquals(50, response.membrosAtivos());
		assertEquals(50, response.membrosNovos());
		assertEquals(100.0, response.retencao());
	}
}
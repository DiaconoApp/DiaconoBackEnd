package com.diacono.diacono.usecases.dashboard.ministerios;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarKpiEvolucaoMinisteriosDashUseCaseTest {

	@Mock
	private MembroMinisterioRepository membroMinisterioRepository;

	@Mock
	private DashboardPeriodoValidator periodoValidator;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private BuscarKpiEvolucaoMinisteriosDashUseCase useCase;

	@Test
	void deveRetornarEvolucaoQuandoPeriodoForUmAno() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idIgreja = UUID.fromString("22222222-2222-2222-2222-222222222222");
		List<MinisterioDashEvolucaoDTO> esperado = List.of(new MinisterioDashEvolucaoDTO(10, LocalDate.of(2026, 1, 1)));

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroMinisterioRepository.buscarDashEvolucaoUmAno(2026, idMinisterio, idIgreja)).thenReturn(esperado);

		List<MinisterioDashEvolucaoDTO> response = useCase.execute(2026, 2026, idMinisterio);

		assertEquals(1, response.size());
		assertEquals(10, response.getFirst().quantidadeMembros());
		verify(periodoValidator).validarAnoInicioEFim(2026, 2026);
	}

	@Test
	void deveRetornarEvolucaoQuandoPeriodoForIntervalo() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idIgreja = UUID.fromString("22222222-2222-2222-2222-222222222222");
		List<MinisterioDashEvolucaoDTO> esperado = List.of(new MinisterioDashEvolucaoDTO(15, LocalDate.of(2026, 2, 1)));

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroMinisterioRepository.buscarDashEvolucaoPeriodo(2025, 2026, idMinisterio, idIgreja)).thenReturn(esperado);

		List<MinisterioDashEvolucaoDTO> response = useCase.execute(2025, 2026, idMinisterio);

		assertEquals(1, response.size());
		assertEquals(15, response.getFirst().quantidadeMembros());
	}

	@Test
	void deveLancarExcecaoQuandoIdMinisterioForNulo() {
		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(2025, 2026, null));

		assertEquals("Nenhum dado encontrado para o período informado.", ex.getMessage());
	}

	@Test
	void deveLancarExcecaoQuandoRespostaVierVazia() {
		UUID idMinisterio = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idIgreja = UUID.fromString("22222222-2222-2222-2222-222222222222");

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroMinisterioRepository.buscarDashEvolucaoPeriodo(2025, 2026, idMinisterio, idIgreja)).thenReturn(List.of());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(2025, 2026, idMinisterio));

		assertEquals("Nenhum dado encontrado para o período informado.", ex.getMessage());
	}
}
package com.diacono.diacono.usecases.dashboard.ministerios;

import com.diacono.diacono.applications.dtos.ministerio.MinisterioEventoDashDTO;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.dashboard.validation.DashboardPeriodoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class buscarKpiQuantidadeEventosPorMinisterioDashUseCaseTest {

	@Mock
	private DashboardPeriodoValidator periodoValidator;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private EventoRepository eventoRepository;

	@InjectMocks
	private buscarKpiQuantidadeEventosPorMinisterioDashUseCase useCase;

	@Test
	void deveRetornarQuantidadeEventosPorMinisterioComSucesso() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		List<MinisterioEventoDashDTO> esperado = List.of(new MinisterioEventoDashDTO("Louvor", 10));

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(eventoRepository.contarEventosPorMinisterioNoPeriodo(2025, 2026, idIgreja)).thenReturn(esperado);

		List<MinisterioEventoDashDTO> response = useCase.execute(2025, 2026);

		assertEquals(1, response.size());
		assertEquals("Louvor", response.getFirst().nomeMinisterio());
		verify(periodoValidator).validarAnoInicioEFim(2025, 2026);
	}

	@Test
	void deveLancarExcecaoQuandoNaoExistiremDados() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(eventoRepository.contarEventosPorMinisterioNoPeriodo(2025, 2026, idIgreja)).thenReturn(List.of());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(2025, 2026));

		assertEquals("Nenhum dado encontrado para o período informado.", ex.getMessage());
	}
}
package com.diacono.diacono.usecases.dashboard.membros;

import com.diacono.diacono.applications.dtos.membro.MembroDashEvolucaoDTO;
import com.diacono.diacono.domain.repository.MembroRepository;
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

//noinspection unused
@ExtendWith(MockitoExtension.class)
class BuscarKpiEvolucaoMembrosDashUseCaseTest {

	@Mock
	private DashboardPeriodoValidator periodoValidator;

	@Mock
	private MembroRepository membroRepository;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private BuscarKpiEvolucaoMembrosDashUseCase useCase;

	@Test
	void deveRetornarEvolucaoQuandoExistiremDados() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		List<MembroDashEvolucaoDTO> esperado = List.of(new MembroDashEvolucaoDTO(LocalDate.of(2026, 1, 1), 10L));

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroRepository.buscarMembrosPorAno(idIgreja, 2025, 2026)).thenReturn(esperado);

		List<MembroDashEvolucaoDTO> response = useCase.execute(2025, 2026);

		assertEquals(1, response.size());
		assertEquals(10L, response.getFirst().quantidade());
		verify(periodoValidator).validarAnoInicioEFim(2025, 2026);
	}

	@Test
	void deveLancarExcecaoQuandoNaoExistiremDados() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroRepository.buscarMembrosPorAno(idIgreja, 2025, 2026)).thenReturn(List.of());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(2025, 2026));

		assertEquals("Nenhum dado encontrado para o período informado.", ex.getMessage());
	}
}
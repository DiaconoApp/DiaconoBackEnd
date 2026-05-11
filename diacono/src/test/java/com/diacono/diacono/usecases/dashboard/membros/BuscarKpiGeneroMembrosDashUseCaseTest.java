package com.diacono.diacono.usecases.dashboard.membros;

import com.diacono.diacono.applications.dtos.dashboard.DashboardGeneroMembroDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDashGeneroDTO;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

//noinspection unused
@ExtendWith(MockitoExtension.class)
class BuscarKpiGeneroMembrosDashUseCaseTest {

	@Mock
	private DashboardPeriodoValidator periodoValidator;

	@Mock
	private MembroRepository membroRepository;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private BuscarKpiGeneroMembrosDashUseCase useCase;

	@Test
	void deveRetornarPercentuaisDeGenero() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		MembroDashGeneroDTO genero = new MembroDashGeneroDTO(30, 70);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroRepository.buscarMembrosPorGenero(idIgreja, 2026)).thenReturn(genero);

		DashboardGeneroMembroDTO response = useCase.execute(2025, 2026);

		assertEquals(30.0, response.masculino());
		assertEquals(70.0, response.feminino());
	}

	@Test
	void deveRetornarZerosQuandoTotalForZero() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		MembroDashGeneroDTO genero = new MembroDashGeneroDTO(0, 0);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroRepository.buscarMembrosPorGenero(idIgreja, 2026)).thenReturn(genero);

		DashboardGeneroMembroDTO response = useCase.execute(2025, 2026);

		assertEquals(0.0, response.masculino());
		assertEquals(0.0, response.feminino());
	}

	@Test
	void deveLancarExcecaoQuandoNaoExistiremDadosNoRepositorio() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroRepository.buscarMembrosPorGenero(idIgreja, 2026)).thenReturn(null);

		assertThrows(com.diacono.diacono.global.error.exceptions.ObjectNotFoundException.class, () -> useCase.execute(2025, 2026));
	}
}
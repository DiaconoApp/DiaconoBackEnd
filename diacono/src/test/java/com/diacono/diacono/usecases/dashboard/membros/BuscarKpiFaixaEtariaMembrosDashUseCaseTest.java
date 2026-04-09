package com.diacono.diacono.usecases.dashboard.membros;

import com.diacono.diacono.applications.dtos.dashboard.DashboardFaixaEtariaMembroDTO;
import com.diacono.diacono.applications.dtos.membro.MembroDashFaixaEtariaDTO;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
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
class BuscarKpiFaixaEtariaMembrosDashUseCaseTest {

	@Mock
	private DashboardPeriodoValidator periodoValidator;

	@Mock
	private MembroRepository membroRepository;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private BuscarKpiFaixaEtariaMembrosDashUseCase useCase;

	@Test
	void deveRetornarPercentuaisDeFaixaEtaria() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		MembroDashFaixaEtariaDTO faixa = new MembroDashFaixaEtariaDTO(10, 10, 30, 30, 20);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroRepository.buscarMembrosPorFaixaEtaria(idIgreja, 2026)).thenReturn(faixa);

		DashboardFaixaEtariaMembroDTO response = useCase.execute(2025, 2026);

		assertEquals(10, response.criancas());
		assertEquals(10, response.adolescentes());
		assertEquals(30, response.jovens());
		assertEquals(30, response.adultos());
		assertEquals(20, response.idosos());
	}

	@Test
	void deveRetornarZerosQuandoTotalForZero() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		MembroDashFaixaEtariaDTO faixa = new MembroDashFaixaEtariaDTO(0, 0, 0, 0, 0);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroRepository.buscarMembrosPorFaixaEtaria(idIgreja, 2026)).thenReturn(faixa);

		DashboardFaixaEtariaMembroDTO response = useCase.execute(2025, 2026);

		assertEquals(0, response.criancas());
		assertEquals(0, response.idosos());
	}

	@Test
	void deveLancarExcecaoQuandoNaoExistiremDados() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(membroRepository.buscarMembrosPorFaixaEtaria(idIgreja, 2026)).thenReturn(null);

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(2025, 2026));

		assertEquals("Nenhum dado encontrado.", ex.getMessage());
	}
}
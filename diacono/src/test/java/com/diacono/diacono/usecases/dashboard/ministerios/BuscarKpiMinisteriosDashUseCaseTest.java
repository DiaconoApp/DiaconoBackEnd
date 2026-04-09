package com.diacono.diacono.usecases.dashboard.ministerios;

import com.diacono.diacono.applications.dtos.evento.EventoKpiDTO;
import com.diacono.diacono.applications.dtos.ministerio.KpisMinisteriosDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioKpisResponseDTO;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.domain.repository.MinisteriosRepository;
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
class BuscarKpiMinisteriosDashUseCaseTest {

	@Mock
	private MinisteriosRepository ministeriosRepository;

	@Mock
	private DashboardPeriodoValidator periodoValidator;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private EventoRepository eventoRepository;

	@InjectMocks
	private BuscarKpiMinisteriosDashUseCase useCase;

	@Test
	void deveRetornarKpisMinisteriosComSucesso() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		MinisterioKpisResponseDTO kpiMinisterio = new MinisterioKpisResponseDTO(4, 12.5);
		EventoKpiDTO kpiEvento = new EventoKpiDTO("Louvor", 20L);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(ministeriosRepository.buscarKpis(idIgreja, 2026)).thenReturn(kpiMinisterio);
		when(eventoRepository.buscarKpisEvento(2025, 2026, idIgreja)).thenReturn(List.of(kpiEvento));

		KpisMinisteriosDTO response = useCase.execute(2025, 2026);

		assertEquals("Louvor", response.eventoKpiDTO().nomeMinisterio());
		assertEquals(4, response.ministerioKpisResponseDTO().ministeriosAtivos());
		verify(periodoValidator).validarAnoInicioEFim(2025, 2026);
	}

	@Test
	void deveLancarExcecaoQuandoKpiEventoVierVazio() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");
		MinisterioKpisResponseDTO kpiMinisterio = new MinisterioKpisResponseDTO(4, 12.5);

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(ministeriosRepository.buscarKpis(idIgreja, 2026)).thenReturn(kpiMinisterio);
		when(eventoRepository.buscarKpisEvento(2025, 2026, idIgreja)).thenReturn(List.of());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(2025, 2026));

		assertEquals("Nenhum dado encontrado para o período informado.", ex.getMessage());
	}

	@Test
	void deveLancarExcecaoQuandoKpiMinisterioForNulo() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");

		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(ministeriosRepository.buscarKpis(idIgreja, 2026)).thenReturn(null);
		when(eventoRepository.buscarKpisEvento(2025, 2026, idIgreja)).thenReturn(List.of(new EventoKpiDTO("Louvor", 20L)));

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(2025, 2026));

		assertEquals("Nenhum dado encontrado para o período informado.", ex.getMessage());
	}
}
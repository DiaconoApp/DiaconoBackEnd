package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.evento.EventoUpdateDTO;
import com.diacono.diacono.applications.mappers.endereco.EnderecoEventoMapper;
import com.diacono.diacono.domain.entity.EscalaEvento;
import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.escalasevento.GerarEscalaEventoUseCase;
import com.diacono.diacono.usecases.eventos.validation.ValidarIdExternoPreenchido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//noinspection unused
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class AtualizarEventoUseCaseTest {

	@Mock
	private EventoRepository eventoRepository;

	@Mock
	private BuscarEnderecoEventoPorUUIDUseCase buscarEnderecoEventoPorUUIDUseCase;

	@Mock
	private ValidarIdExternoPreenchido validarIdExternoPreenchido;

	@Mock
	private EnderecoEventoMapper enderecoEventoMapper;

	@Mock
	private BuscarMinisterioPorUUIDUseCase buscarMinisterioPorUUIDUseCase;

	@Mock
	private GerarEscalaEventoUseCase gerarEscalaEventoUseCase;

	@InjectMocks
	private AtualizarEventoUseCase useCase;

	@Test
	void deveAtualizarEventoComSucesso() {
		UUID idEvento = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idMinisterioAtual = UUID.fromString("22222222-2222-2222-2222-222222222222");

		Ministerio ministerioAtual = Ministerio.builder().nome("Louvor").build();
		ReflectionTestUtils.setField(ministerioAtual, "idExterno", idMinisterioAtual);
		EscalaEvento escalaAtual = EscalaEvento.builder().ministerio(ministerioAtual).build();

		Evento evento = Evento.builder()
				.nome("Evento Antigo")
				.descricao("Descricao Antiga")
				.publicoAlvo("GERAL")
				.custo(BigDecimal.TEN)
				.dataHoraInicio(LocalDateTime.of(2026, 5, 10, 19, 0))
				.dataHoraFim(LocalDateTime.of(2026, 5, 10, 21, 0))
				.escalaEvento(new HashSet<>(Set.of(escalaAtual)))
				.enderecoEvento(EnderecoEvento.builder().cep("12345678").numero("10").build())
				.build();

		EventoUpdateDTO request = new EventoUpdateDTO(
				List.of(idMinisterioAtual),
				null,
				"Evento Novo",
				"Descricao Nova",
				"JOVENS",
				LocalDateTime.of(2026, 5, 11, 19, 0),
				LocalDateTime.of(2026, 5, 11, 21, 0),
				BigDecimal.valueOf(20)
		);

		when(eventoRepository.findByIdExterno(idEvento)).thenReturn(Optional.of(evento));
		when(gerarEscalaEventoUseCase.executeParaAtualizacao(evento, evento.getEscalaEvento(), List.of(idMinisterioAtual))).thenReturn(Set.of(escalaAtual));

		RestResponseMessageDTO response = useCase.execute(request, idEvento);

		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals("Evento atualizado com sucesso", response.getMessage());
		assertEquals("Evento Novo", evento.getNome());
		assertEquals(1, evento.getEscalaEvento().size());
		assertEquals("Louvor", evento.getEscalaEvento().iterator().next().getMinisterio().getNome());
		assertEquals(escalaAtual, evento.getEscalaEvento().iterator().next());
		verify(eventoRepository).save(evento);
	}

	@Test
	void deveLancarExcecaoQuandoEventoNaoForEncontrado() {
		UUID idEvento = UUID.fromString("11111111-1111-1111-1111-111111111111");
		EventoUpdateDTO request = new EventoUpdateDTO(null, null, null, null, null, null, null, null);

		when(eventoRepository.findByIdExterno(idEvento)).thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(request, idEvento));

		assertEquals("Evento não encontrado", ex.getMessage());
	}
}
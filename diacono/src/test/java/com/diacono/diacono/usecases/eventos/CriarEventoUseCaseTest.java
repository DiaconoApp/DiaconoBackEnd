package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.evento.EnderecoEventoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoCreateDTO;
import com.diacono.diacono.applications.dtos.recorrencia.RecorrenciaCreateDTO;
import com.diacono.diacono.applications.mappers.endereco.EnderecoEventoMapper;
import com.diacono.diacono.applications.mappers.evento.EventoMapper;
import com.diacono.diacono.applications.mappers.recorrencia.RecorrenciaMapper;
import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.entity.Ministerio;
import com.diacono.diacono.domain.entity.Recorrencia;
import com.diacono.diacono.domain.enums.TipoRecorrencia;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.usecases.eventos.validation.ValidarHora;
import com.diacono.diacono.usecases.igreja.BuscarIgrejaPorUUIDUseCase;
import com.diacono.diacono.usecases.ministerio.BuscarMembroMinisterioLiderMinisterioComFiltroUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class CriarEventoUseCaseTest {

	@Mock
	private EventoRepository eventoRepository;

	@Mock
	private EventoMapper eventoMapper;

	@Mock
	private BuscarEnderecoEventoPorUUIDUseCase buscarEnderecoEventoPorUUIDUseCase;

	@Mock
	private RecorrenciaMapper recorrenciaMapper;

	@Mock
	private BuscarMembroMinisterioLiderMinisterioComFiltroUseCase buscarMembroMinisterioLiderMinisterioComFiltroUseCase;

	@Mock
	private MembroRepository membroRepository;

	@Mock
	private BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private ValidarHora validarHora;

	@Mock
	private EnderecoEventoMapper enderecoEventoMapper;

	@Mock
	private BuscarMinisterioPorUUIDUseCase buscarMinisterioPorUUIDUseCase;

	@InjectMocks
	private CriarEventoUseCase useCase;

	@Test
	void deveCriarEventoSemRecorrenciaComSucesso() {
		UUID idEndereco = UUID.fromString("11111111-1111-1111-1111-111111111111");
		UUID idMembro = UUID.fromString("22222222-2222-2222-2222-222222222222");
		UUID idIgreja = UUID.fromString("33333333-3333-3333-3333-333333333333");
		UUID idMinisterio = UUID.fromString("44444444-4444-4444-4444-444444444444");

		EventoCreateDTO request = criarEventoCreateDTONaoRepete(idEndereco, idMinisterio);

		EnderecoEvento enderecoEvento = EnderecoEvento.builder().cep("12345678").numero("10").build();
		Recorrencia recorrencia = Recorrencia.builder().tipoRecorrencia(TipoRecorrencia.NAO_REPETE).build();
		Evento evento = Evento.builder()
				.nome("Culto")
				.descricao("Descricao")
				.publicoAlvo("GERAL")
				.dataHoraInicio(request.dataHoraInicio())
				.dataHoraFim(request.dataHoraFim())
				.custo(request.custo())
				.build();
		Membro membro = new Membro();
		Igreja igreja = Igreja.builder().nome("Igreja Central").build();
		Ministerio ministerio = Ministerio.builder().nome("Louvor").build();

		when(recorrenciaMapper.paraRecorrencia(request.recorrencia())).thenReturn(recorrencia);
		when(buscarEnderecoEventoPorUUIDUseCase.execute(idEndereco)).thenReturn(enderecoEvento);
		when(eventoMapper.paraEvento(request)).thenReturn(evento);
		when(jwtUtils.getSubject()).thenReturn(idMembro);
		when(membroRepository.findByIdExterno(idMembro)).thenReturn(Optional.of(membro));
		when(jwtUtils.getIgrejaId()).thenReturn(idIgreja);
		when(buscarIgrejaPorUUIDUseCase.execute(idIgreja)).thenReturn(igreja);
		when(buscarMinisterioPorUUIDUseCase.execute(List.of(idMinisterio))).thenReturn(Set.of(ministerio));
		when(eventoRepository.save(evento)).thenReturn(evento);

		RestResponseMessageDTO response = useCase.execute(request);

		assertEquals(HttpStatus.CREATED, response.getStatus());
		assertEquals("Evento sem recorrência criado com sucesso", response.getMessage());
		verify(eventoRepository).save(evento);
		verifyNoInteractions(buscarMembroMinisterioLiderMinisterioComFiltroUseCase);
	}

	@Test
	void deveLancarExcecaoQuandoRecorrenciaSemanalNaoTiverDatas() {
		EventoCreateDTO request = criarEventoCreateDTOSemanalInvalido();

		FieldInvalidException ex = assertThrows(FieldInvalidException.class, () -> useCase.execute(request));

		assertEquals("É necessário preencher os campos de inicío e término da recorrência", ex.getMessage());
	}

	@Test
	void deveLancarExcecaoQuandoOrganizadorNaoForEncontrado() {
		UUID idMembro = UUID.fromString("22222222-2222-2222-2222-222222222222");

		when(membroRepository.findByIdExterno(idMembro)).thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.buscarPorUUID(idMembro));

		assertEquals("Membro não encontrado", ex.getMessage());
	}

	private EventoCreateDTO criarEventoCreateDTONaoRepete(UUID idEndereco, UUID idMinisterio) {
		return new EventoCreateDTO(
				List.of(idMinisterio),
				new EnderecoEventoDTO(idEndereco, "12345678", "SP", "SAO PAULO", "CENTRO", "RUA A", "AP 1", "10", "SEDE"),
				new RecorrenciaCreateDTO(TipoRecorrencia.NAO_REPETE, null, null),
				"Culto",
				"Descricao",
				"GERAL",
				LocalDateTime.of(2026, 5, 10, 19, 0),
				LocalDateTime.of(2026, 5, 10, 21, 0),
				BigDecimal.ZERO
		);
	}

	private EventoCreateDTO criarEventoCreateDTOSemanalInvalido() {
		return new EventoCreateDTO(
				List.of(UUID.fromString("44444444-4444-4444-4444-444444444444")),
				new EnderecoEventoDTO(
						UUID.fromString("11111111-1111-1111-1111-111111111111"),
						"12345678", "SP", "SAO PAULO", "CENTRO", "RUA A", "AP 1", "10", "SEDE"
				),
				new RecorrenciaCreateDTO(TipoRecorrencia.SEMANAL, null, null),
				"Culto",
				"Descricao",
				"GERAL",
				LocalDateTime.of(2026, 5, 10, 19, 0),
				LocalDateTime.of(2026, 5, 10, 21, 0),
				BigDecimal.ZERO
		);
	}
}
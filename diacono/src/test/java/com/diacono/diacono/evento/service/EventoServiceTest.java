package com.diacono.diacono.evento.service;

import com.diacono.diacono.application.exceptions.TimeInvalidException;
import com.diacono.diacono.application.mappers.EventoMapper;
import com.diacono.diacono.application.mappers.EventoUpdateMapper;
import com.diacono.diacono.presentation.dto.request.EnderecoEventoDTO;
import com.diacono.diacono.presentation.dto.request.EventoCreateDTO;
import com.diacono.diacono.presentation.dto.request.EventoUpdateDTO;
import com.diacono.diacono.presentation.dto.request.RecorrenciaCreateDTO;
import com.diacono.diacono.presentation.dto.response.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.presentation.dto.response.EventoCompletoDTO;
import com.diacono.diacono.presentation.dto.response.EventoSimplificadoDTO;
import com.diacono.diacono.domain.entities.EnderecoEvento;
import com.diacono.diacono.domain.entities.Evento;
import com.diacono.diacono.domain.entities.Recorrencia;
import com.diacono.diacono.domain.enums.TipoRecorrencia;
import com.diacono.diacono.infrastructure.persistence.EventoRepository;
import com.diacono.diacono.presentation.dto.response.RestResponseMessage;
import com.diacono.diacono.application.exceptions.FieldInvalidException;
import com.diacono.diacono.application.exceptions.ObjectNotFoundException;
import com.diacono.diacono.application.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.infrastructure.extractor.JwtClaimsExtractor;
import com.diacono.diacono.domain.entities.Membro;
import com.diacono.diacono.membro.service.MembroService;
import com.diacono.diacono.domain.entities.Ministerio;
import com.diacono.diacono.ministerio.service.MinisterioService;
import com.diacono.diacono.domain.entities.Igreja;
import com.diacono.diacono.Igreja.service.IgrejaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private EventoMapper eventoMapper;

    @Mock
    private EventoUpdateMapper eventoUpdateMapper;

    @Mock
    private EnderecoEventoService enderecoEventoService;

    @Mock
    private RecorrenciaService recorrenciaService;

    @Mock
    private MinisterioService ministerioService;

    @Mock
    private MembroService membroService;

    @Mock
    private IgrejaService igrejaService;

    @Mock
    private JwtClaimsExtractor jwtClaimsExtractor;

    @InjectMocks
    private EventoService eventoService;

    private UUID eventoId;
    private UUID membroId;
    private UUID igrejaId;
    private UUID ministerioId;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    @BeforeEach
    void setUp() {
        eventoId = UUID.randomUUID();
        membroId = UUID.randomUUID();
        igrejaId = UUID.randomUUID();
        ministerioId = UUID.randomUUID();
        dataHoraInicio = LocalDateTime.now().plusDays(1);
        dataHoraFim = LocalDateTime.now().plusDays(1).plusHours(2);
    }

    @Test
    @DisplayName("Deve buscar eventos por mês e ano com sucesso")
    void buscarEventosPorMesEAnoSucesso() {
        int mes = 12;
        int ano = 2024;

        Evento evento = new Evento();
        ArrayList<Evento> eventos = new ArrayList<>();
        eventos.add(evento);
        EventoSimplificadoDTO eventoResponseDTO = mock(EventoSimplificadoDTO.class);

        when(jwtClaimsExtractor.getIgrejaId()).thenReturn(igrejaId);
        when(eventoRepository.findByPeriodo(any(LocalDateTime.class), any(LocalDateTime.class), eq(igrejaId))).thenReturn(eventos);
        when(eventoMapper.paraEventoSimplificado(eventos)).thenReturn(eventoResponseDTO);

        EventoSimplificadoDTO result = eventoService.buscarEventosPorMesEAno(mes, ano);

        assertNotNull(result);
        assertEquals(eventoResponseDTO, result);
        verify(eventoRepository).findByPeriodo(any(LocalDateTime.class), any(LocalDateTime.class), eq(igrejaId));
        verify(eventoMapper).paraEventoSimplificado(eventos);
    }

    @Test
    @DisplayName("Deve lançar exceção quando mês é inválido")
    void buscarEventosPorMesEAnoMesInvalidoDeveRetornarErro() {
        int mesInvalido = 13;
        int ano = 2024;

        assertThrows(FieldInvalidException.class,
            () -> eventoService.buscarEventosPorMesEAno(mesInvalido, ano));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ano é inválido")
    void buscarEventosPorMesEAnoAnoInvalidoDeveRetornarErro() {
        int mes = 12;
        int anoInvalido = 0;

        assertThrows(FieldInvalidException.class,
            () -> eventoService.buscarEventosPorMesEAno(mes, anoInvalido));
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhum evento é encontrado")
    void buscarEventosPorMesEAnoNenhumEventoEncontradoDeveRetornarErro() {
        int mes = 12;
        int ano = 2024;

        when(jwtClaimsExtractor.getIgrejaId()).thenReturn(null);

        when(eventoRepository.findByPeriodo(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                isNull()
        )).thenReturn(new ArrayList<>());

        assertThrows(ObjectNotFoundException.class,
                () -> eventoService.buscarEventosPorMesEAno(mes, ano));
    }

    @Test
    @DisplayName("Deve buscar evento específico com sucesso")
    void buscarEventoEspecificoSucesso() {
        Evento evento = new Evento();
        EventoCompletoDTO eventoCompletoDTO = mock(EventoCompletoDTO.class);

        when(eventoRepository.findByIdExterno(eventoId)).thenReturn(evento);
        when(eventoMapper.paraEventoCompletoDTO(evento)).thenReturn(eventoCompletoDTO);

        EventoCompletoDTO result = eventoService.buscarEventoEspecifico(eventoId);

        assertNotNull(result);
        assertEquals(eventoCompletoDTO, result);
        verify(eventoRepository).findByIdExterno(eventoId);
        verify(eventoMapper).paraEventoCompletoDTO(evento);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do evento é nulo ao buscar evento específico")
    void buscarEventoEspecificoIdNuloDeveRetornarErro() {
        assertThrows(FieldInvalidException.class,
            () -> eventoService.buscarEventoEspecifico(null));
    }

    @Test
    @DisplayName("Deve criar evento sem recorrência com sucesso")
    void criarEventoSemRecorrenciaSucesso() {
        RecorrenciaCreateDTO recorrenciaDTO = new RecorrenciaCreateDTO(
            TipoRecorrencia.NAO_REPETE, null, null
        );
        EnderecoEventoDTO enderecoDTO = new EnderecoEventoDTO(
            null, "12345678", "SP", "São Paulo", "Centro", "Rua Teste", "Apto 123", "123", "Endereço Teste"
        );
        EventoCreateDTO eventoCreateDTO = new EventoCreateDTO(
            List.of(ministerioId), enderecoDTO, recorrenciaDTO, "Evento Teste", "Descrição",
            "Público Geral", dataHoraInicio, dataHoraFim, BigDecimal.ZERO
        );

        Recorrencia recorrencia = new Recorrencia();
        EnderecoEvento endereco = new EnderecoEvento();
        Evento evento = new Evento();
        Membro organizador = new Membro();
        Igreja igreja = new Igreja();
        Set<Ministerio> ministerios = new HashSet<>();

        when(jwtClaimsExtractor.getSubject()).thenReturn(membroId);
        when(jwtClaimsExtractor.getIgrejaId()).thenReturn(igrejaId);
        when(recorrenciaService.converterDtoToRecorrencia(recorrenciaDTO)).thenReturn(recorrencia);
        when(enderecoEventoService.converterDtoToEndereco(enderecoDTO)).thenReturn(endereco);
        when(eventoMapper.paraEvento(eventoCreateDTO)).thenReturn(evento);
        when(membroService.buscarPorUUID(membroId)).thenReturn(organizador);
        when(igrejaService.buscarUUID(igrejaId)).thenReturn(igreja);
        when(ministerioService.buscarPorUUID(eventoCreateDTO.fkMinisterios())).thenReturn(ministerios);
        when(eventoRepository.save(evento)).thenReturn(evento);

        RestResponseMessage result = eventoService.criarEvento(eventoCreateDTO);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatus());
        assertEquals("Evento sem recorrência criado com sucesso", result.getMessage());
        verify(recorrenciaService).validarRecorrencia(recorrenciaDTO, dataHoraInicio);
        verify(enderecoEventoService).validarEnderecoEvento(enderecoDTO);
        verify(eventoRepository).save(evento);
    }

    @Test
    @DisplayName("Deve criar evento com recorrência semanal com sucesso")
    void criarEventoRecorrenciaSemanalSucesso() {
        RecorrenciaCreateDTO recorrenciaDTO = new RecorrenciaCreateDTO(
            TipoRecorrencia.SEMANAL, LocalDate.now().plusDays(1), LocalDate.now().plusWeeks(4)
        );
        EnderecoEventoDTO enderecoDTO = new EnderecoEventoDTO(
            null, "12345678", "SP", "São Paulo", "Centro", "Rua Teste", "Apto 123", "123", "Endereço Teste"
        );
        EventoCreateDTO eventoCreateDTO = new EventoCreateDTO(
            List.of(ministerioId), enderecoDTO, recorrenciaDTO, "Evento Teste", "Descrição",
            "Público Geral", dataHoraInicio, dataHoraFim, BigDecimal.ZERO
        );

        Recorrencia recorrencia = new Recorrencia();
        EnderecoEvento endereco = new EnderecoEvento();
        Evento evento = new Evento();
        evento.setDataHoraInicio(dataHoraInicio);
        evento.setDataHoraFim(dataHoraFim);
        Membro organizador = new Membro();
        Igreja igreja = new Igreja();
        Set<Ministerio> ministerios = new HashSet<>();

        when(jwtClaimsExtractor.getSubject()).thenReturn(membroId);
        when(jwtClaimsExtractor.getIgrejaId()).thenReturn(igrejaId);
        when(recorrenciaService.converterDtoToRecorrencia(recorrenciaDTO)).thenReturn(recorrencia);
        when(enderecoEventoService.converterDtoToEndereco(enderecoDTO)).thenReturn(endereco);
        when(eventoMapper.paraEvento(eventoCreateDTO)).thenReturn(evento);
        when(membroService.buscarPorUUID(membroId)).thenReturn(organizador);
        when(igrejaService.buscarUUID(igrejaId)).thenReturn(igreja);
        when(ministerioService.buscarPorUUID(eventoCreateDTO.fkMinisterios())).thenReturn(ministerios);
        when(eventoRepository.save(evento)).thenReturn(evento);
        when(eventoRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        RestResponseMessage result = eventoService.criarEvento(eventoCreateDTO);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatus());
        assertEquals("Eventos com recorrência semanal criado com sucesso", result.getMessage());
        verify(eventoRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Deve criar evento com recorrência mensal com sucesso")
    void criarEventoRecorrenciaMensalSucesso() {
        RecorrenciaCreateDTO recorrenciaDTO = new RecorrenciaCreateDTO(
            TipoRecorrencia.MENSAL, LocalDate.now().plusDays(1), LocalDate.now().plusMonths(3)
        );
        EnderecoEventoDTO enderecoDTO = new EnderecoEventoDTO(
            null, "12345678", "SP", "São Paulo", "Centro", "Rua Teste", "Apto 123", "123", "Endereço Teste"
        );
        EventoCreateDTO eventoCreateDTO = new EventoCreateDTO(
            List.of(ministerioId), enderecoDTO, recorrenciaDTO, "Evento Teste", "Descrição",
            "Público Geral", dataHoraInicio, dataHoraFim, BigDecimal.ZERO
        );

        Recorrencia recorrencia = new Recorrencia();
        EnderecoEvento endereco = new EnderecoEvento();
        Evento evento = new Evento();
        evento.setDataHoraInicio(dataHoraInicio);
        evento.setDataHoraFim(dataHoraFim);
        Membro organizador = new Membro();
        Igreja igreja = new Igreja();
        Set<Ministerio> ministerios = new HashSet<>();

        when(jwtClaimsExtractor.getSubject()).thenReturn(membroId);
        when(jwtClaimsExtractor.getIgrejaId()).thenReturn(igrejaId);
        when(recorrenciaService.converterDtoToRecorrencia(recorrenciaDTO)).thenReturn(recorrencia);
        when(enderecoEventoService.converterDtoToEndereco(enderecoDTO)).thenReturn(endereco);
        when(eventoMapper.paraEvento(eventoCreateDTO)).thenReturn(evento);
        when(membroService.buscarPorUUID(membroId)).thenReturn(organizador);
        when(igrejaService.buscarUUID(igrejaId)).thenReturn(igreja);
        when(ministerioService.buscarPorUUID(eventoCreateDTO.fkMinisterios())).thenReturn(ministerios);
        when(eventoRepository.save(evento)).thenReturn(evento);
        when(eventoRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        RestResponseMessage result = eventoService.criarEvento(eventoCreateDTO);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatus());
        assertEquals("Eventos com recorrência mensal criados com sucesso", result.getMessage());
        verify(eventoRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Deve lançar exceção quando horário de fim é antes do horário de início")
    void criarEventoHorarioFimAntesInicioDeveRetornarErro() {
        LocalDateTime horaInicioInvalida = LocalDateTime.now().plusDays(1).plusHours(2);
        LocalDateTime horaFimInvalida = LocalDateTime.now().plusDays(1);

        RecorrenciaCreateDTO recorrenciaDTO = new RecorrenciaCreateDTO(
            TipoRecorrencia.NAO_REPETE, null, null
        );
        EnderecoEventoDTO enderecoDTO = new EnderecoEventoDTO(
            null, "12345678", "SP", "São Paulo", "Centro", "Rua Teste", "Apto 123", "123", "Endereço Teste"
        );
        EventoCreateDTO eventoCreateDTO = new EventoCreateDTO(
            List.of(ministerioId), enderecoDTO, recorrenciaDTO, "Evento Teste", "Descrição",
            "Público Geral", horaInicioInvalida, horaFimInvalida, BigDecimal.ZERO
        );

        assertThrows(TimeInvalidException.class, () -> eventoService.criarEvento(eventoCreateDTO));
    }

    @Test
    @DisplayName("Deve lançar exceção quando horário é no passado")
    void criarEventoHorarioPassadoDeveRetornarErro() {
        LocalDateTime horaPassada = LocalDateTime.now().minusHours(1);

        RecorrenciaCreateDTO recorrenciaDTO = new RecorrenciaCreateDTO(
            TipoRecorrencia.NAO_REPETE, null, null
        );
        EnderecoEventoDTO enderecoDTO = new EnderecoEventoDTO(
            null, "12345678", "SP", "São Paulo", "Centro", "Rua Teste", "Apto 123", "123", "Endereço Teste"
        );
        EventoCreateDTO eventoCreateDTO = new EventoCreateDTO(
            List.of(ministerioId), enderecoDTO, recorrenciaDTO, "Evento Teste", "Descrição",
            "Público Geral", horaPassada, horaPassada.minusHours(1), BigDecimal.ZERO
        );

        assertThrows(TimeInvalidException.class, () -> eventoService.criarEvento(eventoCreateDTO));
    }

    @Test
    @DisplayName("Deve apagar evento com sucesso")
    void apagarEventoSucesso() {
        when(eventoRepository.deleteByIdExterno(eventoId)).thenReturn(1L);

        RestResponseMessage result = eventoService.apagarEvento(eventoId);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("Evento apagado com sucesso", result.getMessage());
        verify(eventoRepository).deleteByIdExterno(eventoId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do evento é nulo ao apagar")
    void apagarEventoIdNuloDeveRetornarErro() {
        assertThrows(FieldInvalidException.class, () -> eventoService.apagarEvento(null));
    }

    @Test
    @DisplayName("Deve lançar exceção quando evento não existe ao apagar")
    void apagarEventoNaoExisteDeveRetornarErro() {
        when(eventoRepository.deleteByIdExterno(eventoId)).thenReturn(0L);

        assertThrows(ObjectNotFoundException.class, () -> eventoService.apagarEvento(eventoId));
    }

    @Test
    @DisplayName("Deve apagar eventos múltiplos com sucesso")
    void apagarEventosMultiplosSucesso() {
        Evento evento = new Evento();
        Recorrencia recorrencia = new Recorrencia();
        evento.setRecorrencia(recorrencia);
        evento.setDataHoraInicio(dataHoraInicio);

        List<Evento> eventos = Arrays.asList(evento);

        when(jwtClaimsExtractor.getIgrejaId()).thenReturn(igrejaId);
        when(eventoRepository.findByIdExterno(eventoId)).thenReturn(evento);
        when(eventoRepository.findByPeriodoAndRecorrencia(recorrencia, dataHoraInicio, igrejaId)).thenReturn(eventos);

        RestResponseMessage result = eventoService.apagarEventosMultiplos(eventoId);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("Evento apagado com sucesso", result.getMessage());
        verify(eventoRepository).deleteAll(anyList());
    }

    @Test
    @DisplayName("Deve lançar exceção quando evento não existe ao apagar múltiplos eventos")
    void apagarEventosMultiplosEventoNaoExisteDeveRetornarErro() {
        when(eventoRepository.findByIdExterno(eventoId)).thenReturn(null);

        assertThrows(ObjectNotFoundException.class, () -> eventoService.apagarEventosMultiplos(eventoId));
    }

    @Test
    @DisplayName("Deve alterar evento com sucesso")
    void alterarEventoSucesso() {
        EnderecoEventoDTO enderecoDTO = new EnderecoEventoDTO(
            null, "87654321", "RJ", "Rio de Janeiro", "Novo Bairro", "Nova Rua", "Apto 456", "456", "Novo Endereço"
        );
        EventoUpdateDTO eventoUpdateDTO = new EventoUpdateDTO(
            List.of(ministerioId), enderecoDTO, "Novo Nome", "Nova Descrição", "Novo Público",
            dataHoraInicio.plusHours(1), dataHoraFim.plusHours(1), BigDecimal.TEN
        );

        Evento evento = new Evento();
        EnderecoEvento enderecoAtual = new EnderecoEvento();
        enderecoAtual.setCep("12345678");
        evento.setEnderecoEvento(enderecoAtual);
        evento.setMinisterios(new HashSet<>());

        EnderecoEvento novoEndereco = new EnderecoEvento();
        Set<Ministerio> ministerios = new HashSet<>();

        when(eventoRepository.findByIdExterno(eventoId)).thenReturn(evento);
        when(enderecoEventoService.converterDtoToEndereco(enderecoDTO)).thenReturn(novoEndereco);
        when(ministerioService.buscarPorUUID(eventoUpdateDTO.fkMinisterios())).thenReturn(ministerios);
        when(eventoRepository.save(evento)).thenReturn(evento);

        RestResponseMessage result = eventoService.alterarEvento(eventoUpdateDTO, eventoId);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("Evento atualizado com sucesso", result.getMessage());
        verify(eventoRepository).save(evento);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do evento é nulo ao alterar")
    void alterarEventoIdNuloDeveRetornarErro() {
        EventoUpdateDTO eventoUpdateDTO = new EventoUpdateDTO(
            null, null, "Novo Nome", null, null, null, null, null
        );

        assertThrows(FieldInvalidException.class,
            () -> eventoService.alterarEvento(eventoUpdateDTO, null));
    }

    @Test
    @DisplayName("Deve lançar exceção quando evento não existe ao alterar")
    void alterarEventoNaoExisteDeveRetornarErro() {
        EventoUpdateDTO eventoUpdateDTO = new EventoUpdateDTO(
            null, null, "Novo Nome", null, null, null, null, null
        );

        when(eventoRepository.findByIdExterno(eventoId)).thenReturn(null);

        assertThrows(ObjectSaveErrorException.class,
            () -> eventoService.alterarEvento(eventoUpdateDTO, eventoId));
    }

    @Test
    @DisplayName("Deve buscar endereço evento com sucesso")
    void buscarEnderecoEventoSucesso() {
        EnderecoEventoSimplificadoDTO enderecoDTO = mock(EnderecoEventoSimplificadoDTO.class);

        when(enderecoEventoService.buscarEnderecoIgreja()).thenReturn(enderecoDTO);

        EnderecoEventoSimplificadoDTO result = eventoService.buscarEnderecoEvento();

        assertNotNull(result);
        assertEquals(enderecoDTO, result);
        verify(enderecoEventoService).buscarEnderecoIgreja();
    }
}

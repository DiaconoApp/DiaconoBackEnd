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
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.entity.Recorrencia;
import com.diacono.diacono.domain.enums.TipoRecorrencia;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.infrastructure.messaging.EventoProducer;
import com.diacono.diacono.usecases.escalasevento.GerarEscalaEventoUseCase;
import com.diacono.diacono.usecases.igreja.BuscarIgrejaPorUUIDUseCase;
import com.diacono.diacono.usecases.eventos.validation.ValidarHora;
import com.diacono.diacono.usecases.ministerio.BuscarMembroMinisterioLiderMinisterioComFiltroUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CriarEventoUseCase {

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;
    private final BuscarEnderecoEventoPorUUIDUseCase buscarEnderecoEventoPorUUIDUseCase;
    private final RecorrenciaMapper recorrenciaMapper;
    private final BuscarMembroMinisterioLiderMinisterioComFiltroUseCase buscarMembroMinisterioLiderMinisterioComFiltroUseCase;
    private final MembroRepository membroRepository;
    private final BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase;
    private final JwtUtils jwtUtils;
    private final ValidarHora validarHora;
    private final EnderecoEventoMapper enderecoEventoMapper;
    private final BuscarMinisterioPorUUIDUseCase buscarMinisterioPorUUIDUseCase;
    private final EventoProducer eventoProducer;
    private final GerarEscalaEventoUseCase gerarEscalaEventoUseCase;

    public CriarEventoUseCase(EventoRepository eventoRepository, EventoMapper eventoMapper, BuscarEnderecoEventoPorUUIDUseCase buscarEnderecoEventoPorUUIDUseCase, RecorrenciaMapper recorrenciaMapper, BuscarMembroMinisterioLiderMinisterioComFiltroUseCase buscarMembroMinisterioLiderMinisterioComFiltroUseCase, MembroRepository membroRepository, BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase, JwtUtils jwtUtils, ValidarHora validarHora, EnderecoEventoMapper enderecoEventoMapper, BuscarMinisterioPorUUIDUseCase buscarMinisterioPorUUIDUseCase, EventoProducer eventoProducer, GerarEscalaEventoUseCase gerarEscalaEventoUseCase) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
        this.buscarEnderecoEventoPorUUIDUseCase = buscarEnderecoEventoPorUUIDUseCase;
        this.recorrenciaMapper = recorrenciaMapper;
        this.buscarMembroMinisterioLiderMinisterioComFiltroUseCase = buscarMembroMinisterioLiderMinisterioComFiltroUseCase;
        this.membroRepository = membroRepository;
        this.buscarIgrejaPorUUIDUseCase = buscarIgrejaPorUUIDUseCase;
        this.jwtUtils = jwtUtils;
        this.validarHora = validarHora;
        this.enderecoEventoMapper = enderecoEventoMapper;
        this.buscarMinisterioPorUUIDUseCase = buscarMinisterioPorUUIDUseCase;
        this.eventoProducer = eventoProducer;
        this.gerarEscalaEventoUseCase = gerarEscalaEventoUseCase;
    }

    @Transactional
    public RestResponseMessageDTO execute(EventoCreateDTO request, UUID igrejaId) {

        validarRecorrencia(request.recorrencia(), request.dataHoraInicio());
        validarEnderecoEvento(request.endereco());
        validarHora.validaHoraInicioMenorHoraFim(request.dataHoraInicio(), request.dataHoraFim());
        validarHora.validarHoraFuturo(request.dataHoraInicio(), request.dataHoraFim());

        if (request.recorrencia().tipoRecorrencia().equals(TipoRecorrencia.NAO_REPETE)) {
            criarEventoSemRecorrencia(request, igrejaId);
            return new RestResponseMessageDTO(HttpStatus.CREATED, "Evento sem recorrência criado com sucesso");
        }

        if (request.recorrencia().tipoRecorrencia().equals(TipoRecorrencia.SEMANAL)) {
            criarEventoRecorrenciaSemanal(request, igrejaId);
            return new RestResponseMessageDTO(HttpStatus.CREATED, "Eventos com recorrência semanal criado com sucesso");
        }

        if (request.recorrencia().tipoRecorrencia().equals(TipoRecorrencia.MENSAL)) {
            criarEventoRecorrenciaMensal(request, igrejaId);
            return new RestResponseMessageDTO(HttpStatus.CREATED, "Eventos com recorrência mensal criados com sucesso");
        }

        return new RestResponseMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Motivo não mapeado -> Criação do Evento");
    }

    public void validarRecorrencia(RecorrenciaCreateDTO recorrencia, LocalDateTime comparativoEvento){

        if(!recorrencia.tipoRecorrencia().equals(TipoRecorrencia.NAO_REPETE)){

            if(recorrencia.dataTerminoRecorrencia() == null || recorrencia.dataInicioRecorrencia() == null){
                throw  new FieldInvalidException("É necessário preencher os campos de inicío e término da recorrência");
            }

            if(!recorrencia.dataTerminoRecorrencia().isAfter(recorrencia.dataInicioRecorrencia())){
                throw new FieldInvalidException("A data final da recorrência precisa ser maior que a data de início, para eventos com recorrência.");
            }

            if(recorrencia.dataInicioRecorrencia().plusDays(365).isBefore(recorrencia.dataTerminoRecorrencia())){
                throw  new FieldInvalidException("A data final da recorrência precisar estar dentro do período de um ano");
            }

            if(!recorrencia.dataInicioRecorrencia().isEqual(comparativoEvento.toLocalDate())){
                throw new FieldInvalidException("A data de início da recorrência precisa ser igual à data de início do evento.");
            }

        }
    }

    private void validarEnderecoEvento(EnderecoEventoDTO endereco){
        if (endereco.idExterno() != null) {
            return;
        }

        if (endereco.cep() == null || endereco.cep().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o CEP");
        }

        if (endereco.estado() == null || endereco.estado().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o estado");
        }

        if (endereco.cidade() == null || endereco.cidade().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher a cidade");
        }

        if (endereco.bairro() == null || endereco.bairro().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o bairro");
        }

        if (endereco.rua() == null || endereco.rua().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher a rua");
        }

        if (endereco.numero() == null || endereco.numero().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o número");
        }

        if (endereco.apelido() == null || endereco.apelido().isBlank()) {
            throw new FieldInvalidException("Você precisa preencher o apelido");
        }
    }

    private RestResponseMessageDTO criarEventoRecorrenciaSemanal(EventoCreateDTO request, UUID igrejaId) {

        Evento evento = criarEventoSemRecorrencia(request, igrejaId);

        List<Evento> eventos = new ArrayList<>();

        LocalDate dataInicio = request.recorrencia().dataInicioRecorrencia();
        LocalDate dataFim = request.recorrencia().dataTerminoRecorrencia();
        long semanas = ChronoUnit.WEEKS.between(dataInicio, dataFim);

        for (int i = 1; i <= semanas; i++) {
            Evento novoEvento = new Evento();

            novoEvento.setIgreja(evento.getIgreja());
            novoEvento.setOrganizador(evento.getOrganizador());
            novoEvento.setEnderecoEvento(evento.getEnderecoEvento());
            novoEvento.setRecorrencia(evento.getRecorrencia());
            novoEvento.setNome(evento.getNome());
            novoEvento.setDescricao(evento.getDescricao());
            novoEvento.setPublicoAlvo(evento.getPublicoAlvo());
            novoEvento.setDataHoraInicio(evento.getDataHoraInicio().plusWeeks(i));
            novoEvento.setDataHoraFim(evento.getDataHoraFim().plusWeeks(i));
            novoEvento.setCusto(evento.getCusto());
            novoEvento.setEscalaEvento(gerarEscalaEventoUseCase.executeParaClonagem(novoEvento, evento.getEscalaEvento()));

            eventos.add(novoEvento);
        }

        List<Evento> eventosSalvos = eventoRepository.saveAll(eventos);
        eventosSalvos.forEach(eventoProducer::publicarEventoCriadoAposCommit);

        return new RestResponseMessageDTO(HttpStatus.CREATED, "Eventos com recorrência semanal criado com sucesso");
    }


    private RestResponseMessageDTO criarEventoRecorrenciaMensal(EventoCreateDTO request, UUID igrejaId) {

        Evento eventoBase = criarEventoSemRecorrencia(request, igrejaId);

        List<Evento> eventos = new ArrayList<>();

        LocalDate dataInicio = request.recorrencia().dataInicioRecorrencia();
        LocalDate dataFim = request.recorrencia().dataTerminoRecorrencia();
        long meses = ChronoUnit.MONTHS.between(dataInicio, dataFim);

        for (int i = 1; i <= meses; i++) {
            Evento novoEvento = new Evento();

            novoEvento.setIgreja(eventoBase.getIgreja());
            novoEvento.setOrganizador(eventoBase.getOrganizador());
            novoEvento.setEnderecoEvento(eventoBase.getEnderecoEvento());
            novoEvento.setRecorrencia(eventoBase.getRecorrencia());
            novoEvento.setNome(eventoBase.getNome());
            novoEvento.setDescricao(eventoBase.getDescricao());
            novoEvento.setPublicoAlvo(eventoBase.getPublicoAlvo());
            novoEvento.setDataHoraInicio(eventoBase.getDataHoraInicio().plusMonths(i));
            novoEvento.setDataHoraFim(eventoBase.getDataHoraFim().plusMonths(i));
            novoEvento.setCusto(eventoBase.getCusto());
            novoEvento.setEscalaEvento(gerarEscalaEventoUseCase.executeParaClonagem(novoEvento, eventoBase.getEscalaEvento()));

            eventos.add(novoEvento);
        }

        List<Evento> eventosSalvos = eventoRepository.saveAll(eventos);
        eventosSalvos.forEach(eventoProducer::publicarEventoCriadoAposCommit);

        return new RestResponseMessageDTO(HttpStatus.CREATED, "Eventos com recorrência mensal criados com sucesso");
    }

    private Evento criarEventoSemRecorrencia(EventoCreateDTO request, UUID igrejaId) {

        Recorrencia recorrencia = converterDtoToRecorrencia(request.recorrencia());

        EnderecoEvento endereco;
        if (request.endereco().idExterno() == null) {
            endereco = enderecoEventoMapper.paraEndereco(request.endereco());
        } else {
            endereco = buscarEnderecoEventoPorUUIDUseCase.execute(request.endereco().idExterno());
        }

        Evento evento = eventoMapper.paraEvento(request);

        evento.setEnderecoEvento(endereco);
        evento.setRecorrencia(recorrencia);
        evento.setOrganizador(buscarPorUUID(jwtUtils.getSubject()));
        evento.setIgreja(buscarIgrejaPorUUIDUseCase.execute(igrejaId));
        evento.setEscalaEvento(gerarEscalaEventoUseCase.executeParaCriacao(evento, request.fkMinisterios(), igrejaId));

        Evento eventoSalvo = eventoRepository.save(evento);
        eventoProducer.publicarEventoCriadoAposCommit(eventoSalvo);

        return eventoSalvo;
    }

    public Membro buscarPorUUID(UUID idExterno) {
        Membro membro = membroRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ObjectNotFoundException("Membro não encontrado"));

        return membro;
    }

    public Recorrencia converterDtoToRecorrencia(RecorrenciaCreateDTO recorrenciaCreateDTO){
        return recorrenciaMapper.paraRecorrencia(recorrenciaCreateDTO);
    }
}

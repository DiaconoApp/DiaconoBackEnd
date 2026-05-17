package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.evento.EnderecoEventoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoUpdateDTO;
import com.diacono.diacono.applications.mappers.endereco.EnderecoEventoMapper;
import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.domain.service.EscalaStatusDomainService;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.escalasevento.GerarEscalaEventoUseCase;
import com.diacono.diacono.usecases.eventos.validation.ValidarIdExternoPreenchido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class AtualizarEventoUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AtualizarEventoUseCase.class);

    private final EventoRepository eventoRepository;
    private final BuscarEnderecoEventoPorUUIDUseCase buscarEnderecoEventoPorUUIDUseCase;
    private final ValidarIdExternoPreenchido validarIdExternoPreenchido;
    private final EnderecoEventoMapper enderecoEventoMapper;
    private final GerarEscalaEventoUseCase gerarEscalaEventoUseCase;
    private final EscalaStatusDomainService escalaStatusDomainService;

    public AtualizarEventoUseCase(EventoRepository eventoRepository, BuscarEnderecoEventoPorUUIDUseCase buscarEnderecoEventoPorUUIDUseCase, ValidarIdExternoPreenchido validarIdExternoPreenchido, EnderecoEventoMapper enderecoEventoMapper, GerarEscalaEventoUseCase gerarEscalaEventoUseCase, EscalaStatusDomainService escalaStatusDomainService) {
        this.eventoRepository = eventoRepository;
        this.buscarEnderecoEventoPorUUIDUseCase = buscarEnderecoEventoPorUUIDUseCase;
        this.validarIdExternoPreenchido = validarIdExternoPreenchido;
        this.enderecoEventoMapper = enderecoEventoMapper;
        this.gerarEscalaEventoUseCase = gerarEscalaEventoUseCase;
        this.escalaStatusDomainService = escalaStatusDomainService;
    }

    @Transactional
    public RestResponseMessageDTO execute(EventoUpdateDTO request, UUID idExterno, UUID igrejaId){

        validarIdExternoPreenchido.validarIdExternoPreenchido(idExterno);
        Evento evento = buscarEventoPorUUID(idExterno, igrejaId);

        //validar endereço e ver diferenças -> para atualizar apenas se houver mudanças

        if(request.endereco() != null && validarEnderecoDiferente(evento.getEnderecoEvento(), request.endereco())){
            EnderecoEvento enderecoAtualizado;
            if(request.endereco().idExterno() == null){
                enderecoAtualizado = enderecoEventoMapper.paraEndereco(request.endereco());
            } else {
                enderecoAtualizado = buscarEnderecoEventoPorUUIDUseCase.execute(request.endereco().idExterno());
            }
            evento.setEnderecoEvento(enderecoAtualizado);
        }

        //validar outros campos que precisam ser atualizados

        if(request.fkMinisterios() != null && !request.fkMinisterios().isEmpty()){
            evento.setEscalaEvento(gerarEscalaEventoUseCase.executeParaAtualizacao(evento, evento.getEscalaEvento(), request.fkMinisterios(), igrejaId));
        }

        if (request.nome() != null) {
            evento.setNome(request.nome());
        }

        if (request.descricao() != null) {
            evento.setDescricao(request.descricao());
        }

        if (request.publicoAlvo() != null) {
            evento.setPublicoAlvo(request.publicoAlvo());
        }

        if (request.dataHoraInicio() != null) {
            evento.setDataHoraInicio(request.dataHoraInicio());
        }

        if (request.dataHoraFim() != null) {
            evento.setDataHoraFim(request.dataHoraFim());
        }

        if (request.custo() != null) {
            evento.setCusto(request.custo());
        }

        eventoRepository.save(evento);
        escalaStatusDomainService.recalcularStatusEvento(idExterno);

        logger.info("Evento atualizado com sucesso. eventoId=[{}] igrejaId=[{}]", idExterno, igrejaId);

        return new RestResponseMessageDTO(HttpStatus.OK, "Evento atualizado com sucesso");

    }

    //metodos para validar

    private Evento buscarEventoPorUUID(UUID idExterno, UUID igrejaId){
        Evento evento = eventoRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ObjectNotFoundException("Evento não encontrado"));

        if (igrejaId != null && (evento.getIgreja() == null || evento.getIgreja().getIdExterno() == null || !igrejaId.equals(evento.getIgreja().getIdExterno()))) {
            logger.warn("Tentativa de atualização de evento fora do escopo da igreja autenticada. eventoId=[{}] igrejaId=[{}]", idExterno, igrejaId);
            throw new ObjectNotFoundException("Evento não encontrado");
        }

        return evento;
    }

    private boolean validarEnderecoDiferente(EnderecoEvento endereco, EnderecoEventoDTO enderecoEventoDTO){

        if(enderecoEventoDTO.idExterno() == null){

            if(enderecoEventoDTO.cep() == null){
                return false;
            }

            return !enderecoEventoDTO.cep().equals(endereco.getCep()) || enderecoEventoDTO.numero() == null || !enderecoEventoDTO.numero().equals(endereco.getNumero());

        }

        return !enderecoEventoDTO.idExterno().equals(endereco.getIdExterno());
    }

}
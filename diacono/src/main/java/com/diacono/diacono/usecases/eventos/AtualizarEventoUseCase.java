package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.evento.EnderecoEventoDTO;
import com.diacono.diacono.applications.dtos.evento.EventoUpdateDTO;
import com.diacono.diacono.applications.mappers.endereco.EnderecoEventoMapper;
import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.entity.Evento;
import com.diacono.diacono.domain.repository.EventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.escalasevento.GerarEscalaEventoUseCase;
import com.diacono.diacono.usecases.eventos.validation.ValidarIdExternoPreenchido;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class AtualizarEventoUseCase {

    private final EventoRepository eventoRepository;
    private final BuscarEnderecoEventoPorUUIDUseCase buscarEnderecoEventoPorUUIDUseCase;
    private final ValidarIdExternoPreenchido validarIdExternoPreenchido;
    private final EnderecoEventoMapper enderecoEventoMapper;
    private final GerarEscalaEventoUseCase gerarEscalaEventoUseCase;

    public AtualizarEventoUseCase(EventoRepository eventoRepository, BuscarEnderecoEventoPorUUIDUseCase buscarEnderecoEventoPorUUIDUseCase, ValidarIdExternoPreenchido validarIdExternoPreenchido, EnderecoEventoMapper enderecoEventoMapper, GerarEscalaEventoUseCase gerarEscalaEventoUseCase) {
        this.eventoRepository = eventoRepository;
        this.buscarEnderecoEventoPorUUIDUseCase = buscarEnderecoEventoPorUUIDUseCase;
        this.validarIdExternoPreenchido = validarIdExternoPreenchido;
        this.enderecoEventoMapper = enderecoEventoMapper;
        this.gerarEscalaEventoUseCase = gerarEscalaEventoUseCase;
    }

    @Transactional
    public RestResponseMessageDTO execute(EventoUpdateDTO request, UUID idExterno){

        validarIdExternoPreenchido.validarIdExternoPreenchido(idExterno);
        Evento evento = buscarEventoPorUUID(idExterno);

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
            evento.setEscalaEvento(gerarEscalaEventoUseCase.executeParaAtualizacao(evento, evento.getEscalaEvento(), request.fkMinisterios()));
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

        return new RestResponseMessageDTO(HttpStatus.OK, "Evento atualizado com sucesso");

    }

    //metodos para validar

    private Evento buscarEventoPorUUID(UUID idExterno){

        Evento evento = eventoRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ObjectNotFoundException("Evento não encontrado"));

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
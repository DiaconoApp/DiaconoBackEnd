//package com.diacono.diacono.eventoministerio.service;
//
//import com.diacono.diacono.escala.service.EscalaService;
//import com.diacono.diacono.eventoministerio.model.dto.request.EventoMinisterioNaoConfirmadoDTO;
//import com.diacono.diacono.eventoministerio.model.dto.response.EventoMinisterioEscalaDTO;
//import com.diacono.diacono.eventoministerio.model.entity.EventoMinisterio;
//import com.diacono.diacono.eventoministerio.repository.EventoMinisterioRepository;
//import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
//import com.diacono.diacono.global.util.JwtUtils;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//
//
//@Service
//public class EventoMinisterioService {
//    private final EscalaService escalaService;
//    private final EventoMinisterioRepository eventoMinisterioRepository;
//    private final JwtUtils jwtUtils;
//
//    public EventoMinisterioService(
//            @Lazy EscalaService escalaService,
//            EventoMinisterioRepository eventoMinisterioRepository, JwtUtils jwtUtils
//    ) {
//        this.escalaService = escalaService;
//        this.eventoMinisterioRepository = eventoMinisterioRepository;
//        this.jwtUtils = jwtUtils;
//    }
//
//    public List<EventoMinisterioEscalaDTO> buscarEventosMinisteriosPorMinisterioMesAno (UUID idExternoMinisterio, Integer mes, Integer ano) {
//        List<EventoMinisterioEscalaDTO> eventosMinisterio = eventoMinisterioRepository
//                .findByEventoMinisterioAndMesAndAno(idExternoMinisterio, mes, ano);
//
//        return eventosMinisterio;
//    }
//
//    public List<EventoMinisterioEscalaDTO> buscarEventosMinisteriosPorMesAno (Integer mes, Integer ano) {
//        List<EventoMinisterioEscalaDTO> eventosMinisterio = eventoMinisterioRepository
//                .findByMesAndAno(mes, ano);
//
//        return eventosMinisterio;
//    }
//
//    @Transactional
//    public EventoMinisterio salvarEventoMinisterio (EventoMinisterio eventoMinisterio) {
//        return eventoMinisterioRepository.save(eventoMinisterio);
//    }
//
//    @Transactional
//    public EventoMinisterio confirmarEventoMinisterio (EventoMinisterioNaoConfirmadoDTO eventoMinisterio) {
//        if(escalaService.buscarEscalasPorEventoMinisterioId(eventoMinisterio.idExterno()).isEmpty()) throw new RuntimeException("Não é possível confirmar as escalas sem escalas atribuídas.");
//
//        EventoMinisterio eventoMinisterioAConfirmar = eventoMinisterioRepository.findByIdExterno(eventoMinisterio.idExterno());
//
//        if(eventoMinisterioAConfirmar == null) throw new ObjectNotFoundException("Evento do ministério não encontrado para o ID fornecido.");
//
//        eventoMinisterioAConfirmar.setIsConfirmado(true);
//
//        return eventoMinisterioRepository.save(eventoMinisterioAConfirmar);
//    }
//
//    @Transactional
//    public Integer deletarEventoMinisterio (EventoMinisterio eventoMinisterio) {
//        if(eventoMinisterioRepository.findByIdExterno(eventoMinisterio.getIdExterno()) == null) throw new ObjectNotFoundException("Evento do ministério não encontrado para o ID fornecido.");
//
//        escalaService.excluirEscalasPorEventoMinisterio(eventoMinisterio.getIdExterno());
//
//        return eventoMinisterioRepository.deleteByEvento(eventoMinisterio.getEvento());
//    }
//
//
//    public EventoMinisterio buscarEventosMinisteriosPorIdExternoEvento (UUID IdExternoEvento) {
//        EventoMinisterio eventoMinisterio = eventoMinisterioRepository.findByIdExterno(IdExternoEvento);
//
//        if (eventoMinisterio == null) {
//            throw new ObjectNotFoundException("Nenhum evento ministério encontrado para o ID fornecido.");
//        }
//
//        return eventoMinisterio;
//    }
//
//
//
//}

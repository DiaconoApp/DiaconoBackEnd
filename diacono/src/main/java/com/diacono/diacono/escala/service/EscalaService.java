//package com.diacono.diacono.escala.service;
//
//import com.diacono.diacono.escala.model.dto.request.EscalasSalvarDTO;
//import com.diacono.diacono.escala.model.dto.response.EscalaMembroDTO;
//import com.diacono.diacono.escala.model.entity.Escala;
//import com.diacono.diacono.escala.repository.EscalaRepository;
//import com.diacono.diacono.evento.model.dto.response.EventoUnicoSimplificadoDTO;
//import com.diacono.diacono.eventoministerio.model.entity.EventoMinisterio;
//import com.diacono.diacono.eventoministerio.service.EventoMinisterioService;
//import com.diacono.diacono.global.util.JwtUtils;
//import com.diacono.diacono.membro.model.dto.response.MembroSimplificadoDTO;
//import com.diacono.diacono.membro.service.MembroService;
//import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
//import com.diacono.diacono.membroministerio.service.MembroMinisterioService;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.*;
//// Adicionar validações de nulos nos parametros ?
//
//@Service
//public class EscalaService {
//
//    private final MembroMinisterioService membroMinisterioService;
//    private final EventoMinisterioService eventoMinisterioService;
//    private final MembroService membroService;
//    private final EscalaRepository escalaRepository;
//    private final JwtUtils jwtUtils;
//
//    public EscalaService(MembroMinisterioService membroMinisterioService, EventoMinisterioService eventoMinisterioService, MembroService membroService, EscalaRepository escalaRepository, JwtUtils jwtUtils) {
//        this.membroMinisterioService = membroMinisterioService;
//        this.eventoMinisterioService = eventoMinisterioService;
//        this.membroService = membroService;
//        this.escalaRepository = escalaRepository;
//        this.jwtUtils = jwtUtils;
//    }
//
//    public List<MembroSimplificadoDTO> buscarMembrosMinisterioesAleatorios (UUID idExternoMinisterio, int quantidadeDeEscalas, EventoUnicoSimplificadoDTO eventoUnicoSimplificadoDTO) {
//        List<MembroSimplificadoDTO> membrosMinisteriosLivres = membroService.buscarMembrosDisponiveisParaEscala(idExternoMinisterio, eventoUnicoSimplificadoDTO);
//
//        Integer quantidadeMembrosLivres = membrosMinisteriosLivres.size();
//
//        if (quantidadeMembrosLivres == 0) throw new RuntimeException("Nenhum membro disponível para gerar as escalas");
//        if (quantidadeMembrosLivres < quantidadeDeEscalas) throw new RuntimeException("Quantidade de membros livres insuficiente para gerar as escalas");
//
//        Collections.shuffle(membrosMinisteriosLivres);
//        return membrosMinisteriosLivres.subList(0, quantidadeDeEscalas -1);
//    }
//
//    public List<EscalaMembroDTO> buscarEscalasPorEventoMinisterioId (UUID eventoMinisterioId) {
//        List<EscalaMembroDTO> escalas = escalaRepository.findByEventoMinisterioId(eventoMinisterioId);
//
//        return escalas;
//    }
//
//    public List<EscalaMembroDTO> buscarEscalasPorMembroIdMesAno (Integer mes, Integer ano) {
//        UUID idExternoMembroMinisterio = jwtUtils.getSubject();
//
//        List<EscalaMembroDTO> escalasMembro = escalaRepository
//                .findByMembroIdAndMesAndAno(idExternoMembroMinisterio, mes, ano);
//
//        if (escalasMembro.isEmpty()) {
//            throw new RuntimeException("Nenhuma escala encontrada para o membro no mês e ano informados");
//        }
//
//        return escalasMembro;
//    }
//
//    @Transactional
//    public List<Escala> salvarEscalasPorEventoMinisterio (UUID idExternoEventoMinisterio, EscalasSalvarDTO escalasSalvarDTO) {
//        List<Escala> escalasParaSalvar = new ArrayList<>();
//        List<MembroMinisterio> membrosMinisterio = membroMinisterioService.buscarMembroMinisterioPorId(escalasSalvarDTO.fkMembrosMinisterio());
//        EventoMinisterio eventoMinisterio = eventoMinisterioService.buscarEventosMinisteriosPorIdExternoEvento(idExternoEventoMinisterio);
//        for (MembroMinisterio membroMinisterio : membrosMinisterio) {
//            Escala escala = new Escala();
//            escala.setMembroMinisterio(membroMinisterio);
//            escala.setEventoMinisterio(eventoMinisterio);
//
//            escalasParaSalvar.add(escala);
//        }
//
//        return escalaRepository.saveAll(escalasParaSalvar);
//    }
//
//    public List<Escala> editarEscalasPorEventoMinisterio (UUID idExternoEventoMinisterio, EscalasSalvarDTO escalasSalvarDTO) {
//        if (escalasSalvarDTO.fkMembrosMinisterio().isEmpty()) throw new RuntimeException("A lista de membros para escala não pode estar vazia.");
//        if (idExternoEventoMinisterio == null) throw new RuntimeException("O ID externo do evento ministério não pode ser nulo.");
//
//        return salvarEscalasPorEventoMinisterio(idExternoEventoMinisterio, escalasSalvarDTO);
//    }
//
//    @Transactional
//    public Integer excluirEscalasPorEventoMinisterio (UUID eventoMinisterioId) {
//        if(eventoMinisterioId.toString().isEmpty()) throw new RuntimeException("O ID externo do evento ministério não pode ser nulo ou vazio.");
//
//        return escalaRepository.deleteByEventoMinisterioId(eventoMinisterioId);
//    }
//}

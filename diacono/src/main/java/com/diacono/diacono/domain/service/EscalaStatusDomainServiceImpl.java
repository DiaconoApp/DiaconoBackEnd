package com.diacono.diacono.domain.service;

import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.EventoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EscalaStatusDomainServiceImpl implements EscalaStatusDomainService {

	private static final Logger logger = LoggerFactory.getLogger(EscalaStatusDomainServiceImpl.class);

	private final EscalaMinisterioRepository escalaMinisterioRepository;
	private final EscalaEventoRepository escalaEventoRepository;
	private final EventoRepository eventoRepository;

	public EscalaStatusDomainServiceImpl(
			EscalaMinisterioRepository escalaMinisterioRepository,
			EscalaEventoRepository escalaEventoRepository,
			EventoRepository eventoRepository
	) {
		this.escalaMinisterioRepository = escalaMinisterioRepository;
		this.escalaEventoRepository = escalaEventoRepository;
		this.eventoRepository = eventoRepository;
	}

	@Override
	public void recalcularStatusPorEscalaEventoId(UUID escalaEventoId) {
		if (escalaEventoId == null) {
			logger.warn("Tentativa de recalcular status com escalaEventoId nulo");
			return;
		}

		EnumStatusEvento statusEscalaEvento = escalaMinisterioRepository.areAllConfirmadosByEscalaEventoId(escalaEventoId)
				? EnumStatusEvento.CONFIRMADO
				: EnumStatusEvento.PENDENTE;

		escalaEventoRepository.updateStatusByEscalaEventoId(escalaEventoId, statusEscalaEvento);
		logger.info("Status de EscalaEvento atualizado [escalaEventoId={}, novoStatus={}]",
			escalaEventoId, statusEscalaEvento);

		UUID eventoId = escalaEventoRepository.findEventoIdByEscalaEventoId(escalaEventoId);
		if (eventoId == null) {
			logger.debug("EscalaEvento sem evento associado [escalaEventoId={}]", escalaEventoId);
			return;
		}

		recalcularStatusEvento(eventoId);
	}

	@Override
	public void recalcularStatusEvento(UUID eventoId) {
		if (eventoId == null) {
			logger.warn("Tentativa de recalcular status com eventoId nulo");
			return;
		}

		EnumStatusEvento statusEvento = escalaEventoRepository.areAllConfirmadosByEventoId(eventoId)
				? EnumStatusEvento.CONFIRMADO
				: EnumStatusEvento.PENDENTE;

		eventoRepository.updateStatusByEventoId(eventoId, statusEvento);
		logger.info("Status de Evento atualizado [eventoId={}, novoStatus={}]",
			eventoId, statusEvento);
	}
}


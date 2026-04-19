package com.diacono.diacono.domain.service;

import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EscalaStatusDomainServiceImpl implements EscalaStatusDomainService {

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
		EnumStatusEvento statusEscalaEvento = escalaMinisterioRepository.areAllConfirmadosByEscalaEventoId(escalaEventoId)
				? EnumStatusEvento.CONFIRMADO
				: EnumStatusEvento.PENDENTE;

		escalaEventoRepository.updateStatusByEscalaEventoId(escalaEventoId, statusEscalaEvento);

		UUID eventoId = escalaEventoRepository.findEventoIdByEscalaEventoId(escalaEventoId);
		if (eventoId == null) {
			return;
		}

		recalcularStatusEvento(eventoId);
	}

	@Override
	public void recalcularStatusEvento(UUID eventoId) {
		EnumStatusEvento statusEvento = escalaEventoRepository.areAllConfirmadosByEventoId(eventoId)
				? EnumStatusEvento.CONFIRMADO
				: EnumStatusEvento.PENDENTE;

		eventoRepository.updateStatusByEventoId(eventoId, statusEvento);
	}
}


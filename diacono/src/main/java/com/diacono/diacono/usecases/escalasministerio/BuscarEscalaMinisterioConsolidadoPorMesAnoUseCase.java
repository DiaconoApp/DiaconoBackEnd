package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.escalasevento.EscalaEventoConsolidadoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEvento;
import com.diacono.diacono.domain.repository.EscalaEventoRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase {
	// TODO: Mudar a funcao chamado do repo
	// TODO: Buscar ministerios por ID deve ser uma outra chamada, endpoint

	private final EscalaEventoRepository escalaEventoRepository;
	private final MembroMinisterioRepository membroMinisterioRepository;

	public BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase(
			EscalaEventoRepository escalaEventoRepository,
			MembroMinisterioRepository membroMinisterioRepository
	) {
		this.escalaEventoRepository = escalaEventoRepository;
		this.membroMinisterioRepository = membroMinisterioRepository;
	}

	public List<EscalaEventoConsolidadoDTO> execute(
			UUID idIgreja,
			UUID idMembro,
			Integer mes,
			Integer ano,
			EnumStatusEvento status,
			String nomeEvento
	) {
		validarMesEAno(mes, ano);

		YearMonth anoMes = YearMonth.of(ano, mes);
		LocalDateTime inicioMes = anoMes.atDay(1).atStartOfDay();
		LocalDateTime fimMes = anoMes.atEndOfMonth().atTime(23, 59, 59);

		UUID ministerioId = buscarMinisterioLider(idMembro, idIgreja);

		return escalaEventoRepository
				.findEscalaEventoConsolidadoByPeriodo(idIgreja, inicioMes, fimMes, status, ministerioId, nomeEvento);
	}

	private UUID buscarMinisterioLider(UUID idMembro, UUID idIgreja) {
		List<MinisterioSuperSimplificadoDTO> ministerios = membroMinisterioRepository.buscarMinisterioLider(idMembro, idIgreja);

		if (ministerios.isEmpty()) {
			throw new ObjectNotFoundException("Nenhum ministério encontrado para o líder informado.");
		}

		return ministerios.getFirst().idExterno();
	}

	private void validarMesEAno(int mes, int ano) {
		if (mes < 1 || mes > 12) {
			throw new FieldInvalidException("O mês precisa estar entre 1 e 12");
		}

		if (ano <= 0) {
			throw new FieldInvalidException("O ano precisa ser maior que 0");
		}
	}
}

package com.diacono.diacono.usecases.escalasministerio;

import com.diacono.diacono.applications.dtos.escalaministerio.EscalaMinisterioConsolidadoDTO;
import com.diacono.diacono.applications.dtos.ministerio.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.domain.enums.EnumStatusEscalaMinisterio;
import com.diacono.diacono.domain.repository.EscalaMinisterioRepository;
import com.diacono.diacono.domain.repository.MembroMinisterioRepository;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.escalasevento.validation.ValidarMesEAno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase {
	// TODO: Mudar a funcao chamado do repo
	// TODO: Buscar ministerios por ID deve ser uma outra chamada, endpoint
	private static final Logger logger = LoggerFactory.getLogger(BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase.class);

	private final EscalaMinisterioRepository escalaMinisterioRepository;
	private final MembroMinisterioRepository membroMinisterioRepository;
	private final ValidarMesEAno validarMesEAno;

	public BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase(
			EscalaMinisterioRepository escalaMinisterioRepository,
			MembroMinisterioRepository membroMinisterioRepository,
			ValidarMesEAno validarMesEAno) {
		this.escalaMinisterioRepository = escalaMinisterioRepository;
		this.membroMinisterioRepository = membroMinisterioRepository;
		this.validarMesEAno = validarMesEAno;
	}

	public List<EscalaMinisterioConsolidadoDTO> execute(
			UUID igrejaId,
			UUID  membroId,
			UUID ministerioId,
			Integer mes,
			Integer ano,
			EnumStatusEscalaMinisterio status,
			String nomeEvento
	) {
		validarIdsObrigatorios(igrejaId, membroId);
		validarMesEAno.validarMesEAno(mes, ano);

		List<UUID> listaMinisterios = montarMinisterioIds(igrejaId, membroId, ministerioId);

		YearMonth anoMes = YearMonth.of(ano, mes);
		LocalDateTime inicioMes = anoMes.atDay(1).atStartOfDay();
		LocalDateTime fimMes = anoMes.atEndOfMonth().atTime(23, 59, 59);
		String nomeEventoNormalizado = normalizarNomeEvento(nomeEvento);

		logger.info("Consulta consolidado escalas ministério: igrejaId=[{}], membroId=[{}], mes=[{}], ano=[{}], status=[{}], ministerioId=[{}], filtroNomeEvento=[{}]",
				igrejaId, membroId, mes, ano, status, ministerioId, nomeEventoNormalizado != null);

		return escalaMinisterioRepository
				.findEscalaMinisterioConsolidadoByPeriodo(igrejaId, inicioMes, fimMes, status, listaMinisterios, nomeEventoNormalizado);
	}

	private List<UUID> montarMinisterioIds (UUID igrejaId, UUID  membroId, UUID ministerioId) {
		List<UUID> listaMinisterios = membroMinisterioRepository
				.buscarMinisterioLider(membroId, igrejaId)
				.stream()
				.map(MinisterioSuperSimplificadoDTO::idExterno)
				.distinct()
				.toList();

		if(listaMinisterios.isEmpty()){
			throw new ObjectNotFoundException("Nenhum ministério encontrado para o líder informado.");
		}

		if (ministerioId != null) {
			validarMinisterioId(listaMinisterios, ministerioId);
			return List.of(ministerioId);
		}

		return listaMinisterios;
	}

	private void validarMinisterioId(List<UUID> ministeriosDoLider, UUID ministerioId) {
		if (!ministeriosDoLider.contains(ministerioId)) {
			throw new ObjectNotFoundException("O líder informado não possui vínculo com o ministério solicitado.");
		}
	}

	private void validarIdsObrigatorios(UUID igrejaId, UUID membroId) {
		if (igrejaId == null || membroId == null) {
			logger.warn("Consulta consolidado escalas ministério com ids inválidos: igrejaIdPresente=[{}], membroIdPresente=[{}]",
					igrejaId != null, membroId != null);
			throw new FieldInvalidException("Ids de igreja e membro sao obrigatórios");
		}
	}

	private String normalizarNomeEvento(String nomeEvento) {
		return Optional.ofNullable(nomeEvento)
				.map(String::trim)
				.filter(nome -> !nome.isBlank())
				.orElse(null);
	}
}

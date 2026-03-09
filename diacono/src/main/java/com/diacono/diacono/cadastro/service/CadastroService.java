package com.diacono.diacono.cadastro.service;

import com.diacono.diacono.Igreja.model.dto.response.IgrejaSemiCompletoDTO;
import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.cadastro.model.dto.CadastroExternoDTO;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.service.MembroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servico de cadastro publico de membros
 * OWASP A01: Implementa validacoes de negocio antes de persistir dados sensiveis
 */
@Service
public class CadastroService {

    // OWASP A05: Logging seguro sem expor dados sensiveis
    private static final Logger logger = LoggerFactory.getLogger(CadastroService.class);

    private final IgrejaService igrejaService;
    private final MembroService membroService;

    public CadastroService(IgrejaService igrejaService, MembroService membroService) {
        this.igrejaService = igrejaService;
        this.membroService = membroService;
    }

    /**
     * Busca lista de igrejas disponiveis para cadastro publico
     * OWASP A01: Endpoint publico que nao expoe dados sensiveis de membros
     */
    public List<IgrejaSemiCompletoDTO> buscarIgrejas() {
        logger.debug("Buscando lista de igrejas disponiveis para cadastro");

        List<IgrejaSemiCompletoDTO> igrejas = igrejaService.buscarIgrejas();
        return igrejas;
    }

    /**
     * Cadastra novo membro via formulario publico
     * OWASP A02/A07: Delegacao de criptografia de senha ao MembroService
     * OWASP A01: Validacoes de entrada antes de persistir
     */
    public RestResponseMessage cadastrarMembro(CadastroExternoDTO cadastroDTO) {
        try {
            // OWASP A02: Delegacao de hash de senha ao MembroService (usa BCrypt)
            Membro membro = membroService.criarMembroExterno(cadastroDTO);

            // OWASP A05: Log de sucesso sem expor PII (apenas ID interno)
            logger.info("Membro cadastrado com sucesso, ID interno: {}", membro.getIdInterno());

            return new RestResponseMessage(HttpStatus.CREATED, "Usuario cadastrado com sucesso");

        } catch (ObjectNotFoundException e) {
            // OWASP A05: Log interno detalhado, mensagem generica ao usuario
            logger.warn("Falha no cadastro: recurso nao encontrado - {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            // OWASP A05: Tratamento de excecoes inesperadas sem expor stack trace
            logger.error("Erro inesperado durante cadastro de membro: {}", e.getMessage());
            throw new RuntimeException("Falha ao processar cadastro", e);
        }
    }
}

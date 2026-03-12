package com.diacono.diacono.membroministerio.service;

import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import com.diacono.diacono.global.util.JwtUtils;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membroministerio.mapper.MembroMinisterioMapper;
import com.diacono.diacono.membroministerio.model.dto.response.MembroMinisterioInfoMembroDTO;
import com.diacono.diacono.membroministerio.model.entity.EnumCargoMembroMinisterio;
import com.diacono.diacono.membroministerio.model.entity.MembroMinisterio;
import com.diacono.diacono.membroministerio.repository.MembroMinisterioRepository;
import com.diacono.diacono.membroministerio.model.dto.response.MinisterioDashEvolucaoDTO;
import com.diacono.diacono.membroministerio.model.dto.response.MinisterioDashQuantidadeMembrosDTO;
import com.diacono.diacono.ministerio.model.dto.response.MinisterioSuperSimplificadoDTO;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class MembroMinisterioService {

    private final MembroMinisterioRepository membroMinisterioRepository;
    private final MembroMinisterioMapper mapper;
    private final JwtUtils jwtUtils;

    public MembroMinisterioService(MembroMinisterioRepository membroMinisterioRepository, MembroMinisterioMapper mapper, JwtUtils jwtUtils) {
        this.membroMinisterioRepository = membroMinisterioRepository;
        this.mapper = mapper;
        this.jwtUtils = jwtUtils;
    }

    // OWASP A05: validacao defensiva antes de deletar do banco.
    public void apagarMembroMinisterioPorMembro(Membro membro){
        if (membro == null) {
            throw new ObjectNotFoundException("Membro não pode ser nulo para remover associações com ministérios.");
        }

        int count = membroMinisterioRepository.deleteByMembro(membro);
    }

    // OWASP A05: validacao defensiva antes de persistir payload.
    public void salvarTodos(MembroMinisterio membrosMinisterios){
        if (membrosMinisterios == null) {
            throw new ObjectSaveErrorException("MembroMinisterio não pode ser nulo.");
        }

        MembroMinisterio membroMinisterios = membroMinisterioRepository.save(membrosMinisterios);

        if(membroMinisterios == null || membroMinisterios.getIdInterno() == null){
            throw new ObjectSaveErrorException("Nenhum membro_ministerio foi salvo.");
        }
    }

    public Page<MembroMinisterioInfoMembroDTO> buscarPorMembroMinisterioComFiltro(UUID idMinisterio, Pageable pageable, String texto, EnumStatusMembro status){

        String textoFormatado = null;
        if (texto != null && !texto.isBlank()) {
            textoFormatado = "%" + texto + "%";
        }

        Page<MembroMinisterio> page = membroMinisterioRepository.buscarPorMembroMinisterioComFiltro(pageable,idMinisterio, textoFormatado, status);

        if(page.isEmpty()){
            throw new ObjectNotFoundException("Nenhum membro_ministerio encontrado com os filtros informados.");
        }

        //fazer o mapper para MembroMinisterioDTO
        Page<MembroMinisterioInfoMembroDTO> response = page.map(mapper::paraMembroMinisterioInfoMembroDTO);


        return response;
    }

    public Page<MembroMinisterioInfoMembroDTO> buscarPorMembroMinisterioSemFiltro(UUID idMinisterio, Pageable pageable){


        Page<MembroMinisterio> page = membroMinisterioRepository.buscarPorMembroMinisterioSemFiltro(pageable, idMinisterio);

        if(page.isEmpty()){
            throw new ObjectNotFoundException("Nenhum membro_ministerio encontrado com os filtros informados.");
        }

        //fazer o mapper para MembroMinisterioDTO
        Page<MembroMinisterioInfoMembroDTO> response = page.map(mapper::paraMembroMinisterioInfoMembroDTO);

        return response;
    }

    public void adicionarMembroMinisterioLiderMinisterio(Long idMinisterio, Long idMembro){
        // OWASP A01/A05: valida identificadores antes de criar entidade.
        if (idMinisterio == null || idMinisterio <= 0) {
            throw new ObjectSaveErrorException("ID do ministério inválido.");
        }
        if (idMembro == null || idMembro <= 0) {
            throw new ObjectSaveErrorException("ID do membro inválido.");
        }

        Ministerio ministerio = Ministerio.builder()
                .idInterno(idMinisterio)
                .build();

        Membro membro = Membro.builder()
                .idInterno(idMembro)
                .build();

        LocalDate dataHoje = LocalDate.now();

        MembroMinisterio membroMinisterio = MembroMinisterio.builder()
                .ministerio(ministerio)
                .membro(membro)
                .cargoMembro(EnumCargoMembroMinisterio.MEMBRO_MINISTERIO)
                .nomeMinisterio(ministerio.getNome())
                .dataRegistro(dataHoje)
                .build();

        MembroMinisterio membroMinisterioSalvo = membroMinisterioRepository.save(membroMinisterio);

        if(membroMinisterioSalvo.getIdInterno() == null){
            throw new ObjectSaveErrorException("Membro do ministério não foi salvo.");
        }
    }

    // OWASP A01/A07: remove membro validando identificadores e contexto de autenticacao.
    public void removerMembroMinisterioLiderMinisterio(UUID idMinisterio, UUID idMembro){
        // OWASP A05: valida UUIDs antes de fazer delete no banco.
        if (idMinisterio == null) {
            throw new ObjectNotFoundException("ID do ministério não pode ser nulo.");
        }
        if (idMembro == null) {
            throw new ObjectNotFoundException("ID do membro não pode ser nulo.");
        }

        int count = membroMinisterioRepository.deleteByMembroIdExternoAndMinisterioIdExterno(idMembro, idMinisterio);

        if(count == 0){
            throw new ObjectNotFoundException("Membro do ministério não encontrado para remoção.");
        }
    }

    // OWASP A01/A07: consulta sensivel validando contexto autenticado.
    public List<MinisterioSuperSimplificadoDTO> buscarMinisterioLider(UUID idMembro, UUID idIgreja){
        // OWASP A05: valida UUIDs antes de consulta.
        if (idMembro == null) {
            throw new ObjectNotFoundException("ID do membro não pode ser nulo.");
        }
        if (idIgreja == null) {
            throw new ObjectNotFoundException("ID da igreja não pode ser nulo.");
        }

        List<MinisterioSuperSimplificadoDTO> ministerios = membroMinisterioRepository.buscarMinisterioLider(idMembro, idIgreja);

        if(ministerios.isEmpty()){
            throw new ObjectNotFoundException("Nenhum ministério encontrado para o líder informado.");
        }

        return ministerios;
    }

    // OWASP A05: metodo chamado em escala, valida input antes de consulta.
    public List<MembroMinisterio> buscarMembroMinisterioPorId(List<UUID> idsExternoMembroMinisterio) {
        // OWASP A05: valida lista antes de usar.
        if (idsExternoMembroMinisterio == null || idsExternoMembroMinisterio.isEmpty()) {
            throw new ObjectNotFoundException("Lista de IDs não pode ser nula ou vazia.");
        }

        List<MembroMinisterio> membrosMinisterio = membroMinisterioRepository
                .findAllByIdExternoIn(idsExternoMembroMinisterio);

        if(membrosMinisterio.isEmpty()){
            throw new ObjectNotFoundException("Nenhum membro_ministerio encontrado para os IDs fornecidos.");
        }

        return membrosMinisterio;
    }

    // Dashboard - OWASP A01/A07: metodos de dashboard com validacao de contexto autenticado.

    public List<MinisterioDashEvolucaoDTO> ministerioBuscarDashEvolucao(int anoInicio, int anoFim, UUID idMinisterio){
        // OWASP A01/A07: obtem contexto autenticado de forma segura.
        UUID idIgreja = jwtUtils.getIgrejaId();

        // OWASP A05: valida ID ministerio se fornecido.
        if (idMinisterio != null && idMinisterio.toString().isBlank()) {
            throw new ObjectNotFoundException("ID do ministério inválido se fornecido.");
        }

        if(anoInicio == anoFim){
            List<MinisterioDashEvolucaoDTO> response = membroMinisterioRepository.buscarDashEvolucaoUmAno(anoFim, idMinisterio, idIgreja);
            return response;
        }

        List<MinisterioDashEvolucaoDTO> response = membroMinisterioRepository.buscarDashEvolucaoPeriodo(anoInicio, anoFim, idMinisterio, idIgreja);

        return response;
    }

    // OWASP A01/A07: dashboard restrito ao contexto autenticado.
    public List<MinisterioDashQuantidadeMembrosDTO> ministerioBuscarDashQuantidadeMembro(int anoInicio, int anoFim){
        // OWASP A01/A07: obtem contexto autenticado de forma segura.
        UUID igrejaId = jwtUtils.getIgrejaId();

        List<MinisterioDashQuantidadeMembrosDTO> response = membroMinisterioRepository.buscarQuantidadeMembros(anoInicio, anoFim, igrejaId);
        return response;
    }

}

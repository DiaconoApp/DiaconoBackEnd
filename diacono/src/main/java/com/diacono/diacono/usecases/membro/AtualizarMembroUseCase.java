package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.applications.dtos.RestResponseMessageDTO;
import com.diacono.diacono.applications.dtos.membro.EnderecoMembroDTO;
import com.diacono.diacono.applications.dtos.membro.MembroUpdateDTO;
import com.diacono.diacono.domain.entity.EnderecoMembro;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
public class AtualizarMembroUseCase {

    private final MembroRepository membroRepository;

    public AtualizarMembroUseCase(MembroRepository membroRepository) {
        this.membroRepository = membroRepository;
    }

    @Transactional
    public RestResponseMessageDTO execute(UUID idExterno, MembroUpdateDTO request) {
        Membro membro = membroRepository.findByIdExterno(idExterno)
            .orElseThrow(() -> new ObjectNotFoundException("Membro não encontrado"));

        if (request.nome() != null) membro.setNome(request.nome().toLowerCase(Locale.ROOT));
        if (request.email() != null) membro.setEmail(request.email().toLowerCase(Locale.ROOT));
        if (request.celular() != null) membro.setCelular(request.celular());
        if (request.dataNascimento() != null) membro.setDataNascimento(request.dataNascimento());
        if (request.cargo() != null) membro.setCargoMembro(request.cargo());

        if (request.membroEnderecoDTO() != null) {
            atualizarEndereco(membro, request.membroEnderecoDTO());
        }

        membroRepository.save(membro);
        return new RestResponseMessageDTO(HttpStatus.OK, "Membro atualizado com sucesso");
    }

    private void atualizarEndereco(Membro membro, EnderecoMembroDTO dto) {
        EnderecoMembro endereco = membro.getEnderecoMembro();
        if (endereco == null) {
            endereco = new EnderecoMembro();
            membro.setEnderecoMembro(endereco);
        }
        if (dto.cep() != null) endereco.setCep(dto.cep());
        if (dto.estado() != null) endereco.setEstado(dto.estado());
        if (dto.cidade() != null) endereco.setCidade(dto.cidade());
        if (dto.bairro() != null) endereco.setBairro(dto.bairro());
        if (dto.rua() != null) endereco.setRua(dto.rua());
        if (dto.numero() != null) {
            try { endereco.setNumero(Integer.parseInt(dto.numero())); } catch (NumberFormatException ignored) {}
        }
        endereco.setComplemento(dto.complemento());
    }
}
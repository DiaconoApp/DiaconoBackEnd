package com.diacono.diacono.usecases.membro;

import com.diacono.diacono.domain.repository.MembroRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.domain.entity.Membro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BuscarPorEmaiUseCase {

    private final MembroRepository membroRepository;

    public BuscarPorEmaiUseCase(MembroRepository membroRepository) {
        this.membroRepository = membroRepository;
    }

    @Transactional
    public Membro execute(String email) {
        Membro membro = membroRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("Membro não encontrado com o email fornecido."));

        return membro;
    }
}
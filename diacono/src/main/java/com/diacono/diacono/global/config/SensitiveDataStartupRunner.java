package com.diacono.diacono.global.config;

import com.diacono.diacono.domain.entity.GoogleRefreshTokenMembro;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.global.util.SensitiveFieldCryptoUtils;
import com.diacono.diacono.infrastructure.persistence.springdata.GoogleRefreshTokenMembroJpaRepository;
import com.diacono.diacono.infrastructure.persistence.springdata.MembroJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class SensitiveDataStartupRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveDataStartupRunner.class);

    private final MembroJpaRepository membroJpaRepository;
    private final GoogleRefreshTokenMembroJpaRepository googleRefreshTokenMembroJpaRepository;

    public SensitiveDataStartupRunner(
            MembroJpaRepository membroJpaRepository,
            GoogleRefreshTokenMembroJpaRepository googleRefreshTokenMembroJpaRepository
    ) {
        this.membroJpaRepository = membroJpaRepository;
        this.googleRefreshTokenMembroJpaRepository = googleRefreshTokenMembroJpaRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        SensitiveFieldCryptoUtils.validateConfigured();
        atualizarMembrosSemIndices();
        atualizarRefreshTokensSemIndices();
    }

    private void atualizarMembrosSemIndices() {
        List<Membro> membrosSemIndice = membroJpaRepository.findAll()
                .stream()
                .filter(membro -> !membro.possuiIndicesSensiveis())
                .peek(Membro::atualizarIndicesCamposSensiveis)
                .toList();

        if (!membrosSemIndice.isEmpty()) {
            membroJpaRepository.saveAll(membrosSemIndice);
            logger.info("Indices de campos sensiveis atualizados para membros: quantidade=[{}]", membrosSemIndice.size());
        }
    }

    private void atualizarRefreshTokensSemIndices() {
        List<GoogleRefreshTokenMembro> tokensSemIndice = googleRefreshTokenMembroJpaRepository.findAll()
                .stream()
                .filter(token -> !token.possuiIndicesSensiveis())
                .peek(GoogleRefreshTokenMembro::atualizarIndicesCamposSensiveis)
                .toList();

        if (!tokensSemIndice.isEmpty()) {
            googleRefreshTokenMembroJpaRepository.saveAll(tokensSemIndice);
            logger.info("Indices de campos sensiveis atualizados para refresh tokens: quantidade=[{}]", tokensSemIndice.size());
        }
    }
}

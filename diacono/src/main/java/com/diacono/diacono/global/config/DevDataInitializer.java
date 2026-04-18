package com.diacono.diacono.global.config;

import com.diacono.diacono.infrastructure.persistence.Membro.MembroJpaRepository;
import com.diacono.diacono.domain.entity.Membro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Corrige os hashes de senha do data.sql para que todos os membros seed
 * possam fazer login com a senha "123456" em ambiente de desenvolvimento.
 *
 * Roda apenas quando o profile NÃO é "prod".
 */
@Component
@Profile("!prod")
public class DevDataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DevDataInitializer.class);
    private static final String DEV_PASSWORD = "123456";

    private final MembroJpaRepository membroJpaRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DevDataInitializer(MembroJpaRepository membroJpaRepository, BCryptPasswordEncoder passwordEncoder) {
        this.membroJpaRepository = membroJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        List<Membro> membros = membroJpaRepository.findAll();

        if (membros.isEmpty()) {
            logger.warn("DevDataInitializer: nenhum membro encontrado no banco — data.sql pode não ter sido carregado.");
            return;
        }

        String hash = passwordEncoder.encode(DEV_PASSWORD);
        membros.forEach(m -> m.setSenha(hash));
        membroJpaRepository.saveAll(membros);

        logger.info("DevDataInitializer: senha '{}' aplicada a {} membros do seed.", DEV_PASSWORD, membros.size());
        logger.info("DevDataInitializer: contas disponíveis — admin@diacono.com (GOVERNO), joao.pastor@email.com (LIDER), pedro.dc@email.com (MEMBRO)");
    }
}

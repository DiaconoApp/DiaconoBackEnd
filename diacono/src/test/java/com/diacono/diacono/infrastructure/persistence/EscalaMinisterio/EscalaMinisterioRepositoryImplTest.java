package com.diacono.diacono.infrastructure.persistence.EscalaMinisterio;

import com.diacono.diacono.domain.entity.EscalaMinisterio;
import com.diacono.diacono.infrastructure.persistence.springdata.EscalaMinisterioJpaRepository;
import com.diacono.diacono.infrastructure.persistence.gateway.EscalaMinisterioRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class EscalaMinisterioRepositoryImplTest {

    @Mock
    private EscalaMinisterioJpaRepository jpaRepository;

    @InjectMocks
    private EscalaMinisterioRepositoryImpl repository;

    @Test
    void deveApagarEscalasAnterioresEInserirListaNova() {
        UUID igrejaId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID escalaEventoId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        List<EscalaMinisterio> escalasParaSalvar = List.of(
                EscalaMinisterio.builder().build(),
                EscalaMinisterio.builder().build()
        );

        repository.replaceEscalaMinisterioByEscalaEventoId(igrejaId, escalaEventoId, escalasParaSalvar);

        InOrder inOrder = inOrder(jpaRepository);
        inOrder.verify(jpaRepository).deleteByEscalaEventoIdAndIgrejaId(igrejaId, escalaEventoId);
        inOrder.verify(jpaRepository).saveAll(escalasParaSalvar);
        verifyNoMoreInteractions(jpaRepository);
    }
}


package com.diacono.diacono.usecases.googleauth;

import com.diacono.diacono.domain.entity.GoogleRefreshTokenMembro;
import com.diacono.diacono.domain.repository.GoogleRefreshTokenMembroRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class AtualizarSecretGoogleUseCaseTest {

    @Test
    void deveCriarNovoRegistroQuandoMembroAindaNaoPossuirRefreshTokenSalvo() {
        FakeGoogleRefreshTokenMembroRepository repository = new FakeGoogleRefreshTokenMembroRepository();
        AtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase = new AtualizarSecretGoogleUseCase(repository);

        UUID membroId = UUID.randomUUID();
        UUID igrejaId = UUID.randomUUID();
        String email = "usuario@teste.com";
        String refreshToken = "novo-refresh-token";

        atualizarSecretGoogleUseCase.execute(membroId, igrejaId, email, refreshToken);

        assertNotNull(repository.savedEntity);
        assertEquals(membroId, repository.savedEntity.getMembroId());
        assertEquals(igrejaId, repository.savedEntity.getIgrejaId());
        assertEquals(email, repository.savedEntity.getEmail());
        assertEquals(refreshToken, repository.savedEntity.getRefreshToken());
    }

    @Test
    void deveSalvarRefreshTokenNoMesmoEscopoDaIgrejaQuandoJaExistirRegistro() {
        UUID membroId = UUID.randomUUID();
        UUID igrejaId = UUID.randomUUID();
        GoogleRefreshTokenMembro existingEntity = GoogleRefreshTokenMembro.builder()
                .membroId(membroId)
                .igrejaId(igrejaId)
                .email("email-antigo@teste.com")
                .refreshToken("refresh-antigo")
                .createdAt(java.time.LocalDateTime.of(2026, 5, 17, 10, 0))
                .build();

        FakeGoogleRefreshTokenMembroRepository repository = new FakeGoogleRefreshTokenMembroRepository(existingEntity);
        AtualizarSecretGoogleUseCase atualizarSecretGoogleUseCase = new AtualizarSecretGoogleUseCase(repository);

        atualizarSecretGoogleUseCase.execute(membroId, igrejaId, "email-novo@teste.com", "refresh-novo");

        assertNotNull(repository.savedEntity);
        assertEquals(membroId, repository.savedEntity.getMembroId());
        assertEquals(igrejaId, repository.savedEntity.getIgrejaId());
        assertEquals("email-novo@teste.com", repository.savedEntity.getEmail());
        assertEquals("refresh-novo", repository.savedEntity.getRefreshToken());
        assertEquals(membroId, repository.lastFindByMembroId);
        assertEquals(existingEntity.getCreatedAt(), repository.savedEntity.getCreatedAt());
    }

    @Test
    void loginGoogleNaoDevePersistirNadaQuandoGoogleNaoRetornarRefreshToken() throws Exception {
        LoginGoogleUseCaseTest loginGoogleUseCaseTest = new LoginGoogleUseCaseTest();
        loginGoogleUseCaseTest.naoDevePersistirRefreshTokenQuandoGoogleNaoOEnviar();
    }

    private static final class FakeGoogleRefreshTokenMembroRepository implements GoogleRefreshTokenMembroRepository {
        private final GoogleRefreshTokenMembro existingEntity;
        private GoogleRefreshTokenMembro savedEntity;
        private UUID lastFindByMembroId;

        private FakeGoogleRefreshTokenMembroRepository() {
            this.existingEntity = null;
        }

        private FakeGoogleRefreshTokenMembroRepository(GoogleRefreshTokenMembro existingEntity) {
            this.existingEntity = existingEntity;
        }

        @Override
        public GoogleRefreshTokenMembro save(GoogleRefreshTokenMembro googleRefreshTokenMembro) {
            this.savedEntity = googleRefreshTokenMembro;
            return googleRefreshTokenMembro;
        }

        @Override
        public Optional<GoogleRefreshTokenMembro> findByMembroId(UUID membroId) {
            this.lastFindByMembroId = membroId;
            return Optional.ofNullable(existingEntity);
        }

        @Override
        public Optional<GoogleRefreshTokenMembro> findByEmail(String email) {
            return Optional.empty();
        }
    }
}

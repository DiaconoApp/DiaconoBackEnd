package com.diacono.diacono.cadastro.service;

import com.diacono.diacono.Igreja.model.dto.response.IgrejaSemiCompletoDTO;
import com.diacono.diacono.Igreja.service.IgrejaService;
import com.diacono.diacono.cadastro.model.dto.CadastroExternoDTO;
import com.diacono.diacono.global.dto.response.RestResponseMessage;
import com.diacono.diacono.membro.model.entity.EnumGeneroMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.service.MembroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastroServiceTest {

    @Mock
    private IgrejaService igrejaService;

    @Mock
    private MembroService membroService;

    @InjectMocks
    private CadastroService cadastroService;

    private UUID igrejaId;
    private CadastroExternoDTO cadastroExternoDTO;
    private IgrejaSemiCompletoDTO igrejaSemiCompletoDTO1;
    private IgrejaSemiCompletoDTO igrejaSemiCompletoDTO2;

    @BeforeEach
    void setUp() {
        igrejaId = UUID.randomUUID();

        cadastroExternoDTO = new CadastroExternoDTO(
            igrejaId,
            "João Silva",
            "12345678901",
            null,
            "joao@email.com",
            "11999999999",
            "123456",
            EnumGeneroMembro.FEMININO,
            null
        );

        igrejaSemiCompletoDTO1 = new IgrejaSemiCompletoDTO(
            UUID.randomUUID(),
            "Igreja Batista Central"
        );

        igrejaSemiCompletoDTO2 = new IgrejaSemiCompletoDTO(
            UUID.randomUUID(),
            "Igreja Metodista"
        );
    }

    @Test
    @DisplayName("Deve buscar todas as igrejas com sucesso")
    void buscarIgrejasSucesso() {
        List<IgrejaSemiCompletoDTO> igrejasEsperadas = Arrays.asList(igrejaSemiCompletoDTO1, igrejaSemiCompletoDTO2);

        when(igrejaService.buscarIgrejas()).thenReturn(igrejasEsperadas);

        List<IgrejaSemiCompletoDTO> result = cadastroService.buscarIgrejas();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(igrejaSemiCompletoDTO1, result.get(0));
        assertEquals(igrejaSemiCompletoDTO2, result.get(1));
        verify(igrejaService).buscarIgrejas();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há igrejas")
    void buscarIgrejasListaVazia() {
        List<IgrejaSemiCompletoDTO> igrejasVazias = Arrays.asList();

        when(igrejaService.buscarIgrejas()).thenReturn(igrejasVazias);

        List<IgrejaSemiCompletoDTO> result = cadastroService.buscarIgrejas();

        assertNotNull(result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(igrejaService).buscarIgrejas();
    }

    @Test
    @DisplayName("Deve cadastrar membro com sucesso")
    void cadastrarMembroSucesso() {
        Membro membroSalvo = new Membro();
        membroSalvo.setNome("João Silva");
        membroSalvo.setEmail("joao@email.com");
        ReflectionTestUtils.setField(membroSalvo, "idExterno", UUID.randomUUID());

        when(membroService.criarMembroExterno(cadastroExternoDTO)).thenReturn(membroSalvo);

        RestResponseMessage result = cadastroService.cadastrarMembro(cadastroExternoDTO);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatus());
        assertEquals("Usuário cadastrado com sucesso", result.getMessage());
        verify(membroService).criarMembroExterno(cadastroExternoDTO);
    }

    @Test
    @DisplayName("Deve lançar exceção quando dados de cadastro são nulos")
    void cadastrarMembroDadosNulosDeveRetornarErro() {
        when(membroService.criarMembroExterno(null)).thenThrow(new IllegalArgumentException("Dados de cadastro não podem ser nulos"));

        assertThrows(IllegalArgumentException.class, () -> cadastroService.cadastrarMembro(null));
        verify(membroService).criarMembroExterno(null);
    }

    @Test
    @DisplayName("Deve propagar exceção do serviço de membro ao cadastrar")
    void cadastrarMembroErroServicoMembroDeveRetornarErro() {
        RuntimeException exception = new RuntimeException("Erro interno do serviço");

        when(membroService.criarMembroExterno(cadastroExternoDTO)).thenThrow(exception);

        RuntimeException thrownException = assertThrows(RuntimeException.class,
            () -> cadastroService.cadastrarMembro(cadastroExternoDTO));

        assertEquals("Erro interno do serviço", thrownException.getMessage());
        verify(membroService).criarMembroExterno(cadastroExternoDTO);
    }

    @Test
    @DisplayName("Deve propagar exceção do serviço de igreja ao buscar igrejas")
    void buscarIgrejasErroServicoIgrejaDeveRetornarErro() {
        RuntimeException exception = new RuntimeException("Erro ao buscar igrejas");

        when(igrejaService.buscarIgrejas()).thenThrow(exception);

        RuntimeException thrownException = assertThrows(RuntimeException.class,
            () -> cadastroService.buscarIgrejas());

        assertEquals("Erro ao buscar igrejas", thrownException.getMessage());
        verify(igrejaService).buscarIgrejas();
    }
}
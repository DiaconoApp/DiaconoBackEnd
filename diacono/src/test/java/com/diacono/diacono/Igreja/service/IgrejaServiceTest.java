package com.diacono.diacono.Igreja.service;

import com.diacono.diacono.applications.mappers.igreja.IgrejaMapper;
import com.diacono.diacono.applications.dtos.igreja.IgrejaSemiCompletoDTO;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.infrastructure.persistence.Igreja.IgrejaJpaRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.use_cases.IgrejaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IgrejaServiceTest {

    @Mock
    private IgrejaJpaRepository igrejaJpaRepository;

    @Mock
    private IgrejaMapper igrejaMapper;

    @InjectMocks
    private IgrejaService igrejaService;

    private UUID igrejaId;

    @BeforeEach
    void setUp() {
        igrejaId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve buscar igreja por UUID com sucesso")
    void buscarUUIDSucesso() {
        Igreja igreja = new Igreja();
        igreja.setNome("Igreja Batista Central");

        when(igrejaJpaRepository.findByIdExterno(igrejaId)).thenReturn(igreja);

        Igreja result = igrejaService.buscarUUID(igrejaId);

        assertNotNull(result);
        assertEquals(igreja, result);
        assertEquals("Igreja Batista Central", result.getNome());
        verify(igrejaJpaRepository).findByIdExterno(igrejaId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando igreja não é encontrada por UUID")
    void buscarUUIDIgrejaNaoEncontradaDeveRetornarErro() {
        when(igrejaJpaRepository.findByIdExterno(igrejaId)).thenReturn(null);

        assertThrows(ObjectNotFoundException.class,
            () -> igrejaService.buscarUUID(igrejaId));

        verify(igrejaJpaRepository).findByIdExterno(igrejaId);
    }

    @Test
    @DisplayName("Deve buscar todas as igrejas com sucesso")
    void buscarIgrejasSucesso() {
        Igreja igreja1 = new Igreja();
        igreja1.setNome("Igreja Batista Central");

        Igreja igreja2 = new Igreja();
        igreja2.setNome("Igreja Presbiteriana");

        List<Igreja> igrejas = Arrays.asList(igreja1, igreja2);

        IgrejaSemiCompletoDTO dto1 = new IgrejaSemiCompletoDTO(
            UUID.randomUUID(), "Igreja Batista Central"
        );
        IgrejaSemiCompletoDTO dto2 = new IgrejaSemiCompletoDTO(
            UUID.randomUUID(), "Igreja Presbiteriana"
        );

        List<IgrejaSemiCompletoDTO> igrejasDTO = Arrays.asList(dto1, dto2);

        when(igrejaJpaRepository.findAll()).thenReturn(igrejas);
        when(igrejaMapper.paraListaIgrejaSemiCompletoDTO(igrejas)).thenReturn(igrejasDTO);

        List<IgrejaSemiCompletoDTO> result = igrejaService.buscarIgrejas();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Igreja Batista Central", result.getFirst().nome());
        assertEquals("Igreja Presbiteriana", result.get(1).nome());
        verify(igrejaJpaRepository).findAll();
        verify(igrejaMapper).paraListaIgrejaSemiCompletoDTO(igrejas);
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista de igrejas é nula")
    void buscarIgrejasListaNulaDeveRetornarErro() {
        when(igrejaJpaRepository.findAll()).thenReturn(null);

        assertThrows(ObjectNotFoundException.class,
            () -> igrejaService.buscarIgrejas());

        verify(igrejaJpaRepository).findAll();
        verify(igrejaMapper, never()).paraListaIgrejaSemiCompletoDTO(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista de igrejas está vazia")
    void buscarIgrejasListaVaziaDeveRetornarErro() {
        List<Igreja> igrejasVazias = new ArrayList<>();

        when(igrejaJpaRepository.findAll()).thenReturn(igrejasVazias);

        assertThrows(ObjectNotFoundException.class,
            () -> igrejaService.buscarIgrejas());

        verify(igrejaJpaRepository).findAll();
        verify(igrejaMapper, never()).paraListaIgrejaSemiCompletoDTO(any());
    }
}

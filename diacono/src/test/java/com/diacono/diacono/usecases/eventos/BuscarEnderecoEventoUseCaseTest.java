package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.evento.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.applications.mappers.endereco.EnderecoEventoMapper;
import com.diacono.diacono.domain.entity.EnderecoEvento;
import com.diacono.diacono.domain.entity.EnderecoIgreja;
import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.repository.EnderecoEventoRepository;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.usecases.igreja.BuscarIgrejaPorUUIDUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

//noinspection unused
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class BuscarEnderecoEventoUseCaseTest {

	@Mock
	private BuscarIgrejaPorUUIDUseCase buscarIgrejaPorUUIDUseCase;

	@Mock
	private EnderecoEventoRepository enderecoEventoRepository;

	@Mock
	private EnderecoEventoMapper enderecoEventoMapper;

	@InjectMocks
	private BuscarEnderecoEventoUseCase useCase;

	@Test
	void deveRetornarEnderecoEventoSimplificadoComSucesso() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");

		EnderecoIgreja enderecoIgreja = EnderecoIgreja.builder().cep("12345678").numero("10").build();
		Igreja igreja = Igreja.builder().nome("Igreja Central").enderecoIgreja(enderecoIgreja).build();

		EnderecoEvento enderecoEvento = EnderecoEvento.builder().cep("12345678").numero("10").build();
		EnderecoEventoSimplificadoDTO dto = new EnderecoEventoSimplificadoDTO(
				"12345678", "RUA A", "SAO PAULO", "CENTRO", "AP 1", "10", "SEDE",
				UUID.fromString("22222222-2222-2222-2222-222222222222")
		);

		when(buscarIgrejaPorUUIDUseCase.execute(idIgreja)).thenReturn(igreja);
		when(enderecoEventoRepository.findByCep("12345678", "10")).thenReturn(Optional.of(enderecoEvento));
		when(enderecoEventoMapper.paraEnderecoEventoSimplificadoDTO(enderecoEvento)).thenReturn(dto);

		EnderecoEventoSimplificadoDTO response = useCase.execute(idIgreja);

		assertSame(dto, response);
	}

	@Test
	void deveLancarExcecaoQuandoEnderecoEventoNaoEstiverRegistrado() {
		UUID idIgreja = UUID.fromString("11111111-1111-1111-1111-111111111111");

		EnderecoIgreja enderecoIgreja = EnderecoIgreja.builder().cep("12345678").numero("10").build();
		Igreja igreja = Igreja.builder().nome("Igreja Central").enderecoIgreja(enderecoIgreja).build();

		when(buscarIgrejaPorUUIDUseCase.execute(idIgreja)).thenReturn(igreja);
		when(enderecoEventoRepository.findByCep("12345678", "10")).thenReturn(Optional.empty());

		ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> useCase.execute(idIgreja));

		assertEquals("Endereço do evento não registrado", ex.getMessage());
	}
}
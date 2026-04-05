package com.diacono.diacono.usecases.eventos;

import com.diacono.diacono.applications.dtos.evento.EnderecoEventoSimplificadoDTO;
import com.diacono.diacono.usecases.EnderecoEventoService;
import org.springframework.stereotype.Service;

@Service
public class BuscarEnderecoEventoUseCase {

    private final EnderecoEventoService enderecoEventoService;

    public BuscarEnderecoEventoUseCase(EnderecoEventoService enderecoEventoService) {
        this.enderecoEventoService = enderecoEventoService;
    }

    public EnderecoEventoSimplificadoDTO execute() {
        return enderecoEventoService.buscarEnderecoIgreja();
    }
}
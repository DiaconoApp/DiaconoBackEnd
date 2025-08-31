package com.diacono.diacono.membros.controller;

import com.diacono.diacono.membros.dto.MembrosCreateRequestDTO;
import com.diacono.diacono.membros.dto.MembrosResponseDTO;
import com.diacono.diacono.membros.service.MembroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/membros")
public class MembrosController {

    private final MembroService membrosService;


    public MembrosController(MembroService membrosService) {
        this.membrosService = membrosService;
    }

    @PostMapping
    public ResponseEntity<MembrosResponseDTO> createMembro(@RequestBody @Valid MembrosCreateRequestDTO membrosDTO){

        MembrosResponseDTO response = membrosService.criar(membrosDTO);

        return ResponseEntity.status(201).body(response);


    }

}

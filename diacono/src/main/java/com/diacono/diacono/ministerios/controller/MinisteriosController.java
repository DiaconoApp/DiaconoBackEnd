package com.diacono.diacono.ministerios.controller;

import com.diacono.diacono.membros.dto.MembrosResponseDTO;
import com.diacono.diacono.membros.dto.MembrosUpdateDTO;
import com.diacono.diacono.ministerios.dto.MinisterioCreateDTO;
import com.diacono.diacono.ministerios.dto.MinisterioResponseDTO;
import com.diacono.diacono.ministerios.dto.MinisterioUpdateDTO;
import com.diacono.diacono.ministerios.service.MinisteriosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ministerios")
public class MinisteriosController {

    private final MinisteriosService ministerio;

    public MinisteriosController(MinisteriosService ministerio) {
        this.ministerio = ministerio;
    }

    @PostMapping
    public ResponseEntity<MinisterioResponseDTO> createMinisterio(@RequestBody @Valid MinisterioCreateDTO ministerioDTO){

        MinisterioResponseDTO response = ministerio.createMinisterio(ministerioDTO);

        return ResponseEntity.status(201).body(response);

    }

    @GetMapping
    public ResponseEntity<List<MinisterioResponseDTO>> readAllMinisterios(){

        List<MinisterioResponseDTO> response = ministerio.getAllMinisterios();

        return response != null ? ResponseEntity.status(200).body(response) : ResponseEntity.status(204).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<MinisterioResponseDTO> readUniqueMinisterio(@PathVariable Integer id){

        MinisterioResponseDTO response = ministerio.getForIDMinisterio(id);

        return response != null ? ResponseEntity.status(200).body(response) : ResponseEntity.status(204).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMinisterio(@PathVariable Integer id){

        Boolean response = ministerio.deleteMinisterio(id);

        return response != null ? ResponseEntity.status(204).build() : ResponseEntity.status(404).build();

    }

    @PatchMapping("/{id}")
    public ResponseEntity<MinisterioResponseDTO> updateSomeFieldsMinisterio(@RequestBody MinisterioUpdateDTO ministerioDTO, @PathVariable Integer id){

        MinisterioResponseDTO response = ministerio.updateMinisterio(ministerioDTO, id);

        return response != null ? ResponseEntity.status(200).body(response) : ResponseEntity.status(404).build();
    }


}

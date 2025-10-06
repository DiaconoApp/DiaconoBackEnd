package com.diacono.diacono.membros.controller;

import com.diacono.diacono.membros.model.dto.MembrosCreateDTO;
import com.diacono.diacono.membros.model.dto.MembrosResponseDTO;
import com.diacono.diacono.membros.model.dto.MembrosUpdateDTO;
import com.diacono.diacono.membros.service.MembroService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/membros")
public class MembrosController {

    private final MembroService membrosService;

    public MembrosController(MembroService membrosService)
    {
        this.membrosService = membrosService;
    }

    @PostMapping
    public ResponseEntity<MembrosResponseDTO> createMembro(@RequestBody @Valid MembrosCreateDTO membrosDTO){

        MembrosResponseDTO response = membrosService.criar(membrosDTO);

        return ResponseEntity.status(201).body(response);
    }


    @GetMapping
    public ResponseEntity<Page<MembrosResponseDTO>> getAllMembros(Pageable pageable) {
        Page<MembrosResponseDTO> response = membrosService.getAll(pageable);
        return response.hasContent()
                ? ResponseEntity.ok(response)
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/ativos/count")
    public ResponseEntity<Long> getMembrosAtivos() {
        Long userAtivos = membrosService.getMembroAtivos();
        return ResponseEntity.ok(userAtivos);
    }

    @GetMapping("/com-ministerio/count")
    public ResponseEntity<Long> getMembrosComMinisterio() {
        Long userComMinisterio = membrosService.getMembrosComMinisterio();
        return ResponseEntity.ok(userComMinisterio);
    }

    @GetMapping("/discipulados/count")
    public ResponseEntity<Long> getTotalMembrosDiscipulados() {
        Long count = membrosService.getCountMembrosDiscipulados();
        return ResponseEntity.ok(count);
    }

//
//    @GetMapping("/{id}")
//    public ResponseEntity<MembrosResponseDTO> readUniqueMembro(@PathVariable Integer id){
//        MembrosResponseDTO response = membrosService.getForID(id);
//
//        return response != null ? ResponseEntity.status(200).body(response) : ResponseEntity.status(204).build();
//
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteMembro(@PathVariable Integer id){
//
//        Boolean response = membrosService.deleteMembro(id);
//
//        return response != null ? ResponseEntity.status(204).build() : ResponseEntity.status(404).build();
//
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<MembrosResponseDTO> updateSomeFields(@RequestBody @Valid MembrosUpdateDTO membrosUpdate, @PathVariable Integer id){
//
//        MembrosResponseDTO response = membrosService.updateMembro(membrosUpdate, id);
//
//        return response != null ? ResponseEntity.status(200).body(response) : ResponseEntity.status(404).build();
//    }

}

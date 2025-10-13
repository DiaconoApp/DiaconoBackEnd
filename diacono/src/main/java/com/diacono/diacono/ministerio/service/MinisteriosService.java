package com.diacono.diacono.ministerio.service;

import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.ministerio.model.dto.MinisterioCreateDTO;
import com.diacono.diacono.ministerio.model.dto.MinisterioResponseDTO;
import com.diacono.diacono.ministerio.model.dto.MinisterioUpdateDTO;
import com.diacono.diacono.ministerio.model.entity.Ministerio;
import com.diacono.diacono.ministerio.repository.MinisteriosRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MinisteriosService {

    private final MinisteriosRepository ministerios;

    public MinisteriosService(MinisteriosRepository ministerios) {
        this.ministerios = ministerios;
    }

    public MinisterioResponseDTO createMinisterio(MinisterioCreateDTO ministerioDTO){

        Ministerio response = new Ministerio(ministerioDTO.nome(), ministerioDTO.dataCriacao(), ministerioDTO.nomeLider(), ministerioDTO.status());
        ministerios.save(response);

        return new MinisterioResponseDTO(response.getNome(), response.getDataCriacao(), response.getNomeLider(), response.getStatus());

    }

    public List<MinisterioResponseDTO> getAllMinisterios(){

        List<Ministerio> ministerio = ministerios.findAll();

        if(ministerio.isEmpty()){
            return null;
        }

        List<MinisterioResponseDTO> ministeriosDTO = ministerio.stream()
                .map(m -> new MinisterioResponseDTO(m.getNome(), m.getDataCriacao(), m.getNomeLider(), m.getStatus()))
                .toList();

        return ministeriosDTO;

    }

    public MinisterioResponseDTO getForIDMinisterio (Long id){

        if(ministerios.existsById(id)){

            Ministerio encontrado = ministerios.findById(id).get();
            return new MinisterioResponseDTO(encontrado.getNome(), encontrado.getDataCriacao(), encontrado.getNomeLider(), encontrado.getStatus());

        }

        return null;

    }

    public Boolean deleteMinisterio(Long id){

        if(ministerios.existsById(id)){

            ministerios.deleteById(id);
            return true;

        }

        return false;

    }

    public MinisterioResponseDTO updateMinisterio(MinisterioUpdateDTO ministerioDTO, Long id){

        if(ministerios.existsById(id)){

            Ministerio encontrado = ministerios.findById(id).get();

            if(ministerioDTO.nome() != null){
                encontrado.setNome(ministerioDTO.nome());
            }

            if(ministerioDTO.status() != null){
                encontrado.setStatus(ministerioDTO.status());
            }

            ministerios.save(encontrado);


            return new MinisterioResponseDTO(encontrado.getNome(), encontrado.getDataCriacao(), encontrado.getNomeLider(), encontrado.getStatus());
        }

        return null;

    }


    /*ESSE MÉTODO SE RELACIONA COM EVENTO*/
    public List<Ministerio> buscarPorUUID(List<UUID> idExterno){
        List<Ministerio> ministerios = new ArrayList<>();

        idExterno.forEach(
                id -> {
                    if(idExterno != null){
                        List<Ministerio> encontrado = this.ministerios.findByIdExterno(id);
                        if(!encontrado.isEmpty()){
                            ministerios.add(encontrado.get(0));
                        }
                    }
                }
        );

        if(ministerios.isEmpty() || ministerios == null){
            throw new ObjectNotFoundException("Ministérios não encontrados");
        }

        return ministerios;

    }

}

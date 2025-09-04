package com.diacono.diacono.ministerios.service;

import com.diacono.diacono.membros.dto.MembrosResponseDTO;
import com.diacono.diacono.membros.dto.MembrosUpdateDTO;
import com.diacono.diacono.membros.entity.Membro;
import com.diacono.diacono.ministerios.dto.MinisterioCreateDTO;
import com.diacono.diacono.ministerios.dto.MinisterioResponseDTO;
import com.diacono.diacono.ministerios.dto.MinisterioUpdateDTO;
import com.diacono.diacono.ministerios.entity.Ministerio;
import com.diacono.diacono.ministerios.repository.MinisteriosRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public MinisterioResponseDTO getForIDMinisterio (Integer id){

        if(ministerios.existsById(id)){

            Ministerio encontrado = ministerios.findById(id).get();
            return new MinisterioResponseDTO(encontrado.getNome(), encontrado.getDataCriacao(), encontrado.getNomeLider(), encontrado.getStatus());

        }

        return null;

    }

    public Boolean deleteMinisterio(Integer id){

        if(ministerios.existsById(id)){

            ministerios.deleteById(id);
            return true;

        }

        return false;

    }

    public MinisterioResponseDTO updateMinisterio(MinisterioUpdateDTO ministerioDTO, Integer id){

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

}

package com.diacono.diacono.endereco.model.entity;

import com.diacono.diacono.global.util.IdEntityUtils;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Endereco extends IdEntityUtils {

    private String cep;
    private String rua;
    private String cidade;
    private String bairro;
    private String complemento;
    private String numero;
    private String apelido;

}

package com.diacono.diacono.endereco.model.entity;

import com.diacono.diacono.global.util.IdEntityUtils;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class EnderecoEvento extends IdEntityUtils {

    private String cep;

    private String estado;

    private String cidade;

    private String bairro;

    private String rua;

    private String complemento;

    private String numero;

    private String apelido;

}

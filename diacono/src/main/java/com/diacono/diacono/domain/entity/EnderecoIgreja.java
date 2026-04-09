package com.diacono.diacono.domain.entity;

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
public class EnderecoIgreja extends IdEntityUtils {

    private String cep;

    private String estado;

    private String cidade;

    private String bairro;

    private String rua;

    private String complemento;

    private String numero;

}

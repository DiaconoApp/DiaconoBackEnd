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
public class EnderecoEvento extends IdEntityUtils {

    private String cep;

    private String estado;

    private String cidade;

    private String bairro;

    private String rua;

    private String complemento;

    private String numero;

    private String apelido;

    @Override
    public String toString() {
        StringBuilder endereco = new StringBuilder();
        appendCampo(endereco, rua);
        appendCampo(endereco, numero);
        appendCampo(endereco, complemento);
        appendCampo(endereco, bairro);
        appendCampo(endereco, cidade);
        appendCampo(endereco, estado);
        appendCampo(endereco, cep);
        return endereco.toString();
    }

    private void appendCampo(StringBuilder builder, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return;
        }

        if (!builder.isEmpty()) {
            builder.append(" - ");
        }

        builder.append(valor.trim());
    }

}

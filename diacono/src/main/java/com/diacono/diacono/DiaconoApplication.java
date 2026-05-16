package com.diacono.diacono;

import com.diacono.diacono.domain.entity.Igreja;
import com.diacono.diacono.domain.entity.EnderecoMembro;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumGeneroMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.repository.IgrejaRepository;
import com.diacono.diacono.infrastructure.persistence.springdata.MembroJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DiaconoApplication {
    // TODO: Controllers e UseCases Ministerios pendente de revisao OWASP v3
    private static final Logger logger = LoggerFactory.getLogger(DiaconoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DiaconoApplication.class, args);
	}

    @Bean
    public CommandLineRunner demo(MembroJpaRepository repository, IgrejaRepository igrejaRepository, BCryptPasswordEncoder passwordEncoder) {
        return (args) -> {

            UUID idExterno = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

            Igreja igrejaIcf = igrejaRepository.findByIdExterno(idExterno)
                    .orElseThrow(() -> new IllegalStateException("Igreja não encontrada para idExterno: " + idExterno));

            EnderecoMembro enderecoMembro = EnderecoMembro.builder()
                    .rua("Rua Ibatiba")
                    .numero(123)
                    .bairro("Vila Metalúrgica")
                    .cidade("Santo Andre")
                    .estado("São Paulo")
                    .cep("09220608")
                    .complemento("Casa 138")
                    .build();
            LocalDate dataNascimento = LocalDate.of(2002, 10, 24);


            String senha = "urubu@100";



            Membro governo = Membro.builder()
                    .nome("Tico")
                    .email("fabiam.damaceno@gmail.com")
                    .igreja(igrejaIcf)
                    .enderecoMembro(enderecoMembro)
                    .cpf("74431506012")
                    .dataNascimento(dataNascimento)
                    .dataRegistro(LocalDate.now())
                    .celular("11987654321")
                    .senha(passwordEncoder.encode(senha))
                    .status(EnumStatusMembro.ATIVO)
                    .cargoMembro(EnumCargoMembro.GOVERNO)
                    .generoMembro(EnumGeneroMembro.MASCULINO)
                    .build();

            repository.save(governo);

            logger.info("Entidade governo inicial criada: membroId=[{}], igrejaId=[{}]", governo.getIdExterno(), igrejaIcf.getIdExterno());
        };
    }

}

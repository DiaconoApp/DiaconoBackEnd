package com.diacono.diacono;

import com.diacono.diacono.domain.entities.EnderecoMembro;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumGeneroMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.domain.entities.Igreja;
import com.diacono.diacono.infrastructure.persistence.IgrejaRepository;
import com.diacono.diacono.domain.entities.Membro;
import com.diacono.diacono.infrastructure.persistence.MembroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootApplication
public class DiaconoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiaconoApplication.class, args);
	}

    @Bean
    public CommandLineRunner demo(MembroRepository repository, IgrejaRepository igrejaRepository, BCryptPasswordEncoder passwordEncoder) {
        return (args) -> {

            UUID idExterno = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

            Igreja igrejaIcf = igrejaRepository.findByIdExterno(idExterno);

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


            String senha = "izael123";



            Membro governo = Membro.builder()
                    .nome("Tico")
                    .email("tico@gmail.com")
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

            System.out.println("Entidade criada e salva com sucesso: " + governo.getNome());
        };
    }

}

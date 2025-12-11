package com.diacono.diacono;

import com.diacono.diacono.Igreja.model.entity.EnderecoIgreja;
import com.diacono.diacono.Igreja.model.entity.Igreja;
import com.diacono.diacono.Igreja.repository.IgrejaRepository;
import com.diacono.diacono.membro.model.entity.*;
import com.diacono.diacono.membro.repository.MembroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class DiaconoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiaconoApplication.class, args);
	}

    @Bean
    public CommandLineRunner demo(MembroRepository repository, IgrejaRepository igrejaRepository, BCryptPasswordEncoder passwordEncoder) {
        return (args) -> {

            EnderecoIgreja enderecoIgreja = EnderecoIgreja.builder()
                    .rua("Rua Palmeira de Vinho")
                    .numero("584")
                    .bairro("Jardim Elba")
                    .cidade("São Paulo")
                    .estado("São Paulo")
                    .cep("03980070")
                    .build();


            Igreja igrejaIcf = Igreja.builder()
                    .nome("ICF")
                    .cnpj("96966954000122")
                    .enderecoIgreja(enderecoIgreja)
                    .build();


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

            igrejaRepository.save(igrejaIcf);

            repository.save(governo);

            System.out.println("Entidade criada e salva com sucesso: " + governo.getNome());
        };
    }

}

# Diacono

Tecnologias e dependências

- Java 21
- Spring Boot (3.5.x)
  - spring-boot-starter-web
  - spring-boot-starter-data-jpa
  - spring-boot-starter-data-jdbc
  - spring-boot-starter-validation
  - spring-boot-devtools (runtime, opcional)
- Banco em memória H2 (dependência `com.h2database:h2`)
- Testes: `spring-boot-starter-test`

Contexto e propósito

Este é o backend da aplicação Diacono, responsável por fornecer uma API REST para gerenciar membros e ministérios de uma organização. É um serviço leve pensado para uso em ambiente desenvolvimento e provas de conceito, usando H2 em memória por padrão. Pelo menos, enquanto a solução está em fase de desenvolvimento.

Funcionalidades principais

- CRUD para membros
  - Criação com validação de dados (ex.: email, CPF, CEP)
  - Leitura (lista e por id)
  - Atualização parcial (PATCH) de campos selecionados
  - Remoção por id

- CRUD para ministérios
  - Criação com validação de data e campos obrigatórios
  - Leitura (lista e por id)
  - Atualização parcial (nome, status)
  - Remoção por id

Guia de Execução

1. Entrar na pasta do módulo:

   cd diacono

2. Compilar e executar com Maven:

   mvn clean package
   mvn spring-boot:run
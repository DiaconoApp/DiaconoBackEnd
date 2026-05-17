# Estágio 1: Build (Compilação)
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
# Copia apenas os arquivos de dependências primeiro (otimiza o cache do Docker)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
# Copia o código fonte e gera o pacote
COPY src ./src
# Pulamos os testes aqui para evitar erros com banco de dados no build
RUN ./mvnw clean package -DskipTests

# Estágio 2: Produção (Execução)
FROM eclipse-temurin:21-jre-jammy AS production
WORKDIR /app
# Porta configurada no seu application.properties
EXPOSE 8080
# Copia o JAR do estágio de build e renomeia para facilitar a execução
COPY --from=builder /app/target/diacono-0.0.1-SNAPSHOT.jar app-core.jar
# Usa variáveis de ambiente para o Java, permitindo ajustes de memória se necessário
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app-core.jar"]
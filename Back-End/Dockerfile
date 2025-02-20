# Etapa de Build
FROM maven:3.8.8-openjdk-17 AS build

WORKDIR /app
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./

# Concede permissão de execução ao Maven Wrapper
RUN chmod +x mvnw

# Baixa as dependências antes de copiar o código-fonte para otimizar o cache
RUN ./mvnw dependency:go-offline

# Copia o restante do código-fonte
COPY src ./src

# Compila o projeto
RUN ./mvnw package -DskipTests

# Etapa de Produção
FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

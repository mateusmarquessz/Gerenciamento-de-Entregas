
# Gerenciamento de Entregas

Este projeto é um sistema de gerenciamento de entregas desenvolvido com Spring Boot e segurança baseada em JWT. Ele permite que administradores (ADMIN) e usuários comuns (USER) gerenciem entregas, com funcionalidades de criação, atualização e listagem de entregas.

## Requisitos

Antes de rodar o programa, você precisa garantir que o seguinte esteja instalado em sua máquina:

- **Java 17 ou superior**: O sistema foi desenvolvido utilizando Java 17. [Download do Java](https://adoptopenjdk.net/)
- **Maven**: Usado para gerenciar as dependências do projeto. [Download do Maven](https://maven.apache.org/download.cgi)
- **Banco de Dados PostgreSQL**: O sistema utiliza o PostgreSQL para armazenar os dados das entregas. [Download do PostgreSQL](https://www.postgresql.org/download/)

## Configuração do Banco de Dados PostgreSQL

1. **Instalar o PostgreSQL**: Se ainda não tiver o PostgreSQL instalado, siga as instruções de instalação no [site oficial do PostgreSQL](https://www.postgresql.org/download/).

2. **Crie um banco de dados no PostgreSQL**:
   - Abra o terminal ou o pgAdmin.
   - Crie um banco de dados para o sistema:

     ```sql
     CREATE DATABASE gerenciamento_de_entregas;
     ```

3. **Crie um usuário para o banco de dados**:
   - Crie um usuário e defina uma senha:

     ```sql
     CREATE USER seu_usuario WITH PASSWORD 'sua_senha';
     ```

4. **Dê permissões ao usuário**:
   - Conceda permissões ao usuário para o banco de dados:

     ```sql
     GRANT ALL PRIVILEGES ON DATABASE gerenciamento_de_entregas TO seu_usuario;
     ```

## Configuração do Projeto

1. **Atualize as configurações do banco de dados**:
   - No seu projeto, abra o arquivo `src/main/resources/application.properties`.
   - Atualize as seguintes propriedades com os dados do seu banco de dados:

     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
     spring.datasource.username=seu_usuario
     spring.datasource.password=sua_senha
     ```

## Executando o Sistema

1. **Clone o repositório**:

   ```bash
   git clone https://github.com/seu-usuario/gerenciamento-de-entregas.git
   ```

2. **Instale as dependências do projeto**:

   ```bash
   mvn install
   ```

3. **Execute o projeto**:

   ```bash
   mvn spring-boot:run
   ```

   Ou, se preferir, execute o projeto diretamente pela sua IDE (como IntelliJ IDEA ou Eclipse).

## Swagger

Após iniciar o sistema, você pode acessar a interface do Swagger para visualizar e testar os endpoints da API.

- Acesse: [http://localhost:8080/swagger-ui/index.html#/v](http://localhost:8080/swagger-ui/index.html#/v)

  - **Swagger**: Oferece uma interface interativa onde você pode visualizar todos os endpoints disponíveis e realizar requisições diretamente do navegador. Ele facilita a documentação e testes da API de forma visual.

## Testando a Conexão com o Banco de Dados

- Você pode acessar o banco de dados diretamente utilizando um cliente SQL, como o pgAdmin ou DBeaver, para verificar se o sistema está funcionando corretamente.
- Além disso, se configurado corretamente, o Spring Boot criará as tabelas automaticamente no banco de dados.

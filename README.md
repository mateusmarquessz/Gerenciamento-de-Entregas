
# Gerenciamento de Entregas

Este projeto é um sistema de gerenciamento de entregas desenvolvido com Spring Boot e segurança baseada em JWT. Ele permite que administradores (ADMIN) e usuários comuns (USER) gerenciem entregas, com funcionalidades de criação, atualização e listagem de entregas.

## Requisitos

Antes de rodar o programa, você precisa garantir que o seguinte esteja instalado em sua máquina:

- **Java 17 ou superior**: O sistema foi desenvolvido utilizando Java 17. [Download do Java](https://www.oracle.com/java/technologies/downloads/#jdk23-windows)
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
     spring.datasource.url=url_do_seu_banco
     spring.datasource.username=seu_usuario
     spring.datasource.password=sua_senha
     ```
## Usuário Padrão ADMIN
- Ao iniciar o sistema pela primeira vez, um usuário padrão com role ADMIN será criado automaticamente. 
- Este usuário tem as seguintes credenciais:
  - Email: admin@admin.com
  - Senha: admin
- Esse usuário tem permissões administrativas para gerenciar as entregas e os usuários do sistema.

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

## Diagrama de Relacionamento das Entidades

<a href="https://ibb.co/C59pWNSs"><img src="https://i.ibb.co/SXmDJWZ7/entidade-Relacionamento.png" alt="entidade-Relacionamento" border="0"></a>




## Swagger

Após iniciar o sistema, você pode acessar a interface do Swagger para visualizar e testar os endpoints da API.

- Acesse: [http://localhost:8080/swagger-ui/index.html#/v](http://localhost:8080/swagger-ui/index.html#/v)

  - **Swagger**: Oferece uma interface interativa onde você pode visualizar todos os endpoints disponíveis e realizar requisições diretamente do navegador. Ele facilita a documentação e testes da API de forma visual.
  - Esta configurado para receber token clicando no cadeado voce coloca seu token e faz a requisicao

## Endpoints no Insominia

A seguir estão os endpoints disponíveis para interagir com a API.

### Autenticação

#### **POST /auth/login**
- **Descrição**: Realiza o login de um usuário.
- **Corpo da requisição**:
    ```json
    {
      "email": "admin@admin.com",
      "senha": "admin"
    }
    ```
- **Resposta**:
    - Sucesso: `200 OK` com um token JWT.
    - Erro: `400 Bad Request` se os dados forem inválidos.

#### **POST /auth/register-admin**
- **Descrição**: Cria um novo usuário com o papel de administrador (Somente ADMIN).
- **Corpo da requisição**:
    ```json
    {
      "nome": "teste",
      "email": "teste@teste.com",
      "senha": "teste"
    }
    ```
  - **Token**:
  ```Auth
   Token JWT gerado com o login
   Bearer
  ```
  
- **Resposta**:
    - Sucesso: `201 Created`.
    - Erro: `400 Bad Request` se os dados forem inválidos.


#### **POST /auth/register**
- **Descrição**: Cria um novo usuário com o papel de Usuario (Role User).
- **Corpo da requisição**:
    ```json
    {
      "nome": "teste1",
      "email": "teste1@teste.com",
      "senha": "teste"
    }
    ```
- **Resposta**:
    - Sucesso: `201 Created`.
    - Erro: `400 Bad Request` se os dados forem inválidos.
---

### Endpoints do Usuario

#### **GET /api/users/profile/{id}**
- **Descrição**: Recupera o perfil do usuário com o ID especificado.
- **Resposta**:
    - Sucesso: `200 OK` com os dados do usuário.
    - Erro: `403 Forbidden` se o usuário não tiver permissão para acessar os dados ou se os dados forem inválidos.
- **Token**:
  ```Auth
   Token JWT gerado com o login
   Bearer
  ```

#### **PUT /api/users/{id}**
- **Descrição**: Atualiza os dados do usuário com o ID especificado.
- **Resposta**:
    - Sucesso: `200 OK` com os dados do usuário atualizados.
    - Erro: `403 Forbidden` se o usuário não tiver permissão para atualizar os dados ou se os dados forem inválidos.
- **Token**:
  ```Auth
   Token JWT gerado com o login
   Bearer
  ```

#### **Delete /api/users/{id}**
- **Descrição**: Exclui um usuário. Apenas usuários com permissão de ADMIN podem excluir outros usuários.
- **Resposta**:
    - Sucesso: `200 OK` se o usuário for excluído com sucesso.
    - Erro: `403 Forbidden` se o usuário não for um ADMIN.
- **Token**:
  ```Auth
   Token JWT gerado com o login
   Bearer
  ```


  #### **GET /api/users**
- **Descrição**: Recupera todos os usuários. Apenas ADMIN pode acessar esta lista.
- **Resposta**:
    - Sucesso: `200 OK`  com a lista de todos os usuários.
    - Erro: `403 Forbidden` se o usuário não for ADMIN.
- **Token**:
  ```Auth
   Token JWT gerado com o login
   Bearer
  ```

### Endpoints de Entregas

#### **GET /entregas**
- **Descrição**: Lista todas as entregas (somente para ADMIN).
- **Resposta**:
    - Sucesso: `200 OK` com a lista de entregas.
    - Erro: `403 Forbidden` se o usuário não for admin.
- **Token**:
  ```Auth
   Token JWT gerado com o login
   Bearer
  ```

#### **GET /entregas/usuario/{usuarioId}**
- **Descrição**: Lista todas as entregas associadas a um usuário específico (somente para USER).
- **Parâmetros**:
    - `usuarioId`: ID do usuário cujas entregas serão listadas.
- **Resposta**:
    - Sucesso: `200 OK` com a lista de entregas.
    - Erro: `403 Forbidden` se o usuário não for o dono das entregas.
- **Token**:
  ```Auth
   Token JWT gerado com o login
   Bearer
  ```
  
#### **POST /entregas**
- **Descrição**: Cria uma nova entrega (somente para ADMIN).
- **Corpo da requisição**:
    ```json
    {
      "descricao": "Entrega de documentos importantes",
      "usuarioID": 2
    }
    ```
- **Token**:
  ```Auth
   Token JWT gerado com o login
   Bearer
  ```
  
- **Resposta**:
    - Sucesso: `201 Created` com os dados da nova entrega.
    - Erro: `400 Bad Request` se os dados forem inválidos.

#### **PUT /entregas/{id}**
- **Descrição**: Atualiza a descrição e o status de uma entrega (somente para ADMIN).
- **Parâmetros**:
    - `id`: ID da entrega a ser atualizada.
- **Corpo da requisição**:
    ```json
    {
      "descricao": "Nova descrição da entrega",
      "status": "EM_TRANSITO"
    }
    ```
- **Token**:
  ```Auth
   Token JWT gerado com o login
   Bearer
  ```
  
- **Resposta**:
    - Sucesso: `200 OK` com os dados atualizados.
    - Erro: `403 Forbidden` se o usuário não for admin.

#### **PUT /entregas/{entregaId}/atualizar-status**
- **Descrição**: Permite que um usuário atualize o status de uma entrega de `EM_TRANSITO` para `ENTREGUE`.
- **Parâmetros**:
    - `entregaId`: ID da entrega a ser atualizada.
    - `usuarioId`: ID do usuario dono da entrega
- **Corpo da requisição**:
    ```json
    {
      "usuarioID": 4
    }
    ```
- **Token**:
  ```Auth
   Token JWT gerado com o login
   Bearer
  ```
  
- **Resposta**:
    - Sucesso: `200 OK` com os dados atualizados.
    - Erro: `403 Forbidden` se o status não puder ser atualizado.

#### **DELETE /entregas/{id}**
- **Descrição**: Deleta uma entrega (somente para ADMIN).
- **Parâmetros**:
    - `id`: ID da entrega a ser excluída.
- **Resposta**:
    - Sucesso: `200 OK` com confirmação de exclusão.
    - Erro: `403 Forbidden` se o usuário não for admin.
- **Token**:
  ```Auth
   Token JWT gerado com o login
   Bearer
  ```
  
---



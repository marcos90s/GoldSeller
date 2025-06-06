# goldSellerAPI

API RESTful para gerenciamento de transações de venda de ouro em jogos, construída com Java e Spring Boot.

## Visão Geral

Este projeto fornece uma API para gerenciar usuários e transações de ouro em jogos. Ele inclui funcionalidades de autenticação JWT, gerenciamento de usuários e registro de transações (tanto transações dentro do jogo quanto transações reais).

## Funcionalidades

*   **Autenticação JWT:** Protege as APIs com autenticação baseada em token JWT.
*   **Gerenciamento de Usuários:** Permite criar, listar, atualizar e excluir usuários.
*   **Transações de Jogo:** Permite registrar e gerenciar transações de compra e venda de ouro dentro do jogo.
*   **Transações Reais:** Permite registrar e gerenciar transações de compra e venda de ouro com dinheiro real.
*   **Controle de Acesso:** Implementa controle de acesso baseado em papéis (ADMIN ou COMMON).

## Tecnologias Utilizadas

*   Java
*   Spring Boot
*   Spring Security
*   JWT (JSON Web Tokens)
*   Maven

## Pré-requisitos

*   Java 17 ou superior
*   Maven
*   Banco de dados (ex: MySQL, PostgreSQL)

## Configuração

1.  **Clone o repositório:**

    ```bash
    git clone <URL_DO_REPOSITORIO>
    ```

2.  **Configure o banco de dados:**

    *   Crie um banco de dados no seu sistema de gerenciamento de banco de dados (ex: MySQL, PostgreSQL).
    *   Configure as propriedades de conexão do banco de dados no arquivo `src/main/resources/application.properties`.

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/gold_seller
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=update
    ```

3.  **Compile e execute a aplicação:**

    ```bash
    mvn spring-boot:run
    ```

    A API estará disponível em `http://localhost:8080`.

## Endpoints da API

### Autenticação

*   `POST /auth/login`: Autentica um usuário e retorna um token JWT.
    *   Corpo da requisição:

        ```json
        {
          "email": "seu_email",
          "password": "sua_senha"
        }
        ```

    *   Resposta de sucesso:

        ```json
        {
          "token": "seu_token_jwt",
          "type": "Bearer",
          "userId": "id_do_usuario",
          "email": "seu_email",
          "role": "COMMON"
        }
        ```

*   `POST /auth/register`: Registra um novo usuário.
    *   Corpo da requisição:

        ```json
        {
          "name": "seu_nome",
          "email": "seu_email",
          "password": "sua_senha"
        }
        ```

### Usuários

*   `POST /users`: Cria um novo usuário (requer permissão de ADMIN).
    *   Corpo da requisição:

        ```json
        {
          "name": "nome_do_usuario",
          "email": "email_do_usuario",
          "password": "senha_do_usuario",
          "role": "COMMON"
        }
        ```

*   `GET /users`: Lista todos os usuários (requer permissão de ADMIN).
*   `GET /users/{id}`: Obtém um usuário pelo ID.
*   `PUT /users/{id}`: Atualiza um usuário pelo ID.
*   `DELETE /users/{id}`: Exclui um usuário pelo ID (requer permissão de ADMIN).

### Transações de Jogo

*   `POST /game`: Cria uma nova transação de jogo.
    *   Corpo da requisição:

        ```json
        {
          "type": "SALE",
          "amount": 10,
          "quantity": 1,
          "itemName": "Espada"
        }
        ```

*   `GET /game`: Lista todas as transações de jogo do usuário autenticado.
*   `GET /game/{id}`: Obtém uma transação de jogo pelo ID.

### Transações Reais

*   `POST /real`: Cria uma nova transação real.
    *   Corpo da requisição:

        ```json
        {
          "amount": 100.0,
          "amountInGold": 1000,
          "charName": "Personagem",
          "description": "Compra de ouro"
        }
        ```

*   `GET /real`: Lista todas as transações reais do usuário autenticado.
*   `GET /real/{id}`: Obtém uma transação real pelo ID.

## Segurança

As APIs são protegidas com autenticação JWT. Para acessar os endpoints protegidos, você precisa fornecer um token JWT válido no header `Authorization` da requisição.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

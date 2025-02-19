# Todo API

## Descrição

Esta é uma API RESTful para gerenciamento de tarefas (To-Do List). Ela permite criar, listar, atualizar e deletar tarefas.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **Flyway**
- **Microsoft SQL Server**
- **Swagger/OpenAPI** (para documentação da API)

## Como Rodar o Projeto

1. Clone o repositório:
   ```sh
   git clone https://github.com/LucioGarciaRios/todo-api.git
   ```
2. Acesse a pasta do projeto:
   ```sh
   cd todo-api
   ```
3. Comando para criar um container do SqlServer no docker
   ```sh
   docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=Senha@12345" -p 1433:1433 -d mcr.microsoft.com/mssql/server:2022-latest
   ```
4. Compile o projeto:
   ```sh
   mvn clean install
   ```
5. Execute a API:
   ```sh
   mvn spring-boot:run
   ```
6. A API estará disponível em `http://localhost:8080`.

## Variaveis de ambiente

Você precisará preencher as seguintes variaveis de ambiente:

   ${DB_URL} - jdbc:sqlserver://localhost:1433;databaseName=master;encrypt=false
   ${DB_USR} - sa
   ${DB_PWD} - Senha@12345
   ${LOG-LEVEL} - DEBUG


## Documentação da API

A API conta com documentação interativa via Swagger. Após iniciar o projeto, acesse:

🌐 `http://localhost:8080/swagger-ui.html`

O postman do projéto está disponível na raiz do projeto no diretório: postman

## Autor

👨‍💻 Desenvolvido por **Lúcio Garcia**.


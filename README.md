# API REST para gerenciamento de livros e usuários desevolvida com Spring Boot

## Tecnologias

- Java
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven
- JUnit
- Mockito

## Funcionalidades
- Gerenciar Livros (Create, Read, Update, Delete)
- Gerenciar Usuarios (Create, Read, Update, Delete)
- Gerenciar Alugueis (Create, Read, Update, Delete)

## Arquitetura
O projeto segue o padrão de arquitetura em camadas:
- Controller - Recebe as requisições HTTP
- Service - Regras de Negócio
- Repository - Acesso ao banco de dados
- Entity - Representação das tabelas
- DTO - Transferência de dados
- Exception Handler - Tratamento global de exceções

## Como rodar o projeto

- git clone https://github.com/kayquesann/biblioteca-icpi.git
- cd biblioteca-icpi
- ./mvnw spring-boot:run
- A API ficará disponível em: http://localhost:8080

## Testes
O projeto possui testes unitários utilizando:
- JUnit
- Mockito
- MockMVC
- Para rodar os testes: ./mvnw test

## Autor: 
Kayque Ferreira


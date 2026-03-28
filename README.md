# API REST para gerenciamento de livros e usuários desevolvida com Spring Boot

## Tecnologias

- Java
- Spring Boot
- Spring Data JPA
- H2 Database
- Gradle
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

1. Abra o terminal e execute: git clone https://github.com/kayquesann/biblioteca-icpi.git
2. Abra o Intellij IDEA, clique em "Open", selecione a pasta clonada e aguarde o Intellij importar as dependências do Gradle
3. Se necessário configurar o JDK, vá em File > Project Structure. Em Project, selecione o java 17 (ou superior)
4. Vá até a classe principal BibliotecaIcpiApplication e execute.  
  
Se quiser rodar o projeto sem Intellij, basta:
1. Clonar repositório com git clone https://github.com/kayquesann/biblioteca-icpi.git
2. Entrar na pasta biblioteca-icpi > cd biblioteca-icpi
3. Executar com ./gradlew bootRun


## Testes
O projeto possui testes unitários utilizando:
- JUnit
- Mockito
- MockMVC
- Para rodar os testes: clique com o botão direito na pasta test e clique em Run Tests  
  
Se quiser rodar sem Intellij, basta executar na pasta biblioteca-icpi o comando ./gradlew test

## Autor: 
Kayque Ferreira


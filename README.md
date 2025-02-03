# Desafio-Tecnico-TransactionAuthorizer-LuigiCuri
Desafio técnico para Autorização de transações de cartão de crédito.


## 1. Tecnologias utilizadas:
- Java 17
- Maven
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Spring Security
- Swagger OpenAPI
- Postman

## 2. Requisitos para o building e o running da aplicação:
- MySQL para uso do banco de dados
- JDK
- IntelliJ, Eclipse ou outra IDE

Para Autenticação login Spring Security
> Usuário: admin
> 
> Senha: desafio

## 2.1. Configurando o Banco de Dados

### Passo 1
Para que a aplicação funcione por completo, é necessário criar uma Database através do MySQL e inserir o nome da mesma no arquivo de configuração ***/src/main/resources/application.properties***
Crie uma nova database com o nome que desejar:

![image](https://github.com/user-attachments/assets/f04cf66d-866c-4418-8a2a-1b12ec01e91f)


Neste exemplo será criada a database com o nome "desafio":

### Passo 2
Abra o arquivo de configuração do projeto spring /src/main/resources/application.properties , copie e cole estes campos de configuração abaixo. Caso a porta utilizada e o nome da database sejam diferentes, altere para os nomes e portas utilizadas no seu MySQL.

``` 
spring.datasource.url=jdbc:mysql://localhost:3306/desafio
spring.datasource.username=root
spring.datasource.password=localhost
spring.jpa.show-sql=true

# Database driver class
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA (Hibernate) properties
spring.jpa.hibernate.ddl-auto=create

# Spring Security Properties
logging.level.org.springframework.security=debug 
```
Caso necessário, altere os campos para seus respectivos nomes de usuário e senha:
>spring.datasource.username=root
>
>spring.datasource.password=localhost
>
### Passo 3
Execute o boot da aplicação, ao executar a aplicação serão criadas as tabelas e colunas no banco de dados.

## 3.1. Acessando a API:

Você pode utilizar a API tanto pelo Swagger, quanto pelo Postman(recomendado)

### 3.2. Acessando API pelo Swagger
Para consultar os endpoints através do Swagger, acesse: http://localhost:8080/swagger-ui/index.html

#### 4.1 Autenticação Spring Security
É necessário realizar autenticação de login na página
> Usuário: admin
> 
> Senha: transaction


Após autenticar, será redirecionado para a página do Swagger.


### 4.2. Acessando API pelo Postman
Abra o Postman e insira um dos endpoints da API de lanches
#### 4.2.1. Configurando Basic Auth
Clique na aba "Authorization", selecione o tipo "Basic auth" e então, insira nos campos Username e Password os respectivos usuários e senha do Spring Security

> Usuário: admin
> 
> Senha: transaction



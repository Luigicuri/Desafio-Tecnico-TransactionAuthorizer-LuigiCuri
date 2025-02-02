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
- Postman para requisições HTTP(recomendado)

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


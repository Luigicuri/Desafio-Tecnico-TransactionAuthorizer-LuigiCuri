# Desafio-Tecnico-TransactionAuthorizer-LuigiCuri
Desafio técnico para Autorização de transações de cartão de crédito.


## 1. Tecnologias utilizadas:
- Java 17
- Maven
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- H2 para testes em memória
- Spring Security
- Swagger OpenAPI
- Postman
- Flyway (optado por maior adequação ao projeto, comparado ao Liquibase, o qual utilizei inicialmente)

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

### 2.1.1. Configurando o H2

Estabeleça uma conexão com o banco H2

![image](https://github.com/user-attachments/assets/0f9ecbb5-89f5-47f4-b635-c930b0cd3222)


### Passo 2
Abra o arquivo de configuração do projeto spring /src/main/resources/application.properties , copie e cole estes campos de configuração abaixo. Caso a porta utilizada e o nome da database sejam diferentes, altere para os nomes e portas utilizadas no seu MySQL.

``` 
spring.application.name=desafiocaju

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/desafio?serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=localhost
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate (Disable Auto DDL - Flyway Handles Schema)
spring.jpa.hibernate.ddl-auto=none
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Ensure Flyway Runs Before Hibernate
spring.flyway.enabled=false
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true
spring.flyway.locations=classpath:db/migration

spring.flyway.url=jdbc:mysql://localhost:3306/desafio
spring.flyway.user=root
spring.flyway.password=localhost

# Enable Flyway Debug Logs
logging.level.org.flywaydb=DEBUG

```
Caso necessário, altere os campos para seus respectivos nomes de usuário e senha:
>spring.datasource.username=root
>
>spring.datasource.password=localhost

OBS.: verifique o arquivo ***src/test/resources/application-test.properties*** caso deseje alterar alguma configuração específica.

### Passo 3
Execute o boot da aplicação. Ao executar a aplicação serão criadas as tabelas e colunas no banco de dados. 

## 3.1. Acessando a API:

Você pode utilizar a API tanto pelo Swagger, quanto pelo Postman(recomendado).

## 3.2. Acessando API pelo Swagger
Para consultar os endpoints através do Swagger, acesse: http://localhost:8080/swagger-ui/index.html

![image](https://github.com/user-attachments/assets/ca95fa21-2586-4252-ac68-81dffd7753b5)


## 4.1. Autenticação Spring Security
É necessário realizar autenticação de login na página
> Usuário: admin
> 
> Senha: transaction


Após autenticar, será redirecionado para a página do Swagger.


## 4.2. Acessando API pelo Postman
Abra o Postman e insira um dos endpoints da API de lanches
### 4.2.1. Configurando Basic Auth
Clique na aba "Authorization", selecione o tipo "Basic auth" e então, insira nos campos Username e Password os respectivos usuários e senha do Spring Security

![image](https://github.com/user-attachments/assets/c843078e-1d4c-4536-8deb-aec2473c1c4f)

> Usuário: admin
> 
> Senha: transaction

## 5. Testes Unitários e de Integração

Foram realizados testes unitários e de integração para o projeto dentro do tempo estimado. 

## 6. Requisitos

O sistema deve processar transações com base no saldo da categoria da carteira do usuário, determinado pelo MCC e nome do comerciante. Se o saldo da categoria não for suficiente, o sistema tentará usar o saldo da categoria CASH. Se ambos os saldos forem insuficientes, a transação será rejeitada.

## 7. L4

Para garantir a integridade dos dados em transações simultâneas (L4), foram avaliadas duas soluções:

Uso de Bancos de Dados RDBMS: Bancos de dados como o MySQL oferecem dois tipos de bloqueio (pessimista e otimista) para evitar conflitos e inconsistências durante transações simultâneas.

Uso de travas com ConcurrentHashMap<Long, ReentrantLock>: Inicialmente, foi implementada uma solução com travas utilizando ConcurrentHashMap<Long, ReentrantLock>. No entanto, durante os testes, essa abordagem se mostrou não tão viável para a entrega do projeto, principalmente devido a questões de desempenho e complexidade de gerenciamento.

Dessa forma, optou-se por uma abordagem mais simples para bloquear transações simultâneas, utilizando as anotações do Spring, como @Transactional, que gerencia automaticamente a consistência das transações.



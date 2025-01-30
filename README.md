# Banco Javer - Microservices 🚀

Bem-vindo ao **Banco Javer**! Aqui você encontrará a base de código para um sistema de microserviços desenvolvido com **Spring Boot**, focado no **cadastro de clientes bancários**. Este projeto é uma demonstração completa da criação e integração de microserviços, com o objetivo de gerenciar contas bancárias de clientes de maneira eficiente e escalável. Prepare-se para entender como conectar múltiplos microserviços e construir uma arquitetura sólida!

---

## O Projeto

Este repositório contém o desenvolvimento de um **sistema bancário** utilizando **microserviços** que operam de forma interconectada. O Banco Javer é uma aplicação de cadastro de clientes com funcionalidades robustas para a gestão de contas bancárias, como criação, atualização, exclusão e cálculo de **score de crédito**.

A aplicação é dividida em duas partes:

1. **Primeira Aplicação**: Atua como intermediária, expondo endpoints REST para realizar operações CRUD (Create, Read, Update, Delete) e calcular o score de crédito (com base no saldo da conta).
2. **Segunda Aplicação**: Responsável pela persistência dos dados em uma base de dados H2, gerenciando as informações dos clientes.

Este é um **desafio** focado em microserviços, onde o objetivo principal é a **integração de sistemas** e a **robustez dos testes**, garantindo que as funcionalidades sejam confiáveis e atendam às regras de negócio.

---

## Regras de Negócio 📊

### 1. Cadastro de Conta
- Campos obrigatórios: **nome**, **telefone**, **saldo inicial**, e **correntista**.
- Campos gerenciados automaticamente: **ID** e **score_credito**.

### 2. Ativação de Conta Corrente
- A conta pode ser marcada como **corrente** posteriormente, caso não tenha sido no momento da criação.

### 3. Validação de Saldo Positivo
- O saldo da conta **nunca pode ser negativo** e deve ser **igual ou maior que zero**.

### 4. Cálculo Automático de Score
- O sistema calcula automaticamente o **score_credito** com a fórmula: `score_credito = saldo_cc * 0.1`.

### 5. Unicidade de Campos
- O **telefone** e **ID** de cada cliente devem ser únicos em todo o sistema.

### 6. Desativação de Conta
- A conta corrente só pode ser desativada (correntista = false) **se o saldo for zero**.

### 7. Exclusão de Conta
- A conta só pode ser excluída **se a opção de conta corrente estiver desativada** (correntista = false).

### 8. Validação de Campos Obrigatórios
- Durante a criação ou atualização da conta, todos os campos obrigatórios devem ser validados pelo sistema.

---

## Tecnologias Utilizadas 🛠️

### Backend
- **Java 21**: Linguagem principal para o desenvolvimento dos microserviços.
- **Spring Boot**: Framework para criação dos microserviços e exposição dos endpoints REST.
- **Spring Data JPA**: Para interação com o banco de dados.
- **Spring Cloud**: Para gerenciar a configuração e descoberta de serviços.
- **H2**: Banco de dados local em memória utilizado na segunda aplicação para armazenar dados de forma leve durante o desenvolvimento.

### Testes
- **JUnit 5**: Framework para testes unitários e integração.
- **HttpTestClient (Spring)**: Usado para garantir a cobertura de 100% nos testes de endpoints.

### Dependências
- **spring-boot-starter-data-jpa**: Proporciona integração com **JPA (Java Persistence API)** para realizar operações de persistência no banco de dados de maneira eficiente e simples.
  
- **spring-boot-starter-validation**: Facilita a validação de dados em objetos Java usando as anotações da especificação **Bean Validation** (JSR 303/JSR 380).

- **spring-boot-starter-web**: Fornece suporte para criar serviços RESTful com **Spring MVC** e **Jackson** para serialização de JSON, permitindo a comunicação entre microserviços.

- **springdoc-openapi-starter-webmvc-ui**: Adiciona suporte ao **OpenAPI 3.0** para gerar e exibir a documentação da API de forma interativa utilizando **Swagger UI**.

- **spring-boot-devtools**: Ferramentas de desenvolvimento para facilitar o ciclo de vida de desenvolvimento, como reinicialização automática e configuração rápida.

- **h2**: Banco de dados em memória utilizado para testes e desenvolvimento. Oferece uma solução rápida e fácil de configurar, sem necessidade de um banco de dados externo.

 **spring-cloud-starter-openfeign**: Integração com **Spring Cloud** para usar o **Feign** em microserviços, facilitando a comunicação entre sistemas distribuídos. Inclui a configuração necessária para o uso do Feign no contexto de microserviços na nuvem.

- **mysql-connector-j**: Driver JDBC necessário para conectar o Spring Boot a um banco de dados **MySQL**. Essencial para a operação do banco de dados em ambiente de produção.

- **lombok**: Biblioteca que simplifica o código, eliminando a necessidade de escrever métodos repetitivos como getters, setters, construtores e equals/hashCode.

- **spring-boot-starter-webflux**: Proporciona o suporte a **programação reativa** usando **Spring WebFlux**, permitindo a criação de APIs reativas e escaláveis.

- **spring-boot-starter-test**: Pacote para realizar testes automatizados com **JUnit** e **Mockito**, garantindo a qualidade e robustez das implementações.

- **modelmapper**: Ferramenta para **mapeamento de objetos**, permitindo transformar objetos de diferentes camadas de aplicação (por exemplo, DTOs e entidades) de forma simples e eficiente.


---

## Funcionalidades Principais 🎯

1. **Cadastro de Clientes**:
   - Permite o cadastro de clientes com as informações obrigatórias, como nome, telefone, saldo inicial, e se são correntistas.

2. **Cálculo Automático de Score de Crédito**:
   - O score de crédito é calculado automaticamente a partir do saldo da conta utilizando a fórmula: `score_credito = saldo_cc * 0.1`.

3. **Operações CRUD**:
   - A primeira aplicação expõe os endpoints REST para permitir as operações **Create**, **Read**, **Update**, e **Delete**.

4. **Ativação e Desativação de Conta Corrente**:
   - Funcionalidade para ativar ou desativar uma conta como conta corrente, de acordo com a necessidade do cliente.

5. **Validação de Saldo**:
   - A validação de saldo é crucial para garantir que nenhuma conta possua saldo negativo.

6. **Exclusão de Conta**:
   - A conta pode ser excluída apenas se a opção de conta corrente estiver previamente desativada.

---

## Como Executar o Projeto ⚙️

### Pré-requisitos
Antes de rodar o projeto, certifique-se de ter os seguintes itens instalados:

- **Java 17 ou superior**


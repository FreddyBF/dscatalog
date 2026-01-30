# Projeto API RESTful de Catálogo de Produtos - Spring Boot

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-%20Concluido-yellow?style=for-the-badge)

## 📌 Visão Geral

API RESTful moderna e robusta para gerenciamento de **produtos, categorias e utilizadores**, desenvolvida com **Spring Boot** e **Java 21**, seguindo boas práticas de arquitetura em camadas, segurança com **JWT**, validação de dados, paginação e tratamento global de erros.

O projeto foi construído com foco em **qualidade de código**, **organização**, **padrões profissionais** e **facilidade de manutenção**.

---

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.x**
- **Spring Web**
- **Spring Data JPA (Hibernate)**
- **Spring Security + JWT**
- **Spring Validation (Jakarta Validation)**
- **PostgreSQL**
- **Flyway** (versionamento de banco de dados)
- **UUID** como chave primária
- **Lombok**
- **JUnit 5 / Spring Boot Test**
- **AssertJ**

---

## 🛠 Funcionalidades Implementadas

### 🔐 Autenticação e Segurança
- Autenticação com **JWT**
- Filtro de segurança customizado
- Controle de acesso por roles
- `AccessDeniedHandler` e `AuthenticationEntryPoint` customizados
- Respostas JSON padronizadas para erros de segurança

### 📦 Catálogo
- CRUD completo de produtos
- CRUD de categorias
- Associação Produto ↔ Categoria
- Listagem paginada
- DTOs específicos para criação, actualização e resposta

### 👤 Utulizadores
- Cadastro de utilizadores
- Login com geração de token JWT
- Seed inicial roles

### 🧪 Testes
- Testes de service layer
- Testes de controller
- Testes com Spring Security

### ⚠️ Tratamento de Erros
- Handler global de exceções
- Validação de campos com mensagens claras
- Padrão único de resposta de erro

---

## 📋 Endpoints Principais

### 🔑 Autenticação
| Método | Endpoint        | Descrição |
|------|-----------------|-----------|
| POST | `/auth/login`   | Login e geração de token JWT |
| POST | `/auth/register`   | Registo de utilizador |
| POST | `/auth/refresh`   | Geração de token JWT |

### 📦 Produtos
| Método | Endpoint              | Descrição |
|------|-----------------------|-----------|
| POST | `/products`           | Criar produto |
| GET  | `/products`           | Listar produtos (paginado) |
| GET  | `/products/{id}`      | Buscar produto por ID |
| PUT  | `/products/{id}`      | Atualizar produto |
| DELETE | `/products/{id}`    | Remover produto |

### 🗂 Categorias
| Método | Endpoint              | Descrição |
|------|-----------------------|-----------|
| GET  | `/categories`         | Listar categorias |
| POST | `/categories`         | Criar categoria |

---

## 📄 Exemplo de Request (POST /products)

```json
{
  "name": "Notebook Dell",
  "price": 4599.90,
  "description": "Notebook para desenvolvimento",
  "imgUrl": "https://img.com/notebook.png",
  "categoriesIds": [
    "a3f85f64-5717-4562-b3fc-2c963f66afa6"
  ]
}

```

## 📄 Exemplo de Response


```json

{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "name": "Notebook Dell",
  "price": 4599.90,
  "description": "Notebook para desenvolvimento",
  "imgUrl": "https://img.com/notebook.png",
  "categories": [
    {
      "id": "a3f85f64-5717-4562-b3fc-2c963f66afa6",
      "name": "Informática"
    }
  ]
}

```
## 🗂 Estrutura do Projeto

```bash
src/main/java/com/github/freddy/dscatalog
├── auth/            → JWT filter e serviços de token
├── config/          → Configurações (Security, Seed, etc.)
├── controller/      → Controllers REST
├── dto/             → DTOs (request, response, error)
├── exception/       → Exceções customizadas e handler global
├── model/           → Entidades JPA
├── repository/      → Repositórios Spring Data JPA
├── security/        → Implementações de segurança
├── service/         → Lógica de negócio
└── DscatalogApplication.java
```
## 🗂 Testes Automatizados

```bash
src/test/java/com/github/freddy/dscatalog
├── controller/
│   └── ProductControllerTests.java
├── service/
│   ├── CategoryServiceTests.java
│   └── ProductServiceTests.java
```


## ⚙ Configuração e Execução

### Pré-requisitos

- JDK 21+
- Maven
- PostgresSQL

### Rodar o projeto

```bash
./mvnw spring-boot:run

```
- A API ficará disponível em: 
```bash
http://localhost:8080/api/v1/products 

```

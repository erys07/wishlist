# Wishlist API

API REST para gerenciamento de lista de desejos (Wishlist) de clientes em um e-commerce.

## 🚀 Tecnologias

- Java 25
- Spring Boot 4.0.0-SNAPSHOT
- MongoDB 7.0
- Maven
- Docker & Docker Compose

## 📋 Como executar

### Usando Docker Compose

```bash
docker-compose up --build
```

A API estará disponível em: `http://localhost:8080`

### Executar localmente

```bash
# Iniciar MongoDB
docker-compose up mongodb -d

# Executar aplicação
./mvnw spring-boot:run
```

## 🧪 Executar testes

```bash
# Executar todos os testes
./mvnw test

# Executar testes e gerar relatório de cobertura
./mvnw clean test jacoco:report
```

## 📚 Endpoints

### 1. Adicionar Item
**POST** `/wishlist/item`

**Request:**
```json
{
    "userId": "user123",
    "itemId": "item001",
    "name": "Produto Exemplo"
}
```

**Response (201 Created):**
```json
{
    "wishlistId": "wishlist-id",
    "itemId": "item001",
    "name": "Produto Exemplo"
}
```

**Regras:**
- Limite máximo de **20 itens** por wishlist
- Se o item já existir, retorna os dados do item existente
- Se a wishlist não existir, ela será criada automaticamente

---

### 2. Listar Itens
**GET** `/wishlist/{userId}/items`

**Response (200 OK):**
```json
{
    "items": [
        {
            "itemId": "item001",
            "name": "Produto Exemplo"
        }
    ]
}
```

---

### 3. Verificar se Item Existe
**GET** `/wishlist/{userId}/items/{itemId}`

**Response (200 OK):**
```json
{
    "exists": true
}
```

---

### 4. Remover Item
**DELETE** `/wishlist/{userId}/items/{itemId}`

**Response (204 No Content):**
```
(Sem conteúdo)
```

---

## ⚠️ Tratamento de Erros

Erros retornam no formato:

```json
{
    "message": "Mensagem de erro descritiva",
    "timestamp": "2025-11-17T10:30:00",
    "path": "/wishlist/item"
}
```

**Códigos de Status:**
- `201 Created`: Item adicionado
- `200 OK`: Operação bem-sucedida
- `204 No Content`: Item removido
- `400 Bad Request`: Validação falhou ou limite excedido
- `404 Not Found`: Recurso não encontrado
- `500 Internal Server Error`: Erro interno

---

## 📖 Postman

Importe a collection: `Wishlist_API.postman_collection.json`

**Variáveis:**
- `baseUrl`: `http://localhost:8080`
- `userId`: `user123`
- `itemId`: `item001`

---

## 🏗️ Arquitetura

Clean Architecture com separação em camadas:
- `domain/`: Entidades, exceções, repositórios
- `application/`: Casos de uso, DTOs, serviços
- `infra/`: Controllers, configurações

---

## 📊 Testes

40 testes cobrindo controllers, use cases, services e exceptions.

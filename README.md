# Wishlist API - Documentação

## Como executar

1. **Subir os containers:**
   ```bash
   docker-compose up --build
   ```

2. **A API estará disponível em:**
   ```
   http://localhost:8080
   ```

3. **Importar a collection no Postman:**
   - Abra o Postman
   - Clique em "Import"
   - Selecione o arquivo `Wishlist_API.postman_collection.json`

## Endpoints

### 1. Adicionar Item
**POST** `/wishlist/item`

**Request Body:**
```json
{
    "userId": "user123",
    "itemId": "item001",
    "name": "Produto Exemplo"
}
```

**Response (200 OK):**
```json
{
    "wishlistId": "wishlist-id",
    "itemId": "item001",
    "name": "Produto Exemplo"
}
```

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

### 3. Verificar se Item Existe
**GET** `/wishlist/{userId}/items/{itemId}`

**Response (200 OK):**
```json
{
    "exists": true
}
```

### 4. Remover Item
**DELETE** `/wishlist/{userId}/items/{itemId}`

**Response (200 OK):**
```
(No content)
```

## Variáveis do Postman

A collection inclui as seguintes variáveis que podem ser alteradas:
- `baseUrl`: `http://localhost:8080`
- `userId`: `user123`
- `itemId`: `item001`

## Fluxo de Teste Recomendado

1. **Adicionar Item** - Adiciona um item à wishlist
2. **Listar Itens** - Verifica se o item foi adicionado
3. **Verificar Item Existe** - Confirma que o item existe
4. **Remover Item** - Remove o item
5. **Verificar Item Existe** - Confirma que o item foi removido (deve retornar `false`)


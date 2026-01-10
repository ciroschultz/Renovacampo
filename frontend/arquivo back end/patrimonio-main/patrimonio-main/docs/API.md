# 🌐 Documentação da API - RenovaCampo

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Autenticação](#-autenticação)
- [Propriedades](#-propriedades-properties)
- [Projetos](#-projetos-projects)
- [Investidores](#-investidores-investors)
- [Empreendimentos](#-empreendimentos-enterprises)
- [Gestão de Arquivos](#-gestão-de-arquivos)
- [Códigos de Resposta](#-códigos-de-resposta)
- [Exemplos de Uso](#-exemplos-de-uso)

## 🎯 Visão Geral

A API REST do RenovaCampo segue os padrões RESTful e retorna dados em formato JSON. A API é dividida em módulos principais que gerenciam diferentes aspectos do sistema rural.

### Base URLs

- **Local**: `http://localhost:8080`
- **Rede**: `http://192.168.15.7:8080`
- **API Prefix**: `/api/v1`

### Headers Padrão

```http
Content-Type: application/json
Accept: application/json
```

### Formato de Resposta

```json
{
  "data": {},
  "status": "success|error",
  "message": "Descrição da resposta",
  "timestamp": "2025-06-06T02:15:05.751Z"
}
```

## 🔐 Autenticação

> **Nota**: Na versão atual (v1.1-SNAPSHOT), a API não requer autenticação. Autenticação será implementada na v1.2.0.

## 🏠 Propriedades (Properties)

### Modelo de Dados

```json
{
  "id": 1,
  "name": "Fazenda Santa Clara",
  "description": "Propriedade rural para cultivo de soja e milho",
  "totalArea": 500.0,
  "availableArea": 300.0,
  "type": "FARM",
  "address": "Zona Rural",
  "state": "RS",
  "city": "Santa Maria",
  "latitude": -29.6842,
  "longitude": -53.8069
}
```

### Endpoints

#### Listar Propriedades

```http
GET /api/v1/property
```

**Resposta:**
```json
[
  {
    "id": 1,
    "name": "Fazenda Santa Clara",
    "description": "Propriedade rural para cultivo de soja e milho",
    "totalArea": 500.0,
    "availableArea": 300.0,
    "type": "FARM",
    "address": "Zona Rural",
    "state": "RS",
    "city": "Santa Maria",
    "latitude": -29.6842,
    "longitude": -53.8069
  }
]
```

#### Buscar Propriedade por ID

```http
GET /api/v1/property/{id}
```

**Parâmetros:**
- `id` (path): ID da propriedade

**Exemplo:**
```bash
curl http://localhost:8080/api/v1/property/1
```

#### Criar Propriedade

```http
POST /api/v1/property
```

**Body:**
```json
{
  "name": "Nova Fazenda",
  "description": "Descrição da propriedade",
  "totalArea": 1000.0,
  "availableArea": 800.0,
  "type": "FARM",
  "address": "Zona Rural",
  "state": "RS", 
  "city": "Porto Alegre",
  "latitude": -30.0346,
  "longitude": -51.2177
}
```

#### Deletar Propriedade

```http
DELETE /api/v1/property/{id}
```

## 📋 Projetos (Projects)

### Modelo de Dados

```json
{
  "id": 1,
  "name": "Plantio de Soja 2025",
  "category": "AGRICULTURE",
  "description": "Projeto de plantio de soja na safra 2025",
  "startDate": "2025-01-15",
  "estimatedEndDate": "2025-06-30", 
  "endDate": null,
  "priority": "HIGH",
  "status": "IN_PROGRESS",
  "totalEstimatedCosts": 50000.0,
  "totalCosts": 25000.0,
  "totalInvestment": 60000.0,
  "estimatedReturnOverInvestment": 0.20
}
```

### Enums

#### Status
- `PLANNING` - Planejamento
- `APPROVED` - Aprovado
- `IN_PROGRESS` - Em andamento
- `ON_HOLD` - Em espera
- `COMPLETED` - Concluído
- `CANCELLED` - Cancelado

#### Prioridade
- `LOW` - Baixa
- `MEDIUM` - Média
- `HIGH` - Alta
- `CRITICAL` - Crítica

### Endpoints

#### Listar Projetos

```http
GET /api/v1/project
```

#### Buscar Projeto por ID

```http
GET /api/v1/project/{id}
```

#### Criar Projeto

```http
POST /api/v1/project
```

**Body:**
```json
{
  "name": "Novo Projeto",
  "category": "AGRICULTURE",
  "description": "Descrição do projeto",
  "startDate": "2025-01-01",
  "estimatedEndDate": "2025-12-31",
  "priority": "MEDIUM",
  "status": "PLANNING",
  "totalEstimatedCosts": 100000.0,
  "totalInvestment": 120000.0,
  "estimatedReturnOverInvestment": 0.15
}
```

#### Atualizar Projeto

```http
PUT /api/v1/project/{id}
```

#### Deletar Projeto

```http
DELETE /api/v1/project/{id}
```

#### Filtrar por Status

```http
GET /api/v1/project/status/{status}
```

**Exemplo:**
```bash
curl http://localhost:8080/api/v1/project/status/IN_PROGRESS
```

#### Filtrar por Prioridade

```http
GET /api/v1/project/priority/{priority}
```

#### Filtrar por Categoria

```http
GET /api/v1/project/category/{category}
```

#### Buscar Projetos

```http
GET /api/v1/project/search?q={query}
```

#### Listar Categorias

```http
GET /api/v1/project/categories
```

#### Atualizar Status

```http
PATCH /api/v1/project/{id}/status
```

**Body:**
```json
{
  "status": "COMPLETED"
}
```

## 👥 Investidores (Investors)

### Modelo de Dados

```json
{
  "id": 1,
  "name": "João Silva",
  "taxId": "123.456.789-00",
  "email": "joao@email.com",
  "phone": "(51) 99999-9999",
  "address": "Rua das Flores, 123",
  "city": "Porto Alegre",
  "state": "RS",
  "totalFunds": 500000.0,
  "investedFunds": 200000.0,
  "availableFunds": 300000.0,
  "description": "Investidor em agronegócio",
  "active": true,
  "createDate": "2025-01-01T10:00:00Z",
  "updateDate": "2025-06-06T10:00:00Z"
}
```

### Endpoints

#### Listar Investidores

```http
GET /api/v1/investors
```

#### Buscar Investidor por ID

```http
GET /api/v1/investors/{id}
```

#### Buscar por CPF/CNPJ

```http
GET /api/v1/investors/tax-id/{taxId}
```

**Exemplo:**
```bash
curl "http://localhost:8080/api/v1/investors/tax-id/123.456.789-00"
```

#### Criar Investidor

```http
POST /api/v1/investors
```

**Body:**
```json
{
  "name": "Maria Santos",
  "taxId": "987.654.321-00",
  "email": "maria@email.com",
  "phone": "(51) 88888-8888",
  "address": "Av. Principal, 456",
  "city": "Caxias do Sul",
  "state": "RS",
  "totalFunds": 1000000.0,
  "description": "Investidora focada em projetos sustentáveis"
}
```

#### Atualizar Investidor

```http
PUT /api/v1/investors/{id}
```

#### Deletar Investidor

```http
DELETE /api/v1/investors/{id}
```

#### Ativar Investidor

```http
PATCH /api/v1/investors/{id}/activate
```

#### Desativar Investidor

```http
PATCH /api/v1/investors/{id}/deactivate
```

#### Buscar por Nome

```http
GET /api/v1/investors/search?name={name}
```

#### Buscar por Localização

```http
GET /api/v1/investors/location?city={city}&state={state}
```

#### Investidores com Fundos Disponíveis

```http
GET /api/v1/investors/with-funds
```

#### Estatísticas dos Investidores

```http
GET /api/v1/investors/statistics
```

**Resposta:**
```json
{
  "totalInvestors": 5,
  "activeInvestors": 4,
  "totalFunds": 2500000.0,
  "investedFunds": 800000.0,
  "availableFunds": 1700000.0,
  "averageFundUtilization": 0.32
}
```

## 🏢 Empreendimentos (Enterprises)

### Modelo de Dados

```json
{
  "id": 1,
  "name": "Empreendimento Soja Verde",
  "description": "Cultivo sustentável de soja orgânica",
  "propertyId": 1,
  "projectId": 1,
  "status": "ACTIVE",
  "launchDate": "2025-01-15",
  "fundingDeadline": "2025-03-31",
  "expectedCompletionDate": "2025-12-31",
  "totalInvestmentRequired": 500000.0,
  "totalInvestmentRaised": 300000.0,
  "minimumInvestment": 10000.0,
  "expectedCommodityValueIncrease": 0.25,
  "fundingProgress": 0.60,
  "active": true,
  "createDate": "2025-01-01T10:00:00Z",
  "updateDate": "2025-06-06T10:00:00Z"
}
```

### Enums

#### Status
- `PLANNING` - Planejamento
- `ACTIVE` - Ativo
- `COMPLETED` - Concluído
- `SUSPENDED` - Suspenso
- `CANCELLED` - Cancelado

### Endpoints

#### Listar Empreendimentos

```http
GET /api/v1/enterprises
```

#### Buscar Empreendimento por ID

```http
GET /api/v1/enterprises/{id}
```

#### Criar Empreendimento

```http
POST /api/v1/enterprises
```

**Body:**
```json
{
  "name": "Novo Empreendimento",
  "description": "Descrição do empreendimento",
  "propertyId": 1,
  "projectId": 1,
  "status": "PLANNING",
  "launchDate": "2025-07-01",
  "fundingDeadline": "2025-09-30",
  "expectedCompletionDate": "2026-06-30",
  "totalInvestmentRequired": 750000.0,
  "minimumInvestment": 15000.0,
  "expectedCommodityValueIncrease": 0.30
}
```

#### Atualizar Empreendimento

```http
PUT /api/v1/enterprises/{id}
```

#### Deletar Empreendimento

```http
DELETE /api/v1/enterprises/{id}
```

#### Filtrar por Status

```http
GET /api/v1/enterprises/status/{status}
```

#### Empreendimentos Abertos para Investimento

```http
GET /api/v1/enterprises/open-funding
```

#### Empreendimentos com Funding Insuficiente

```http
GET /api/v1/enterprises/underfunded
```

#### Empreendimentos com Prazo de Funding Vencido

```http
GET /api/v1/enterprises/overdue-funding
```

#### Buscar por Propriedade

```http
GET /api/v1/enterprises/property/{propertyId}
```

#### Buscar por Projeto

```http
GET /api/v1/enterprises/project/{projectId}
```

#### Listar Investidores do Empreendimento

```http
GET /api/v1/enterprises/{enterpriseId}/investors
```

**Resposta:**
```json
[
  {
    "id": 1,
    "enterpriseId": 1,
    "investorId": 1,
    "investmentAmount": 50000.0,
    "shareholdingPercentage": 10.0,
    "investmentDate": "2025-02-15"
  }
]
```

#### Adicionar Investidor ao Empreendimento

```http
POST /api/v1/enterprises/{enterpriseId}/investors
```

**Body:**
```json
{
  "investorId": 2,
  "investmentAmount": 75000.0,
  "shareholdingPercentage": 15.0,
  "investmentDate": "2025-06-06"
}
```

#### Remover Investidor do Empreendimento

```http
DELETE /api/v1/enterprises/{enterpriseId}/investors/{investorId}
```

#### Estatísticas dos Empreendimentos

```http
GET /api/v1/enterprises/statistics
```

#### Empreendimentos Completando entre Datas

```http
GET /api/v1/enterprises/completing-between?startDate={date}&endDate={date}
```

#### Validar Investimento

```http
GET /api/v1/enterprises/{enterpriseId}/can-accept-investment?amount={amount}
```

**Resposta:**
```json
{
  "canAccept": true,
  "reason": "Investment within acceptable range",
  "maxAmount": 200000.0,
  "remainingFunding": 200000.0
}
```

## 📁 Gestão de Arquivos

### Upload de Fotos

```http
POST /api/v1/photos/upload/{propertyId}
```

**Headers:**
```http
Content-Type: multipart/form-data
```

**Form Data:**
- `file`: Arquivo de imagem (JPG, PNG, max 5MB)

**Resposta:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "fileName": "photo.jpg",
  "fileSize": 1024000,
  "uploadDate": "2025-06-06T10:00:00Z",
  "thumbnailGenerated": true
}
```

### Obter Thumbnail

```http
GET /api/v1/photos/{id}/thumbnail
```

### Obter Primeira Foto da Propriedade

```http
GET /api/v1/photos/property/{propertyId}/thumbnail
```

### Upload de Documentos

```http
POST /api/v1/files/upload/{propertyId}?fileType=DOCUMENT
```

**Headers:**
```http
Content-Type: multipart/form-data
```

**Form Data:**
- `file`: Arquivo (PDF, DOC, XLS, max 10MB)

### Download de Arquivo

```http
GET /api/v1/files/{id}
```

### Deletar Arquivo

```http
DELETE /api/v1/files/{id}
```

## 📊 Códigos de Resposta

| Código | Descrição | Exemplo |
|--------|-----------|---------|
| `200` | Sucesso | Operação realizada com sucesso |
| `201` | Criado | Recurso criado com sucesso |
| `204` | Sem Conteúdo | Operação realizada, sem dados para retornar |
| `400` | Requisição Inválida | Dados de entrada inválidos |
| `404` | Não Encontrado | Recurso não existe |
| `409` | Conflito | Violação de regra de negócio |
| `422` | Entidade Não Processável | Erro de validação |
| `500` | Erro Interno | Erro no servidor |

### Formato de Erro

```json
{
  "error": "VALIDATION_ERROR",
  "message": "Dados de entrada inválidos",
  "details": [
    {
      "field": "name",
      "message": "Nome é obrigatório"
    },
    {
      "field": "totalArea", 
      "message": "Área total deve ser maior que zero"
    }
  ],
  "timestamp": "2025-06-06T10:00:00Z"
}
```

## 💡 Exemplos de Uso

### Criar uma Propriedade com Projeto e Empreendimento

```bash
# 1. Criar propriedade
curl -X POST http://localhost:8080/api/v1/property \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Fazenda Nova Esperança",
    "description": "Propriedade para cultivo de milho",
    "totalArea": 800.0,
    "availableArea": 600.0,
    "type": "FARM",
    "address": "Zona Rural",
    "state": "RS",
    "city": "Passo Fundo"
  }'

# Resposta: {"id": 15, ...}

# 2. Criar projeto
curl -X POST http://localhost:8080/api/v1/project \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Cultivo de Milho 2025",
    "category": "AGRICULTURE", 
    "description": "Projeto de cultivo sustentável",
    "startDate": "2025-07-01",
    "estimatedEndDate": "2025-12-31",
    "priority": "HIGH",
    "status": "PLANNING",
    "totalEstimatedCosts": 200000.0,
    "totalInvestment": 250000.0,
    "estimatedReturnOverInvestment": 0.18
  }'

# Resposta: {"id": 7, ...}

# 3. Criar empreendimento
curl -X POST http://localhost:8080/api/v1/enterprises \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Empreendimento Milho Sustentável",
    "description": "Cultivo de milho com práticas sustentáveis",
    "propertyId": 15,
    "projectId": 7,
    "status": "PLANNING",
    "launchDate": "2025-07-01",
    "fundingDeadline": "2025-08-31",
    "expectedCompletionDate": "2025-12-31",
    "totalInvestmentRequired": 250000.0,
    "minimumInvestment": 5000.0,
    "expectedCommodityValueIncrease": 0.18
  }'
```

### Adicionar Investidor a um Empreendimento

```bash
# 1. Buscar investidores disponíveis
curl http://localhost:8080/api/v1/investors/with-funds

# 2. Adicionar investidor ao empreendimento
curl -X POST http://localhost:8080/api/v1/enterprises/1/investors \
  -H "Content-Type: application/json" \
  -d '{
    "investorId": 1,
    "investmentAmount": 50000.0,
    "shareholdingPercentage": 20.0,
    "investmentDate": "2025-06-06"
  }'
```

### Upload de Foto para Propriedade

```bash
# Upload de foto
curl -X POST http://localhost:8080/api/v1/photos/upload/1 \
  -F "file=@/path/to/photo.jpg"

# Obter thumbnail
curl http://localhost:8080/api/v1/photos/property/1/thumbnail \
  --output thumbnail.jpg
```

### Monitoramento de Empreendimentos

```bash
# Listar empreendimentos com funding insuficiente
curl http://localhost:8080/api/v1/enterprises/underfunded

# Verificar estatísticas
curl http://localhost:8080/api/v1/enterprises/statistics

# Verificar empreendimentos com prazo vencido
curl http://localhost:8080/api/v1/enterprises/overdue-funding
```

## 🔧 Ferramentas de Teste

### Postman Collection

Importe a collection Postman disponível em `qa/postman/RenovaCampo.postman_collection.json`

### Testes Automatizados

```bash
# Executar suite de testes da API
cd qa/test-reports
python3 realistic_test_executor.py
```

### Swagger UI (Planejado v1.2.0)

```
http://localhost:8080/swagger-ui.html
```

---

**Próximo passo**: [🏗️ Documentação de Arquitetura](ARQUITETURA.md)
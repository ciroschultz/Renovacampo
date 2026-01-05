# Objetivo do Projeto - Renova Campo

## Meta Principal

Desenvolver o sistema completo da plataforma **Renova Campo**, que conecta proprietários de terras, produtores rurais e investidores para agricultura regenerativa e sustentável.

---

## Stack Tecnológica

### Frontend
| Tecnologia | Uso |
|------------|-----|
| **HTML5** | Estrutura das páginas |
| **CSS3** | Estilização e responsividade |
| **JavaScript** | Interatividade e consumo de APIs |

### Backend
| Tecnologia | Uso |
|------------|-----|
| **Java** | Linguagem principal |
| **Spring Boot** | Framework backend |
| **Spring Data JPA** | Persistência de dados |
| **Spring Security** | Autenticação e autorização |
| **Maven/Gradle** | Gerenciamento de dependências |

### Banco de Dados
| Tecnologia | Uso |
|------------|-----|
| **PostgreSQL** ou **MySQL** | Banco de dados relacional |
| **H2** | Banco em memória para testes |

---

## Módulos a Desenvolver

### 1. Módulo de Autenticação
- [ ] Cadastro de usuários (3 perfis)
- [ ] Login/Logout
- [ ] Recuperação de senha
- [ ] Verificação de e-mail

### 2. Módulo de Propriedades
- [ ] CRUD de propriedades
- [ ] Upload de fotos e documentos
- [ ] Integração com mapas
- [ ] Filtros e busca

### 3. Módulo de Projetos
- [ ] CRUD de projetos
- [ ] Gestão de status e prioridade
- [ ] Acompanhamento financeiro
- [ ] Upload de documentos

### 4. Módulo de Investidores
- [ ] CRUD de investidores
- [ ] Gestão de fundos
- [ ] Validação CPF/CNPJ
- [ ] Histórico de investimentos

### 5. Módulo de Empreendimentos
- [ ] Vinculação Propriedade + Projeto + Investidor
- [ ] Cálculo de progresso de financiamento
- [ ] Gestão de cotas

### 6. Simulador
- [ ] Cálculo de rentabilidade por cultura
- [ ] Parâmetros configuráveis
- [ ] Exibição de resultados

### 7. Dashboard
- [ ] Estatísticas gerais
- [ ] Gráficos e métricas
- [ ] Visão por perfil de usuário

---

## Estrutura do Projeto

```
renova-campo/
├── frontend/
│   ├── index.html
│   ├── css/
│   │   ├── style.css
│   │   ├── components.css
│   │   └── responsive.css
│   ├── js/
│   │   ├── main.js
│   │   ├── api.js
│   │   ├── auth.js
│   │   └── utils.js
│   └── pages/
│       ├── login.html
│       ├── cadastro.html
│       ├── propriedades.html
│       ├── projetos.html
│       ├── investidores.html
│       ├── simulador.html
│       └── dashboard.html
│
├── backend/
│   └── renovacampo/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/com/renovacampo/
│       │   │   │   ├── RenovaCampoApplication.java
│       │   │   │   ├── config/
│       │   │   │   ├── controller/
│       │   │   │   ├── service/
│       │   │   │   ├── repository/
│       │   │   │   ├── model/
│       │   │   │   ├── dto/
│       │   │   │   └── exception/
│       │   │   └── resources/
│       │   │       ├── application.properties
│       │   │       └── data.sql
│       │   └── test/
│       ├── pom.xml
│       └── README.md
│
└── docs/
    ├── RESUMO_RENOVACAMPO.md
    ├── objetivo.md
    └── API.md
```

---

## API REST - Endpoints Planejados

### Autenticação
```
POST /api/auth/register     - Cadastro de usuário
POST /api/auth/login        - Login
POST /api/auth/logout       - Logout
POST /api/auth/refresh      - Refresh token
```

### Propriedades
```
GET    /api/v1/properties          - Listar todas
GET    /api/v1/properties/{id}     - Buscar por ID
POST   /api/v1/properties          - Criar nova
PUT    /api/v1/properties/{id}     - Atualizar
DELETE /api/v1/properties/{id}     - Deletar
```

### Projetos
```
GET    /api/v1/projects            - Listar todos
GET    /api/v1/projects/{id}       - Buscar por ID
POST   /api/v1/projects            - Criar novo
PUT    /api/v1/projects/{id}       - Atualizar
DELETE /api/v1/projects/{id}       - Deletar
```

### Investidores
```
GET    /api/v1/investors           - Listar todos
GET    /api/v1/investors/{id}      - Buscar por ID
GET    /api/v1/investors/tax/{cpf} - Buscar por CPF/CNPJ
POST   /api/v1/investors           - Criar novo
PUT    /api/v1/investors/{id}      - Atualizar
DELETE /api/v1/investors/{id}      - Deletar
```

### Empreendimentos
```
GET    /api/v1/enterprises         - Listar todos
GET    /api/v1/enterprises/{id}    - Buscar por ID
POST   /api/v1/enterprises         - Criar novo
PUT    /api/v1/enterprises/{id}    - Atualizar
DELETE /api/v1/enterprises/{id}    - Deletar
```

### Simulador
```
POST   /api/v1/simulator/calculate - Calcular rentabilidade
GET    /api/v1/simulator/cultures  - Listar culturas disponíveis
```

---

## Padrões e Boas Práticas

### Backend
- Arquitetura em camadas (Controller → Service → Repository)
- DTOs para transferência de dados
- Validação com Bean Validation
- Tratamento centralizado de exceções
- Documentação com Swagger/OpenAPI

### Frontend
- Código semântico e acessível
- CSS com variáveis (custom properties)
- JavaScript modular
- Responsividade (mobile-first)
- Consumo de API via Fetch API

### Segurança
- Autenticação JWT
- Validação de entrada de dados
- Proteção contra SQL Injection
- Proteção contra XSS
- CORS configurado

---

## Fases de Desenvolvimento

### Fase 1 - Setup e Autenticação
1. Configurar projeto Spring Boot
2. Configurar banco de dados
3. Implementar autenticação
4. Criar páginas de login/cadastro

### Fase 2 - Módulos Core
1. CRUD de Propriedades
2. CRUD de Projetos
3. CRUD de Investidores
4. Integração frontend/backend

### Fase 3 - Funcionalidades Avançadas
1. Módulo de Empreendimentos
2. Simulador de investimentos
3. Dashboard com estatísticas

### Fase 4 - Refinamento
1. Testes
2. Documentação da API
3. Otimizações de performance
4. Deploy

---

## Cores do Sistema

```css
:root {
    --verde-principal: #004F3B;
    --laranja: #E95E1D;
    --fundo: #f8f8f5;
    --texto: #333333;
    --branco: #ffffff;
}
```

---

## Próximos Passos

1. **Criar estrutura de pastas** do projeto
2. **Inicializar projeto Spring Boot** com dependências
3. **Criar páginas HTML** base do frontend
4. **Configurar banco de dados**
5. **Implementar primeiro módulo** (Autenticação)

---

*Documento criado em: Janeiro/2025*

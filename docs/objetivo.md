# Objetivo do Projeto - Renova Campo

---

## Configurações do Projeto

| Configuração | Valor |
|--------------|-------|
| **Pasta Oficial** | `C:/Users/ciroa/Renovacampo` |
| **Sistema Operacional** | Windows |
| **Versão do Projeto** | 1.0.0 |
| **Data de Início** | Janeiro/2025 |

> **IMPORTANTE:** Todo o desenvolvimento deve ser feito dentro desta pasta.

---

## Instruções para o Claude

### Ao INICIALIZAR o projeto:
1. Ler arquivos de documentação (`.md`)
2. Ver estrutura de arquivos do projeto
3. Ler histórico do git (`git log`)

### Ao ENCERRAR a sessão:
1. `git add .` - Adicionar alterações
2. `git commit -m "mensagem"` - Fazer commit
3. `git push` - Empurrar para repositório remoto

---

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
| **Maven** | Gerenciamento de dependências |

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
C:/Users/ciroa/Renovacampo/
├── frontend/
│   ├── index.html
│   ├── css/
│   │   └── style.css
│   ├── js/
│   │   └── main.js
│   ├── pages/
│   └── assets/
│
├── backend/
│   └── renovacampo/
│       ├── src/main/java/com/renovacampo/
│       │   ├── controller/
│       │   ├── service/
│       │   ├── repository/
│       │   ├── model/
│       │   └── dto/
│       ├── src/main/resources/
│       └── pom.xml
│
└── docs/
    ├── RESUMO_RENOVACAMPO.md
    └── objetivo.md
```

---

## API REST - Endpoints

### Autenticação
```
POST /api/auth/register
POST /api/auth/login
POST /api/auth/logout
```

### Propriedades
```
GET    /api/v1/properties
GET    /api/v1/properties/{id}
POST   /api/v1/properties
PUT    /api/v1/properties/{id}
DELETE /api/v1/properties/{id}
```

### Projetos
```
GET    /api/v1/projects
GET    /api/v1/projects/{id}
POST   /api/v1/projects
PUT    /api/v1/projects/{id}
DELETE /api/v1/projects/{id}
```

### Investidores
```
GET    /api/v1/investors
GET    /api/v1/investors/{id}
POST   /api/v1/investors
PUT    /api/v1/investors/{id}
DELETE /api/v1/investors/{id}
```

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

## Fases de Desenvolvimento

1. **Fase 1** - Setup e Autenticação
2. **Fase 2** - Módulos Core (Propriedades, Projetos, Investidores)
3. **Fase 3** - Funcionalidades Avançadas (Empreendimentos, Simulador, Dashboard)
4. **Fase 4** - Testes e Deploy

---

*Documento criado em: Janeiro/2025*

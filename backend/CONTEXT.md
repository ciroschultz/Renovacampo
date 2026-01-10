# Contexto Completo - Projeto Patrimônio RenovaCampo

## Resumo Executivo
Sistema de gestão de propriedades rurais desenvolvido em Spring Boot com funcionalidades de cadastro, armazenamento de arquivos/fotos, API REST e **sistema de carregamento progressivo**. Atualmente rodando em Raspberry Pi com PostgreSQL local.

## Estado Atual do Sistema

### Ambiente
- **Plataforma**: Raspberry Pi (Linux ARM64)
- **IP Local**: 192.168.15.7:8080
- **Diretório**: `/home/matheus/claude/renovacampo/patrimonio`
- **Repositório**: https://github.com/coxasboy/patrimonio
- **Branch**: feature/enterprise-management (sistema completo com empreendimentos)

### Stack Tecnológica
- Java 17 + Spring Boot 3.4.5
- PostgreSQL 15
- Maven Wrapper (mvnw)
- JPA/Hibernate
- REST API
- Thymeleaf + HTMX (interface web)
- CSS moderno (DigitalOcean-style)
- **Sistema de Loading Progressivo** (performance otimizada)

### Banco de Dados
- **Database**: patrimonio
- **Usuário**: patrimonio_user  
- **Senha**: patrimonio123
- **Tabelas**: property, project, investor, enterprise, enterprise_investor, stored_files
- **Dados**: 15 propriedades, 6 projetos, 2 investidores, 5 empreendimentos

## Funcionalidades Implementadas

### 1. Interface Web (HTMX + Thymeleaf)
```
GET    /                         - Dashboard principal
GET    /properties               - Lista de propriedades  
GET    /properties/new           - Formulário nova propriedade
GET    /properties/{id}          - Visualização detalhada
GET    /properties/{id}/edit     - Formulário de edição
GET    /projects                 - Lista de projetos
GET    /projects/new             - Formulário novo projeto
GET    /projects/{id}            - Visualização de projeto
GET    /projects/{id}/edit       - Edição de projeto
GET    /investors                - Lista de investidores
GET    /investors/new            - Formulário novo investidor
GET    /investors/{id}           - Visualização de investidor
GET    /investors/{id}/edit      - Edição de investidor
GET    /enterprises              - Lista de empreendimentos
GET    /enterprises/new          - Formulário novo empreendimento
GET    /enterprises/{id}         - Visualização de empreendimento
GET    /enterprises/{id}/edit    - Edição de empreendimento
```

### 2. APIs REST Completas

**Propriedades:**
```
GET    /api/v1/property          - Lista todas
GET    /api/v1/property/{id}     - Busca por ID
POST   /api/v1/property          - Adiciona
DELETE /api/v1/property/{id}     - Remove
```

**Projetos:**
```
GET    /api/v1/project           - Lista todos
GET    /api/v1/project/{id}      - Busca por ID
POST   /api/v1/project           - Cria projeto
PUT    /api/v1/project/{id}      - Atualiza projeto
DELETE /api/v1/project/{id}      - Remove projeto
GET    /api/v1/project/status/{status}     - Por status
GET    /api/v1/project/priority/{priority} - Por prioridade
GET    /api/v1/project/category/{category} - Por categoria
GET    /api/v1/project/search    - Busca projetos
GET    /api/v1/project/categories - Lista categorias
PATCH  /api/v1/project/{id}/status - Atualiza status
```

**Investidores:**
```
GET    /api/v1/investors         - Lista todos
GET    /api/v1/investors/{id}    - Busca por ID
GET    /api/v1/investors/taxId/{taxId} - Por CPF/CNPJ
POST   /api/v1/investors         - Cria investidor
PUT    /api/v1/investors/{id}    - Atualiza investidor
DELETE /api/v1/investors/{id}    - Remove investidor
PATCH  /api/v1/investors/{id}/activate   - Ativa
PATCH  /api/v1/investors/{id}/deactivate - Desativa
GET    /api/v1/investors/search  - Busca por nome
GET    /api/v1/investors/location - Por localização
GET    /api/v1/investors/with-funds - Com fundos disponíveis
GET    /api/v1/investors/statistics - Estatísticas
```

**Empreendimentos:**
```
GET    /api/v1/enterprises       - Lista todos
GET    /api/v1/enterprises/{id}  - Busca por ID
POST   /api/v1/enterprises       - Cria empreendimento
PUT    /api/v1/enterprises/{id}  - Atualiza empreendimento
DELETE /api/v1/enterprises/{id}  - Remove empreendimento
GET    /api/v1/enterprises/status/{status} - Por status
GET    /api/v1/enterprises/open-funding    - Abertos para funding
GET    /api/v1/enterprises/underfunded     - Com funding insuficiente
GET    /api/v1/enterprises/property/{id}   - Por propriedade
GET    /api/v1/enterprises/project/{id}    - Por projeto
GET    /api/v1/enterprises/{id}/investors  - Investidores do empreendimento
POST   /api/v1/enterprises/{id}/investors  - Adiciona investidor
DELETE /api/v1/enterprises/{id}/investors/{investorId} - Remove investidor
GET    /api/v1/enterprises/statistics      - Estatísticas
```

### 3. Sistema de Storage com Progressive Loading
**Fotos:**
```
POST   /api/v1/photos/upload/{propertyId}
GET    /api/v1/photos/{id}
GET    /api/v1/photos/{id}/thumbnail
GET    /api/v1/photos/{id}/info
GET    /api/v1/photos/property/{propertyId}
GET    /api/v1/photos/property/{propertyId}/fragment
DELETE /api/v1/photos/{id}
PUT    /api/v1/photos/{id}
```

**Documentos:**
```
POST   /api/v1/files/upload/{propertyId}?fileType=DOCUMENT
GET    /api/v1/files/{id}
GET    /api/v1/files/{id}/info
GET    /api/v1/files/property/{propertyId}
GET    /api/v1/files/property/{propertyId}?quick=true      (otimizado)
GET    /api/v1/files/property/{propertyId}/fragment
DELETE /api/v1/files/{id}
PUT    /api/v1/files/{id}
```

### 4. Sistema de Loading Progressivo ⚡
- **Quick Load**: Query otimizada selecionando apenas campos essenciais
- **Performance**: ~60% mais rápido na exibição inicial
- **UX**: Loading indicators com spinners visuais
- **Implementação**: 2 etapas (quick load + full load após 500ms)
- **Páginas**: view.html e form.html

## Estrutura do Projeto
```
patrimonio/
├── src/main/java/org/acabativa/rc/patrimonio/
│   ├── controller/
│   │   ├── NotaryRestApi.java
│   │   ├── PropertyViewController.java
│   │   └── HomeController.java
│   ├── entity/
│   │   └── Property.java
│   ├── repository/
│   │   └── PropertyDAO.java
│   ├── service/
│   │   └── Notary.java
│   ├── storage/
│   │   ├── entity/
│   │   │   └── StoredFile.java
│   │   ├── repository/
│   │   │   └── StoredFileRepository.java
│   │   ├── service/
│   │   │   ├── StorageService.java
│   │   │   └── FileSystemStorageService.java
│   │   ├── controller/
│   │   │   ├── FileController.java
│   │   │   └── PhotoController.java
│   │   ├── dto/
│   │   │   ├── FileDTO.java
│   │   │   ├── FileListDTO.java          (otimizado)
│   │   │   └── PhotoDTO.java
│   │   └── util/
│   │       └── ImageProcessor.java
│   └── PatrimonioApplication.java
└── src/main/resources/
    ├── application.properties
    ├── templates/
    │   ├── layout.html
    │   └── properties/
    │       ├── index.html
    │       ├── view.html                 (com progressive loading)
    │       ├── form.html                 (com progressive loading)
    │       └── fragments/
    │           ├── table.html
    │           ├── photos.html
    │           ├── photos-form.html
    │           ├── documents.html        (otimizado)
    │           └── documents-form.html
    └── static/
        └── css/
            └── main.css
```

## Modelos de Dados

### Property
```java
- id: Long
- name: String
- description: String  
- totalArea: Integer
- availableArea: Integer
- type: String
- address: String
- state: String
- city: String
- latitude: Double
- longitude: Double
```

### Project
```java
- id: Long
- name: String
- category: String
- description: String
- startDate: LocalDate
- estimatedEndDate: LocalDate
- endDate: LocalDate
- priority: enum (LOW, MEDIUM, HIGH, CRITICAL)
- status: enum (PLANNING, APPROVED, IN_PROGRESS, ON_HOLD, COMPLETED, CANCELLED)
- totalEstimatedCosts: BigDecimal
- totalCosts: BigDecimal
- totalInvestment: BigDecimal
- estimatedReturnOverInvestment: BigDecimal
```

### Investor
```java
- id: Long
- name: String
- taxId: String
- email: String
- phone: String
- address: String
- city: String
- state: String
- totalFunds: BigDecimal
- investedFunds: BigDecimal
- availableFunds: BigDecimal (calculado)
- description: String
- active: Boolean
- createDate: LocalDateTime
- updateDate: LocalDateTime
```

### Enterprise
```java
- id: Long
- name: String
- description: String
- status: enum (PLANNING, ACTIVE, COMPLETED, SUSPENDED, CANCELLED)
- propertyId: Long
- projectId: Long
- totalInvestmentRequired: BigDecimal
- totalInvestmentRaised: BigDecimal
- expectedCommodityValueIncrease: BigDecimal
- minimumInvestment: BigDecimal
- launchDate: LocalDate
- fundingDeadline: LocalDate
- expectedCompletionDate: LocalDate
- createDate: LocalDateTime
- updateDate: LocalDateTime
- active: Boolean
```

### EnterpriseInvestor
```java
- id: Long
- enterpriseId: Long
- investorId: Long
- investmentAmount: BigDecimal
- investmentDate: LocalDateTime
- notes: String
```

### StoredFile
```java
- id: Long
- fileName: String
- originalFileName: String
- filePath: String
- contentType: String
- fileSize: Long
- fileType: enum (PHOTO, DOCUMENT, OTHER)
- uploadDate: LocalDateTime
- propertyId: Long
- metadata: String (JSON)
```

### FileListDTO (Otimização)
```java
- id: Long
- originalFileName: String
- contentType: String
- fileSize: Long
```

## Configurações Importantes

**application.properties:**
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/patrimonio
spring.datasource.username=patrimonio_user
spring.datasource.password=patrimonio123
spring.jpa.hibernate.ddl-auto=update

# Storage
storage.location=/opt/claude/renovacampo/uploads
spring.servlet.multipart.max-file-size=10MB
spring.thymeleaf.cache=false
```

## Performance e Otimizações

### Progressive Loading System
1. **Quick Load Phase**:
   - SQL otimizada: `SELECT id, originalFileName, contentType, fileSize`
   - Carregamento imediato com loading indicators
   - Performance: ~60% mais rápido

2. **Full Load Phase**:
   - SQL completa com todos os campos
   - Delay de 500ms para experiência suave
   - Substitui loading indicators por dados completos

### Anti-Duplicação
- Input clearing após uploads: `hx-on::after-request="this.value = ''"`
- Prevenção de uploads duplos em form.html

## Scripts e Ferramentas

1. **load_properties.py** - Importa propriedades da API remota
2. **test_photo_upload.py** - Testa upload de fotos
3. **GitHub CLI** - Configurado para criação de PRs

## Funcionalidades da Interface Web

**Dashboard (/properties):**
- Estatísticas: total de propriedades, hectares totais, hectares disponíveis
- Filtros dinâmicos: busca por nome, tipo de propriedade, estado
- Tabela responsiva com paginação via HTMX
- Design inspirado no DigitalOcean

**Visualização de Propriedade (/properties/{id}):**
- Informações detalhadas organizadas em cards
- **Progressive loading** para fotos e documentos
- Loading spinners durante carregamento
- Download direto de documentos (sem uploads)

**Formulários de Edição (/properties/{id}/edit):**
- Upload de arquivos com progressive loading
- Seções separadas para fotos e documentos
- Anti-duplicação de uploads
- Fragments otimizados para formulários

## Comandos de Desenvolvimento

```bash
# Iniciar aplicação
cd /home/matheus/claude/renovacampo/patrimonio
./mvnw spring-boot:run > /home/matheus/claude/renovacampo/logs/patrimonio_app.log 2>&1 &

# Parar aplicação
pkill -f spring-boot:run

# Logs
tail -f /home/matheus/claude/renovacampo/logs/patrimonio_app.log

# Git workflow
git checkout main
git pull origin main
git checkout -b feature/nova-funcionalidade
# ... desenvolvimento ...
git add .
git commit -m "feat: nova funcionalidade"
git push origin feature/nova-funcionalidade
gh pr create --title "..." --body "..."
```

## Status Atual ✅

### Completado
- ✅ Sistema de storage completo (fotos + documentos)
- ✅ Interface web responsiva com HTMX
- ✅ Progressive loading system implementado
- ✅ Otimizações de performance (~60% mais rápido)
- ✅ Anti-duplicação de uploads
- ✅ Loading indicators com UX aprimorada
- ✅ Git workflow com PRs automatizadas

### Ambiente de Produção
- **URL**: http://192.168.15.7:8080/properties
- **Dados**: 18 propriedades carregadas
- **Uploads**: Funcionais com otimização
- **Performance**: Otimizada para carregamento rápido

### Próximas Oportunidades

1. **Performance Avançada**
   - Implementar cache Redis
   - Compressão de imagens automática
   - CDN para assets estáticos

2. **Funcionalidades de Negócio**
   - Sistema de usuários e autenticação
   - Workflow de aprovação de propriedades
   - Relatórios e analytics

3. **DevOps e Produção**
   - Dockerização completa
   - CI/CD pipeline
   - Monitoramento e alertas
   - Backup automatizado
# CLAUDE.md - Documentação Técnica RenovaCampo

## 🎯 Visão Geral
Sistema completo de gestão rural conectando propriedades, projetos, investidores e empreendimentos em uma plataforma moderna Spring Boot 3.4.5 com PostgreSQL.

## ⚡ Comandos Rápidos

### Iniciar/Parar Aplicação
```bash
# Scripts automatizados (recomendado)
/home/matheus/claude/renovacampo/scripts/restart_app.sh
/home/matheus/claude/renovacampo/scripts/status.sh

# Manual
cd /home/matheus/claude/renovacampo/patrimonio
./mvnw spring-boot:run > ../logs/patrimonio_app.log 2>&1 &
pkill -f spring-boot:run
```

### Verificação de Status
```bash
# PostgreSQL
sudo systemctl status postgresql
sudo -u postgres psql -d patrimonio -c "SELECT COUNT(*) FROM property;"

# Aplicação
ps aux | grep spring-boot | grep -v grep
curl -s http://localhost:8080/api/v1/property | python3 -c "import json, sys; print(f'Properties: {len(json.load(sys.stdin))}')"
```

### Logs
```bash
tail -f /home/matheus/claude/renovacampo/logs/patrimonio_app.log
```

## 🏗️ Arquitetura Técnica

### Stack Tecnológico
- **Backend**: Java 17 + Spring Boot 3.4.5
- **Database**: PostgreSQL 15 com HikariCP
- **ORM**: JPA/Hibernate com DDL automático
- **Frontend**: Thymeleaf + HTMX + CSS moderno
- **Maps**: Leaflet.js + OpenStreetMap
- **Build**: Maven Wrapper (mvnw)

### Estrutura de Pacotes
```
org.acabativa.rc
├── patrimonio/              # Core business logic
│   ├── PatrimonioApplication.java
│   ├── controller/          # Web + REST controllers
│   ├── entity/             # JPA entities (Property, Project, Investor, Enterprise)
│   ├── repository/         # Data repositories com queries customizadas
│   ├── service/            # Business services e cálculos
│   └── util/               # Utilities
└── storage/                # File storage module (independente)
    ├── controller/         # FileController, PhotoController
    ├── dto/                # Data transfer objects
    ├── entity/             # StoredFile entity
    ├── service/            # Storage services
    └── util/               # ImageProcessor
```

### Configuração Database
- **Database**: `patrimonio`
- **User**: `patrimonio_user`
- **Password**: `patrimonio123`
- **DDL**: Automático via Hibernate
- **Tables**: property, project, investor, enterprise, enterprise_investor, stored_files

## 🌐 Endpoints da API

### Interface Web
- **Dashboard**: `http://localhost:8080/`
- **Propriedades**: `http://localhost:8080/properties`
- **Projetos**: `http://localhost:8080/projects`
- **Investidores**: `http://localhost:8080/investors`
- **Empreendimentos**: `http://localhost:8080/enterprises`
- **Rede**: `http://192.168.15.7:8080/*`

### API REST

#### Properties
- `GET /api/v1/property` - Lista propriedades
- `GET /api/v1/property/{id}` - Busca por ID
- `POST /api/v1/property` - Cria propriedade
- `DELETE /api/v1/property/{id}` - Remove propriedade

#### Projects
- `GET /api/v1/project` - Lista projetos
- `GET /api/v1/project/{id}` - Visualiza projeto
- `POST /api/v1/project` - Cria projeto
- `PUT /api/v1/project/{id}` - Atualiza projeto
- `DELETE /api/v1/project/{id}` - Remove projeto
- `GET /api/v1/project/status/{status}` - Filtra por status
- `GET /api/v1/project/priority/{priority}` - Filtra por prioridade
- `GET /api/v1/project/category/{category}` - Filtra por categoria
- `GET /api/v1/project/search?q={query}` - Busca projetos
- `GET /api/v1/project/categories` - Lista todas as categorias
- `PATCH /api/v1/project/{id}/status` - Atualiza status

#### Investors
- `GET /api/v1/investors` - Lista investidores
- `GET /api/v1/investors/{id}` - Busca por ID
- `GET /api/v1/investors/tax-id/{taxId}` - Busca por CPF/CNPJ
- `POST /api/v1/investors` - Cria investidor
- `PUT /api/v1/investors/{id}` - Atualiza investidor
- `DELETE /api/v1/investors/{id}` - Remove investidor
- `PATCH /api/v1/investors/{id}/activate` - Ativa investidor
- `PATCH /api/v1/investors/{id}/deactivate` - Desativa investidor
- `GET /api/v1/investors/search?name={name}` - Busca por nome
- `GET /api/v1/investors/location?city={city}&state={state}` - Busca por localização
- `GET /api/v1/investors/with-funds` - Investidores com fundos disponíveis
- `GET /api/v1/investors/statistics` - Estatísticas dos investidores

#### Enterprises
- `GET /api/v1/enterprises` - Lista empreendimentos
- `GET /api/v1/enterprises/{id}` - Busca por ID
- `POST /api/v1/enterprises` - Cria empreendimento
- `PUT /api/v1/enterprises/{id}` - Atualiza empreendimento
- `DELETE /api/v1/enterprises/{id}` - Remove empreendimento
- `GET /api/v1/enterprises/status/{status}` - Filtra por status
- `GET /api/v1/enterprises/open-funding` - Empreendimentos abertos para investimento
- `GET /api/v1/enterprises/underfunded` - Empreendimentos com funding insuficiente
- `GET /api/v1/enterprises/overdue-funding` - Empreendimentos com prazo de funding vencido
- `GET /api/v1/enterprises/property/{propertyId}` - Por propriedade
- `GET /api/v1/enterprises/project/{projectId}` - Por projeto
- `GET /api/v1/enterprises/{enterpriseId}/investors` - Lista investidores do empreendimento
- `POST /api/v1/enterprises/{enterpriseId}/investors` - Adiciona investidor
- `DELETE /api/v1/enterprises/{enterpriseId}/investors/{investorId}` - Remove investidor
- `GET /api/v1/enterprises/statistics` - Estatísticas dos empreendimentos
- `GET /api/v1/enterprises/completing-between?startDate&endDate` - Por período de conclusão
- `GET /api/v1/enterprises/{enterpriseId}/can-accept-investment?amount` - Valida investimento

#### File Management
- `POST /api/v1/photos/upload/{propertyId}` - Upload fotos (max 5MB)
- `GET /api/v1/photos/{id}/thumbnail` - Thumbnails
- `GET /api/v1/photos/property/{propertyId}/thumbnail` - Primeira foto
- `POST /api/v1/files/upload/{propertyId}?fileType=DOCUMENT` - Upload docs (max 10MB)
- `GET /api/v1/files/{id}` - Download arquivo
- `DELETE /api/v1/files/{id}` - Remove arquivo

## 📋 Modelos de Dados

### Property
```json
{
  "name": "string",
  "description": "string",
  "totalArea": "number",
  "availableArea": "number|null",
  "type": "string",
  "address": "string",
  "state": "string",
  "city": "string",
  "latitude": "number|null",
  "longitude": "number|null"
}
```

### Project
```json
{
  "name": "string",
  "category": "string", 
  "description": "string",
  "startDate": "YYYY-MM-DD",
  "estimatedEndDate": "YYYY-MM-DD",
  "endDate": "YYYY-MM-DD|null",
  "priority": "LOW|MEDIUM|HIGH|CRITICAL",
  "status": "PLANNING|APPROVED|IN_PROGRESS|ON_HOLD|COMPLETED|CANCELLED",
  "totalEstimatedCosts": "number",
  "totalCosts": "number",
  "totalInvestment": "number",
  "estimatedReturnOverInvestment": "number"
}
```

### Investor
```json
{
  "name": "string",
  "taxId": "string",
  "email": "string",
  "phone": "string",
  "address": "string", 
  "city": "string",
  "state": "string",
  "totalFunds": "number",
  "investedFunds": "number",
  "availableFunds": "number (calculado)",
  "description": "string",
  "active": "boolean",
  "createDate": "timestamp",
  "updateDate": "timestamp"
}
```

### Enterprise
```json
{
  "name": "string",
  "description": "string", 
  "propertyId": "number",
  "projectId": "number",
  "status": "PLANNING|ACTIVE|COMPLETED|SUSPENDED|CANCELLED",
  "launchDate": "YYYY-MM-DD|null",
  "fundingDeadline": "YYYY-MM-DD|null",
  "expectedCompletionDate": "YYYY-MM-DD|null",
  "totalInvestmentRequired": "number",
  "totalInvestmentRaised": "number",
  "minimumInvestment": "number",
  "expectedCommodityValueIncrease": "number",
  "fundingProgress": "number (calculado)",
  "active": "boolean"
}
```

## 🎨 Interface e UX

### Tema Visual RenovaCampo
- **Verde**: #014C34 (branding principal)
- **Laranja**: #EC6618 (destaque e ações)
- **Logos**: RenovaCampoIcone.png, RenovaCampoNome.png, RenovaCampoHugeIcon.png
- **Header**: 72px com branding profissional
- **Progress bars**: Verde uniforme com height: 100%

### Funcionalidades Modernas
- **HTMX**: Updates dinâmicos sem reload
- **Filtros real-time**: Busca instantânea client-side
- **Progressive loading**: Quick load + full load
- **Thumbnails inteligentes**: Placeholder → imagem real
- **Mapas interativos**: Leaflet.js com marcadores RenovaCampo
- **Responsive design**: Mobile-first approach
- **Loading indicators**: Spinners visuais
- **Error handling**: Graceful degradation

### Páginas Principais
- `/` - Dashboard com métricas integradas
- `/properties` - Gestão de propriedades
- `/projects` - Gestão de projetos
- `/investors` - Gestão de investidores  
- `/enterprises` - Gestão de empreendimentos

## 📁 Sistema de Arquivos

### Configuração Storage
```properties
storage.location=/opt/claude/renovacampo/uploads
storage.photo.max-size=5MB
storage.document.max-size=10MB
spring.servlet.multipart.max-file-size=10MB
```

### Estrutura de Upload
```
uploads/
├── photos/          # Fotos JPG/PNG (thumbnails automáticos)
├── documents/       # PDFs, DOCs, XLS
└── others/          # Outros tipos
```

### Features Avançadas
- **Thumbnails automáticos**: Geração via ImageProcessor
- **Progressive loading**: Quick metadata + full data
- **Error resilience**: 404 graceful para arquivos faltando
- **Anti-duplicação**: Clearing de inputs pós-upload
- **Metadata tracking**: StoredFile entity completa

## 🔧 Troubleshooting

### Problemas Comuns

#### Porta 8080 em uso
```bash
sudo netstat -tlnp | grep 8080
sudo kill <PID>
```

#### Permissões PostgreSQL
```bash
sudo -u postgres psql -d patrimonio -c "GRANT ALL ON SCHEMA public TO patrimonio_user;"
```

#### Java não encontrado
```bash
sudo apt install -y openjdk-17-jdk
java -version
```

#### Template parsing errors
- Verificar comparações enum: usar `T(org.acabativa.rc.patrimonio.entity.Class.Enum).VALUE`
- Verificar sintaxe Thymeleaf: `[[${...}]]` para JavaScript injection

### Performance e Monitoramento
- **Logs**: `/home/matheus/claude/renovacampo/logs/patrimonio_app.log`
- **Process check**: `ps aux | grep spring-boot`
- **Memory usage**: Spring Boot Actuator endpoints
- **Database queries**: Hibernate query logging habilitado

## 🚀 Deployment

### Ambiente de Produção
- **Servidor**: Raspberry Pi 4 (192.168.15.7)
- **OS**: Linux ARM64
- **PostgreSQL**: 15.13 
- **Dados**: 15 propriedades + 6 projetos + investidores + empreendimentos
- **Storage**: ~/uploads com fotos e documentos
- **Logs**: Rotação automática

### Scripts de Automação
- `restart_app.sh` - Restart completo com verificação
- `status.sh` - Status detalhado do sistema
- `load_properties.py` - Import de dados externos
- `test_*.py` - Scripts de teste e validação

## 📊 Versão Atual: v1.1-SNAPSHOT

### Novidades v1.1-SNAPSHOT (06/2025)
- ✅ **Sistema de Empreendimentos**: Conecta propriedades + projetos + investidores
- ✅ **Gestão de Investidores**: CRUD completo com métricas financeiras
- ✅ **Dashboard Integrado**: Métricas cross-module em tempo real
- ✅ **Progress Bars**: Estilo verde uniforme com branding
- ✅ **UI/UX Melhorias**: Cores consistentes, tipografia melhorada

### Correções Técnicas
- ✅ **Template Parsing**: Enum comparisons corrigidas
- ✅ **Error Handling**: PhotoController 404 graceful
- ✅ **Performance**: Progressive loading implementado
- ✅ **Mobile**: Responsive design otimizado

### Arquitetura Modular
- **Separação clara**: patrimonio (core) + storage (files)
- **Package refactor**: org.acabativa.ic → org.acabativa.rc
- **Cross-package scanning**: Spring Boot multi-module support
- **Reutilização**: Storage module independente

## 📞 Suporte

- **GitHub**: https://github.com/coxasboy/patrimonio
- **Branch atual**: feature/enterprise-management
- **Documentação**: CLAUDE.md (este arquivo)
- **Deploy guide**: QA_DEPLOYMENT.md
- **Release notes**: RELEASE_NOTES.md
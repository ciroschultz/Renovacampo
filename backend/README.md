# 🌱 RenovaCampo - Sistema de Gestão Rural

<div align="center">
  <img src="src/main/resources/static/RenovaCampoHugeIcon.png" alt="RenovaCampo Logo" width="200"/>
  
  ![Java](https://img.shields.io/badge/Java-17-orange)
  ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-green)
  ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue)
  ![Version](https://img.shields.io/badge/Version-1.1--SNAPSHOT-yellow)
</div>

## 📋 Índice

- [📖 Sobre o Projeto](#-sobre-o-projeto)
- [🚀 Início Rápido](#-início-rápido)
- [💻 Instalação](#-instalação)
- [🏗️ Arquitetura](#️-arquitetura)
- [📚 Documentação](#-documentação)
- [🧪 Testes](#-testes)
- [🔧 Configuração](#-configuração)
- [📞 Suporte](#-suporte)

## 📖 Sobre o Projeto

O **RenovaCampo** é um sistema completo de gestão rural que revoluciona a forma como propriedades agrícolas são gerenciadas, conectando propriedades, projetos, investidores e empreendimentos em uma plataforma moderna e intuitiva.

### ✨ Principais Funcionalidades

- 🏠 **Gestão de Propriedades**: Cadastro e gerenciamento completo de propriedades rurais
- 📋 **Controle de Projetos**: Planejamento e acompanhamento de projetos agrícolas
- 👥 **Gestão de Investidores**: Sistema completo de relacionamento com investidores
- 🏢 **Empreendimentos**: Conecta propriedades, projetos e investidores em negócios
- 📊 **Dashboard Integrado**: Visão unificada com métricas em tempo real
- 📸 **Gestão de Arquivos**: Upload e organização de fotos e documentos
- 🗺️ **Mapas Interativos**: Localização visual das propriedades

### 🎯 Público-Alvo

- **Produtores Rurais**: Gestão eficiente das suas propriedades
- **Investidores**: Acompanhamento de portfolios rurais
- **Cooperativas**: Gerenciamento de múltiplas propriedades
- **Consultorias**: Suporte a clientes rurais

## 🚀 Início Rápido

### Pré-requisitos

- Java 17 ou superior
- PostgreSQL 15 ou superior
- Maven 3.6+ (ou usar Maven Wrapper incluído)

### Execução Rápida

```bash
# Clone o repositório
git clone https://github.com/coxasboy/patrimonio.git
cd patrimonio

# Execute com Maven Wrapper
./mvnw spring-boot:run
```

🌐 **Acesse**: http://localhost:8080

### Scripts Automatizados

```bash
# Restart da aplicação
/home/matheus/claude/renovacampo/scripts/restart_app.sh

# Verificar status
/home/matheus/claude/renovacampo/scripts/status.sh
```

## 💻 Instalação

Para instruções detalhadas de instalação, consulte: [📖 Guia de Instalação](docs/INSTALACAO.md)

### Configuração do Banco de Dados

```sql
-- Criar banco e usuário
CREATE DATABASE patrimonio;
CREATE USER patrimonio_user WITH PASSWORD 'patrimonio123';
GRANT ALL PRIVILEGES ON DATABASE patrimonio TO patrimonio_user;
```

### Configuração da Aplicação

```properties
# application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/patrimonio
spring.datasource.username=patrimonio_user
spring.datasource.password=patrimonio123
storage.location=/opt/claude/renovacampo/uploads
```

## 🏗️ Arquitetura

### Stack Tecnológico

| Componente | Tecnologia | Versão |
|------------|------------|--------|
| **Backend** | Java + Spring Boot | 17 + 3.4.5 |
| **Banco de Dados** | PostgreSQL | 15+ |
| **ORM** | JPA/Hibernate | 6.6+ |
| **Frontend** | Thymeleaf + HTMX | - |
| **Mapas** | Leaflet.js | - |
| **Build** | Maven | 3.6+ |

### Estrutura do Projeto

```
patrimonio/
├── src/main/java/org/acabativa/rc/
│   ├── patrimonio/          # Módulo principal
│   │   ├── controller/      # Controllers Web + REST
│   │   ├── entity/         # Entidades JPA
│   │   ├── repository/     # Repositórios de dados
│   │   └── service/        # Lógica de negócio
│   └── storage/            # Módulo de arquivos
├── src/main/resources/
│   ├── templates/          # Templates Thymeleaf
│   └── static/            # Recursos estáticos
├── docs/                  # Documentação detalhada
├── qa/                    # Testes e QA
└── target/               # Build artifacts
```

Para mais detalhes: [📖 Documentação de Arquitetura](docs/ARQUITETURA.md)

## 📚 Documentação

### Documentação Disponível

| Documento | Descrição |
|-----------|-----------|
| [🔧 Instalação](docs/INSTALACAO.md) | Guia completo de instalação e configuração |
| [🏗️ Arquitetura](docs/ARQUITETURA.md) | Detalhes técnicos da arquitetura |
| [🌐 API](docs/API.md) | Documentação completa da API REST |
| [🧪 Testes](docs/TESTES.md) | Guia de testes e QA |
| [⚙️ Configuração](docs/CONFIGURACAO.md) | Opções de configuração avançada |
| [🚀 Deploy](docs/DEPLOY.md) | Guia de deployment |

### Documentação Técnica

- **[CLAUDE.md](CLAUDE.md)**: Documentação técnica completa
- **[CONTEXT.md](CONTEXT.md)**: Contexto e histórico do projeto
- **[RELEASE_NOTES.md](RELEASE_NOTES.md)**: Notas de versão

## 🧪 Testes

### Executar Testes

```bash
# Testes unitários
./mvnw test

# Testes de QA automatizados
cd qa/test-reports
python3 realistic_test_executor.py
```

### Cobertura de Testes

- ✅ **84%** de sucesso nos testes automatizados
- ✅ **75** casos de teste abrangentes
- ✅ Testes de UI, API e integração

Para mais detalhes: [📖 Documentação de Testes](docs/TESTES.md)

## 🔧 Configuração

### Principais Configurações

```properties
# Banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/patrimonio
spring.jpa.hibernate.ddl-auto=update

# Upload de arquivos
storage.location=/opt/claude/renovacampo/uploads
spring.servlet.multipart.max-file-size=10MB

# Logs
logging.file.name=../logs/patrimonio_app.log
logging.level.org.acabativa.rc=INFO
```

### Variáveis de Ambiente

```bash
export SPRING_PROFILES_ACTIVE=production
export DATABASE_URL=postgresql://localhost:5432/patrimonio
export STORAGE_LOCATION=/opt/uploads
```

Para configurações avançadas: [📖 Guia de Configuração](docs/CONFIGURACAO.md)

## 📊 Status do Projeto

### Versão Atual: v1.1-SNAPSHOT

#### ✨ Últimas Novidades
- 🏢 **Sistema de Empreendimentos**: Conecta propriedades, projetos e investidores
- 👥 **Gestão Completa de Investidores**: CRUD + métricas financeiras
- 📊 **Dashboard Integrado**: Visão unificada de todos os módulos
- 🎨 **UI/UX Melhorada**: Design consistente e responsivo

#### 🔧 Próximas Funcionalidades (v1.2.0)
- 🔐 Sistema de autenticação e autorização
- 📄 Relatórios em PDF e Excel
- 📈 Analytics avançados
- 📱 PWA (Progressive Web App)

### Estatísticas

- **15** propriedades cadastradas
- **6** projetos ativos
- **2** investidores registrados
- **5** empreendimentos em andamento

## 🤝 Contribuição

### Como Contribuir

1. **Fork** o projeto
2. Crie uma **branch** para sua feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. **Push** para a branch (`git push origin feature/AmazingFeature`)
5. Abra um **Pull Request**

### Padrões de Código

- Seguir convenções Java/Spring Boot
- Documentar APIs com comentários
- Incluir testes para novas funcionalidades
- Usar mensagens de commit descritivas

## 📞 Suporte

### 🆘 Precisa de Ajuda?

- **📧 Email**: suporte@renovacampo.com.br
- **🐛 Issues**: [GitHub Issues](https://github.com/coxasboy/patrimonio/issues)
- **📖 Wiki**: [Documentação Completa](https://github.com/coxasboy/patrimonio/wiki)

### 🌐 Links Úteis

- **Aplicação**: http://localhost:8080 (local) | http://192.168.15.7:8080 (rede)
- **Repositório**: https://github.com/coxasboy/patrimonio
- **Branch Atual**: feature/enterprise-management

---

<div align="center">
  <p>Desenvolvido com ❤️ pela equipe RenovaCampo</p>
  <p>© 2025 RenovaCampo - Revolucionando a gestão rural</p>
</div>

## 📄 Licença

Este projeto está licenciado sob a Apache License 2.0 - veja o arquivo [LICENSE](LICENSE) para detalhes.
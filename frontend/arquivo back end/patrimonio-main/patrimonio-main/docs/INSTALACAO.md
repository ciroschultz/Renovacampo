# 🔧 Guia de Instalação - RenovaCampo

## 📋 Índice

- [Pré-requisitos](#-pré-requisitos)
- [Instalação do Java](#-instalação-do-java)
- [Instalação do PostgreSQL](#-instalação-do-postgresql)
- [Configuração do Banco de Dados](#-configuração-do-banco-de-dados)
- [Instalação da Aplicação](#-instalação-da-aplicação)
- [Configuração Inicial](#-configuração-inicial)
- [Verificação da Instalação](#-verificação-da-instalação)
- [Problemas Comuns](#-problemas-comuns)

## 🔧 Pré-requisitos

### Requisitos de Sistema

| Component | Mínimo | Recomendado |
|-----------|---------|-------------|
| **CPU** | 2 cores | 4+ cores |
| **RAM** | 2GB | 4GB+ |
| **Disco** | 10GB | 20GB+ |
| **SO** | Linux/Windows/macOS | Ubuntu 20.04+ |

### Software Necessário

- ☕ **Java**: 17 ou superior
- 🐘 **PostgreSQL**: 15 ou superior  
- 🔨 **Maven**: 3.6+ (ou usar wrapper incluído)
- 🌐 **Git**: Para clone do repositório

## ☕ Instalação do Java

### Ubuntu/Debian

```bash
# Atualizar repositórios
sudo apt update

# Instalar OpenJDK 17
sudo apt install -y openjdk-17-jdk

# Verificar instalação
java -version
javac -version
```

### CentOS/RHEL/Fedora

```bash
# Para CentOS/RHEL
sudo yum install -y java-17-openjdk-devel

# Para Fedora
sudo dnf install -y java-17-openjdk-devel

# Verificar instalação
java -version
```

### Windows

1. Baixar OpenJDK 17 de [https://openjdk.java.net/](https://openjdk.java.net/)
2. Executar o instalador
3. Configurar `JAVA_HOME` nas variáveis de ambiente
4. Adicionar `%JAVA_HOME%\bin` ao PATH

### macOS

```bash
# Com Homebrew
brew install openjdk@17

# Ou baixar diretamente do site oficial
# https://openjdk.java.net/
```

## 🐘 Instalação do PostgreSQL

### Ubuntu/Debian

```bash
# Instalar PostgreSQL
sudo apt install -y postgresql postgresql-contrib

# Iniciar serviço
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Verificar status
sudo systemctl status postgresql
```

### CentOS/RHEL/Fedora

```bash
# Instalar PostgreSQL
sudo yum install -y postgresql-server postgresql-contrib

# Inicializar banco
sudo postgresql-setup initdb

# Iniciar serviço
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### Windows

1. Baixar PostgreSQL de [https://www.postgresql.org/download/windows/](https://www.postgresql.org/download/windows/)
2. Executar o instalador
3. Seguir o wizard de instalação
4. Lembrar da senha do usuário `postgres`

### macOS

```bash
# Com Homebrew
brew install postgresql
brew services start postgresql
```

## 🗃️ Configuração do Banco de Dados

### 1. Acessar PostgreSQL

```bash
# Como usuário postgres
sudo -u postgres psql
```

### 2. Criar Banco e Usuário

```sql
-- Criar banco de dados
CREATE DATABASE patrimonio;

-- Criar usuário
CREATE USER patrimonio_user WITH PASSWORD 'patrimonio123';

-- Conceder privilégios
GRANT ALL PRIVILEGES ON DATABASE patrimonio TO patrimonio_user;

-- Conceder privilégios no schema public
\c patrimonio
GRANT ALL ON SCHEMA public TO patrimonio_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO patrimonio_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO patrimonio_user;

-- Sair do psql
\q
```

### 3. Configurar Acesso (pg_hba.conf)

```bash
# Localizar arquivo de configuração
sudo find /etc -name "pg_hba.conf" 2>/dev/null

# Editar arquivo (exemplo Ubuntu)
sudo nano /etc/postgresql/15/main/pg_hba.conf
```

Adicionar/modificar linha:
```
# TYPE  DATABASE        USER            ADDRESS                 METHOD
local   patrimonio      patrimonio_user                         md5
host    patrimonio      patrimonio_user 127.0.0.1/32            md5
```

### 4. Reiniciar PostgreSQL

```bash
sudo systemctl restart postgresql
```

### 5. Testar Conexão

```bash
# Testar conexão
psql -h localhost -U patrimonio_user -d patrimonio

# Se funcionar, você verá:
# patrimonio=>
```

## 📦 Instalação da Aplicação

### 1. Clonar Repositório

```bash
# Via HTTPS
git clone https://github.com/coxasboy/patrimonio.git

# Via SSH (se configurado)
git clone git@github.com:coxasboy/patrimonio.git

# Entrar no diretório
cd patrimonio
```

### 2. Verificar Maven

```bash
# Verificar Maven instalado
mvn --version

# Ou usar wrapper (recomendado)
./mvnw --version
```

### 3. Configurar application.properties

```bash
# Editar configurações
nano src/main/resources/application.properties
```

Configuração básica:
```properties
# Banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/patrimonio
spring.datasource.username=patrimonio_user
spring.datasource.password=patrimonio123
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Upload de arquivos
storage.location=/opt/claude/renovacampo/uploads
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Servidor
server.port=8080

# Logs
logging.file.name=../logs/patrimonio_app.log
logging.level.org.acabativa.rc=INFO
```

### 4. Criar Diretórios

```bash
# Criar diretório de uploads
sudo mkdir -p /opt/claude/renovacampo/uploads/{photos,documents,others}
sudo chown -R $USER:$USER /opt/claude/renovacampo/

# Criar diretório de logs
mkdir -p ../logs
```

### 5. Compilar e Executar

```bash
# Compilar projeto
./mvnw clean compile

# Executar aplicação
./mvnw spring-boot:run
```

## ⚙️ Configuração Inicial

### 1. Verificar Acesso

Abrir navegador em: http://localhost:8080

### 2. Verificar API

```bash
# Testar API
curl http://localhost:8080/api/v1/property

# Deve retornar: []
```

### 3. Carregar Dados de Exemplo (Opcional)

```bash
# Se existir script de carga
python3 ../scripts/load_properties.py
```

### 4. Configurar para Produção

```bash
# Criar service systemd (opcional)
sudo nano /etc/systemd/system/renovacampo.service
```

Conteúdo do service:
```ini
[Unit]
Description=RenovaCampo Application
After=postgresql.service

[Service]
Type=simple
User=renovacampo
WorkingDirectory=/opt/renovacampo/patrimonio
ExecStart=/opt/renovacampo/patrimonio/mvnw spring-boot:run
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Ativar service:
```bash
sudo systemctl daemon-reload
sudo systemctl enable renovacampo
sudo systemctl start renovacampo
```

## ✅ Verificação da Instalação

### 1. Verificar Serviços

```bash
# PostgreSQL
sudo systemctl status postgresql

# Aplicação (se usando systemd)
sudo systemctl status renovacampo

# Ou verificar processo
ps aux | grep spring-boot
```

### 2. Verificar Conectividade

```bash
# Testar aplicação
curl -s http://localhost:8080/ | grep -i renovacampo

# Testar API
curl -s http://localhost:8080/api/v1/property | python3 -c "import json, sys; print(f'Properties: {len(json.load(sys.stdin))}')"
```

### 3. Verificar Logs

```bash
# Logs da aplicação
tail -f ../logs/patrimonio_app.log

# Logs do PostgreSQL
sudo tail -f /var/log/postgresql/postgresql-15-main.log
```

### 4. Teste Completo

```bash
# Navegar até qa/test-reports
cd qa/test-reports

# Executar testes automatizados
python3 realistic_test_executor.py
```

## 🚨 Problemas Comuns

### Java não encontrado

```bash
# Verificar JAVA_HOME
echo $JAVA_HOME

# Se vazio, configurar
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
```

### PostgreSQL não conecta

```bash
# Verificar se está rodando
sudo systemctl status postgresql

# Verificar portas
sudo netstat -tlnp | grep 5432

# Testar conexão
psql -h localhost -U patrimonio_user -d patrimonio
```

### Porta 8080 em uso

```bash
# Verificar o que está usando a porta
sudo netstat -tlnp | grep 8080

# Matar processo se necessário
sudo kill <PID>

# Ou usar porta diferente
export SERVER_PORT=8081
./mvnw spring-boot:run
```

### Permissões de arquivo

```bash
# Fixar permissões do Maven Wrapper
chmod +x mvnw

# Fixar permissões de upload
sudo chown -R $USER:$USER /opt/claude/renovacampo/uploads/
```

### Erro de memória

```bash
# Aumentar heap do Maven
export MAVEN_OPTS="-Xmx2g"

# Ou no comando
./mvnw -Xmx2g spring-boot:run
```

### Banco não cria tabelas

Verificar configuração:
```properties
# Em application.properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Verificar logs para erros de DDL.

### Upload de arquivos falha

```bash
# Verificar diretório existe
ls -la /opt/claude/renovacampo/uploads/

# Verificar permissões
sudo chown -R $USER:$USER /opt/claude/renovacampo/

# Verificar configuração
grep storage.location src/main/resources/application.properties
```

## 📞 Suporte

Se os problemas persistirem:

1. **Verificar logs**: `tail -f ../logs/patrimonio_app.log`
2. **Consultar documentação**: [README.md](../README.md)
3. **Abrir issue**: [GitHub Issues](https://github.com/coxasboy/patrimonio/issues)

---

**Próximo passo**: [🏗️ Documentação de Arquitetura](ARQUITETURA.md)
# Setup Local - Renova Campo

## Pre-requisitos

### 1. Java 17 (JDK)
```powershell
# Via Winget
winget install EclipseAdoptium.Temurin.17.JDK

# Ou baixe manualmente:
# https://adoptium.net/temurin/releases/?version=17&os=windows
```

Apos instalar, reinicie o terminal e verifique:
```powershell
java -version
```

### 2. PostgreSQL
```powershell
# Via Winget
winget install PostgreSQL.PostgreSQL

# Ou baixe manualmente:
# https://www.postgresql.org/download/windows/
```

## Configuracao do Banco de Dados

### Via pgAdmin (Interface Grafica)
1. Abra o pgAdmin
2. Conecte ao servidor local
3. Clique direito em "Login/Group Roles" > Create > Login/Group Role
   - Name: `patrimonio_user`
   - Password: `patrimonio123`
   - Privileges: Can login = Yes
4. Clique direito em "Databases" > Create > Database
   - Name: `patrimonio`
   - Owner: `patrimonio_user`

### Via Linha de Comando
```powershell
# Abra o psql como postgres
psql -U postgres

# Execute os comandos:
CREATE USER patrimonio_user WITH PASSWORD 'patrimonio123';
CREATE DATABASE patrimonio OWNER patrimonio_user;
\q
```

Ou execute o script:
```powershell
psql -U postgres -f setup-database.sql
```

## Iniciando o Backend

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

O backend estara disponivel em: http://localhost:8080

## Iniciando o Frontend

```powershell
cd frontend
python -m http.server 8000
```

O frontend estara disponivel em: http://localhost:8000

## Testando a Integracao

1. Acesse http://localhost:8000
2. Clique em "Tenho terra para arrendar"
3. Preencha o formulario e envie
4. Os dados serao salvos no banco PostgreSQL

## Estrutura do Projeto

```
Renovacampo/
├── frontend/          # Site HTML/CSS/JS
│   ├── index.html
│   ├── css/
│   ├── js/
│   └── pages/
├── backend/           # API Spring Boot
│   ├── src/
│   ├── pom.xml
│   └── mvnw.cmd
├── uploads/           # Arquivos enviados
│   ├── photos/
│   ├── documents/
│   └── others/
└── docs/              # Documentacao
```

## Endpoints da API

| Metodo | Endpoint | Descricao |
|--------|----------|-----------|
| GET | /api/v1/property | Lista propriedades |
| POST | /api/v1/property | Cria propriedade |
| GET | /api/v1/project | Lista projetos |
| POST | /api/v1/project | Cria projeto |
| GET | /api/v1/investors | Lista investidores |
| POST | /api/v1/investors | Cria investidor |

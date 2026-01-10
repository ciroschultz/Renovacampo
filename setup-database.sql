-- Script para configurar o banco de dados PostgreSQL
-- Execute este script como usuario postgres

-- Criar usuario
CREATE USER patrimonio_user WITH PASSWORD 'patrimonio123';

-- Criar banco de dados
CREATE DATABASE patrimonio OWNER patrimonio_user;

-- Conectar ao banco e dar permissoes
\c patrimonio
GRANT ALL PRIVILEGES ON DATABASE patrimonio TO patrimonio_user;
GRANT ALL ON SCHEMA public TO patrimonio_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO patrimonio_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO patrimonio_user;

-- Mensagem de sucesso
\echo 'Banco de dados configurado com sucesso!'

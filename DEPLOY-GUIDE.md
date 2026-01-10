# Guia de Deploy - Renova Campo

## Visao Geral do Projeto

O Renova Campo tem **2 partes**:

| Parte | Tecnologia | O que faz |
|-------|------------|-----------|
| **Frontend** | HTML/CSS/JS | Site que o usuario ve (paginas, formularios) |
| **Backend** | Java/Spring Boot | API que processa dados e banco de dados |

---

## FASE 1: Preparar o Codigo no GitHub

### 1.1 Primeiro, vamos enviar o codigo para o GitHub

No seu computador Windows, abra o terminal na pasta do projeto:

```bash
# Verificar se esta na pasta certa
cd C:\Users\ciroa\Renovacampo

# Ver o status atual
git status

# Adicionar todos os arquivos
git add .

# Criar um commit
git commit -m "Versao para deploy"

# Conectar ao seu repositorio do GitHub (substitua pelo seu)
git remote add origin https://github.com/SEU_USUARIO/renovacampo.git

# Enviar para o GitHub
git push -u origin master
```

**Dica:** Se ja tiver o remote configurado, so precisa do `git push`

---

## FASE 2: Preparar o Servidor na Digital Ocean

### 2.1 Acessar o servidor

```bash
# No seu computador, conecte via SSH
ssh root@SEU_IP_DIGITAL_OCEAN
```

### 2.2 Atualizar o sistema

```bash
# Atualizar lista de pacotes
sudo apt update

# Atualizar pacotes instalados
sudo apt upgrade -y
```

### 2.3 Instalar ferramentas necessarias

```bash
# Instalar Git (para baixar o codigo)
sudo apt install git -y

# Instalar Nginx (servidor web para o frontend)
sudo apt install nginx -y

# Instalar Java 17 (para o backend)
sudo apt install openjdk-17-jdk -y

# Instalar PostgreSQL (banco de dados)
sudo apt install postgresql postgresql-contrib -y

# Verificar instalacoes
git --version
nginx -v
java -version
psql --version
```

---

## FASE 3: Configurar o Banco de Dados

### 3.1 Criar banco e usuario

```bash
# Acessar o PostgreSQL
sudo -u postgres psql

# Dentro do PostgreSQL, executar:
CREATE DATABASE renovacampo;
CREATE USER renovacampo_user WITH ENCRYPTED PASSWORD 'SuaSenhaSegura123';
GRANT ALL PRIVILEGES ON DATABASE renovacampo TO renovacampo_user;
\q
```

---

## FASE 4: Baixar o Codigo no Servidor

### 4.1 Clonar o repositorio

```bash
# Criar pasta para o projeto
sudo mkdir -p /var/www
cd /var/www

# Clonar do GitHub
sudo git clone https://github.com/SEU_USUARIO/renovacampo.git

# Dar permissoes
sudo chown -R www-data:www-data /var/www/renovacampo
```

---

## FASE 5: Configurar o Frontend (Nginx)

### 5.1 Criar configuracao do Nginx

```bash
# Criar arquivo de configuracao
sudo nano /etc/nginx/sites-available/renovacampo
```

### 5.2 Colar este conteudo:

```nginx
server {
    listen 80;
    server_name seu-dominio.com.br www.seu-dominio.com.br;

    # Ou use o IP se nao tiver dominio:
    # server_name SEU_IP_DIGITAL_OCEAN;

    # Frontend - arquivos estaticos
    root /var/www/renovacampo/frontend;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    # Proxy para o Backend (API)
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### 5.3 Ativar o site

```bash
# Criar link simbolico
sudo ln -s /etc/nginx/sites-available/renovacampo /etc/nginx/sites-enabled/

# Remover configuracao padrao
sudo rm /etc/nginx/sites-enabled/default

# Testar configuracao
sudo nginx -t

# Reiniciar Nginx
sudo systemctl restart nginx
sudo systemctl enable nginx
```

---

## FASE 6: Configurar o Backend (Java)

### 6.1 Configurar o arquivo de propriedades

```bash
# Editar configuracao do backend
sudo nano /var/www/renovacampo/backend/src/main/resources/application.properties
```

Ajustar as configuracoes:

```properties
# Banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/renovacampo
spring.datasource.username=renovacampo_user
spring.datasource.password=SuaSenhaSegura123

# Servidor
server.port=8080

# Upload de arquivos
storage.location=/var/www/renovacampo/uploads
```

### 6.2 Compilar o backend

```bash
cd /var/www/renovacampo/backend

# Dar permissao de execucao ao Maven
chmod +x mvnw

# Compilar (pode demorar alguns minutos)
./mvnw clean package -DskipTests
```

### 6.3 Criar servico para o backend

```bash
# Criar arquivo de servico
sudo nano /etc/systemd/system/renovacampo.service
```

Colar este conteudo:

```ini
[Unit]
Description=Renova Campo Backend
After=network.target postgresql.service

[Service]
Type=simple
User=www-data
WorkingDirectory=/var/www/renovacampo/backend
ExecStart=/usr/bin/java -jar target/patrimonio-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

### 6.4 Iniciar o servico

```bash
# Recarregar systemd
sudo systemctl daemon-reload

# Iniciar o backend
sudo systemctl start renovacampo

# Habilitar inicio automatico
sudo systemctl enable renovacampo

# Verificar status
sudo systemctl status renovacampo
```

---

## FASE 7: Criar pasta de uploads

```bash
# Criar pasta para arquivos enviados
sudo mkdir -p /var/www/renovacampo/uploads
sudo chown -R www-data:www-data /var/www/renovacampo/uploads
sudo chmod -R 755 /var/www/renovacampo/uploads
```

---

## FASE 8: Configurar Firewall

```bash
# Permitir HTTP e HTTPS
sudo ufw allow 'Nginx Full'
sudo ufw allow ssh
sudo ufw enable
sudo ufw status
```

---

## FASE 9: (Opcional) Adicionar HTTPS com Let's Encrypt

```bash
# Instalar Certbot
sudo apt install certbot python3-certbot-nginx -y

# Obter certificado SSL (substitua pelo seu dominio)
sudo certbot --nginx -d seu-dominio.com.br -d www.seu-dominio.com.br

# Renovacao automatica
sudo systemctl enable certbot.timer
```

---

## Comandos Uteis para Manutencao

```bash
# Ver logs do backend
sudo journalctl -u renovacampo -f

# Reiniciar backend
sudo systemctl restart renovacampo

# Reiniciar Nginx
sudo systemctl restart nginx

# Atualizar codigo do GitHub
cd /var/www/renovacampo
sudo git pull origin master
sudo systemctl restart renovacampo
sudo systemctl restart nginx

# Ver uso de disco
df -h

# Ver uso de memoria
free -h
```

---

## Checklist Final

- [ ] Servidor atualizado
- [ ] Git instalado
- [ ] Nginx instalado e configurado
- [ ] Java 17 instalado
- [ ] PostgreSQL instalado e banco criado
- [ ] Codigo clonado do GitHub
- [ ] Backend compilado e rodando
- [ ] Frontend acessivel pelo navegador
- [ ] Uploads funcionando
- [ ] (Opcional) HTTPS configurado

---

## Estrutura de Pastas no Servidor

```
/var/www/renovacampo/
├── frontend/           # Arquivos HTML/CSS/JS
│   ├── index.html
│   ├── css/
│   ├── js/
│   ├── pages/
│   └── imagens/
├── backend/            # Codigo Java
│   ├── src/
│   ├── target/         # JAR compilado fica aqui
│   └── mvnw
└── uploads/            # Arquivos enviados pelos usuarios
```

---

## Problemas Comuns

### Backend nao inicia
```bash
# Verificar logs
sudo journalctl -u renovacampo -n 50

# Verificar se a porta 8080 esta livre
sudo netstat -tlnp | grep 8080
```

### Nginx erro 502
```bash
# Verificar se o backend esta rodando
curl http://localhost:8080/api/v1/property

# Reiniciar backend
sudo systemctl restart renovacampo
```

### Permissao negada em uploads
```bash
sudo chown -R www-data:www-data /var/www/renovacampo/uploads
sudo chmod -R 755 /var/www/renovacampo/uploads
```

---

## Contato para Duvidas

Se tiver problemas, anote:
1. Qual comando executou
2. Qual erro apareceu
3. Em qual fase estava

Isso ajuda a resolver mais rapido!

# 🚀 Guia de Deploy - RenovaCampo

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Deploy Local](#-deploy-local)
- [Deploy em Servidor](#-deploy-em-servidor)
- [Deploy com Docker](#-deploy-com-docker)
- [Deploy em Cloud](#-deploy-em-cloud)
- [Monitoramento](#-monitoramento)
- [Backup e Recovery](#-backup-e-recovery)
- [Troubleshooting](#-troubleshooting)

## 🎯 Visão Geral

O RenovaCampo suporta múltiplas estratégias de deploy, desde desenvolvimento local até ambientes de produção em cloud. Este guia cobre todas as opções disponíveis.

### Arquiteturas de Deploy

```
Development    →    Staging    →    Production
    ↓               ↓               ↓
Local JAR      →   Server JAR  →   Docker/K8s
PostgreSQL     →   PostgreSQL  →   Managed DB
File System    →   File System →   Object Storage
```

## 💻 Deploy Local

### Pré-requisitos

- Java 17+
- PostgreSQL 15+
- Maven 3.6+
- Git

### Deploy Rápido

```bash
# Clone e setup
git clone https://github.com/coxasboy/patrimonio.git
cd patrimonio

# Configurar banco
sudo -u postgres createdb patrimonio
sudo -u postgres psql -c "CREATE USER patrimonio_user WITH PASSWORD 'patrimonio123';"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE patrimonio TO patrimonio_user;"

# Configurar storage
mkdir -p /opt/claude/renovacampo/uploads
sudo chown -R $USER:$USER /opt/claude/renovacampo/

# Executar aplicação
./mvnw spring-boot:run
```

### Script de Deploy Automatizado

```bash
#!/bin/bash
# deploy-local.sh

set -e

echo "🚀 RenovaCampo Local Deploy Script"
echo "================================="

# Verificar pré-requisitos
command -v java >/dev/null 2>&1 || { echo "❌ Java não encontrado"; exit 1; }
command -v psql >/dev/null 2>&1 || { echo "❌ PostgreSQL não encontrado"; exit 1; }

# Configurar banco se não existir
if ! sudo -u postgres psql -lqt | cut -d \| -f 1 | grep -qw patrimonio; then
    echo "📦 Criando banco de dados..."
    sudo -u postgres createdb patrimonio
    sudo -u postgres psql -c "CREATE USER patrimonio_user WITH PASSWORD 'patrimonio123';"
    sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE patrimonio TO patrimonio_user;"
fi

# Verificar conectividade
echo "🔍 Testando conexão com banco..."
if ! PGPASSWORD=patrimonio123 psql -h localhost -U patrimonio_user -d patrimonio -c "SELECT 1;" >/dev/null 2>&1; then
    echo "❌ Não foi possível conectar ao banco"
    exit 1
fi

# Configurar diretórios
echo "📁 Configurando diretórios..."
sudo mkdir -p /opt/claude/renovacampo/uploads/{photos,documents,others}
sudo chown -R $USER:$USER /opt/claude/renovacampo/
mkdir -p ../logs

# Build e deploy
echo "🔨 Compilando aplicação..."
./mvnw clean package -DskipTests

echo "▶️ Iniciando aplicação..."
./mvnw spring-boot:run &

# Aguardar aplicação subir
echo "⏳ Aguardando aplicação inicializar..."
for i in {1..30}; do
    if curl -s http://localhost:8080/actuator/health >/dev/null 2>&1; then
        echo "✅ Aplicação disponível em http://localhost:8080"
        exit 0
    fi
    sleep 2
done

echo "❌ Timeout aguardando aplicação"
exit 1
```

## 🖥️ Deploy em Servidor

### Setup do Servidor (Ubuntu/Debian)

```bash
#!/bin/bash
# server-setup.sh

# Atualizar sistema
sudo apt update && sudo apt upgrade -y

# Instalar Java 17
sudo apt install -y openjdk-17-jdk

# Instalar PostgreSQL
sudo apt install -y postgresql postgresql-contrib

# Instalar Nginx (proxy reverso)
sudo apt install -y nginx

# Criar usuário da aplicação
sudo useradd -m -s /bin/bash renovacampo
sudo mkdir -p /opt/renovacampo
sudo chown renovacampo:renovacampo /opt/renovacampo
```

### Configuração PostgreSQL

```sql
-- Como usuário postgres
sudo -u postgres psql

-- Criar banco e usuário
CREATE DATABASE patrimonio;
CREATE USER patrimonio_user WITH PASSWORD 'SuaSenhaSegura123!';
GRANT ALL PRIVILEGES ON DATABASE patrimonio TO patrimonio_user;

-- Configurações de performance
ALTER SYSTEM SET shared_buffers = '256MB';
ALTER SYSTEM SET effective_cache_size = '1GB';
ALTER SYSTEM SET maintenance_work_mem = '64MB';
ALTER SYSTEM SET checkpoint_completion_target = 0.9;
ALTER SYSTEM SET wal_buffers = '16MB';
ALTER SYSTEM SET default_statistics_target = 100;

-- Reiniciar PostgreSQL
\q
sudo systemctl restart postgresql
```

### Deploy da Aplicação

```bash
#!/bin/bash
# deploy-server.sh

APP_DIR="/opt/renovacampo"
APP_USER="renovacampo"
APP_NAME="patrimonio"
JAR_FILE="patrimonio-1.1-SNAPSHOT.jar"

# Build da aplicação
echo "🔨 Building application..."
./mvnw clean package -DskipTests

# Parar aplicação se estiver rodando
sudo systemctl stop renovacampo 2>/dev/null || true

# Copiar JAR
echo "📦 Deploying JAR..."
sudo cp target/$JAR_FILE $APP_DIR/
sudo chown $APP_USER:$APP_USER $APP_DIR/$JAR_FILE

# Configurar diretórios
sudo mkdir -p $APP_DIR/{logs,uploads/{photos,documents,others}}
sudo chown -R $APP_USER:$APP_USER $APP_DIR

# Criar service systemd
sudo tee /etc/systemd/system/renovacampo.service > /dev/null <<EOF
[Unit]
Description=RenovaCampo Application
After=postgresql.service
Wants=postgresql.service

[Service]
Type=simple
User=$APP_USER
Group=$APP_USER
WorkingDirectory=$APP_DIR
ExecStart=/usr/bin/java -jar $APP_DIR/$JAR_FILE
Environment=SPRING_PROFILES_ACTIVE=production
Environment=STORAGE_LOCATION=$APP_DIR/uploads
Environment=LOG_FILE=$APP_DIR/logs/patrimonio_app.log
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
EOF

# Iniciar serviço
sudo systemctl daemon-reload
sudo systemctl enable renovacampo
sudo systemctl start renovacampo

echo "✅ Deploy concluído!"
echo "📊 Status: sudo systemctl status renovacampo"
echo "📄 Logs: sudo journalctl -u renovacampo -f"
```

### Configuração Nginx

```nginx
# /etc/nginx/sites-available/renovacampo
server {
    listen 80;
    server_name renovacampo.local;

    # Redirect HTTP to HTTPS (produção)
    # return 301 https://$server_name$request_uri;

    # Logs
    access_log /var/log/nginx/renovacampo_access.log;
    error_log /var/log/nginx/renovacampo_error.log;

    # Proxy para aplicação
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # Timeouts
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
    }

    # Upload de arquivos grandes
    client_max_body_size 10M;

    # Cache para recursos estáticos
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        proxy_pass http://localhost:8080;
        proxy_cache_valid 200 1h;
        expires 1h;
        add_header Cache-Control "public, immutable";
    }

    # Health check
    location /health {
        proxy_pass http://localhost:8080/actuator/health;
        access_log off;
    }
}
```

Ativar configuração:
```bash
sudo ln -s /etc/nginx/sites-available/renovacampo /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

## 🐳 Deploy com Docker

### Dockerfile

```dockerfile
# Dockerfile
FROM openjdk:17-jdk-slim

# Metadata
LABEL maintainer="renovacampo@example.com"
LABEL version="1.1-SNAPSHOT"
LABEL description="RenovaCampo Rural Management System"

# Criar usuário não-root
RUN addgroup --system renovacampo && adduser --system --group renovacampo

# Diretório da aplicação
WORKDIR /opt/renovacampo

# Copiar JAR
COPY target/patrimonio-1.1-SNAPSHOT.jar app.jar

# Criar diretórios
RUN mkdir -p logs uploads/{photos,documents,others}

# Configurar permissões
RUN chown -R renovacampo:renovacampo /opt/renovacampo

# Variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=production
ENV JAVA_OPTS="-Xms512m -Xmx1g"

# Porta da aplicação
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Usuário para execução
USER renovacampo

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

### Docker Compose

```yaml
# docker-compose.yml
version: '3.8'

services:
  app:
    build: .
    image: renovacampo/patrimonio:1.1-SNAPSHOT
    container_name: renovacampo-app
    restart: unless-stopped
    environment:
      - SPRING_PROFILES_ACTIVE=production
      - DATABASE_URL=jdbc:postgresql://db:5432/patrimonio
      - DB_USERNAME=patrimonio_user
      - DB_PASSWORD=patrimonio123
      - STORAGE_LOCATION=/opt/renovacampo/uploads
      - JAVA_OPTS=-Xms512m -Xmx1g
    volumes:
      - ./uploads:/opt/renovacampo/uploads
      - ./logs:/opt/renovacampo/logs
    ports:
      - "8080:8080"
    depends_on:
      db:
        condition: service_healthy
    networks:
      - renovacampo-network

  db:
    image: postgres:15-alpine
    container_name: renovacampo-db
    restart: unless-stopped
    environment:
      - POSTGRES_DB=patrimonio
      - POSTGRES_USER=patrimonio_user
      - POSTGRES_PASSWORD=patrimonio123
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U patrimonio_user -d patrimonio"]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - renovacampo-network

  nginx:
    image: nginx:alpine
    container_name: renovacampo-nginx
    restart: unless-stopped
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
      - ./ssl:/etc/nginx/ssl
    depends_on:
      - app
    networks:
      - renovacampo-network

volumes:
  postgres_data:

networks:
  renovacampo-network:
    driver: bridge
```

### Scripts Docker

```bash
#!/bin/bash
# docker-deploy.sh

set -e

echo "🐳 RenovaCampo Docker Deploy"
echo "============================"

# Build da aplicação
echo "🔨 Building application..."
./mvnw clean package -DskipTests

# Build da imagem Docker
echo "🐳 Building Docker image..."
docker build -t renovacampo/patrimonio:1.1-SNAPSHOT .

# Deploy com Docker Compose
echo "🚀 Starting services..."
docker-compose up -d

# Aguardar aplicação
echo "⏳ Waiting for application..."
for i in {1..60}; do
    if curl -s http://localhost:8080/actuator/health >/dev/null 2>&1; then
        echo "✅ Application ready at http://localhost:8080"
        exit 0
    fi
    sleep 2
done

echo "❌ Timeout waiting for application"
exit 1
```

## ☁️ Deploy em Cloud

### AWS ECS Fargate

```yaml
# ecs-task-definition.json
{
  "family": "renovacampo-patrimonio",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "512",
  "memory": "1024",
  "executionRoleArn": "arn:aws:iam::ACCOUNT:role/ecsTaskExecutionRole",
  "taskRoleArn": "arn:aws:iam::ACCOUNT:role/ecsTaskRole",
  "containerDefinitions": [
    {
      "name": "patrimonio",
      "image": "renovacampo/patrimonio:1.1-SNAPSHOT",
      "cpu": 512,
      "memory": 1024,
      "essential": true,
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "SPRING_PROFILES_ACTIVE",
          "value": "production"
        },
        {
          "name": "DATABASE_URL",
          "value": "jdbc:postgresql://renovacampo-db.cluster-xxx.us-east-1.rds.amazonaws.com:5432/patrimonio"
        }
      ],
      "secrets": [
        {
          "name": "DB_USERNAME",
          "valueFrom": "arn:aws:secretsmanager:us-east-1:ACCOUNT:secret:renovacampo/db:username::"
        },
        {
          "name": "DB_PASSWORD",
          "valueFrom": "arn:aws:secretsmanager:us-east-1:ACCOUNT:secret:renovacampo/db:password::"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/renovacampo-patrimonio",
          "awslogs-region": "us-east-1",
          "awslogs-stream-prefix": "ecs"
        }
      },
      "healthCheck": {
        "command": [
          "CMD-SHELL",
          "curl -f http://localhost:8080/actuator/health || exit 1"
        ],
        "interval": 30,
        "timeout": 5,
        "retries": 3,
        "startPeriod": 60
      }
    }
  ]
}
```

### Kubernetes Deployment

```yaml
# k8s/deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: renovacampo-patrimonio
  labels:
    app: patrimonio
spec:
  replicas: 2
  selector:
    matchLabels:
      app: patrimonio
  template:
    metadata:
      labels:
        app: patrimonio
    spec:
      containers:
      - name: patrimonio
        image: renovacampo/patrimonio:1.1-SNAPSHOT
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: DATABASE_URL
          valueFrom:
            configMapKeyRef:
              name: renovacampo-config
              key: database-url
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: renovacampo-secrets
              key: db-username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: renovacampo-secrets
              key: db-password
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        volumeMounts:
        - name: uploads
          mountPath: /opt/renovacampo/uploads
      volumes:
      - name: uploads
        persistentVolumeClaim:
          claimName: renovacampo-uploads-pvc

---
apiVersion: v1
kind: Service
metadata:
  name: patrimonio-service
spec:
  selector:
    app: patrimonio
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: LoadBalancer

---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: patrimonio-ingress
  annotations:
    kubernetes.io/ingress.class: nginx
    cert-manager.io/cluster-issuer: letsencrypt-prod
spec:
  tls:
  - hosts:
    - patrimonio.renovacampo.com
    secretName: patrimonio-tls
  rules:
  - host: patrimonio.renovacampo.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: patrimonio-service
            port:
              number: 80
```

## 📊 Monitoramento

### Prometheus & Grafana

```yaml
# monitoring/docker-compose.yml
version: '3.8'

services:
  prometheus:
    image: prom/prometheus
    container_name: prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
      - '--web.console.libraries=/etc/prometheus/console_libraries'
      - '--web.console.templates=/etc/prometheus/consoles'
      - '--web.enable-lifecycle'

  grafana:
    image: grafana/grafana
    container_name: grafana
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin
    volumes:
      - grafana_data:/var/lib/grafana
      - ./grafana/dashboards:/var/lib/grafana/dashboards
      - ./grafana/provisioning:/etc/grafana/provisioning

volumes:
  grafana_data:
```

### Configuração Prometheus

```yaml
# prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'renovacampo-patrimonio'
    static_configs:
      - targets: ['app:8080']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
```

### Alertas

```yaml
# alerts.yml
groups:
- name: renovacampo-alerts
  rules:
  - alert: ApplicationDown
    expr: up{job="renovacampo-patrimonio"} == 0
    for: 1m
    labels:
      severity: critical
    annotations:
      summary: "RenovaCampo application is down"
      
  - alert: HighMemoryUsage
    expr: (process_resident_memory_bytes / process_virtual_memory_max_bytes) * 100 > 80
    for: 5m
    labels:
      severity: warning
    annotations:
      summary: "High memory usage detected"
      
  - alert: DatabaseConnectionsHigh
    expr: hikaricp_connections_active > 15
    for: 2m
    labels:
      severity: warning
    annotations:
      summary: "High database connections"
```

## 💾 Backup e Recovery

### Script de Backup

```bash
#!/bin/bash
# backup.sh

BACKUP_DIR="/opt/backups/renovacampo"
DATE=$(date +%Y%m%d_%H%M%S)
DB_NAME="patrimonio"
DB_USER="patrimonio_user"

# Criar diretório de backup
mkdir -p $BACKUP_DIR

# Backup do banco de dados
echo "📦 Backing up database..."
PGPASSWORD=patrimonio123 pg_dump -h localhost -U $DB_USER -d $DB_NAME | gzip > $BACKUP_DIR/db_$DATE.sql.gz

# Backup dos uploads
echo "📁 Backing up uploads..."
tar -czf $BACKUP_DIR/uploads_$DATE.tar.gz -C /opt/claude/renovacampo uploads/

# Backup dos logs
echo "📄 Backing up logs..."
tar -czf $BACKUP_DIR/logs_$DATE.tar.gz -C /opt/claude/renovacampo logs/

# Limpar backups antigos (manter últimos 30 dias)
find $BACKUP_DIR -name "*.gz" -mtime +30 -delete

echo "✅ Backup completed: $BACKUP_DIR"
```

### Script de Recovery

```bash
#!/bin/bash
# recovery.sh

if [ $# -ne 1 ]; then
    echo "Usage: $0 <backup_date>"
    echo "Example: $0 20250606_123000"
    exit 1
fi

BACKUP_DATE=$1
BACKUP_DIR="/opt/backups/renovacampo"

# Parar aplicação
sudo systemctl stop renovacampo

# Restaurar banco
echo "🔄 Restoring database..."
PGPASSWORD=patrimonio123 dropdb -h localhost -U patrimonio_user patrimonio
PGPASSWORD=patrimonio123 createdb -h localhost -U patrimonio_user patrimonio
zcat $BACKUP_DIR/db_$BACKUP_DATE.sql.gz | PGPASSWORD=patrimonio123 psql -h localhost -U patrimonio_user -d patrimonio

# Restaurar uploads
echo "📁 Restoring uploads..."
rm -rf /opt/claude/renovacampo/uploads/*
tar -xzf $BACKUP_DIR/uploads_$BACKUP_DATE.tar.gz -C /opt/claude/renovacampo/

# Iniciar aplicação
sudo systemctl start renovacampo

echo "✅ Recovery completed"
```

## 🔧 Troubleshooting

### Problemas Comuns

#### Aplicação não inicia

```bash
# Verificar logs
sudo journalctl -u renovacampo -f

# Verificar porta
sudo netstat -tlnp | grep 8080

# Verificar Java
java -version

# Verificar banco
PGPASSWORD=patrimonio123 psql -h localhost -U patrimonio_user -d patrimonio -c "SELECT 1;"
```

#### Performance Issues

```bash
# Verificar CPU/Memory
top -p $(pgrep java)

# Verificar conexões DB
sudo netstat -tnlp | grep 5432

# Logs de performance
tail -f /opt/renovacampo/logs/patrimonio_app.log | grep -E "(took|ms|slow)"
```

#### Problemas de Upload

```bash
# Verificar permissões
ls -la /opt/claude/renovacampo/uploads/

# Verificar espaço em disco
df -h /opt/claude/renovacampo/

# Testar upload
curl -X POST -F "file=@test.jpg" http://localhost:8080/api/v1/photos/upload/1
```

### Scripts de Diagnóstico

```bash
#!/bin/bash
# diagnose.sh

echo "🔍 RenovaCampo Diagnostic Script"
echo "================================"

# Verificar Java
echo "☕ Java Version:"
java -version 2>&1 | head -1

# Verificar aplicação
echo "🚀 Application Status:"
if curl -s http://localhost:8080/actuator/health >/dev/null 2>&1; then
    echo "✅ Application is running"
    curl -s http://localhost:8080/actuator/health | jq .
else
    echo "❌ Application is not responding"
fi

# Verificar banco
echo "🗃️ Database Status:"
if PGPASSWORD=patrimonio123 psql -h localhost -U patrimonio_user -d patrimonio -c "SELECT 1;" >/dev/null 2>&1; then
    echo "✅ Database is accessible"
else
    echo "❌ Database connection failed"
fi

# Verificar espaço
echo "💾 Disk Usage:"
df -h /opt/claude/renovacampo/ 2>/dev/null || echo "Storage path not found"

# Verificar logs
echo "📄 Recent Errors:"
tail -20 /opt/renovacampo/logs/patrimonio_app.log 2>/dev/null | grep -i error || echo "No recent errors"
```

---

**Documentação completa finalizada! 🎉**

A estrutura de documentação inclui:
- README.md principal com visão geral
- Guias detalhados para cada aspecto
- Exemplos práticos e scripts
- Troubleshooting e melhores práticas
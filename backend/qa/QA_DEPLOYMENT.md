# QA Deployment Guide - RenovaCampo Patrimônio

## JAR Package Information
- **File**: `patrimonio-0.0.1-SNAPSHOT.jar`
- **Size**: ~54MB
- **Location**: `/home/matheus/claude/renovacampo/patrimonio/target/patrimonio-0.0.1-SNAPSHOT.jar`
- **Build Date**: 2025-06-04
- **Package Structure**: RC (RenovaCampo) - org.acabativa.rc.*

## Prerequisites for QA Environment

### 1. Java Runtime
- **Required**: Java 17 or higher
- **Verify**: `java -version`

### 2. PostgreSQL Database
- **Required**: PostgreSQL 12+
- **Database**: `patrimonio`
- **User**: `patrimonio_user`
- **Password**: `patrimonio123`

### 3. Database Setup
```sql
-- Create database and user
CREATE DATABASE patrimonio;
CREATE USER patrimonio_user WITH ENCRYPTED PASSWORD 'patrimonio123';
GRANT ALL PRIVILEGES ON DATABASE patrimonio TO patrimonio_user;

-- Connect to patrimonio database
\c patrimonio

-- Grant schema permissions
GRANT ALL ON SCHEMA public TO patrimonio_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO patrimonio_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO patrimonio_user;
```

### 4. File Storage Directory
```bash
# Create upload directory
sudo mkdir -p /opt/patrimonio/uploads
sudo chown -R app-user:app-user /opt/patrimonio/uploads
sudo chmod 755 /opt/patrimonio/uploads
```

## Deployment Steps

### 1. Copy JAR to QA Server
```bash
# Transfer JAR file
scp patrimonio-0.0.1-SNAPSHOT.jar user@qa-server:/opt/patrimonio/

# Or download from build artifacts
wget https://github.com/coxasboy/patrimonio/releases/download/v2.0/patrimonio-0.0.1-SNAPSHOT.jar
```

### 2. Create Application Properties
Create `/opt/patrimonio/application-qa.properties`:
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/patrimonio
spring.datasource.username=patrimonio_user
spring.datasource.password=patrimonio123
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Server Configuration
server.port=8080
server.address=0.0.0.0

# File Storage
storage.location=/opt/patrimonio/uploads
storage.photo.max-size=5MB
storage.document.max-size=10MB

# Multipart Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=50MB

# Logging
logging.level.org.acabativa.ic.patrimonio=INFO
logging.file.name=/opt/patrimonio/logs/application.log
```

### 3. Create Systemd Service
Create `/etc/systemd/system/patrimonio.service`:
```ini
[Unit]
Description=RenovaCampo Patrimonio Application
After=network.target postgresql.service

[Service]
Type=simple
User=app-user
Group=app-user
WorkingDirectory=/opt/patrimonio
ExecStart=/usr/bin/java -jar -Dspring.config.location=classpath:/application.properties,/opt/patrimonio/application-qa.properties /opt/patrimonio/patrimonio-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=patrimonio

# Environment
Environment=JAVA_HOME=/usr/lib/jvm/java-17-openjdk
Environment=SPRING_PROFILES_ACTIVE=qa

[Install]
WantedBy=multi-user.target
```

### 4. Start Application
```bash
# Reload systemd
sudo systemctl daemon-reload

# Enable service
sudo systemctl enable patrimonio

# Start service
sudo systemctl start patrimonio

# Check status
sudo systemctl status patrimonio

# View logs
sudo journalctl -u patrimonio -f
```

## Manual Deployment (Alternative)

### Simple Run Command
```bash
cd /opt/patrimonio
java -jar -Dspring.config.location=application-qa.properties patrimonio-0.0.1-SNAPSHOT.jar
```

### Background Process
```bash
nohup java -jar -Dspring.config.location=application-qa.properties patrimonio-0.0.1-SNAPSHOT.jar > application.log 2>&1 &
```

## Environment Verification

### 1. Health Check URLs
- **Main App**: `http://qa-server:8080/properties`
- **API**: `http://qa-server:8080/api/v1/property`
- **Actuator**: `http://qa-server:8080/actuator/health` (if enabled)

### 2. Test Endpoints
```bash
# Test API
curl -X GET http://qa-server:8080/api/v1/property

# Test Web Interface
curl -I http://qa-server:8080/properties
```

### 3. Database Connection Test
```bash
# Check if tables were created
sudo -u postgres psql -d patrimonio -c "\dt"

# Check property count
sudo -u postgres psql -d patrimonio -c "SELECT COUNT(*) FROM property;"
```

## Configuration Options

### Environment Variables
```bash
# Database
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/patrimonio
export SPRING_DATASOURCE_USERNAME=patrimonio_user
export SPRING_DATASOURCE_PASSWORD=patrimonio123

# Server
export SERVER_PORT=8080
export STORAGE_LOCATION=/opt/patrimonio/uploads
```

### JVM Options
```bash
java -jar \
  -Xmx2g \
  -Xms1g \
  -Dspring.profiles.active=qa \
  -Djava.security.egd=file:/dev/./urandom \
  patrimonio-0.0.1-SNAPSHOT.jar
```

## Features Available in QA

### ✅ Working Features
1. **Property CRUD**: Create, read, update, delete properties
2. **Interactive Maps**: Leaflet.js maps with coordinates
3. **File Upload**: Photos and documents (5MB/10MB limits)
4. **Thumbnails**: Property photo thumbnails with fallbacks
5. **Branding**: Complete RenovaCampo visual identity
6. **Filters**: Client-side property filtering
7. **Responsive UI**: Works on desktop and mobile

### 🗂️ Sample Data
- The JAR includes embedded sample data (15 properties)
- Database tables created automatically on first run
- Upload directory structure created automatically

## Troubleshooting

### Common Issues

#### 1. Port Already in Use
```bash
sudo netstat -tlnp | grep 8080
sudo kill <PID>
```

#### 2. Database Connection Failed
```bash
# Check PostgreSQL status
sudo systemctl status postgresql

# Test connection
sudo -u postgres psql -d patrimonio -c "SELECT 1;"
```

#### 3. Permission Denied on Upload Directory
```bash
sudo chown -R app-user:app-user /opt/patrimonio/uploads
sudo chmod 755 /opt/patrimonio/uploads
```

#### 4. Java Version Issues
```bash
# Check Java version
java -version

# Install Java 17 if needed (Ubuntu/Debian)
sudo apt update
sudo apt install openjdk-17-jdk
```

### Log Locations
- **Application**: `/opt/patrimonio/logs/application.log`
- **Systemd**: `sudo journalctl -u patrimonio`
- **PostgreSQL**: `/var/log/postgresql/`

## Security Considerations

### 1. Firewall Configuration
```bash
# Allow application port
sudo ufw allow 8080/tcp

# Allow PostgreSQL (if external access needed)
sudo ufw allow 5432/tcp
```

### 2. Database Security
- Change default passwords in production
- Restrict database access to application server only
- Use SSL connections for database

### 3. File Upload Security
- File type validation enabled
- Size limits enforced (5MB photos, 10MB documents)
- Upload directory outside web root

## Performance Tuning

### Recommended JVM Settings for QA
```bash
java -jar \
  -Xmx4g \
  -Xms2g \
  -XX:+UseG1GC \
  -XX:G1HeapRegionSize=16m \
  -XX:+UseStringDeduplication \
  patrimonio-0.0.1-SNAPSHOT.jar
```

### Database Tuning
```sql
-- Increase connection pool for QA testing
ALTER SYSTEM SET max_connections = 200;
ALTER SYSTEM SET shared_buffers = '256MB';
```

---

**Build Info**:
- **Version**: 0.0.1-SNAPSHOT
- **Spring Boot**: 3.4.5
- **Java**: 17
- **Built**: 2025-06-04 19:40 UTC
- **Package Structure**: org.acabativa.rc.* (RenovaCampo)
- **Features**: Maps, Thumbnails, Branding, File Storage, Modular Architecture

**Architecture Changes (v2.1)**:
- **Package Refactoring**: IC → RC (RenovaCampo)
- **Modular Storage**: Independent storage module at `org.acabativa.rc.storage`
- **Spring Configuration**: Multi-package scanning for patrimonio + storage

**Support**: Check CLAUDE.md for detailed technical documentation
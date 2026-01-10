# 🚀 RenovaCampo - Deployment JAR Guide

## ✅ JAR Build Completed Successfully!

**JAR Location**: `target/patrimonio-1.1-SNAPSHOT.jar`  
**JAR Size**: 54MB  
**Build Date**: June 10, 2025  
**Version**: v1.1-SNAPSHOT  
**Status**: ✅ Ready for Production Deployment

## 📦 JAR Package Information

**File**: `patrimonio-1.1-SNAPSHOT.jar`  
**Location**: `/home/matheus/claude/renovacampo/patrimonio/target/`  
**Size**: 54MB  
**Type**: Executable Spring Boot JAR (includes embedded Tomcat)
**Java Version**: Requires Java 17+

## 🚀 Running the JAR

### Basic Execution
```bash
java -jar patrimonio-1.1-SNAPSHOT.jar
```

### With Custom Port
```bash
java -jar patrimonio-1.1-SNAPSHOT.jar --server.port=8081
```

### With External Configuration
```bash
java -jar patrimonio-1.1-SNAPSHOT.jar --spring.config.location=application.properties
```

### As Background Service
```bash
nohup java -jar patrimonio-1.1-SNAPSHOT.jar > app.log 2>&1 &
```

## ⚙️ Configuration Options

### 1. Database Configuration
Create an `application.properties` file in the same directory as the JAR:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/patrimonio
spring.datasource.username=patrimonio_user
spring.datasource.password=patrimonio123

# Storage
storage.location=/opt/claude/renovacampo/uploads
```

### 2. Environment Variables
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/patrimonio
export SPRING_DATASOURCE_USERNAME=patrimonio_user
export SPRING_DATASOURCE_PASSWORD=patrimonio123
export STORAGE_LOCATION=/opt/claude/renovacampo/uploads
```

### 3. Command Line Arguments
```bash
java -jar patrimonio-1.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/patrimonio \
  --spring.datasource.username=patrimonio_user \
  --spring.datasource.password=patrimonio123 \
  --storage.location=/opt/claude/renovacampo/uploads
```

## 🐧 Linux Service Setup

### 1. Create Service File
```bash
sudo nano /etc/systemd/system/renovacampo.service
```

### 2. Service Configuration
```ini
[Unit]
Description=RenovaCampo Application
After=syslog.target postgresql.service

[Service]
User=matheus
ExecStart=/usr/bin/java -jar /home/matheus/claude/renovacampo/patrimonio/target/patrimonio-1.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=10
StandardOutput=append:/home/matheus/claude/renovacampo/logs/patrimonio_app.log
StandardError=append:/home/matheus/claude/renovacampo/logs/patrimonio_app_error.log

[Install]
WantedBy=multi-user.target
```

### 3. Enable and Start Service
```bash
sudo systemctl daemon-reload
sudo systemctl enable renovacampo.service
sudo systemctl start renovacampo.service
sudo systemctl status renovacampo.service
```

## 📋 Pre-Deployment Checklist

1. **PostgreSQL Running**
   ```bash
   sudo systemctl status postgresql
   ```

2. **Database Exists**
   ```bash
   sudo -u postgres psql -d patrimonio -c "SELECT 1;"
   ```

3. **Upload Directory Exists**
   ```bash
   mkdir -p /opt/claude/renovacampo/uploads/{photos,documents,others}
   ```

4. **Proper Permissions**
   ```bash
   chown -R matheus:matheus /opt/claude/renovacampo/uploads
   ```

## 🔍 Monitoring

### View Logs
```bash
# If running in foreground
# Logs appear in console

# If running as service
journalctl -u renovacampo -f

# If using nohup
tail -f app.log
```

### Check Process
```bash
ps aux | grep patrimonio-1.1-SNAPSHOT.jar
```

### Test Application
```bash
curl http://localhost:8080
```

## 🛑 Stopping the Application

### If Running in Foreground
Press `Ctrl+C`

### If Running as Background Process
```bash
# Find PID
ps aux | grep patrimonio-1.1-SNAPSHOT.jar

# Kill process
kill <PID>
```

### If Running as Service
```bash
sudo systemctl stop renovacampo.service
```

## 🚨 Troubleshooting

### Port Already in Use
```bash
# Check what's using port 8080
sudo lsof -i :8080

# Run on different port
java -jar patrimonio-1.1-SNAPSHOT.jar --server.port=8081
```

### Memory Issues
```bash
# Increase heap size
java -Xmx1g -Xms512m -jar patrimonio-1.1-SNAPSHOT.jar
```

### Database Connection Failed
- Verify PostgreSQL is running
- Check database credentials
- Ensure database exists
- Test connection: `psql -h localhost -U patrimonio_user -d patrimonio`

## 📦 Deployment Package Contents

The JAR includes:
- ✅ All compiled Java classes
- ✅ All templates (Thymeleaf)
- ✅ All static resources (CSS, JS, images)
- ✅ application.properties
- ✅ All dependencies
- ✅ Embedded Tomcat server

## 🔄 Updating the Application

1. Stop current instance
2. Backup current JAR
3. Copy new JAR to location
4. Start application

```bash
# Example update process
sudo systemctl stop renovacampo
mv patrimonio-1.1-SNAPSHOT.jar patrimonio-1.1-SNAPSHOT.jar.backup
cp /path/to/new/patrimonio-1.1-SNAPSHOT.jar .
sudo systemctl start renovacampo
```

## 📊 Performance Tuning

### JVM Options for Production
```bash
java -server \
  -Xmx1g \
  -Xms512m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -jar patrimonio-1.1-SNAPSHOT.jar
```

### Application Properties for Production
```properties
# Performance
spring.jpa.show-sql=false
spring.thymeleaf.cache=true

# Connection Pool
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
```

---

**JAR Ready for Deployment!** 🎉

The application is packaged and ready to run on any system with Java 17+ installed.
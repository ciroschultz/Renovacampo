# ⚙️ Guia de Configuração - RenovaCampo

## 📋 Índice

- [Configurações Básicas](#-configurações-básicas)
- [Banco de Dados](#-banco-de-dados)
- [Upload de Arquivos](#-upload-de-arquivos)
- [Logging](#-logging)
- [Performance](#-performance)
- [Segurança](#-segurança)
- [Profiles de Ambiente](#-profiles-de-ambiente)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)

## 🔧 Configurações Básicas

### application.properties Principal

```properties
# src/main/resources/application.properties

# ===============================
# = SERVER CONFIGURATION
# ===============================
server.port=8080
server.servlet.context-path=/

# ===============================
# = DATABASE CONFIGURATION
# ===============================
spring.datasource.url=jdbc:postgresql://localhost:5432/patrimonio
spring.datasource.username=patrimonio_user
spring.datasource.password=patrimonio123
spring.datasource.driver-class-name=org.postgresql.Driver

# ===============================
# = JPA/HIBERNATE CONFIGURATION
# ===============================
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false

# ===============================
# = FILE UPLOAD CONFIGURATION
# ===============================
storage.location=/opt/claude/renovacampo/uploads
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
spring.servlet.multipart.enabled=true

# ===============================
# = LOGGING CONFIGURATION
# ===============================
logging.file.name=../logs/patrimonio_app.log
logging.level.org.acabativa.rc=INFO
logging.level.org.springframework.web=WARN
logging.level.org.hibernate.SQL=WARN

# ===============================
# = THYMELEAF CONFIGURATION
# ===============================
spring.thymeleaf.cache=false
spring.thymeleaf.mode=HTML
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.servlet.content-type=text/html
```

## 🗃️ Banco de Dados

### Configurações PostgreSQL

#### Desenvolvimento
```properties
# Development - Local PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/patrimonio
spring.datasource.username=patrimonio_user
spring.datasource.password=patrimonio123

# Connection Pool (HikariCP)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=600000
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.validation-timeout=5000
```

#### Produção
```properties
# Production - Optimized settings
spring.datasource.url=jdbc:postgresql://localhost:5432/patrimonio
spring.datasource.username=${DB_USERNAME:patrimonio_user}
spring.datasource.password=${DB_PASSWORD:patrimonio123}

# Production Connection Pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.connection-timeout=30000
```

#### Teste
```properties
# Test - H2 In-Memory Database
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.h2.console.enabled=true
```

### JPA/Hibernate Tuning

```properties
# Performance Optimizations
spring.jpa.hibernate.jdbc.batch_size=20
spring.jpa.hibernate.order_inserts=true
spring.jpa.hibernate.order_updates=true
spring.jpa.hibernate.jdbc.batch_versioned_data=true
spring.jpa.hibernate.generate_statistics=false

# SQL Logging (Development only)
spring.jpa.show-sql=${SHOW_SQL:false}
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

## 📁 Upload de Arquivos

### Configurações de Storage

```properties
# Storage Configuration
storage.location=${STORAGE_LOCATION:/opt/claude/renovacampo/uploads}
storage.photo.max-size=${PHOTO_MAX_SIZE:5MB}
storage.document.max-size=${DOCUMENT_MAX_SIZE:10MB}

# Multipart Configuration
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=${MAX_FILE_SIZE:10MB}
spring.servlet.multipart.max-request-size=${MAX_REQUEST_SIZE:10MB}
spring.servlet.multipart.resolve-lazily=true
```

### Tipos de Arquivo Suportados

```properties
# File Type Configuration
storage.photo.allowed-types=image/jpeg,image/png,image/gif,image/webp
storage.document.allowed-types=application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet

# Thumbnail Configuration
storage.thumbnail.width=200
storage.thumbnail.height=200
storage.thumbnail.quality=0.8
```

### Configuração de Diretórios

```bash
# Criar estrutura de diretórios
mkdir -p /opt/claude/renovacampo/uploads/{photos,documents,others,temp}

# Configurar permissões
sudo chown -R $USER:$USER /opt/claude/renovacampo/
chmod -R 755 /opt/claude/renovacampo/uploads/
```

## 📊 Logging

### Configuração de Logs

```properties
# Logging Configuration
logging.level.org.acabativa.rc=${LOG_LEVEL:INFO}
logging.level.org.springframework.web=${WEB_LOG_LEVEL:WARN}
logging.level.org.hibernate=${HIBERNATE_LOG_LEVEL:WARN}
logging.level.com.zaxxer.hikari=${HIKARI_LOG_LEVEL:WARN}

# File Logging
logging.file.name=${LOG_FILE:../logs/patrimonio_app.log}
logging.file.max-size=${LOG_MAX_SIZE:10MB}
logging.file.max-history=${LOG_MAX_HISTORY:30}
logging.file.total-size-cap=${LOG_TOTAL_SIZE:100MB}

# Log Pattern
logging.pattern.file=%d{ISO8601} [%thread] %-5level %logger{36} - %msg%n
logging.pattern.console=%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
```

### Configuração Logback (Avançada)

```xml
<!-- src/main/resources/logback-spring.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    
    <!-- Console Appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
            <charset>utf8</charset>
        </encoder>
    </appender>
    
    <!-- File Appender -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>../logs/patrimonio_app.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>../logs/patrimonio_app.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxFileSize>10MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>1GB</totalSizeCap>
        </rollingPolicy>
        <encoder>
            <pattern>%d{ISO8601} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- Application Loggers -->
    <logger name="org.acabativa.rc" level="INFO" additivity="false">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </logger>
    
    <!-- SQL Logging (Development) -->
    <springProfile name="development">
        <logger name="org.hibernate.SQL" level="DEBUG"/>
        <logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE"/>
    </springProfile>
    
    <!-- Root Logger -->
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

## ⚡ Performance

### Configurações de Performance

```properties
# JVM Performance Tuning
JAVA_OPTS=-Xms512m -Xmx2g -XX:+UseG1GC -XX:G1HeapRegionSize=16m

# Tomcat Performance
server.tomcat.threads.max=200
server.tomcat.threads.min-spare=10
server.tomcat.accept-count=100
server.tomcat.max-connections=8192

# Connection Pool Tuning
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.leak-detection-threshold=60000

# Cache Configuration
spring.cache.type=simple
spring.cache.cache-names=properties,projects,investors,enterprises
```

### JPA Performance

```properties
# Hibernate Performance
spring.jpa.hibernate.jdbc.batch_size=25
spring.jpa.hibernate.order_inserts=true
spring.jpa.hibernate.order_updates=true
spring.jpa.hibernate.jdbc.batch_versioned_data=true

# Second Level Cache (Redis - Future)
spring.jpa.hibernate.cache.use_second_level_cache=false
spring.jpa.hibernate.cache.use_query_cache=false
```

## 🔒 Segurança

### Configurações de Segurança (v1.2.0)

```properties
# Security Configuration (Planned)
security.jwt.secret=${JWT_SECRET:renovacampo-secret-key}
security.jwt.expiration=${JWT_EXPIRATION:86400}

# HTTPS Configuration
server.ssl.enabled=${SSL_ENABLED:false}
server.ssl.key-store=${SSL_KEYSTORE:}
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD:}
server.ssl.key-store-type=PKCS12

# CORS Configuration
cors.allowed-origins=${CORS_ORIGINS:http://localhost:3000,http://localhost:8080}
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed-headers=*
cors.allow-credentials=true
```

### Configuração Spring Security

```java
// SecurityConfig.java (v1.2.0)
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Value("${security.jwt.secret}")
    private String jwtSecret;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/", "/properties", "/projects").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/dashboard")
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .build();
    }
}
```

## 🌍 Profiles de Ambiente

### Development Profile

```properties
# application-development.properties
spring.profiles.active=development

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/patrimonio_dev
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

# Logging
logging.level.org.acabativa.rc=DEBUG
logging.level.org.springframework.web=DEBUG

# Cache
spring.thymeleaf.cache=false
spring.web.resources.cache.period=0

# DevTools
spring.devtools.restart.enabled=true
spring.devtools.livereload.enabled=true
```

### Test Profile

```properties
# application-test.properties
spring.profiles.active=test

# H2 Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop

# Logging
logging.level.org.acabativa.rc=WARN
logging.level.org.springframework=WARN
logging.level.org.hibernate=WARN

# Disable features
spring.jpa.show-sql=false
management.endpoints.web.exposure.include=health
```

### Production Profile

```properties
# application-production.properties
spring.profiles.active=production

# Database
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate

# Performance
spring.jpa.show-sql=false
spring.thymeleaf.cache=true
spring.web.resources.cache.period=31536000

# Security
server.ssl.enabled=true
management.endpoints.web.exposure.include=health,info,metrics

# Logging
logging.level.org.acabativa.rc=INFO
logging.level.root=WARN
```

## 🔧 Variáveis de Ambiente

### Configuração via Environment Variables

```bash
# Database Configuration
export DATABASE_URL="jdbc:postgresql://localhost:5432/patrimonio"
export DB_USERNAME="patrimonio_user"
export DB_PASSWORD="patrimonio123"

# Storage Configuration
export STORAGE_LOCATION="/opt/claude/renovacampo/uploads"
export MAX_FILE_SIZE="10MB"
export MAX_REQUEST_SIZE="10MB"

# Logging Configuration
export LOG_LEVEL="INFO"
export LOG_FILE="../logs/patrimonio_app.log"

# Performance Configuration
export HIKARI_MAX_POOL_SIZE="20"
export HIKARI_MIN_IDLE="5"

# Security Configuration (v1.2.0)
export JWT_SECRET="your-secret-key-here"
export JWT_EXPIRATION="86400"

# Application Configuration
export SPRING_PROFILES_ACTIVE="production"
export SERVER_PORT="8080"
```

### Docker Environment

```yaml
# docker-compose.yml
version: '3.8'
services:
  app:
    image: renovacampo/patrimonio:1.1-SNAPSHOT
    environment:
      - SPRING_PROFILES_ACTIVE=production
      - DATABASE_URL=jdbc:postgresql://db:5432/patrimonio
      - DB_USERNAME=patrimonio_user
      - DB_PASSWORD=patrimonio123
      - STORAGE_LOCATION=/opt/uploads
    volumes:
      - ./uploads:/opt/uploads
      - ./logs:/opt/logs
    ports:
      - "8080:8080"
    depends_on:
      - db
  
  db:
    image: postgres:15
    environment:
      - POSTGRES_DB=patrimonio
      - POSTGRES_USER=patrimonio_user
      - POSTGRES_PASSWORD=patrimonio123
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

volumes:
  postgres_data:
```

### Kubernetes ConfigMap

```yaml
# configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: renovacampo-config
data:
  application.properties: |
    spring.profiles.active=production
    spring.datasource.url=jdbc:postgresql://postgres-service:5432/patrimonio
    storage.location=/opt/uploads
    logging.level.org.acabativa.rc=INFO
    server.port=8080
---
apiVersion: v1
kind: Secret
metadata:
  name: renovacampo-secrets
type: Opaque
stringData:
  db-username: patrimonio_user
  db-password: patrimonio123
  jwt-secret: your-secret-key
```

## 🔍 Monitoramento e Métricas

### Spring Boot Actuator

```properties
# Actuator Configuration
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=when-authorized
management.info.env.enabled=true

# Custom Health Indicators
management.health.diskspace.enabled=true
management.health.ping.enabled=true
management.health.db.enabled=true

# Metrics
management.metrics.export.prometheus.enabled=true
management.metrics.distribution.percentiles-histogram.http.server.requests=true
```

### Custom Health Check

```java
@Component
public class CustomHealthIndicator implements HealthIndicator {
    
    @Autowired
    private PropertyRepository propertyRepository;
    
    @Override
    public Health health() {
        try {
            long count = propertyRepository.count();
            if (count > 0) {
                return Health.up()
                    .withDetail("properties.count", count)
                    .withDetail("database", "accessible")
                    .build();
            } else {
                return Health.down()
                    .withDetail("properties.count", 0)
                    .withDetail("database", "no data")
                    .build();
            }
        } catch (Exception e) {
            return Health.down()
                .withDetail("database", "not accessible")
                .withException(e)
                .build();
        }
    }
}
```

## 🛠️ Configurações Avançadas

### Custom Properties

```java
@ConfigurationProperties(prefix = "renovacampo")
@Component
public class RenovaCampoProperties {
    
    private Storage storage = new Storage();
    private Performance performance = new Performance();
    
    // Getters and setters
    
    public static class Storage {
        private String location = "/opt/uploads";
        private String photoMaxSize = "5MB";
        private String documentMaxSize = "10MB";
        // Getters and setters
    }
    
    public static class Performance {
        private int connectionPoolSize = 20;
        private int batchSize = 25;
        // Getters and setters
    }
}
```

### Configuration Validation

```java
@Configuration
@Validated
public class ValidationConfig {
    
    @Value("${storage.location}")
    @NotBlank(message = "Storage location cannot be blank")
    private String storageLocation;
    
    @Value("${spring.datasource.url}")
    @Pattern(regexp = "jdbc:postgresql://.*", message = "Must be PostgreSQL URL")
    private String databaseUrl;
    
    @PostConstruct
    public void validateConfiguration() {
        File storageDir = new File(storageLocation);
        if (!storageDir.exists() || !storageDir.canWrite()) {
            throw new IllegalStateException("Storage location not accessible: " + storageLocation);
        }
    }
}
```

---

**Próximo passo**: [🚀 Documentação de Deploy](DEPLOY.md)
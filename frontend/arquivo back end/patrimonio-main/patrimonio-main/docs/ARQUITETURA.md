# 🏗️ Documentação de Arquitetura - RenovaCampo

## 📋 Índice

- [Visão Geral da Arquitetura](#-visão-geral-da-arquitetura)
- [Stack Tecnológico](#-stack-tecnológico)
- [Estrutura de Pacotes](#-estrutura-de-pacotes)
- [Camadas da Aplicação](#-camadas-da-aplicação)
- [Modelo de Dados](#-modelo-de-dados)
- [Padrões de Design](#-padrões-de-design)
- [Segurança](#-segurança)
- [Performance](#-performance)
- [Monitoramento](#-monitoramento)

## 🎯 Visão Geral da Arquitetura

O RenovaCampo segue uma arquitetura **modular em camadas** baseada no Spring Boot, com separação clara de responsabilidades e alta coesão entre módulos.

### Arquitetura Lógica

```
┌─────────────────────────────────────────────────┐
│                 Frontend Layer                  │
│         (Thymeleaf + HTMX + CSS/JS)            │
├─────────────────────────────────────────────────┤
│              Presentation Layer                 │
│           (Controllers Web + REST)              │
├─────────────────────────────────────────────────┤
│                Service Layer                    │
│             (Business Logic)                    │
├─────────────────────────────────────────────────┤
│              Repository Layer                   │
│            (Data Access + JPA)                 │
├─────────────────────────────────────────────────┤
│               Database Layer                    │
│              (PostgreSQL 15)                   │
└─────────────────────────────────────────────────┘
```

### Arquitetura Física

```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   Browser    │◄──►│ Spring Boot  │◄──►│ PostgreSQL   │
│  (Client)    │    │ Application  │    │   Database   │
└──────────────┘    └──────────────┘    └──────────────┘
                            │
                            ▼
                    ┌──────────────┐
                    │ File System  │
                    │   Storage    │
                    └──────────────┘
```

## 🔧 Stack Tecnológico

### Backend Framework

| Componente | Tecnologia | Versão | Propósito |
|------------|------------|--------|-----------|
| **Runtime** | Java | 17 LTS | Plataforma principal |
| **Framework** | Spring Boot | 3.4.5 | Framework web/aplicação |
| **Persistence** | Spring Data JPA | 3.4.5 | ORM e repositórios |
| **Security** | Spring Security | 3.4.5 | Segurança (v1.2.0) |
| **Validation** | Bean Validation | 3.0 | Validação de dados |
| **Database** | PostgreSQL | 15.13 | Banco de dados |
| **Connection Pool** | HikariCP | 5.0+ | Pool de conexões |

### Frontend Stack

| Componente | Tecnologia | Versão | Propósito |
|------------|------------|--------|-----------|
| **Template Engine** | Thymeleaf | 3.1+ | Renderização server-side |
| **JavaScript** | HTMX | 1.9+ | Interações dinâmicas |
| **CSS** | CSS3 + Bootstrap | 5.3+ | Estilização |
| **Maps** | Leaflet.js | 1.9+ | Mapas interativos |
| **Icons** | Font Awesome | 6.0+ | Ícones |

### Build & Deploy

| Componente | Tecnologia | Versão | Propósito |
|------------|------------|--------|-----------|
| **Build Tool** | Maven | 3.9+ | Gerenciamento de dependências |
| **Package** | JAR Executable | - | Empacotamento |
| **JVM** | OpenJDK | 17 | Execução |
| **OS** | Linux (Ubuntu/Debian) | 20.04+ | Sistema operacional |

## 📦 Estrutura de Pacotes

### Organização Principal

```
org.acabativa.rc/
├── patrimonio/                 # Módulo core de negócio
│   ├── PatrimonioApplication   # Main class
│   ├── controller/            # Web e REST controllers
│   ├── entity/               # Entidades JPA
│   ├── repository/           # Repositórios de dados
│   ├── service/              # Lógica de negócio
│   ├── test/                 # Testes internos
│   └── util/                 # Utilitários
└── storage/                   # Módulo de gestão de arquivos
    ├── controller/           # Controllers de arquivo
    ├── dto/                  # Data Transfer Objects
    ├── entity/               # Entidades de arquivo
    ├── repository/           # Repositórios de arquivo
    ├── service/              # Serviços de storage
    └── util/                 # Processamento de imagem
```

### Separação de Responsabilidades

#### Módulo Patrimonio (Core)
- **Responsabilidade**: Lógica de negócio principal
- **Entidades**: Property, Project, Investor, Enterprise
- **Relacionamentos**: Cross-entity business logic
- **APIs**: REST endpoints principais

#### Módulo Storage (Files)
- **Responsabilidade**: Gestão de arquivos e mídia
- **Entidades**: StoredFile
- **Funcionalidades**: Upload, thumbnail, download
- **Independência**: Pode ser reutilizado em outros projetos

## 🏛️ Camadas da Aplicação

### 1. Presentation Layer (Controllers)

#### Web Controllers
```java
@Controller
@RequestMapping("/properties")
public class PropertyViewController {
    // Renderização de páginas Thymeleaf
    // Gerenciamento de formulários
    // Navegação entre páginas
}
```

#### REST Controllers
```java
@RestController
@RequestMapping("/api/v1/property")
public class PropertyRestApi {
    // APIs JSON para frontend
    // Integração com sistemas externos
    // Mobile/SPA support
}
```

**Responsabilidades:**
- Validação de entrada
- Conversão de DTOs
- Tratamento de erros HTTP
- Controle de fluxo de navegação

### 2. Service Layer (Business Logic)

```java
@Service
@Transactional
public class EnterpriseService {
    // Regras de negócio complexas
    // Orquestração entre entidades
    // Cálculos de funding progress
    // Validações de negócio
}
```

**Características:**
- **@Transactional**: Consistência de dados
- **Business Rules**: Validações complexas
- **Cross-Entity**: Lógica entre múltiplas entidades
- **Calculations**: Métricas e estatísticas

### 3. Repository Layer (Data Access)

```java
@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
    @Query("SELECT e FROM Enterprise e WHERE e.active = true AND e.status = :status")
    List<Enterprise> findActiveByStatus(@Param("status") EnterpriseStatus status);
    
    // Custom queries para business logic
    // Otimizações de performance
    // Agregações e estatísticas
}
```

**Funcionalidades:**
- **JPA Repositories**: CRUD automático
- **Custom Queries**: Consultas específicas
- **Named Parameters**: Segurança contra SQL injection
- **Pagination**: Suporte a paginação

### 4. Entity Layer (Domain Model)

```java
@Entity
@Table(name = "enterprise")
public class Enterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;
    
    // Rich domain model com business logic
    // Relacionamentos bem definidos
    // Validações Bean Validation
}
```

**Características:**
- **Rich Domain Model**: Entidades com comportamento
- **Lazy Loading**: Performance otimizada
- **Bean Validation**: Validação declarativa
- **Audit Fields**: createDate, updateDate automáticos

## 🗃️ Modelo de Dados

### Diagrama ER

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│  Property   │    │   Project   │    │  Investor   │
│             │    │             │    │             │
│ + id        │    │ + id        │    │ + id        │
│ + name      │    │ + name      │    │ + name      │
│ + area      │    │ + status    │    │ + funds     │
│ + location  │    │ + costs     │    │ + active    │
└──────┬──────┘    └──────┬──────┘    └──────┬──────┘
       │                  │                  │
       │                  │                  │
       └────────┬─────────┴────────┬─────────┘
                │                  │
        ┌───────▼──────────────────▼───────┐
        │         Enterprise              │
        │                                 │
        │ + id                           │
        │ + name                         │ 
        │ + propertyId                   │
        │ + projectId                    │
        │ + totalInvestmentRequired      │
        │ + totalInvestmentRaised        │
        │ + fundingProgress (calculated) │
        └─────────────┬───────────────────┘
                      │
               ┌──────▼──────┐
               │EnterpriseInvestor│
               │                 │
               │ + enterpriseId  │
               │ + investorId    │
               │ + amount        │
               │ + percentage    │
               └─────────────────┘
```

### Relacionamentos

#### Property (1) → (N) Enterprise
- Uma propriedade pode ter múltiplos empreendimentos
- Empreendimento sempre vinculado a uma propriedade

#### Project (1) → (N) Enterprise  
- Um projeto pode ser usado em múltiplos empreendimentos
- Empreendimento sempre vinculado a um projeto

#### Enterprise (N) ← → (M) Investor
- Relacionamento many-to-many via EnterpriseInvestor
- Investor pode participar de múltiplos empreendimentos
- Enterprise pode ter múltiplos investidores

#### Property (1) → (N) StoredFile
- Propriedade pode ter múltiplas fotos e documentos
- Arquivo sempre vinculado a uma propriedade

### Campos Calculados

```java
// Exemplo: funding progress automático
@Transient
public BigDecimal getFundingProgress() {
    if (totalInvestmentRequired != null && totalInvestmentRequired.compareTo(BigDecimal.ZERO) > 0) {
        return totalInvestmentRaised.divide(totalInvestmentRequired, 2, RoundingMode.HALF_UP);
    }
    return BigDecimal.ZERO;
}
```

## 🎨 Padrões de Design

### 1. Repository Pattern

```java
// Interface baseada em Spring Data JPA
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // Métodos automáticos: save, findById, findAll, delete
    
    // Métodos customizados
    List<Property> findByStateAndCity(String state, String city);
    
    @Query("SELECT p FROM Property p WHERE p.totalArea > :minArea")
    List<Property> findLargeProperties(@Param("minArea") BigDecimal minArea);
}
```

### 2. Service Layer Pattern

```java
@Service
@Transactional
public class EnterpriseService {
    
    private final EnterpriseRepository enterpriseRepository;
    private final PropertyRepository propertyRepository;
    private final ProjectRepository projectRepository;
    
    // Dependency injection via constructor
    public EnterpriseService(EnterpriseRepository enterpriseRepository, ...) {
        this.enterpriseRepository = enterpriseRepository;
        // ...
    }
    
    public Enterprise createEnterprise(Enterprise enterprise) {
        // Validações de negócio
        validateBusinessRules(enterprise);
        
        // Persistência
        return enterpriseRepository.save(enterprise);
    }
}
```

### 3. DTO Pattern

```java
// Para transferência de dados entre camadas
public class EnterpriseDTO {
    private Long id;
    private String name;
    private String propertyName;     // Denormalizado para performance
    private String projectName;      // Denormalizado para performance
    private BigDecimal fundingProgress;
    
    // Conversão de entidade para DTO
    public static EnterpriseDTO from(Enterprise enterprise) {
        // Mapping logic
    }
}
```

### 4. Builder Pattern

```java
// Para construção de objetos complexos
Enterprise enterprise = Enterprise.builder()
    .name("Empreendimento Teste")
    .property(property)
    .project(project)
    .totalInvestmentRequired(new BigDecimal("500000"))
    .minimumInvestment(new BigDecimal("10000"))
    .status(EnterpriseStatus.PLANNING)
    .build();
```

### 5. Strategy Pattern

```java
// Para diferentes tipos de cálculos
public interface FundingCalculationStrategy {
    BigDecimal calculateProgress(Enterprise enterprise);
}

@Component
public class StandardFundingCalculation implements FundingCalculationStrategy {
    public BigDecimal calculateProgress(Enterprise enterprise) {
        // Implementação padrão
    }
}
```

## 🔒 Segurança

### Implementação Atual (v1.1-SNAPSHOT)

- **Sem autenticação**: Acesso livre para desenvolvimento
- **Validação de entrada**: Bean Validation em todas as entidades
- **SQL Injection**: Prevenção via JPA/Hibernate
- **CSRF**: Proteção nativa do Spring (será habilitada em v1.2.0)

### Roadmap de Segurança (v1.2.0)

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/dashboard")
            )
            .build();
    }
}
```

### Validação de Dados

```java
@Entity
public class Enterprise {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String name;
    
    @Positive(message = "Investimento mínimo deve ser positivo")
    private BigDecimal minimumInvestment;
    
    @Future(message = "Data de conclusão deve ser futura")
    private LocalDate expectedCompletionDate;
}
```

## ⚡ Performance

### Estratégias de Otimização

#### 1. Database Performance

```java
// Lazy loading para relacionamentos
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "property_id")
private Property property;

// Batch fetching para N+1 queries
@BatchSize(size = 10)
@OneToMany(mappedBy = "enterprise")
private List<EnterpriseInvestor> investors;

// Query otimizadas com JOIN FETCH
@Query("SELECT e FROM Enterprise e JOIN FETCH e.property JOIN FETCH e.project WHERE e.active = true")
List<Enterprise> findActiveWithRelations();
```

#### 2. Connection Pooling

```properties
# HikariCP configuração
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=600000
```

#### 3. JPA Optimizations

```properties
# Hibernate performance tuning
spring.jpa.hibernate.jdbc.batch_size=20
spring.jpa.hibernate.order_inserts=true
spring.jpa.hibernate.order_updates=true
spring.jpa.hibernate.jdbc.batch_versioned_data=true
```

#### 4. Frontend Performance

```html
<!-- Progressive loading com HTMX -->
<div hx-get="/api/v1/properties/quick" 
     hx-trigger="load"
     hx-swap="outerHTML">
    Loading...
</div>

<!-- Lazy loading de imagens -->
<img src="placeholder.jpg" 
     data-src="real-image.jpg" 
     class="lazy-load">
```

### Métricas de Performance

| Métrica | Atual | Meta |
|---------|-------|------|
| **Page Load** | < 2s | < 1s |
| **API Response** | < 500ms | < 200ms |
| **Database Query** | < 100ms | < 50ms |
| **File Upload** | < 5s | < 3s |

## 📊 Monitoramento

### Logging Strategy

```properties
# Configuração de logs
logging.level.org.acabativa.rc=INFO
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.springframework.web=DEBUG
logging.file.name=../logs/patrimonio_app.log
logging.pattern.file=%d{ISO8601} [%thread] %-5level %logger{36} - %msg%n
```

### Health Checks

```bash
# Application health
curl http://localhost:8080/actuator/health

# Database connectivity
curl http://localhost:8080/actuator/health/db

# Disk space
curl http://localhost:8080/actuator/health/diskSpace
```

### Metrics Collection (Planned v1.3.0)

```java
@RestController
public class MetricsController {
    
    @GetMapping("/api/v1/metrics/enterprises")
    public Map<String, Object> getEnterpriseMetrics() {
        return Map.of(
            "total", enterpriseService.getTotalCount(),
            "active", enterpriseService.getActiveCount(),
            "funding_progress", enterpriseService.getAverageFunding()
        );
    }
}
```

### Error Tracking

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        logger.error("Unexpected error", e);
        
        return ResponseEntity.status(500)
            .body(new ErrorResponse("INTERNAL_ERROR", e.getMessage()));
    }
}
```

## 🔄 Deployment Architecture

### Development Environment

```
┌─────────────────┐
│   Developer     │
│    Machine      │
│                 │
│ Java 17         │
│ PostgreSQL      │
│ ./mvnw spring   │
└─────────────────┘
```

### Production Environment (Raspberry Pi)

```
┌─────────────────────────────────────┐
│         Raspberry Pi 4              │
│                                     │
│ ┌─────────────┐ ┌─────────────────┐ │
│ │ PostgreSQL  │ │  Spring Boot    │ │
│ │   Service   │ │   Application   │ │
│ │   Port:5432 │ │   Port:8080     │ │
│ └─────────────┘ └─────────────────┘ │
│                                     │
│ File System: /opt/claude/uploads    │
└─────────────────────────────────────┘
```

### Scalability Considerations (Future)

```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│Load Balancer │    │  App Server  │    │   Database   │
│   (Nginx)    │◄──►│    Node 1    │◄──►│  (Primary)   │
└──────────────┘    └──────────────┘    └──────────────┘
                           │                     │
                    ┌──────────────┐             │
                    │  App Server  │◄────────────┘
                    │    Node 2    │
                    └──────────────┘
```

---

**Próximo passo**: [🧪 Documentação de Testes](TESTES.md)
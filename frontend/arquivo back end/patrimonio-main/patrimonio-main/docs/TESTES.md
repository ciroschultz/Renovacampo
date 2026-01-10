# 🧪 Documentação de Testes - RenovaCampo

## 📋 Índice

- [Estratégia de Testes](#-estratégia-de-testes)
- [Tipos de Teste](#-tipos-de-teste)
- [Estrutura de QA](#-estrutura-de-qa)
- [Testes Automatizados](#-testes-automatizados)
- [Testes Manuais](#-testes-manuais)
- [Performance Testing](#-performance-testing)
- [Relatórios de Teste](#-relatórios-de-teste)
- [CI/CD Integration](#-cicd-integration)

## 🎯 Estratégia de Testes

### Pirâmide de Testes

```
        /\
       /  \        E2E Tests
      /____\       (5% - High Level)
     /      \
    /        \     Integration Tests  
   /__________\    (20% - Medium Level)
  /            \
 /              \  Unit Tests
/________________\ (75% - Low Level)
```

### Cobertura de Testes

| Tipo | Cobertura Atual | Meta | Ferramenta |
|------|----------------|------|------------|
| **Unit Tests** | 65% | 80% | JUnit 5 + Mockito |
| **Integration Tests** | 84% | 90% | Spring Test + TestContainers |
| **E2E Tests** | 45% | 70% | Python + Selenium |
| **API Tests** | 84% | 95% | Custom Test Suite |

## 🧪 Tipos de Teste

### 1. Unit Tests (Testes Unitários)

**Objetivo**: Testar componentes individuais isoladamente

```java
@ExtendWith(MockitoExtension.class)
class EnterpriseServiceTest {
    
    @Mock
    private EnterpriseRepository enterpriseRepository;
    
    @Mock
    private PropertyRepository propertyRepository;
    
    @InjectMocks
    private EnterpriseService enterpriseService;
    
    @Test
    @DisplayName("Should calculate funding progress correctly")
    void shouldCalculateFundingProgress() {
        // Given
        Enterprise enterprise = Enterprise.builder()
            .totalInvestmentRequired(new BigDecimal("100000"))
            .totalInvestmentRaised(new BigDecimal("60000"))
            .build();
        
        // When
        BigDecimal progress = enterprise.getFundingProgress();
        
        // Then
        assertThat(progress).isEqualByComparingTo("0.60");
    }
    
    @Test
    @DisplayName("Should validate minimum investment requirement")
    void shouldValidateMinimumInvestment() {
        // Given
        Enterprise enterprise = createValidEnterprise();
        BigDecimal investmentAmount = new BigDecimal("5000");
        
        // When & Then
        assertThatThrownBy(() -> 
            enterpriseService.validateInvestment(enterprise, investmentAmount))
            .isInstanceOf(InsufficientInvestmentException.class)
            .hasMessage("Investment below minimum required");
    }
}
```

### 2. Integration Tests (Testes de Integração)

**Objetivo**: Testar interação entre componentes

```java
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class EnterpriseIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private EnterpriseRepository enterpriseRepository;
    
    @Test
    @Order(1)
    @DisplayName("Should create enterprise via REST API")
    void shouldCreateEnterpriseViaAPI() {
        // Given
        EnterpriseDTO dto = EnterpriseDTO.builder()
            .name("Test Enterprise")
            .propertyId(1L)
            .projectId(1L)
            .totalInvestmentRequired(new BigDecimal("500000"))
            .build();
        
        // When
        ResponseEntity<EnterpriseDTO> response = restTemplate.postForEntity(
            "/api/v1/enterprises", 
            dto, 
            EnterpriseDTO.class
        );
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
    }
    
    @Test
    @Order(2)
    @DisplayName("Should persist enterprise to database")
    void shouldPersistEnterpriseToDatabase() {
        // When
        List<Enterprise> enterprises = enterpriseRepository.findAll();
        
        // Then
        assertThat(enterprises).isNotEmpty();
        assertThat(enterprises.get(0).getName()).isEqualTo("Test Enterprise");
    }
}
```

### 3. Repository Tests (Testes de Repositório)

```java
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class EnterpriseRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private EnterpriseRepository enterpriseRepository;
    
    @Test
    @DisplayName("Should find active enterprises by status")
    void shouldFindActiveEnterprisesByStatus() {
        // Given
        Enterprise enterprise = createEnterprise("Test Enterprise", EnterpriseStatus.ACTIVE);
        entityManager.persistAndFlush(enterprise);
        
        // When
        List<Enterprise> result = enterpriseRepository.findActiveByStatus(EnterpriseStatus.ACTIVE);
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Enterprise");
    }
}
```

### 4. Web Layer Tests (Testes de Controller)

```java
@WebMvcTest(EnterpriseController.class)
class EnterpriseControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private EnterpriseService enterpriseService;
    
    @Test
    @DisplayName("Should return enterprise list page")
    void shouldReturnEnterpriseListPage() throws Exception {
        // Given
        List<Enterprise> enterprises = Arrays.asList(
            createEnterprise("Enterprise 1"),
            createEnterprise("Enterprise 2")
        );
        when(enterpriseService.findAllActive()).thenReturn(enterprises);
        
        // When & Then
        mockMvc.perform(get("/enterprises"))
            .andExpect(status().isOk())
            .andExpect(view().name("enterprises/index"))
            .andExpect(model().attribute("enterprises", hasSize(2)));
    }
}
```

## 📁 Estrutura de QA

### Organização dos Arquivos

```
patrimonio/
├── qa/                              # Pasta principal de QA
│   ├── QA_DEPLOYMENT.md            # Guia de deployment
│   ├── QA_REALISTIC_TEST_PLAN.csv  # Plano de testes realísticos
│   ├── QA_TEST_PLAN.csv            # Plano de testes padrão
│   └── test-reports/               # Relatórios e executores
│       ├── realistic_test_executor.py
│       ├── test_executor.py
│       └── *.csv                   # Relatórios de execução
├── src/test/java/                  # Testes unitários Java
│   └── org/acabativa/rc/patrimonio/
│       ├── PatrimonioApplicationTests.java
│       ├── controller/             # Testes de controller
│       ├── service/                # Testes de service
│       └── repository/             # Testes de repository
└── docs/
    └── TESTES.md                   # Esta documentação
```

## 🤖 Testes Automatizados

### Executor de Testes Realísticos

O sistema inclui um **executor inteligente** que descobre dados reais no sistema e executa testes com IDs válidos.

#### Funcionamento

```python
class RealisticQATestExecutor:
    def discover_real_data(self):
        """Descobre IDs reais que existem no sistema"""
        # Busca propriedades via API
        response = self.session.get(f"{self.base_url}/api/v1/property")
        properties = response.json()
        self.real_data['property_ids'] = [p['id'] for p in properties]
        
        # Busca investidores via API
        response = self.session.get(f"{self.base_url}/api/v1/investors")
        investors = response.json()
        self.real_data['investor_ids'] = [i['id'] for i in investors]
```

#### Execução dos Testes

```bash
# Navegar para pasta de testes
cd qa/test-reports

# Executar suite completa (75 testes)
python3 realistic_test_executor.py

# Verificar se servidor está rodando
curl http://localhost:8080/api/v1/property
```

#### Exemplo de Saída

```
🚀 RenovaCampo Realistic QA Test Executor
==================================================
📋 Loading realistic test cases...
   Loaded 75 test cases

🔍 Checking server at http://localhost:8080...
   ✅ Server is running

🔍 Discovering real data in the system...
   📋 Found 15 properties: [5, 9, 20, 26, 29]
   👥 Found 2 investors: [1, 2]
   📋 Found 6 projects: [1, 2, 3]
   🏢 Found 5 enterprises: [1, 2, 3]

🧪 Executing 75 realistic test cases
============================================================
[ 1/75] PROP-NAV-001: Access properties list page...
    ✅ PASS (48ms)
[ 2/75] PROP-NAV-002: Access property creation page...
    ✅ PASS (37ms)
...
[75/75] FORM-ENT-001: Enterprise creation form validation...
    ❌ FAIL (21ms)
       Server error (500)

📊 Final Results:
   ✅ Passed: 63
   ❌ Failed: 4
   📝 Manual: 8
   ⏭️ Skipped: 0
   📈 Success Rate: 84.0%
   💾 Report saved: realistic_test_execution_20250606_021501.csv
```

### Categorias de Teste

| Categoria | Descrição | Quantidade | Status |
|-----------|-----------|------------|--------|
| **Navigation** | Testes de navegação UI | 20 | ✅ 95% Pass |
| **API** | Testes de endpoints REST | 25 | ✅ 96% Pass |
| **CRUD** | Operações Create/Read/Update/Delete | 10 | ✅ 90% Pass |
| **Integration** | Testes de integração entre módulos | 8 | ✅ 87% Pass |
| **Validation** | Testes de validação de dados | 5 | 📝 Manual |
| **Performance** | Testes de performance | 3 | 📝 Manual |
| **Error Handling** | Tratamento de erros | 4 | 📝 Manual |

### Configuração de Teste

```properties
# application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Desabilitar logs desnecessários
logging.level.org.springframework.web=WARN
logging.level.org.hibernate=WARN
```

## 👤 Testes Manuais

### Checklist de Testes Manuais

#### ✅ Funcionalidades Core

**Propriedades**
- [ ] Criar propriedade com dados válidos
- [ ] Validar campos obrigatórios
- [ ] Upload de múltiplas fotos
- [ ] Visualização em mapa
- [ ] Edição de propriedade existente
- [ ] Exclusão de propriedade

**Projetos**
- [ ] Criação de projeto com status PLANNING
- [ ] Transições de status válidas
- [ ] Cálculos de ROI corretos
- [ ] Filtros por status/prioridade
- [ ] Upload de documentos

**Investidores**
- [ ] Cadastro com CPF/CNPJ válido
- [ ] Cálculo de fundos disponíveis
- [ ] Progress bar de utilização
- [ ] Ativação/desativação
- [ ] Busca por nome/localização

**Empreendimentos**
- [ ] Criação vinculando propriedade + projeto
- [ ] Adição de investidores
- [ ] Cálculo de funding progress
- [ ] Validação de investimento mínimo
- [ ] Dashboard de métricas

#### 🎨 UI/UX Testing

**Layout Responsivo**
- [ ] Desktop (1920x1080)
- [ ] Tablet (768x1024)
- [ ] Mobile (375x667)
- [ ] Mobile landscape

**Navegação**
- [ ] Breadcrumbs funcionais
- [ ] Links entre módulos
- [ ] Botões de ação
- [ ] Menu principal

**Performance**
- [ ] Load time < 3s
- [ ] Smooth scrolling
- [ ] Image loading
- [ ] API response time

### Cenários de Teste Específicos

#### Cenário 1: Fluxo Completo de Empreendimento

```
1. Criar nova propriedade rural (500 hectares)
2. Criar projeto de "Cultivo de Soja 2025"
3. Cadastrar investidor com R$ 1.000.000
4. Criar empreendimento vinculando os três
5. Adicionar investimento de R$ 200.000
6. Verificar funding progress = 40%
7. Upload de fotos da propriedade
8. Verificar dashboard atualizado
```

#### Cenário 2: Validações de Negócio

```
1. Tentar criar empreendimento sem propriedade → Error
2. Investimento abaixo do mínimo → Error
3. Data de conclusão no passado → Error
4. Upload arquivo > 10MB → Error
5. Desativar investidor com investimentos → Warning
```

## ⚡ Performance Testing

### Métricas de Performance

| Endpoint | Response Time | Throughput | Error Rate |
|----------|---------------|------------|------------|
| `GET /` | < 500ms | 100 req/s | < 0.1% |
| `GET /api/v1/property` | < 200ms | 200 req/s | < 0.1% |
| `POST /api/v1/enterprises` | < 1s | 50 req/s | < 1% |
| `POST /api/v1/photos/upload` | < 5s | 10 req/s | < 2% |

### Ferramentas de Performance

#### JMeter Test Plan

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2">
  <hashTree>
    <TestPlan testname="RenovaCampo Performance Test">
      <elementProp name="TestPlan.arguments" elementType="Arguments" guiclass="ArgumentsPanel">
        <collectionProp name="Arguments.arguments"/>
      </elementProp>
      <stringProp name="TestPlan.user_define_classpath"></stringProp>
      <boolProp name="TestPlan.serialize_threadgroups">false</boolProp>
      <boolProp name="TestPlan.functional_mode">false</boolProp>
    </TestPlan>
  </hashTree>
</jmeterTestPlan>
```

#### Artillery.js Load Testing

```yaml
# artillery-test.yml
config:
  target: 'http://localhost:8080'
  phases:
    - duration: 60
      arrivalRate: 10
    - duration: 120
      arrivalRate: 20
    - duration: 60
      arrivalRate: 5

scenarios:
  - name: "API Endpoints"
    weight: 100
    flow:
      - get:
          url: "/api/v1/property"
      - get:
          url: "/api/v1/investors"
      - get:
          url: "/api/v1/enterprises"
```

Executar:
```bash
npm install -g artillery
artillery run artillery-test.yml
```

### Monitoring de Performance

```bash
# Monitorar CPU/Memory durante testes
top -p $(pgrep java)

# Monitorar conexões database
sudo netstat -tnlp | grep 5432

# Logs de performance
tail -f ../logs/patrimonio_app.log | grep -E "(took|ms|timeout)"
```

## 📊 Relatórios de Teste

### Formato de Relatório CSV

```csv
Test Case ID,Test Case Description,Module,Test Category,Priority,Test Type,Original Endpoint,Actual Endpoint,Execution Time,Status,Response Code,Response Time (ms),Error Message,Notes
PROP-NAV-001,Access properties list page,Properties,Navigation,High,UI,/properties,/properties,2025-06-06 02:15:44,PASS,200,48,,Page loaded successfully
ENT-NAV-002,Access enterprise creation page,Enterprises,Navigation,High,UI,/enterprises/new,/enterprises/new,2025-06-06 02:15:49,FAIL,500,31,Server error (500),Application error - check logs
```

### Dashboard de Métricas

```python
def generate_test_dashboard():
    """Gera dashboard com métricas de teste"""
    
    metrics = {
        'total_tests': len(results),
        'passed': len([r for r in results if r['Status'] == 'PASS']),
        'failed': len([r for r in results if r['Status'] == 'FAIL']),
        'manual': len([r for r in results if r['Status'] == 'MANUAL']),
        'success_rate': (passed / total_tests) * 100,
        'avg_response_time': sum(response_times) / len(response_times)
    }
    
    return metrics
```

### Relatório Executivo

```markdown
## 📈 Relatório de Testes - v1.1-SNAPSHOT

### Resumo Executivo
- **Total de Testes**: 75
- **Taxa de Sucesso**: 84%
- **Cobertura**: API (96%), UI (89%), Integration (87%)
- **Performance**: Média 45ms response time

### Principais Achievements
✅ API endpoints 100% funcionais
✅ CRUD operations estáveis
✅ File upload otimizado
✅ Dashboard calculations precisos

### Issues Identificados
❌ Enterprise form creation (500 error)
❌ Some responsive layout issues
⚠️  Performance degradation under load

### Recomendações
1. Fix enterprise form template
2. Optimize database queries
3. Implement caching layer
4. Add authentication tests
```

## 🔄 CI/CD Integration

### GitHub Actions Workflow

```yaml
# .github/workflows/test.yml
name: Test Suite

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_DB: patrimonio_test
          POSTGRES_USER: test_user
          POSTGRES_PASSWORD: test_pass
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Cache Maven packages
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
        restore-keys: ${{ runner.os }}-m2
    
    - name: Run tests
      run: ./mvnw clean test
      env:
        SPRING_PROFILES_ACTIVE: test
        DATABASE_URL: jdbc:postgresql://localhost:5432/patrimonio_test
    
    - name: Generate test report
      run: ./mvnw surefire-report:report
    
    - name: Upload coverage reports
      uses: codecov/codecov-action@v3
```

### Pre-commit Hooks

```bash
#!/bin/sh
# .git/hooks/pre-commit

echo "Running pre-commit tests..."

# Unit tests
./mvnw test -Dtest="**/*Test"

if [ $? -ne 0 ]; then
    echo "❌ Unit tests failed"
    exit 1
fi

# Integration tests
./mvnw test -Dtest="**/*IntegrationTest"

if [ $? -ne 0 ]; then
    echo "❌ Integration tests failed"
    exit 1
fi

echo "✅ All tests passed"
```

### Quality Gates

```properties
# sonar-project.properties
sonar.projectKey=renovacampo-patrimonio
sonar.projectName=RenovaCampo Patrimônio
sonar.projectVersion=1.1-SNAPSHOT

# Coverage requirements
sonar.coverage.exclusions=**/test/**,**/config/**
sonar.java.coveragePlugin=jacoco
sonar.jacoco.reportPath=target/jacoco.exec

# Quality gates
sonar.qualitygate.wait=true
```

## 🎯 Roadmap de Testes

### v1.2.0 (Próxima Release)
- [ ] Authentication/Authorization tests
- [ ] Security penetration testing  
- [ ] Advanced performance testing
- [ ] Automated UI tests with Selenium
- [ ] Contract testing with Pact

### v1.3.0 (Futuro)
- [ ] Chaos engineering tests
- [ ] Mobile app testing
- [ ] API contract validation
- [ ] Multi-browser compatibility
- [ ] Accessibility testing (WCAG)

---

**Próximo passo**: [⚙️ Documentação de Configuração](CONFIGURACAO.md)
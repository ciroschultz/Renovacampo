# Renova Campo - Resumo do Projeto

## Visão Geral

O **Renova Campo** é uma plataforma digital que conecta **proprietários de terras ociosas** com **produtores rurais** e **investidores** comprometidos com práticas de **agricultura regenerativa e sustentável**.

---

## Proposta de Valor

> "Agricultura Regenerativa para um Futuro Sustentável"

A plataforma facilita o arrendamento de terras para produção agrícola sustentável, permitindo que investidores financiem projetos certificados e acompanhem seus retornos.

---

## Público-Alvo

### 1. Proprietários de Terra
- Cadastram propriedades com detalhes sobre localização, tamanho e características
- Recebem propostas de produtores interessados em práticas sustentáveis
- Formalizam arrendamento com assessoria jurídica e técnica

### 2. Produtores Rurais
- Encontram terras disponíveis para projetos de agricultura regenerativa
- Submetem projetos detalhados com práticas sustentáveis
- Recebem financiamento de investidores

### 3. Investidores
- Exploram projetos certificados no mural de projetos
- Investem através de sistema de cotas acessíveis
- Acompanham desenvolvimento e recebem retornos

---

## Métricas da Plataforma

| Indicador | Valor |
|-----------|-------|
| Hectares Arrendados | 1.250+ |
| Produtores Ativos | 350+ |
| Investimentos Realizados | R$ 15M+ |

---

## Funcionalidades Principais

### Terras Disponíveis
Listagem de propriedades rurais com informações detalhadas:
- Localização e tamanho (hectares)
- Preço por hectare/ano
- Infraestrutura (água, solo, acesso)
- Status de disponibilidade

**Exemplos de propriedades:**
| Propriedade | Localização | Área | Preço |
|-------------|-------------|------|-------|
| Fazenda Boa Esperança | Ribeirão Preto, SP | 120 ha | R$ 1.200/ha/ano |
| Sítio Recanto Verde | Bragança Paulista, SP | 45 ha | R$ 950/ha/ano |
| Fazenda Horizonte | Rio Verde, GO | 320 ha | R$ 1.500/ha/ano |

### Mural de Projetos
Projetos de agricultura sustentável disponíveis para investimento:

| Projeto | Investimento | Retorno Estimado | Captação |
|---------|--------------|------------------|----------|
| Soja Orgânica em Rotação | R$ 500.000 | 18% a.a. | 65% |
| Pomar Agroflorestal | R$ 280.000 | 15% a.a. | 42% |
| Pecuária Regenerativa | R$ 420.000 | 16% a.a. | 78% |

### Simulador de Investimentos
Ferramenta para calcular rentabilidade e lucro estimado:
- Seleção de cultura (cenoura, batata, mandioquinha, rúcula)
- Parâmetros: área de plantio, sistema de produção, irrigação
- Dados baseados em médias do IBGE e EMBRAPA

### Selo Renova Campo
Certificação que garante práticas de agricultura regenerativa:

- **Conservação do Solo** - Práticas regenerativas comprovadas
- **Gestão Responsável da Água** - Uso eficiente e proteção de nascentes
- **Impacto Social Positivo** - Condições justas de trabalho
- **Biodiversidade** - Diversidade de espécies no sistema produtivo

---

## Estrutura do Sistema

### Módulos do Backend

1. **Módulo Propriedades**
   - CRUD completo
   - Upload de fotos e documentos
   - Integração com mapas (coordenadas)
   - Filtros por tipo, estado, nome

2. **Módulo Projetos**
   - Gestão de status e prioridade
   - Acompanhamento financeiro (ROI)
   - Upload de documentos
   - Filtros por categoria e status

3. **Módulo Investidores**
   - Cadastro com CPF/CNPJ
   - Gestão de fundos (total, investido, disponível)
   - Ativação/desativação de perfil
   - Histórico de investimentos

4. **Módulo Empreendimentos**
   - Integração Propriedade + Projeto + Investidores
   - Cálculo automático de progresso de financiamento
   - Acompanhamento de ROI

5. **Dashboard**
   - Estatísticas gerais de todos os módulos
   - Métricas em tempo real

### API REST
Base URL: `http://localhost:8080`

Endpoints principais:
- `GET/POST /api/v1/property` - Propriedades
- `GET/POST /api/v1/projects` - Projetos
- `GET/POST /api/v1/investors` - Investidores
- `GET/POST /api/v1/enterprises` - Empreendimentos

---

## Fluxo de Cadastro

```
1. Perfil → 2. Dados → 3. Verificação → 4. Conclusão
```

### Formulário de Captação de Terras
1. **Identificação do Proprietário**
   - Nome completo, e-mail, telefone

2. **Informações da Propriedade**
   - Localização (município/estado)
   - Tamanho total (hectares)
   - Área disponível para arrendamento
   - Infraestrutura existente

3. **Escolha do Modelo**
   - Financiamento da Safra
   - Empresa Compartilhada

4. **Considerações Finais**
   - Observações adicionais
   - Autorização de contato

---

## Identidade Visual

### Cores
- **Verde Principal:** `#004F3B`
- **Laranja:** `#E95E1D`
- **Fundo:** `#f8f8f5`

### Tipografia
- Fonte: Poppins

---

## Contato

- **Endereço:** Av. Paulista, 1000, São Paulo - SP
- **E-mail:** contato@renovacampo.com.br
- **Telefone:** (11) 3456-7890

---

## Documentação Técnica

### Arquivos do Projeto

```
Renova Campo/
├── NOVO NEÇOCIO/
│   ├── PLANO_TESTES_MANUAL RENOVA CAMPO.txt
│   ├── Plano de custos Renova Campo (6 meses).pdf
│   ├── Produto e perfil usuários Renova Campo.pptx
│   ├── Catalogo_Renova_Campo_Modelo_MVP.pdf
│   ├── Plano de Tarefas e Responsabilidades.pdf
│   └── Projetos/
│       ├── Projeto_Agricultura_Regenerativa_Sitio_Luz_da_Lua.pdf
│       ├── Projeto_Agricultura_Regenerativa_Sitio_Tamandua.pdf
│       └── Projeto de Agricultura Regenerativa - CHAPADA GUARA.pdf
├── formualrios/
│   ├── Renova_campo_formulario_final.html
│   └── Finais/
└── REUNIÃO/

RENOVACAMPO_DSM_28022025/
├── Web_Renovacampo_onepage.pdf
├── Web_Renovacampo_Cadastro-principal.pdf
├── Web_Renovacampo_Cadastro-Propriedade.pdf
├── Web_Renovacampo_Cadastro-Projeto.pdf
├── Web_Renovacampo_Simulador.pdf
├── Web_Renovacampo_Projeto-Interesse.pdf
└── Web_Renovacampo_parcerias.pdf
```

### Plano de Testes
O sistema possui plano de testes manual completo cobrindo:
- Testes de navegação e UI
- Testes CRUD (Create, Read, Update, Delete)
- Testes de validação de dados
- Testes de upload de arquivos
- Testes de API REST
- Testes de segurança (SQL Injection, XSS)
- Testes de performance
- Testes de integração entre módulos

---

## Depoimentos

> "Minha fazenda estava improdutiva há anos. Com a Renova Campo, encontrei um produtor que implementou um sistema agroflorestal que está recuperando o solo e gerando renda."
> — **Marcos Santos**, Proprietário de Terra

> "Como produtora orgânica, sempre tive dificuldade em encontrar terras adequadas. A plataforma me conectou com um proprietário que compartilha dos mesmos valores de sustentabilidade."
> — **Ana Rodrigues**, Produtora Rural

> "Com o Renova Campo consigo investir meu dinheiro de uma maneira segura, sem precisar saber sobre cultivo ou agricultura. E ainda ajudo minha sociedade com um projeto relevante."
> — **Daniel Duarte**, Investidor

---

*© 2025 Renova Campo. Todos os direitos reservados.*

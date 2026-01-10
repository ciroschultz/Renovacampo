# Plano de Testes Manual - RenovaCampo

## Sumário
- [Visão Geral](#visão-geral)
- [Módulo Propriedades](#módulo-propriedades)
- [Módulo Projetos](#módulo-projetos)
- [Módulo Investidores](#módulo-investidores)
- [Módulo Empreendimentos](#módulo-empreendimentos)
- [Dashboard](#dashboard)
- [Testes de Sistema](#testes-de-sistema)
- [Testes de Integração](#testes-de-integração)

## Visão Geral

Este documento contém todos os casos de teste manual para o sistema RenovaCampo. Cada teste inclui:
- **ID**: Identificador único do teste
- **Categoria**: Tipo de funcionalidade testada
- **Prioridade**: Alta, Média ou Baixa
- **Tipo**: UI, Funcional, Validação, API, etc.
- **Passos**: Instruções detalhadas
- **Resultado Esperado**: O que deve acontecer
- **Pré-requisitos**: Condições necessárias

---

## Módulo Propriedades

### Navegação

#### PROP-NAV-001: Acessar Lista de Propriedades
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Navegar para http://localhost:8080/properties
- **Resultado Esperado**: Página de propriedades carrega com lista de todas as propriedades
- **Pré-requisitos**: Sistema rodando

#### PROP-NAV-002: Acessar Página de Criação de Propriedade
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Clicar no botão 'Nova Propriedade'
- **Resultado Esperado**: Formulário de criação de propriedade carrega corretamente
- **Pré-requisitos**: Página de propriedades carregada

#### PROP-NAV-003: Acessar Detalhes da Propriedade
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Clicar em qualquer linha de propriedade
- **Resultado Esperado**: Página de detalhes carrega com todas as informações
- **Pré-requisitos**: Propriedade existe

#### PROP-NAV-004: Acessar Página de Edição de Propriedade
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Nos detalhes da propriedade, clicar em 'Editar'
- **Resultado Esperado**: Formulário de edição carrega com dados atuais
- **Pré-requisitos**: Propriedade existe

### CRUD

#### PROP-CRUD-001: Criar Nova Propriedade com Todos os Campos
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Preencher todos os campos obrigatórios
  2. Submeter formulário
- **Resultado Esperado**: Propriedade criada com sucesso e aparece na lista
- **Pré-requisitos**: Nenhum

#### PROP-CRUD-002: Criar Propriedade com Campos Mínimos
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Preencher apenas campos obrigatórios
  2. Submeter
- **Resultado Esperado**: Propriedade criada apenas com dados obrigatórios
- **Pré-requisitos**: Nenhum

#### PROP-CRUD-003: Editar Propriedade Existente
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Abrir edição de propriedade
  2. Modificar campos
  3. Salvar
- **Resultado Esperado**: Propriedade atualizada com novas informações
- **Pré-requisitos**: Propriedade existe

#### PROP-CRUD-004: Deletar Propriedade
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Acessar propriedade
  2. Clicar em deletar
  3. Confirmar
- **Resultado Esperado**: Propriedade removida do sistema
- **Pré-requisitos**: Propriedade existe

### Validação

#### PROP-VAL-001: Submeter Formulário com Campos Obrigatórios Vazios
- **Categoria**: Validação
- **Prioridade**: Alta
- **Tipo**: Validação
- **Passos**: 
  1. Deixar campos obrigatórios vazios
  2. Submeter
- **Resultado Esperado**: Erros de validação exibidos
- **Pré-requisitos**: Nenhum

#### PROP-VAL-002: Inserir Valores de Área Inválidos
- **Categoria**: Validação
- **Prioridade**: Média
- **Tipo**: Validação
- **Passos**: 
  1. Inserir números negativos nos campos de área
- **Resultado Esperado**: Sistema rejeita valores inválidos
- **Pré-requisitos**: Nenhum

#### PROP-VAL-003: Inserir Coordenadas Inválidas
- **Categoria**: Validação
- **Prioridade**: Média
- **Tipo**: Validação
- **Passos**: 
  1. Inserir valores de latitude/longitude fora do intervalo
- **Resultado Esperado**: Sistema valida intervalos de coordenadas
- **Pré-requisitos**: Nenhum

### Busca e Filtros

#### PROP-SEARCH-001: Buscar por Nome de Propriedade
- **Categoria**: Busca
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Inserir nome da propriedade na caixa de busca
- **Resultado Esperado**: Propriedades correspondentes filtradas em tempo real
- **Pré-requisitos**: Propriedades existem

#### PROP-SEARCH-002: Filtrar por Tipo de Propriedade
- **Categoria**: Busca
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Selecionar filtro de tipo
- **Resultado Esperado**: Propriedades filtradas pelo tipo selecionado
- **Pré-requisitos**: Propriedades existem

#### PROP-SEARCH-003: Filtrar por Estado
- **Categoria**: Busca
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Selecionar filtro de estado
- **Resultado Esperado**: Propriedades filtradas pelo estado selecionado
- **Pré-requisitos**: Propriedades existem

#### PROP-SEARCH-004: Limpar Todos os Filtros
- **Categoria**: Busca
- **Prioridade**: Baixa
- **Tipo**: Funcional
- **Passos**: 
  1. Aplicar filtros
  2. Limpar filtros
- **Resultado Esperado**: Todas as propriedades exibidas novamente
- **Pré-requisitos**: Filtros aplicados

### Upload de Arquivos

#### PROP-FILE-001: Upload de Foto da Propriedade
- **Categoria**: Upload de Arquivos
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Acessar edição de propriedade
  2. Fazer upload de imagem válida
- **Resultado Esperado**: Foto carregada e miniatura exibida
- **Pré-requisitos**: Propriedade existe

#### PROP-FILE-002: Upload de Documento da Propriedade
- **Categoria**: Upload de Arquivos
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Acessar edição de propriedade
  2. Fazer upload de documento válido
- **Resultado Esperado**: Documento carregado e listado
- **Pré-requisitos**: Propriedade existe

#### PROP-FILE-003: Upload de Tipo de Arquivo Inválido
- **Categoria**: Upload de Arquivos
- **Prioridade**: Média
- **Tipo**: Validação
- **Passos**: 
  1. Tentar fazer upload de tipo de arquivo não suportado
- **Resultado Esperado**: Sistema rejeita upload com mensagem de erro
- **Pré-requisitos**: Propriedade existe

#### PROP-FILE-004: Upload de Arquivo Muito Grande
- **Categoria**: Upload de Arquivos
- **Prioridade**: Média
- **Tipo**: Validação
- **Passos**: 
  1. Tentar fazer upload de arquivo excedendo limite de tamanho
- **Resultado Esperado**: Sistema rejeita com erro de tamanho
- **Pré-requisitos**: Propriedade existe

#### PROP-FILE-005: Deletar Arquivo Carregado
- **Categoria**: Upload de Arquivos
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Fazer upload de arquivo
  2. Deletar arquivo
- **Resultado Esperado**: Arquivo removido do sistema
- **Pré-requisitos**: Arquivo carregado

### Mapas

#### PROP-MAP-001: Exibir Propriedade no Mapa
- **Categoria**: Mapas
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Ver propriedade com coordenadas
- **Resultado Esperado**: Mapa interativo exibe com marcador da propriedade
- **Pré-requisitos**: Propriedade com coordenadas

#### PROP-MAP-002: Interação com Mapa
- **Categoria**: Mapas
- **Prioridade**: Baixa
- **Tipo**: Funcional
- **Passos**: 
  1. Zoom e arrastar mapa
  2. Clicar no marcador
- **Resultado Esperado**: Mapa responde às interações e mostra popup
- **Pré-requisitos**: Propriedade com coordenadas

### API

#### PROP-API-001: Listar Propriedades via API REST
- **Categoria**: API
- **Prioridade**: Alta
- **Tipo**: API
- **Passos**: 
  1. GET /api/v1/property
- **Resultado Esperado**: Retorna lista JSON de todas as propriedades
- **Pré-requisitos**: Nenhum

#### PROP-API-002: Obter Propriedade por ID via API
- **Categoria**: API
- **Prioridade**: Alta
- **Tipo**: API
- **Passos**: 
  1. GET /api/v1/property/{id}
- **Resultado Esperado**: Retorna dados específicos da propriedade
- **Pré-requisitos**: Propriedade existe

#### PROP-API-003: Criar Propriedade via API
- **Categoria**: API
- **Prioridade**: Alta
- **Tipo**: API
- **Passos**: 
  1. POST dados da propriedade para API
- **Resultado Esperado**: Propriedade criada e retorna ID
- **Pré-requisitos**: Nenhum

#### PROP-API-004: Deletar Propriedade via API
- **Categoria**: API
- **Prioridade**: Média
- **Tipo**: API
- **Passos**: 
  1. DELETE /api/v1/property/{id}
- **Resultado Esperado**: Propriedade deletada com sucesso
- **Pré-requisitos**: Propriedade existe

---

## Módulo Projetos

### Navegação

#### PROJ-NAV-001: Acessar Lista de Projetos
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Navegar para /projects
- **Resultado Esperado**: Página de projetos carrega com lista de todos os projetos
- **Pré-requisitos**: Sistema rodando

#### PROJ-NAV-002: Acessar Página de Criação de Projeto
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Clicar no botão 'Novo Projeto'
- **Resultado Esperado**: Formulário de criação de projeto carrega corretamente
- **Pré-requisitos**: Página de projetos carregada

#### PROJ-NAV-003: Acessar Detalhes do Projeto
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Clicar em qualquer linha de projeto
- **Resultado Esperado**: Página de detalhes carrega com todas as informações
- **Pré-requisitos**: Projeto existe

#### PROJ-NAV-004: Acessar Página de Edição de Projeto
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Nos detalhes do projeto, clicar em 'Editar'
- **Resultado Esperado**: Formulário de edição carrega com dados atuais
- **Pré-requisitos**: Projeto existe

### CRUD

#### PROJ-CRUD-001: Criar Novo Projeto com Todos os Campos
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Preencher todos os campos do projeto
  2. Submeter formulário
- **Resultado Esperado**: Projeto criado com sucesso e aparece na lista
- **Pré-requisitos**: Nenhum

#### PROJ-CRUD-002: Criar Projeto com Campos Mínimos
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Preencher apenas campos obrigatórios
  2. Submeter
- **Resultado Esperado**: Projeto criado apenas com dados obrigatórios
- **Pré-requisitos**: Nenhum

#### PROJ-CRUD-003: Editar Projeto Existente
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Abrir edição de projeto
  2. Modificar campos
  3. Salvar
- **Resultado Esperado**: Projeto atualizado com novas informações
- **Pré-requisitos**: Projeto existe

#### PROJ-CRUD-004: Deletar Projeto
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Acessar projeto
  2. Clicar em deletar
  3. Confirmar
- **Resultado Esperado**: Projeto removido do sistema
- **Pré-requisitos**: Projeto existe

### Validação

#### PROJ-VAL-001: Submeter Formulário com Campos Obrigatórios Vazios
- **Categoria**: Validação
- **Prioridade**: Alta
- **Tipo**: Validação
- **Passos**: 
  1. Deixar campos obrigatórios vazios
  2. Submeter
- **Resultado Esperado**: Erros de validação exibidos
- **Pré-requisitos**: Nenhum

#### PROJ-VAL-002: Inserir Intervalos de Data Inválidos
- **Categoria**: Validação
- **Prioridade**: Alta
- **Tipo**: Validação
- **Passos**: 
  1. Definir data de término antes da data de início
- **Resultado Esperado**: Sistema valida lógica de datas
- **Pré-requisitos**: Nenhum

#### PROJ-VAL-003: Inserir Valores Financeiros Negativos
- **Categoria**: Validação
- **Prioridade**: Média
- **Tipo**: Validação
- **Passos**: 
  1. Inserir valores negativos de custos/investimento
- **Resultado Esperado**: Sistema rejeita valores negativos
- **Pré-requisitos**: Nenhum

### Gestão de Status

#### PROJ-STATUS-001: Alterar Status do Projeto
- **Categoria**: Gestão de Status
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Editar projeto
  2. Alterar status
  3. Salvar
- **Resultado Esperado**: Status atualizado e refletido no sistema
- **Pré-requisitos**: Projeto existe

#### PROJ-STATUS-002: Filtrar por Status do Projeto
- **Categoria**: Gestão de Status
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Aplicar filtro de status na lista de projetos
- **Resultado Esperado**: Projetos filtrados pelo status selecionado
- **Pré-requisitos**: Projetos existem

### Gestão de Prioridade

#### PROJ-PRIOR-001: Definir Prioridade do Projeto
- **Categoria**: Gestão de Prioridade
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Editar projeto
  2. Definir prioridade
  3. Salvar
- **Resultado Esperado**: Prioridade atualizada e exibida corretamente
- **Pré-requisitos**: Projeto existe

#### PROJ-PRIOR-002: Filtrar por Prioridade do Projeto
- **Categoria**: Gestão de Prioridade
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Aplicar filtro de prioridade na lista de projetos
- **Resultado Esperado**: Projetos filtrados pela prioridade selecionada
- **Pré-requisitos**: Projetos existem

### Acompanhamento Financeiro

#### PROJ-FIN-001: Calcular ROI Automaticamente
- **Categoria**: Acompanhamento Financeiro
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Inserir valores de custos e investimento
- **Resultado Esperado**: Sistema calcula porcentagem de ROI automaticamente
- **Pré-requisitos**: Nenhum

#### PROJ-FIN-002: Acompanhar Despesas do Projeto
- **Categoria**: Acompanhamento Financeiro
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Atualizar custos totais
  2. Salvar
- **Resultado Esperado**: Acompanhamento de custos atualizado no projeto
- **Pré-requisitos**: Projeto existe

### Gestão de Arquivos

#### PROJ-FILE-001: Upload de Documentos do Projeto
- **Categoria**: Gestão de Arquivos
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Acessar edição de projeto
  2. Fazer upload de documentos
- **Resultado Esperado**: Documentos carregados e listados para o projeto
- **Pré-requisitos**: Projeto existe

#### PROJ-FILE-002: Deletar Documentos do Projeto
- **Categoria**: Gestão de Arquivos
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Fazer upload de documento
  2. Deletar documento
- **Resultado Esperado**: Documento removido do projeto
- **Pré-requisitos**: Documento carregado

### Busca

#### PROJ-SEARCH-001: Buscar Projetos por Nome
- **Categoria**: Busca
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Inserir nome do projeto na busca
- **Resultado Esperado**: Projetos correspondentes filtrados em tempo real
- **Pré-requisitos**: Projetos existem

#### PROJ-SEARCH-002: Buscar Projetos por Categoria
- **Categoria**: Busca
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Filtrar por categoria do projeto
- **Resultado Esperado**: Projetos filtrados pela categoria selecionada
- **Pré-requisitos**: Projetos existem

---

## Módulo Investidores

### Navegação

#### INV-NAV-001: Acessar Lista de Investidores
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Navegar para /investors
- **Resultado Esperado**: Página de investidores carrega com lista de todos os investidores
- **Pré-requisitos**: Sistema rodando

#### INV-NAV-002: Acessar Página de Criação de Investidor
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Clicar no botão 'Novo Investidor'
- **Resultado Esperado**: Formulário de criação de investidor carrega corretamente
- **Pré-requisitos**: Página de investidores carregada

#### INV-NAV-003: Acessar Detalhes do Investidor
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Clicar em qualquer linha de investidor
- **Resultado Esperado**: Página de detalhes carrega com todas as informações
- **Pré-requisitos**: Investidor existe

#### INV-NAV-004: Acessar Página de Edição de Investidor
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Nos detalhes do investidor, clicar em 'Editar'
- **Resultado Esperado**: Formulário de edição carrega com dados atuais
- **Pré-requisitos**: Investidor existe

### CRUD

#### INV-CRUD-001: Criar Novo Investidor com Todos os Campos
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Preencher todos os campos do investidor
  2. Submeter formulário
- **Resultado Esperado**: Investidor criado com sucesso e aparece na lista
- **Pré-requisitos**: Nenhum

#### INV-CRUD-002: Criar Investidor com Campos Mínimos
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Preencher apenas campos obrigatórios
  2. Submeter
- **Resultado Esperado**: Investidor criado apenas com dados obrigatórios
- **Pré-requisitos**: Nenhum

#### INV-CRUD-003: Editar Investidor Existente
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Abrir edição de investidor
  2. Modificar campos
  3. Salvar
- **Resultado Esperado**: Investidor atualizado com novas informações
- **Pré-requisitos**: Investidor existe

#### INV-CRUD-004: Deletar Investidor
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Acessar investidor
  2. Clicar em deletar
  3. Confirmar
- **Resultado Esperado**: Investidor removido do sistema
- **Pré-requisitos**: Investidor existe

### Validação

#### INV-VAL-001: Submeter Formulário com Campos Obrigatórios Vazios
- **Categoria**: Validação
- **Prioridade**: Alta
- **Tipo**: Validação
- **Passos**: 
  1. Deixar campos obrigatórios vazios
  2. Submeter
- **Resultado Esperado**: Erros de validação exibidos
- **Pré-requisitos**: Nenhum

#### INV-VAL-002: Inserir Formato de CPF/CNPJ Inválido
- **Categoria**: Validação
- **Prioridade**: Alta
- **Tipo**: Validação
- **Passos**: 
  1. Inserir CPF/CNPJ mal formatado
- **Resultado Esperado**: Sistema valida formato do CPF/CNPJ
- **Pré-requisitos**: Nenhum

#### INV-VAL-003: Inserir Formato de Email Inválido
- **Categoria**: Validação
- **Prioridade**: Média
- **Tipo**: Validação
- **Passos**: 
  1. Inserir endereço de email mal formatado
- **Resultado Esperado**: Sistema valida formato do email
- **Pré-requisitos**: Nenhum

#### INV-VAL-004: Inserir Valores Financeiros Negativos
- **Categoria**: Validação
- **Prioridade**: Alta
- **Tipo**: Validação
- **Passos**: 
  1. Inserir valores negativos de fundos
- **Resultado Esperado**: Sistema rejeita valores negativos
- **Pré-requisitos**: Nenhum

### Gestão Financeira

#### INV-FIN-001: Calcular Fundos Disponíveis Automaticamente
- **Categoria**: Gestão Financeira
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Inserir fundos totais e investidos
- **Resultado Esperado**: Sistema calcula fundos disponíveis automaticamente
- **Pré-requisitos**: Nenhum

#### INV-FIN-002: Ver Barra de Progresso de Utilização de Fundos
- **Categoria**: Gestão Financeira
- **Prioridade**: Média
- **Tipo**: UI
- **Passos**: 
  1. Acessar detalhes do investidor
- **Resultado Esperado**: Barra de progresso mostra porcentagem de utilização de fundos
- **Pré-requisitos**: Investidor com fundos

#### INV-FIN-003: Atualizar Valores de Fundos do Investidor
- **Categoria**: Gestão Financeira
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Editar investidor
  2. Alterar valores de fundos
  3. Salvar
- **Resultado Esperado**: Valores de fundos atualizados e cálculos renovados
- **Pré-requisitos**: Investidor existe

### Gestão de Status

#### INV-STATUS-001: Ativar/Desativar Investidor
- **Categoria**: Gestão de Status
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Alterar status ativo do investidor
  2. Salvar
- **Resultado Esperado**: Status atualizado e refletido no sistema
- **Pré-requisitos**: Investidor existe

#### INV-STATUS-002: Filtrar por Status do Investidor
- **Categoria**: Gestão de Status
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Aplicar filtro ativo/inativo
- **Resultado Esperado**: Investidores filtrados pelo status selecionado
- **Pré-requisitos**: Investidores existem

### Gestão de Contatos

#### INV-CONTACT-001: Validar Informações de Contato
- **Categoria**: Gestão de Contatos
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Inserir detalhes de telefone e endereço
- **Resultado Esperado**: Informações de contato salvas corretamente
- **Pré-requisitos**: Nenhum

#### INV-CONTACT-002: Exibir Detalhes de Contato
- **Categoria**: Gestão de Contatos
- **Prioridade**: Média
- **Tipo**: UI
- **Passos**: 
  1. Ver página de detalhes do investidor
- **Resultado Esperado**: Todas as informações de contato exibidas adequadamente
- **Pré-requisitos**: Investidor existe

### Busca

#### INV-SEARCH-001: Buscar Investidores por Nome
- **Categoria**: Busca
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Inserir nome do investidor na busca
- **Resultado Esperado**: Investidores correspondentes filtrados em tempo real
- **Pré-requisitos**: Investidores existem

#### INV-SEARCH-002: Buscar Investidores por CPF/CNPJ
- **Categoria**: Busca
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Inserir CPF/CNPJ na busca
- **Resultado Esperado**: Investidor correspondente encontrado pelo CPF/CNPJ
- **Pré-requisitos**: Investidores existem

#### INV-SEARCH-003: Filtrar por Localização do Investidor
- **Categoria**: Busca
- **Prioridade**: Baixa
- **Tipo**: Funcional
- **Passos**: 
  1. Filtrar por cidade ou estado
- **Resultado Esperado**: Investidores filtrados por localização
- **Pré-requisitos**: Investidores existem

### API

#### INV-API-001: Listar Investidores via API REST
- **Categoria**: API
- **Prioridade**: Alta
- **Tipo**: API
- **Passos**: 
  1. GET /api/v1/investors
- **Resultado Esperado**: Retorna lista JSON de todos os investidores
- **Pré-requisitos**: Nenhum

#### INV-API-002: Obter Investidor por ID via API
- **Categoria**: API
- **Prioridade**: Alta
- **Tipo**: API
- **Passos**: 
  1. GET /api/v1/investors/{id}
- **Resultado Esperado**: Retorna dados específicos do investidor
- **Pré-requisitos**: Investidor existe

#### INV-API-003: Obter Investidor por CPF/CNPJ via API
- **Categoria**: API
- **Prioridade**: Alta
- **Tipo**: API
- **Passos**: 
  1. GET /api/v1/investors/tax-id/{taxId}
- **Resultado Esperado**: Retorna investidor pelo CPF/CNPJ
- **Pré-requisitos**: Investidor existe

#### INV-API-004: Criar Investidor via API
- **Categoria**: API
- **Prioridade**: Alta
- **Tipo**: API
- **Passos**: 
  1. POST dados do investidor para API
- **Resultado Esperado**: Investidor criado e retorna ID
- **Pré-requisitos**: Nenhum

#### INV-API-005: Atualizar Investidor via API
- **Categoria**: API
- **Prioridade**: Alta
- **Tipo**: API
- **Passos**: 
  1. PUT dados atualizados para API
- **Resultado Esperado**: Dados do investidor atualizados com sucesso
- **Pré-requisitos**: Investidor existe

#### INV-API-006: Deletar Investidor via API
- **Categoria**: API
- **Prioridade**: Média
- **Tipo**: API
- **Passos**: 
  1. DELETE /api/v1/investors/{id}
- **Resultado Esperado**: Investidor deletado com sucesso
- **Pré-requisitos**: Investidor existe

#### INV-API-007: Ativar/Desativar via API
- **Categoria**: API
- **Prioridade**: Média
- **Tipo**: API
- **Passos**: 
  1. PATCH /api/v1/investors/{id}/activate
- **Resultado Esperado**: Status do investidor alternado com sucesso
- **Pré-requisitos**: Investidor existe

---

## Módulo Empreendimentos

### Navegação

#### ENT-NAV-001: Acessar Lista de Empreendimentos
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Navegar para /enterprises
- **Resultado Esperado**: Página de empreendimentos carrega com lista de todos os empreendimentos
- **Pré-requisitos**: Sistema rodando

#### ENT-NAV-002: Acessar Página de Criação de Empreendimento
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Clicar no botão 'Novo Empreendimento'
- **Resultado Esperado**: Formulário de criação de empreendimento carrega corretamente
- **Pré-requisitos**: Página de empreendimentos carregada

#### ENT-NAV-003: Acessar Detalhes do Empreendimento
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Clicar em qualquer linha de empreendimento
- **Resultado Esperado**: Página de detalhes carrega com todas as informações
- **Pré-requisitos**: Empreendimento existe

#### ENT-NAV-004: Acessar Página de Edição de Empreendimento
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Nos detalhes do empreendimento, clicar em 'Editar'
- **Resultado Esperado**: Formulário de edição carrega com dados atuais
- **Pré-requisitos**: Empreendimento existe

### CRUD

#### ENT-CRUD-001: Criar Novo Empreendimento com Todos os Campos
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Preencher todos os campos do empreendimento
  2. Selecionar propriedade e projeto
  3. Submeter
- **Resultado Esperado**: Empreendimento criado com sucesso e aparece na lista
- **Pré-requisitos**: Propriedade e projeto existem

#### ENT-CRUD-002: Criar Empreendimento com Campos Mínimos
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Preencher apenas campos obrigatórios
  2. Submeter
- **Resultado Esperado**: Empreendimento criado apenas com dados obrigatórios
- **Pré-requisitos**: Propriedade e projeto existem

#### ENT-CRUD-003: Editar Empreendimento Existente
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Abrir edição de empreendimento
  2. Modificar campos
  3. Salvar
- **Resultado Esperado**: Empreendimento atualizado com novas informações
- **Pré-requisitos**: Empreendimento existe

#### ENT-CRUD-004: Deletar Empreendimento
- **Categoria**: CRUD
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Acessar empreendimento
  2. Clicar em deletar
  3. Confirmar
- **Resultado Esperado**: Empreendimento removido do sistema
- **Pré-requisitos**: Empreendimento existe

### Validação

#### ENT-VAL-001: Submeter Formulário com Campos Obrigatórios Vazios
- **Categoria**: Validação
- **Prioridade**: Alta
- **Tipo**: Validação
- **Passos**: 
  1. Deixar campos obrigatórios vazios
  2. Submeter
- **Resultado Esperado**: Erros de validação exibidos
- **Pré-requisitos**: Nenhum

#### ENT-VAL-002: Selecionar Propriedade ou Projeto Inexistente
- **Categoria**: Validação
- **Prioridade**: Alta
- **Tipo**: Validação
- **Passos**: 
  1. Tentar criar empreendimento com referências inválidas
- **Resultado Esperado**: Sistema valida existência de propriedade e projeto
- **Pré-requisitos**: Nenhum

#### ENT-VAL-003: Inserir Intervalos de Data Inválidos
- **Categoria**: Validação
- **Prioridade**: Média
- **Tipo**: Validação
- **Passos**: 
  1. Definir data de conclusão antes da data de lançamento
- **Resultado Esperado**: Sistema valida lógica de datas
- **Pré-requisitos**: Nenhum

#### ENT-VAL-004: Inserir Valores Financeiros Negativos
- **Categoria**: Validação
- **Prioridade**: Alta
- **Tipo**: Validação
- **Passos**: 
  1. Inserir valores negativos de investimento
- **Resultado Esperado**: Sistema rejeita valores negativos
- **Pré-requisitos**: Nenhum

### Integração com Propriedades

#### ENT-PROP-001: Vincular Empreendimento à Propriedade
- **Categoria**: Integração com Propriedades
- **Prioridade**: Alta
- **Tipo**: Integração
- **Passos**: 
  1. Selecionar propriedade durante criação do empreendimento
- **Resultado Esperado**: Empreendimento corretamente vinculado à propriedade selecionada
- **Pré-requisitos**: Propriedade existe

#### ENT-PROP-002: Ver Detalhes da Propriedade do Empreendimento
- **Categoria**: Integração com Propriedades
- **Prioridade**: Média
- **Tipo**: Integração
- **Passos**: 
  1. Acessar detalhes do empreendimento
  2. Ver propriedade vinculada
- **Resultado Esperado**: Informações da propriedade exibidas dentro da visualização do empreendimento
- **Pré-requisitos**: Empreendimento com propriedade

#### ENT-PROP-003: Alterar Propriedade Vinculada
- **Categoria**: Integração com Propriedades
- **Prioridade**: Média
- **Tipo**: Integração
- **Passos**: 
  1. Editar empreendimento
  2. Alterar seleção de propriedade
  3. Salvar
- **Resultado Esperado**: Empreendimento vinculado à nova propriedade com sucesso
- **Pré-requisitos**: Empreendimento e múltiplas propriedades existem

### Integração com Projetos

#### ENT-PROJ-001: Vincular Empreendimento ao Projeto
- **Categoria**: Integração com Projetos
- **Prioridade**: Alta
- **Tipo**: Integração
- **Passos**: 
  1. Selecionar projeto durante criação do empreendimento
- **Resultado Esperado**: Empreendimento corretamente vinculado ao projeto selecionado
- **Pré-requisitos**: Projeto existe

#### ENT-PROJ-002: Ver Detalhes do Projeto do Empreendimento
- **Categoria**: Integração com Projetos
- **Prioridade**: Média
- **Tipo**: Integração
- **Passos**: 
  1. Acessar detalhes do empreendimento
  2. Ver projeto vinculado
- **Resultado Esperado**: Informações do projeto exibidas dentro da visualização do empreendimento
- **Pré-requisitos**: Empreendimento com projeto

#### ENT-PROJ-003: Alterar Projeto Vinculado
- **Categoria**: Integração com Projetos
- **Prioridade**: Média
- **Tipo**: Integração
- **Passos**: 
  1. Editar empreendimento
  2. Alterar seleção de projeto
  3. Salvar
- **Resultado Esperado**: Empreendimento vinculado ao novo projeto com sucesso
- **Pré-requisitos**: Empreendimento e múltiplos projetos existem

### Gestão de Investidores

#### ENT-INV-001: Adicionar Investidor ao Empreendimento
- **Categoria**: Gestão de Investidores
- **Prioridade**: Alta
- **Tipo**: Integração
- **Passos**: 
  1. Acessar empreendimento
  2. Adicionar investidor com valor de investimento
- **Resultado Esperado**: Investidor adicionado ao empreendimento com investimento especificado
- **Pré-requisitos**: Empreendimento e investidor existem

#### ENT-INV-002: Remover Investidor do Empreendimento
- **Categoria**: Gestão de Investidores
- **Prioridade**: Média
- **Tipo**: Integração
- **Passos**: 
  1. Acessar empreendimento
  2. Remover investidor existente
- **Resultado Esperado**: Investidor removido do empreendimento com sucesso
- **Pré-requisitos**: Empreendimento com investidor

#### ENT-INV-003: Atualizar Valor de Investimento do Investidor
- **Categoria**: Gestão de Investidores
- **Prioridade**: Alta
- **Tipo**: Integração
- **Passos**: 
  1. Editar participação do investidor
  2. Alterar valor
  3. Salvar
- **Resultado Esperado**: Valor de investimento atualizado e totais recalculados
- **Pré-requisitos**: Empreendimento com investidor

### Acompanhamento Financeiro

#### ENT-FIN-001: Calcular Progresso de Financiamento Automaticamente
- **Categoria**: Acompanhamento Financeiro
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Adicionar investimentos ao empreendimento
- **Resultado Esperado**: Sistema calcula porcentagem de progresso de financiamento
- **Pré-requisitos**: Empreendimento com investimentos

#### ENT-FIN-002: Ver Dashboard de Financiamento
- **Categoria**: Acompanhamento Financeiro
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Acessar detalhes do empreendimento
- **Resultado Esperado**: Métricas de financiamento e progresso exibidos corretamente
- **Pré-requisitos**: Empreendimento com investimentos

#### ENT-FIN-003: Acompanhar Cálculos de ROI
- **Categoria**: Acompanhamento Financeiro
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Ver métricas financeiras do empreendimento
- **Resultado Esperado**: Porcentagem de retorno esperado calculada e exibida
- **Pré-requisitos**: Empreendimento com dados financeiros

### Gestão de Status

#### ENT-STATUS-001: Alterar Status do Empreendimento
- **Categoria**: Gestão de Status
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Editar empreendimento
  2. Alterar status
  3. Salvar
- **Resultado Esperado**: Status atualizado e refletido em todo o sistema
- **Pré-requisitos**: Empreendimento existe

#### ENT-STATUS-002: Filtrar por Status do Empreendimento
- **Categoria**: Gestão de Status
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Aplicar filtro de status na lista de empreendimentos
- **Resultado Esperado**: Empreendimentos filtrados pelo status selecionado
- **Pré-requisitos**: Empreendimentos existem

#### ENT-STATUS-003: Ver Ações Específicas por Status
- **Categoria**: Gestão de Status
- **Prioridade**: Média
- **Tipo**: UI
- **Passos**: 
  1. Ver empreendimentos com diferentes status
- **Resultado Esperado**: Ações apropriadas disponíveis baseadas no status
- **Pré-requisitos**: Empreendimentos com diferentes status

### Busca

#### ENT-SEARCH-001: Buscar Empreendimentos por Nome
- **Categoria**: Busca
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Inserir nome do empreendimento na busca
- **Resultado Esperado**: Empreendimentos correspondentes filtrados em tempo real
- **Pré-requisitos**: Empreendimentos existem

#### ENT-SEARCH-002: Filtrar por Intervalo de Data de Lançamento
- **Categoria**: Busca
- **Prioridade**: Baixa
- **Tipo**: Funcional
- **Passos**: 
  1. Aplicar filtro de intervalo de datas
- **Resultado Esperado**: Empreendimentos filtrados pelo período de lançamento
- **Pré-requisitos**: Empreendimentos existem

#### ENT-SEARCH-003: Filtrar por Status de Financiamento
- **Categoria**: Busca
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Filtrar por nível de conclusão de financiamento
- **Resultado Esperado**: Empreendimentos filtrados pelo progresso de financiamento
- **Pré-requisitos**: Empreendimentos existem

---

## Dashboard

### Visão Geral

#### DASH-OVER-001: Ver Dashboard Principal
- **Categoria**: Visão Geral
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Navegar para URL raiz
- **Resultado Esperado**: Dashboard carrega com estatísticas gerais
- **Pré-requisitos**: Sistema rodando com dados

#### DASH-OVER-002: Ver Estatísticas de Propriedades
- **Categoria**: Visão Geral
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Verificar seção de propriedades no dashboard
- **Resultado Esperado**: Contagem de propriedades e totais de área exibidos corretamente
- **Pré-requisitos**: Propriedades existem

#### DASH-OVER-003: Ver Estatísticas de Projetos
- **Categoria**: Visão Geral
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Verificar seção de projetos no dashboard
- **Resultado Esperado**: Contagem de projetos e divisão por status exibidos
- **Pré-requisitos**: Projetos existem

#### DASH-OVER-004: Ver Estatísticas de Investidores
- **Categoria**: Visão Geral
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Verificar seção de investidores no dashboard
- **Resultado Esperado**: Contagem de investidores e totais de fundos exibidos
- **Pré-requisitos**: Investidores existem

#### DASH-OVER-005: Ver Estatísticas de Empreendimentos
- **Categoria**: Visão Geral
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Verificar seção de empreendimentos no dashboard
- **Resultado Esperado**: Contagem de empreendimentos e progresso de financiamento exibidos
- **Pré-requisitos**: Empreendimentos existem

### Navegação

#### DASH-NAV-001: Navegar para Módulos a partir do Dashboard
- **Categoria**: Navegação
- **Prioridade**: Alta
- **Tipo**: UI
- **Passos**: 
  1. Clicar nas seções de módulos ou links de navegação
- **Resultado Esperado**: Navegação bem-sucedida para os respectivos módulos
- **Pré-requisitos**: Sistema rodando

### Cálculos

#### DASH-CALC-001: Verificar Precisão dos Cálculos
- **Categoria**: Cálculos
- **Prioridade**: Alta
- **Tipo**: Funcional
- **Passos**: 
  1. Comparar totais do dashboard com dados dos módulos
- **Resultado Esperado**: Todos os cálculos correspondem aos dados reais nos módulos
- **Pré-requisitos**: Dados existem em todos os módulos

### Atualizações em Tempo Real

#### DASH-UPDATE-001: Verificar Atualização dos Dados
- **Categoria**: Atualizações em Tempo Real
- **Prioridade**: Média
- **Tipo**: Funcional
- **Passos**: 
  1. Criar/atualizar dados nos módulos
  2. Retornar ao dashboard
- **Resultado Esperado**: Dashboard reflete as últimas alterações
- **Pré-requisitos**: Sistema rodando

---

## Testes de Sistema

### Performance

#### SYS-PERF-001: Tempos de Carregamento de Página
- **Categoria**: Performance
- **Prioridade**: Média
- **Tipo**: Performance
- **Passos**: 
  1. Navegar para cada página principal
- **Resultado Esperado**: Todas as páginas carregam dentro dos limites de tempo aceitáveis
- **Pré-requisitos**: Sistema rodando

#### SYS-PERF-002: Performance de Upload de Arquivos
- **Categoria**: Performance
- **Prioridade**: Média
- **Tipo**: Performance
- **Passos**: 
  1. Fazer upload de vários tamanhos de arquivo
- **Resultado Esperado**: Uploads completados em tempo razoável
- **Pré-requisitos**: Sistema rodando

#### SYS-PERF-003: Responsividade de Busca e Filtros
- **Categoria**: Performance
- **Prioridade**: Média
- **Tipo**: Performance
- **Passos**: 
  1. Usar funções de busca e filtro
- **Resultado Esperado**: Resultados aparecem rapidamente sem atrasos
- **Pré-requisitos**: Dados existem

### Segurança

#### SYS-SEC-001: Segurança de Upload de Arquivos
- **Categoria**: Segurança
- **Prioridade**: Alta
- **Tipo**: Segurança
- **Passos**: 
  1. Tentar fazer upload de arquivos maliciosos
- **Resultado Esperado**: Sistema rejeita tipos de arquivo não autorizados
- **Pré-requisitos**: Sistema rodando

#### SYS-SEC-002: Prevenção de SQL Injection
- **Categoria**: Segurança
- **Prioridade**: Alta
- **Tipo**: Segurança
- **Passos**: 
  1. Tentar SQL injection nos campos de formulário
- **Resultado Esperado**: Sistema sanitiza entrada e previne injection
- **Pré-requisitos**: Sistema rodando

#### SYS-SEC-003: Prevenção de XSS
- **Categoria**: Segurança
- **Prioridade**: Alta
- **Tipo**: Segurança
- **Passos**: 
  1. Tentar injeção de script nos campos de texto
- **Resultado Esperado**: Sistema escapa e previne execução de script
- **Pré-requisitos**: Sistema rodando

### Design Responsivo

#### SYS-RESP-001: Compatibilidade com Dispositivos Móveis
- **Categoria**: Design Responsivo
- **Prioridade**: Média
- **Tipo**: UI
- **Passos**: 
  1. Acessar sistema em dispositivos móveis
- **Resultado Esperado**: Todas as funções funcionam adequadamente no móvel
- **Pré-requisitos**: Dispositivo móvel

#### SYS-RESP-002: Compatibilidade com Tablets
- **Categoria**: Design Responsivo
- **Prioridade**: Média
- **Tipo**: UI
- **Passos**: 
  1. Acessar sistema em dispositivos tablet
- **Resultado Esperado**: Interface se adapta corretamente às telas de tablet
- **Pré-requisitos**: Dispositivo tablet

#### SYS-RESP-003: Compatibilidade com Desktop
- **Categoria**: Design Responsivo
- **Prioridade**: Baixa
- **Tipo**: UI
- **Passos**: 
  1. Acessar sistema em várias resoluções de desktop
- **Resultado Esperado**: Interface exibida adequadamente em todas as resoluções
- **Pré-requisitos**: Computador desktop

### Tratamento de Erros

#### SYS-ERR-001: Tratamento de Erro 404
- **Categoria**: Tratamento de Erros
- **Prioridade**: Média
- **Tipo**: Tratamento de Erros
- **Passos**: 
  1. Acessar URLs não existentes
- **Resultado Esperado**: Sistema exibe página de erro 404 apropriada
- **Pré-requisitos**: Sistema rodando

#### SYS-ERR-002: Tratamento de Erro 500
- **Categoria**: Tratamento de Erros
- **Prioridade**: Alta
- **Tipo**: Tratamento de Erros
- **Passos**: 
  1. Acionar erros do servidor
- **Resultado Esperado**: Sistema exibe mensagens de erro amigáveis ao usuário
- **Pré-requisitos**: Sistema rodando

#### SYS-ERR-003: Problemas de Conectividade de Rede
- **Categoria**: Tratamento de Erros
- **Prioridade**: Média
- **Tipo**: Tratamento de Erros
- **Passos**: 
  1. Simular problemas de rede
- **Resultado Esperado**: Sistema lida com problemas de rede graciosamente
- **Pré-requisitos**: Problemas de rede

---

## Testes de Integração

### Cross-Module

#### INT-CROSS-001: Integração Empreendimento-Propriedade
- **Categoria**: Cross-Module
- **Prioridade**: Alta
- **Tipo**: Integração
- **Passos**: 
  1. Criar empreendimento com propriedade
  2. Verificar consistência de dados
- **Resultado Esperado**: Dados da propriedade corretamente vinculados e exibidos
- **Pré-requisitos**: Propriedade existe

#### INT-CROSS-002: Integração Empreendimento-Projeto
- **Categoria**: Cross-Module
- **Prioridade**: Alta
- **Tipo**: Integração
- **Passos**: 
  1. Criar empreendimento com projeto
  2. Verificar consistência de dados
- **Resultado Esperado**: Dados do projeto corretamente vinculados e exibidos
- **Pré-requisitos**: Projeto existe

#### INT-CROSS-003: Integração Empreendimento-Investidor
- **Categoria**: Cross-Module
- **Prioridade**: Alta
- **Tipo**: Integração
- **Passos**: 
  1. Adicionar investidor ao empreendimento
  2. Verificar cálculos
- **Resultado Esperado**: Cálculos de investimento atualizados corretamente entre módulos
- **Pré-requisitos**: Investidor existe

#### INT-CROSS-004: Consistência de Dados entre Módulos
- **Categoria**: Cross-Module
- **Prioridade**: Alta
- **Tipo**: Integração
- **Passos**: 
  1. Atualizar dados em um módulo
  2. Verificar outros módulos
- **Resultado Esperado**: Dados relacionados permanecem consistentes em todos os módulos
- **Pré-requisitos**: Dados relacionados existem

### API-UI

#### INT-API-001: Consistência de Dados API e UI
- **Categoria**: API-UI
- **Prioridade**: Alta
- **Tipo**: Integração
- **Passos**: 
  1. Criar dados via API
  2. Verificar exibição na UI
- **Resultado Esperado**: Dados criados via API aparecem corretamente na UI
- **Pré-requisitos**: Sistema rodando

#### INT-API-002: Consistência de Dados UI e API
- **Categoria**: API-UI
- **Prioridade**: Alta
- **Tipo**: Integração
- **Passos**: 
  1. Criar dados via UI
  2. Verificar resposta da API
- **Resultado Esperado**: Dados criados via UI disponíveis através da API
- **Pré-requisitos**: Sistema rodando

---

## Observações Importantes

1. **Prioridades**:
   - **Alta**: Testes críticos que devem ser executados primeiro
   - **Média**: Testes importantes mas não críticos
   - **Baixa**: Testes desejáveis mas opcionais

2. **Tipos de Teste**:
   - **UI**: Testes de interface do usuário
   - **Funcional**: Testes de funcionalidade
   - **Validação**: Testes de validação de dados
   - **API**: Testes de API REST
   - **Performance**: Testes de desempenho
   - **Segurança**: Testes de segurança
   - **Integração**: Testes de integração entre módulos

3. **Execução**:
   - Execute testes de alta prioridade primeiro
   - Documente todos os bugs encontrados
   - Reporte problemas imediatamente
   - Mantenha evidências (screenshots, logs)

4. **Ambiente de Teste**:
   - URL Base: http://localhost:8080
   - Certifique-se de ter dados de teste criados
   - Use navegadores diferentes quando especificado
   - Teste em diferentes resoluções de tela
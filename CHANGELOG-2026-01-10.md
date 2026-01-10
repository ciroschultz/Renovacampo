# Changelog - Sessao 10/01/2026

## Resumo das Alteracoes

### 1. Correcoes de Integracao Frontend-Backend (Investidor)

**Problema:** Os dados do formulario de investidor no frontend nao estavam preenchendo corretamente os campos do backend (cidade, estado, totalFunds).

**Solucao:**
- Corrigido `frontend/js/form-mappers.js`:
  - Nova funcao `parseMoneyValue()` que suporta formato brasileiro (R$ 10.000,00) e americano (R$ 10,000.00)
  - Nova funcao `parseInvestorLocation()` que extrai cidade/estado de formatos como "Cidade_MG", "Cidade - SP", "Cidade/MG"
  - Nova funcao `buildInvestorDescription()` que combina dados relevantes para descricao
  - Fallback de estado usando regioes de interesse

- Corrigido `frontend/pages/cadastro-investidor.html`:
  - Funcoes `submitForm()` e `quickSubmit()` agora usam `FormMappers.mapInvestidorToInvestor()`

### 2. Pagina de Detalhes do Investidor (Backend)

**Arquivo:** `backend/src/main/resources/templates/investors/view.html`

- Adicao de secao "Dados do Formulario" que parseia e exibe o campo `additionalData`
- Organizacao dos dados por categorias (Perfil, Preferencias, ESG, Carbono, etc.)
- Secao de documentos anexados com botao de download
- Estilos CSS para exibicao formatada

### 3. Correcoes de Layout e Portugues

**index.html:**
- Texto do carrossel: "comprometidos e praticas" -> "comprometidos com praticas"
- Simulador renomeado: "Simulador de Investimento" -> "Simulador de Retorno Produtivo"
- Adicionado aviso de desenvolvimento no simulador
- WhatsApp atualizado para https://wa.me/5511984489104
- Links do footer corrigidos para paginas corretas

**parcerias.html:**
- Secao "Nossos Parceiros" (ficticios) substituida por "Seja Nosso Parceiro"
- Nova mensagem convidando organizacoes a se tornarem parceiras
- Formulario configurado para enviar proposta por email (contato@renovacampo.com.br)
- WhatsApp atualizado no footer

**intro-investidor.html:**
- Adicionada tag "meta" nos stats 15+ e 5.000+

### 4. Arquivos Modificados

```
frontend/js/form-mappers.js
frontend/pages/cadastro-investidor.html
frontend/pages/intro-investidor.html
frontend/pages/parcerias.html
frontend/index.html
backend/src/main/resources/templates/investors/view.html
```

### 5. Proximos Passos Sugeridos

- [ ] Testar cadastro completo de investidor pelo frontend
- [ ] Verificar se todos os campos aparecem corretamente no backend
- [ ] Testar envio de proposta de parceria por email
- [ ] Validar links de WhatsApp em todas as paginas

---
Gerado automaticamente durante sessao de desenvolvimento com Claude Code.

/**
 * Mapeadores de Formularios - Renova Campo
 * Converte dados dos formularios frontend para o formato do backend
 * Inclui suporte para dados extras via campo additionalData (JSON)
 */

const FormMappers = {

    /**
     * Campos basicos que vao para colunas especificas da entidade Property
     */
    PROPERTY_BASE_FIELDS: [
        'nome_propiedade', 'nome_prop', 'end_prop', 'area_total', 'area_disp',
        'geolink', 'latitude', 'longitude'
    ],

    /**
     * Campos basicos que vao para colunas especificas da entidade Investor
     */
    INVESTOR_BASE_FIELDS: [
        'nome', 'cpfcnpj', 'email', 'telefone', 'endereco', 'valorMin', 'valorMax'
    ],

    /**
     * Mapeia formulario de Cadastro de Terra para Property do backend
     * Frontend: formulario_terra.html
     * Backend: Property entity
     */
    mapTerraToProperty(formData) {
        // Extrair coordenadas do geolink se fornecido
        let latitude = null;
        let longitude = null;
        if (formData.geolink) {
            const coords = this.extractCoordinates(formData.geolink);
            if (coords) {
                latitude = coords.lat;
                longitude = coords.lng;
            }
        }

        // Separar cidade e estado do endereco
        const location = this.parseLocation(formData.end_prop || '');

        // Dados basicos para colunas especificas
        const baseData = {
            name: formData.nome_propiedade || formData.nome_prop || 'Propriedade sem nome',
            description: formData.ultimas_culturas || formData.obs_erosao || '',
            totalArea: parseInt(formData.area_total) || 0,
            availableArea: parseInt(formData.area_disp) || null,
            type: formData.sit_registral || 'Rural',
            address: formData.end_prop || '',
            city: location.city,
            state: location.state,
            latitude: latitude,
            longitude: longitude
        };

        // Coletar TODOS os dados extras para o campo additionalData
        const additionalData = this.extractAdditionalData(formData, this.PROPERTY_BASE_FIELDS);

        // Adicionar additionalData como JSON string
        baseData.additionalData = JSON.stringify(additionalData);

        return baseData;
    },

    /**
     * Extrai coordenadas de um link do Google Maps ou formato lat,lng
     */
    extractCoordinates(geolink) {
        if (!geolink) return null;

        // Formato: -23.550520,-46.633308
        const directMatch = geolink.match(/(-?\d+\.?\d*),\s*(-?\d+\.?\d*)/);
        if (directMatch) {
            return { lat: parseFloat(directMatch[1]), lng: parseFloat(directMatch[2]) };
        }

        // Formato Google Maps: @-23.550520,-46.633308
        const mapsMatch = geolink.match(/@(-?\d+\.?\d*),(-?\d+\.?\d*)/);
        if (mapsMatch) {
            return { lat: parseFloat(mapsMatch[1]), lng: parseFloat(mapsMatch[2]) };
        }

        return null;
    },

    /**
     * Parseia endereco para extrair cidade e estado
     */
    parseLocation(address) {
        const parts = address.split(/[,\-\/]/);
        if (parts.length >= 2) {
            return {
                city: parts[0].trim(),
                state: parts[parts.length - 1].trim().substring(0, 2).toUpperCase()
            };
        }
        return { city: address, state: '' };
    },

    /**
     * Extrai dados extras (campos que nao sao basicos) para additionalData
     */
    extractAdditionalData(formData, baseFields) {
        const additional = {};
        for (const [key, value] of Object.entries(formData)) {
            // Ignorar campos basicos, vazios e funcoes
            if (baseFields.includes(key)) continue;
            if (value === null || value === undefined || value === '') continue;
            if (typeof value === 'function') continue;

            additional[key] = value;
        }
        return additional;
    },

    /**
     * Campos basicos que vao para colunas especificas da entidade Project
     */
    PROJECT_BASE_FIELDS: [
        'titulo', 'tipo', 'resumoProjeto', 'inicioPrev', 'duracao',
        'custoTotal', 'valorSolicitado', 'retornoEstim'
    ],

    /**
     * Mapeia formulario de Cadastro de Projeto para Project do backend
     * Frontend: cadastro-projeto.html ou formulario_produtor.html
     * Backend: Project entity
     */
    mapProjetoToProject(formData) {
        // Parsear valores monetarios (remover R$ e pontos)
        const parseMoneyValue = (val) => {
            if (!val) return 0;
            const cleaned = String(val).replace(/[R$\s.]/g, '').replace(',', '.');
            return parseFloat(cleaned) || 0;
        };

        // Calcular data estimada de fim baseada na duracao
        let estimatedEndDate = null;
        if (formData.inicioPrev && formData.duracao) {
            const startDate = new Date(formData.inicioPrev);
            startDate.setMonth(startDate.getMonth() + parseInt(formData.duracao));
            estimatedEndDate = startDate.toISOString().split('T')[0];
        }

        // Dados basicos para colunas especificas
        const baseData = {
            name: formData.titulo || formData.nomeProjeto || formData.nome || 'Projeto sem nome',
            category: formData.tipo || formData.categoria || formData.tipoProjeto || 'Geral',
            description: formData.resumoProjeto || formData.descricao || formData.descricaoProjeto || '',

            // Datas
            startDate: formData.inicioPrev || formData.dataInicio || null,
            estimatedEndDate: estimatedEndDate || formData.dataPrevisaoFim || null,

            // Prioridade (LOW, MEDIUM, HIGH, CRITICAL)
            priority: this.mapPriority(formData.prioridade),

            // Status inicial sempre PLANNING
            status: 'PLANNING',

            // Valores financeiros
            totalEstimatedCosts: parseMoneyValue(formData.custoTotal) || parseMoneyValue(formData.custoEstimado) || 0,
            totalInvestment: parseMoneyValue(formData.valorSolicitado) || parseMoneyValue(formData.investimentoNecessario) || 0,
            estimatedReturnOverInvestment: parseFloat(formData.retornoEstim) || parseFloat(formData.retornoEstimado) || 0
        };

        // Coletar TODOS os dados extras para o campo additionalData
        const additionalData = this.extractAdditionalData(formData, this.PROJECT_BASE_FIELDS);

        // Adicionar additionalData como JSON string
        baseData.additionalData = JSON.stringify(additionalData);

        return baseData;
    },

    /**
     * Mapeia formulario de Cadastro de Investidor para Investor do backend
     * Frontend: formulario_investidor.html
     * Backend: Investor entity
     */
    mapInvestidorToInvestor(formData) {
        // Limpar CPF/CNPJ removendo caracteres especiais
        const taxId = (formData.cpfcnpj || formData.cpf || formData.cnpj || '')
            .replace(/[^\d]/g, '');

        // Parsear valores monetarios (remover R$ e pontos)
        const parseMoneyValue = (val) => {
            if (!val) return 0;
            const cleaned = String(val).replace(/[R$\s.]/g, '').replace(',', '.');
            return parseFloat(cleaned) || 0;
        };

        // Parsear endereco para cidade e estado
        const location = this.parseLocation(formData.endereco || '');

        // Dados basicos para colunas especificas
        const baseData = {
            name: formData.nome || formData.nomeCompleto || formData.razaoSocial || '',
            taxId: taxId,
            email: formData.email || '',
            phone: formData.telefone || formData.celular || '',

            // Endereco
            address: formData.endereco || '',
            city: location.city,
            state: location.state,

            // Valores financeiros - usar valor maximo como totalFunds
            totalFunds: parseMoneyValue(formData.valorMax) || parseMoneyValue(formData.valorMin) || 0,
            investedFunds: 0, // Inicia com zero

            // Descricao/Observacoes
            description: formData.observacoesInvest || formData.descricao || formData.perfilInvestidor || '',

            // Status
            active: true
        };

        // Coletar TODOS os dados extras para o campo additionalData
        const additionalData = this.extractAdditionalData(formData, this.INVESTOR_BASE_FIELDS);

        // Adicionar additionalData como JSON string
        baseData.additionalData = JSON.stringify(additionalData);

        return baseData;
    },

    /**
     * Mapeia prioridade do frontend para enum do backend
     */
    mapPriority(prioridade) {
        const map = {
            'baixa': 'LOW',
            'media': 'MEDIUM',
            'alta': 'HIGH',
            'critica': 'CRITICAL',
            'low': 'LOW',
            'medium': 'MEDIUM',
            'high': 'HIGH',
            'critical': 'CRITICAL'
        };
        return map[(prioridade || '').toLowerCase()] || 'MEDIUM';
    },

    /**
     * Coleta todos os dados de um formulario HTML
     */
    collectFormData(formElement) {
        const formData = {};
        const inputs = formElement.querySelectorAll('input, select, textarea');

        inputs.forEach(input => {
            const name = input.name || input.id;
            if (!name) return;

            if (input.type === 'checkbox') {
                formData[name] = input.checked;
            } else if (input.type === 'radio') {
                if (input.checked) {
                    formData[name] = input.value;
                }
            } else if (input.type === 'file') {
                formData[name] = input.files;
            } else {
                formData[name] = input.value;
            }
        });

        return formData;
    },

    /**
     * Valida dados obrigatorios
     */
    validateRequired(data, requiredFields) {
        const errors = [];

        requiredFields.forEach(field => {
            if (!data[field] || (typeof data[field] === 'string' && !data[field].trim())) {
                errors.push(`Campo obrigatorio: ${field}`);
            }
        });

        return {
            isValid: errors.length === 0,
            errors
        };
    },

    /**
     * Valida CPF
     */
    validateCPF(cpf) {
        cpf = cpf.replace(/[^\d]/g, '');
        if (cpf.length !== 11) return false;

        // Verifica CPFs invalidos conhecidos
        if (/^(\d)\1+$/.test(cpf)) return false;

        // Validacao dos digitos
        let sum = 0;
        for (let i = 0; i < 9; i++) {
            sum += parseInt(cpf.charAt(i)) * (10 - i);
        }
        let rev = 11 - (sum % 11);
        if (rev === 10 || rev === 11) rev = 0;
        if (rev !== parseInt(cpf.charAt(9))) return false;

        sum = 0;
        for (let i = 0; i < 10; i++) {
            sum += parseInt(cpf.charAt(i)) * (11 - i);
        }
        rev = 11 - (sum % 11);
        if (rev === 10 || rev === 11) rev = 0;
        if (rev !== parseInt(cpf.charAt(10))) return false;

        return true;
    },

    /**
     * Valida CNPJ
     */
    validateCNPJ(cnpj) {
        cnpj = cnpj.replace(/[^\d]/g, '');
        if (cnpj.length !== 14) return false;

        // Verifica CNPJs invalidos conhecidos
        if (/^(\d)\1+$/.test(cnpj)) return false;

        // Validacao dos digitos
        let size = cnpj.length - 2;
        let numbers = cnpj.substring(0, size);
        const digits = cnpj.substring(size);
        let sum = 0;
        let pos = size - 7;

        for (let i = size; i >= 1; i--) {
            sum += parseInt(numbers.charAt(size - i)) * pos--;
            if (pos < 2) pos = 9;
        }

        let result = sum % 11 < 2 ? 0 : 11 - (sum % 11);
        if (result !== parseInt(digits.charAt(0))) return false;

        size = size + 1;
        numbers = cnpj.substring(0, size);
        sum = 0;
        pos = size - 7;

        for (let i = size; i >= 1; i--) {
            sum += parseInt(numbers.charAt(size - i)) * pos--;
            if (pos < 2) pos = 9;
        }

        result = sum % 11 < 2 ? 0 : 11 - (sum % 11);
        if (result !== parseInt(digits.charAt(1))) return false;

        return true;
    },

    /**
     * Valida CPF ou CNPJ
     */
    validateTaxId(taxId) {
        const cleaned = taxId.replace(/[^\d]/g, '');
        if (cleaned.length === 11) {
            return this.validateCPF(cleaned);
        } else if (cleaned.length === 14) {
            return this.validateCNPJ(cleaned);
        }
        return false;
    }
};

// Exportar para uso global
if (typeof window !== 'undefined') {
    window.FormMappers = FormMappers;
}

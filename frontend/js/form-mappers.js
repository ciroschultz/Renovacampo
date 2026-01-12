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

        // Descricao curta - apenas nome do proprietario e propriedade
        const description = `Proprietário: ${formData.nome_prop || 'Não informado'}. Propriedade: ${formData.nome_propriedade || 'Não informada'}.`;

        // Dados basicos para colunas especificas
        const baseData = {
            name: formData.nome_propriedade || formData.nome_prop || 'Propriedade sem nome',
            description: description,
            totalArea: parseInt(formData.área_total || formData.area_total) || 0,
            availableArea: parseInt(formData.área_disp || formData.area_disp) || null,
            type: formData.uso_atual || 'Rural',
            address: formData.end_prop || '',
            city: location.city,
            state: location.state,
            latitude: latitude,
            longitude: longitude
        };

        // TODOS os dados do formulario vao para additionalData (exceto arquivos)
        const additionalData = {};
        for (const [key, value] of Object.entries(formData)) {
            // Ignorar campos vazios, funcoes e arquivos
            if (value === null || value === undefined || value === '') continue;
            if (typeof value === 'function') continue;
            if (key === 'timestamp') continue;
            additionalData[key] = value;
        }

        // Adicionar additionalData como JSON string
        baseData.additionalData = JSON.stringify(additionalData);

        return baseData;
    },

    /**
     * Constroi descricao formatada com todas as informacoes extras do formulario de terra
     */
    buildPropertyDescription(formData) {
        const lines = [];

        // Dados do proprietario (campos do cadastro-terra.html)
        if (formData.nome_prop) lines.push(`Proprietário: ${formData.nome_prop}`);
        if (formData.cpf_cnpj) lines.push(`CPF/CNPJ: ${formData.cpf_cnpj}`);
        if (formData.email) lines.push(`Email: ${formData.email}`);
        if (formData.telefone) lines.push(`Telefone: ${formData.telefone}`);
        if (formData.end_resid) lines.push(`Endereço Residencial: ${formData.end_resid}`);
        if (formData.horário_contato) lines.push(`Horário para Contato: ${formData.horário_contato}`);

        // Dados da propriedade
        if (formData.matricula) lines.push(`Matrícula: ${formData.matricula}`);
        if (formData.sit_registral) lines.push(`Situação Registral: ${formData.sit_registral}`);
        if (formData.uso_atual) lines.push(`Uso Atual: ${formData.uso_atual}`);
        if (formData.água_fontes) lines.push(`Fontes de Água: ${formData.água_fontes}`);
        if (formData.benfe) lines.push(`Benfeitorias: ${formData.benfe}`);
        if (formData.ultimas_culturas) lines.push(`Últimas Culturas: ${formData.ultimas_culturas}`);

        // Recursos hídricos
        if (formData.outorga) lines.push(`Outorga: ${formData.outorga}`);
        if (formData.irrigacao) lines.push(`Irrigação: ${formData.irrigacao}`);
        if (formData.vazao) lines.push(`Vazão: ${formData.vazao}`);

        // Ambiental
        if (formData.reserva_legal) lines.push(`Reserva Legal: ${formData.reserva_legal}`);
        if (formData.apps) lines.push(`APPs: ${formData.apps}`);

        // Produção
        if (formData.rendimento) lines.push(`Rendimento: ${formData.rendimento}`);
        if (formData.certificacoes) lines.push(`Certificações: ${formData.certificacoes}`);

        // Modalidade e valores
        if (formData.modalidade_interesse) lines.push(`Modalidade: ${formData.modalidade_interesse}`);
        if (formData.aluguel_proposto) lines.push(`Valor Aluguel: R$ ${formData.aluguel_proposto}`);
        if (formData.prazo_min) lines.push(`Prazo Mínimo: ${formData.prazo_min} anos`);
        if (formData.condicoes_essenciais) lines.push(`Condições: ${formData.condicoes_essenciais}`);

        // Autorizacao e assinatura
        if (formData.autoriza_visita) lines.push(`Autoriza Visita: ${formData.autoriza_visita}`);
        if (formData.assinatura) lines.push(`Assinatura: ${formData.assinatura}`);
        if (formData.data_assinatura) lines.push(`Data: ${formData.data_assinatura}`);

        // Geolocalizacao
        if (formData.geolink) lines.push(`Geolocalização: ${formData.geolink}`);

        return lines.join('. ');
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

        // Parsear valores monetarios - suporta formato brasileiro e americano
        // Ex: "R$ 10,000.00" ou "R$ 10.000,00"
        const parseMoneyValue = (val) => {
            if (!val) return 0;
            let cleaned = String(val);
            // Remover R$ e espacos
            cleaned = cleaned.replace(/R\$\s*/g, '').trim();

            // Detectar formato: se tem virgula depois de ponto, e formato brasileiro
            // Se tem ponto depois de virgula, e formato americano
            const lastComma = cleaned.lastIndexOf(',');
            const lastDot = cleaned.lastIndexOf('.');

            if (lastComma > lastDot) {
                // Formato brasileiro: 10.000,00
                cleaned = cleaned.replace(/\./g, '').replace(',', '.');
            } else {
                // Formato americano: 10,000.00
                cleaned = cleaned.replace(/,/g, '');
            }

            return parseFloat(cleaned) || 0;
        };

        // Parsear endereco para cidade e estado - suporta mais formatos
        const location = this.parseInvestorLocation(formData.endereco || '');

        // Extrair estado das regioes de interesse se nao tiver no endereco
        let state = location.state;
        if (!state && formData.regioesInteresse) {
            const regioes = formData.regioesInteresse.split(';');
            const estadosPorRegiao = {
                'Norte': 'AM',
                'Nordeste': 'BA',
                'Centro-Oeste': 'GO',
                'Sudeste': 'SP',
                'Sul': 'PR'
            };
            for (const regiao of regioes) {
                if (estadosPorRegiao[regiao.trim()]) {
                    state = estadosPorRegiao[regiao.trim()];
                    break;
                }
            }
        }

        // Dados basicos para colunas especificas
        const baseData = {
            name: formData.nome || formData.nomeCompleto || formData.razaoSocial || '',
            taxId: taxId,
            email: formData.email || '',
            phone: formData.telefone || formData.celular || '',

            // Endereco
            address: formData.endereco || '',
            city: location.city,
            state: state,

            // Valores financeiros - usar valor maximo como totalFunds
            totalFunds: parseMoneyValue(formData.valorMax) || parseMoneyValue(formData.valorMin) || 0,
            investedFunds: 0, // Inicia com zero

            // Descricao/Observacoes - combinar campos relevantes
            description: this.buildInvestorDescription(formData),

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
     * Parseia endereco de investidor para cidade e estado
     * Suporta formatos: "Cidade - MG", "Cidade_MG", "Cidade/MG", "Cidade, MG"
     */
    parseInvestorLocation(address) {
        if (!address) return { city: '', state: '' };

        // Tentar extrair estado do final (2 letras maiusculas)
        const stateMatch = address.match(/[,\-_\/\s]+([A-Za-z]{2})\s*$/);
        if (stateMatch) {
            const state = stateMatch[1].toUpperCase();
            const city = address.replace(stateMatch[0], '').trim();
            return { city, state };
        }

        // Tentar separar por delimitadores comuns
        const parts = address.split(/[,\-_\/]+/);
        if (parts.length >= 2) {
            const lastPart = parts[parts.length - 1].trim();
            if (lastPart.length === 2) {
                return {
                    city: parts.slice(0, -1).join(' ').trim(),
                    state: lastPart.toUpperCase()
                };
            }
        }

        return { city: address.trim(), state: '' };
    },

    /**
     * Constroi descricao do investidor com dados relevantes
     */
    buildInvestorDescription(formData) {
        const parts = [];

        if (formData.tipoInvestidor) parts.push(`Tipo: ${formData.tipoInvestidor}`);
        if (formData.ramo) parts.push(`Ramo: ${formData.ramo}`);
        if (formData.horizonte) parts.push(`Horizonte: ${formData.horizonte}`);
        if (formData.risco) parts.push(`Risco: ${formData.risco}`);
        if (formData.observacoesInvest) parts.push(formData.observacoesInvest);

        return parts.join('. ') || '';
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

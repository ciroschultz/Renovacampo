/**
 * Modulo de API - Renova Campo
 * Centraliza todas as chamadas ao backend
 */

const API = {
    /**
     * Funcao base para fazer requisicoes HTTP
     */
    async request(endpoint, options = {}) {
        const url = `${CONFIG.API_BASE_URL}${endpoint}`;

        const defaultOptions = {
            headers: CONFIG.REQUEST.HEADERS,
            ...options
        };

        try {
            const response = await fetch(url, defaultOptions);

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || errorData.error || `Erro ${response.status}: ${response.statusText}`);
            }

            // Verificar se ha conteudo para retornar
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            }

            return response;
        } catch (error) {
            console.error('Erro na requisicao:', error);
            throw error;
        }
    },

    /**
     * GET request
     */
    async get(endpoint, params = {}) {
        const queryString = new URLSearchParams(params).toString();
        const url = queryString ? `${endpoint}?${queryString}` : endpoint;
        return this.request(url, { method: 'GET' });
    },

    /**
     * POST request
     */
    async post(endpoint, data) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },

    /**
     * PUT request
     */
    async put(endpoint, data) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },

    /**
     * DELETE request
     */
    async delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    },

    /**
     * Upload de arquivo generico
     */
    async uploadFile(endpoint, file, fieldName = 'file', additionalData = {}) {
        console.log('=== API.uploadFile CHAMADO ===');
        console.log('Endpoint:', endpoint);
        console.log('File:', file);
        console.log('File name:', file ? file.name : 'N/A');
        console.log('File size:', file ? file.size : 'N/A');
        console.log('File type:', file ? file.type : 'N/A');
        console.log('Field name:', fieldName);

        if (!file) {
            console.error('ERRO: Arquivo nulo ou indefinido');
            throw new Error('Arquivo nulo ou indefinido');
        }

        const formData = new FormData();
        formData.append(fieldName, file);
        console.log('FormData criado com campo:', fieldName);

        Object.keys(additionalData).forEach(key => {
            formData.append(key, additionalData[key]);
        });

        const url = `${CONFIG.API_BASE_URL}${endpoint}`;
        console.log('URL completa:', url);

        try {
            console.log('Iniciando fetch POST para:', url);
            const response = await fetch(url, {
                method: 'POST',
                body: formData
                // Nao incluir Content-Type - o browser define automaticamente com boundary
            });

            console.log('Response status:', response.status);
            console.log('Response ok:', response.ok);

            if (!response.ok) {
                const errorText = await response.text();
                console.error('Erro na resposta:', errorText);
                throw new Error(`Erro no upload: ${response.status} ${response.statusText}`);
            }

            const result = await response.json();
            console.log('Upload bem sucedido:', result);
            return result;
        } catch (error) {
            console.error('ERRO no upload:', error);
            console.error('Stack:', error.stack);
            throw error;
        }
    },

    /**
     * Upload de foto (usa 'photo' como nome do campo)
     */
    async uploadPhoto(endpoint, file) {
        return this.uploadFile(endpoint, file, 'photo');
    },

    /**
     * Upload de documento (usa 'file' como nome do campo)
     */
    async uploadDocument(endpoint, file) {
        return this.uploadFile(endpoint, file, 'file');
    }
};

// =====================================================
// SERVICOS ESPECIFICOS
// =====================================================

/**
 * Servico de Propriedades (Terras)
 */
const PropertyService = {
    async getAll() {
        return API.get(CONFIG.ENDPOINTS.PROPERTIES);
    },

    // Retorna apenas propriedades aprovadas para exibição pública
    async getApproved() {
        return API.get(`${CONFIG.ENDPOINTS.PROPERTIES}/approved`);
    },

    async getById(id) {
        return API.get(`${CONFIG.ENDPOINTS.PROPERTIES}/${id}`);
    },

    async create(property) {
        return API.post(CONFIG.ENDPOINTS.PROPERTIES, property);
    },

    async update(id, property) {
        return API.put(`${CONFIG.ENDPOINTS.PROPERTIES}/${id}`, property);
    },

    async delete(id) {
        return API.delete(`${CONFIG.ENDPOINTS.PROPERTIES}/${id}`);
    },

    async uploadPhoto(propertyId, file) {
        return API.uploadPhoto(`${CONFIG.ENDPOINTS.PHOTOS}/upload/${propertyId}`, file);
    },

    async uploadDocument(propertyId, file) {
        return API.uploadDocument(`${CONFIG.ENDPOINTS.FILES}/upload/${propertyId}?type=DOCUMENT`, file);
    },

    async uploadMultiplePhotos(propertyId, files) {
        console.log('=== PropertyService.uploadMultiplePhotos ===');
        console.log('Property ID:', propertyId);
        console.log('Files recebidos:', files);
        console.log('Quantidade de arquivos:', files ? files.length : 0);

        if (!files || files.length === 0) {
            console.warn('Nenhum arquivo para upload');
            return [];
        }

        const results = [];
        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            console.log(`Processando foto ${i + 1}/${files.length}:`, file.name);
            try {
                const result = await this.uploadPhoto(propertyId, file);
                console.log(`Foto ${file.name} enviada com sucesso`);
                results.push({ success: true, file: file.name, data: result });
            } catch (error) {
                console.error(`ERRO ao enviar foto ${file.name}:`, error);
                results.push({ success: false, file: file.name, error: error.message });
            }
        }
        console.log('Resultados do upload de fotos:', results);
        return results;
    },

    async uploadMultipleDocuments(propertyId, files) {
        console.log('=== PropertyService.uploadMultipleDocuments ===');
        console.log('Property ID:', propertyId);
        console.log('Files recebidos:', files);
        console.log('Quantidade de arquivos:', files ? files.length : 0);

        if (!files || files.length === 0) {
            console.warn('Nenhum documento para upload');
            return [];
        }

        const results = [];
        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            console.log(`Processando documento ${i + 1}/${files.length}:`, file.name);
            try {
                const result = await this.uploadDocument(propertyId, file);
                console.log(`Documento ${file.name} enviado com sucesso`);
                results.push({ success: true, file: file.name, data: result });
            } catch (error) {
                console.error(`ERRO ao enviar documento ${file.name}:`, error);
                results.push({ success: false, file: file.name, error: error.message });
            }
        }
        console.log('Resultados do upload de documentos:', results);
        return results;
    }
};

/**
 * Servico de Projetos
 */
const ProjectService = {
    async getAll() {
        return API.get(CONFIG.ENDPOINTS.PROJECTS);
    },

    async getById(id) {
        return API.get(`${CONFIG.ENDPOINTS.PROJECTS}/${id}`);
    },

    async create(project) {
        return API.post(CONFIG.ENDPOINTS.PROJECTS, project);
    },

    async update(id, project) {
        return API.put(`${CONFIG.ENDPOINTS.PROJECTS}/${id}`, project);
    },

    async delete(id) {
        return API.delete(`${CONFIG.ENDPOINTS.PROJECTS}/${id}`);
    },

    async getByStatus(status) {
        return API.get(`${CONFIG.ENDPOINTS.PROJECTS}/status/${status}`);
    },

    async getCategories() {
        return API.get(`${CONFIG.ENDPOINTS.PROJECTS}/categories`);
    },

    async search(query) {
        return API.get(`${CONFIG.ENDPOINTS.PROJECTS}/search`, { q: query });
    }
};

/**
 * Servico de Investidores
 */
const InvestorService = {
    async getAll(active = null) {
        const params = active !== null ? { active } : {};
        return API.get(CONFIG.ENDPOINTS.INVESTORS, params);
    },

    async getById(id) {
        return API.get(`${CONFIG.ENDPOINTS.INVESTORS}/${id}`);
    },

    async getByTaxId(taxId) {
        return API.get(`${CONFIG.ENDPOINTS.INVESTORS}/taxId/${taxId}`);
    },

    async create(investor) {
        return API.post(CONFIG.ENDPOINTS.INVESTORS, investor);
    },

    async update(id, investor) {
        return API.put(`${CONFIG.ENDPOINTS.INVESTORS}/${id}`, investor);
    },

    async delete(id) {
        return API.delete(`${CONFIG.ENDPOINTS.INVESTORS}/${id}`);
    },

    async activate(id) {
        return API.request(`${CONFIG.ENDPOINTS.INVESTORS}/${id}/activate`, { method: 'PATCH' });
    },

    async deactivate(id) {
        return API.request(`${CONFIG.ENDPOINTS.INVESTORS}/${id}/deactivate`, { method: 'PATCH' });
    },

    async search(name) {
        return API.get(`${CONFIG.ENDPOINTS.INVESTORS}/search`, { name });
    },

    async getStatistics() {
        return API.get(`${CONFIG.ENDPOINTS.INVESTORS}/statistics`);
    },

    async getWithAvailableFunds() {
        return API.get(`${CONFIG.ENDPOINTS.INVESTORS}/with-funds`);
    },

    async uploadDocument(investorId, file) {
        return API.uploadDocument(`${CONFIG.ENDPOINTS.FILES}/upload/investor/${investorId}?type=DOCUMENT`, file);
    },

    async uploadMultipleDocuments(investorId, files) {
        const results = [];
        for (const file of files) {
            try {
                const result = await this.uploadDocument(investorId, file);
                results.push({ success: true, file: file.name, data: result });
            } catch (error) {
                results.push({ success: false, file: file.name, error: error.message });
            }
        }
        return results;
    }
};

/**
 * Servico de Empreendimentos
 */
const EnterpriseService = {
    async getAll(search = null) {
        const params = search ? { search } : {};
        return API.get(CONFIG.ENDPOINTS.ENTERPRISES, params);
    },

    async getById(id) {
        return API.get(`${CONFIG.ENDPOINTS.ENTERPRISES}/${id}`);
    },

    async create(enterprise) {
        return API.post(CONFIG.ENDPOINTS.ENTERPRISES, enterprise);
    },

    async update(id, enterprise) {
        return API.put(`${CONFIG.ENDPOINTS.ENTERPRISES}/${id}`, enterprise);
    },

    async delete(id) {
        return API.delete(`${CONFIG.ENDPOINTS.ENTERPRISES}/${id}`);
    },

    async getOpenFunding() {
        return API.get(`${CONFIG.ENDPOINTS.ENTERPRISES}/open-funding`);
    },

    async getStatistics() {
        return API.get(`${CONFIG.ENDPOINTS.ENTERPRISES}/statistics`);
    },

    async addInvestor(enterpriseId, investorId, amount) {
        return API.post(`${CONFIG.ENDPOINTS.ENTERPRISES}/${enterpriseId}/investors`, {
            investorId,
            investmentAmount: amount
        });
    }
};

// Exportar para uso global
if (typeof window !== 'undefined') {
    window.API = API;
    window.PropertyService = PropertyService;
    window.ProjectService = ProjectService;
    window.InvestorService = InvestorService;
    window.EnterpriseService = EnterpriseService;
}

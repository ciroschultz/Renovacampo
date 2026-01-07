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
     * Upload de arquivo
     */
    async uploadFile(endpoint, file, additionalData = {}) {
        const formData = new FormData();
        formData.append('file', file);

        Object.keys(additionalData).forEach(key => {
            formData.append(key, additionalData[key]);
        });

        const url = `${CONFIG.API_BASE_URL}${endpoint}`;

        try {
            const response = await fetch(url, {
                method: 'POST',
                body: formData
                // Nao incluir Content-Type - o browser define automaticamente com boundary
            });

            if (!response.ok) {
                throw new Error(`Erro no upload: ${response.statusText}`);
            }

            return await response.json();
        } catch (error) {
            console.error('Erro no upload:', error);
            throw error;
        }
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
        return API.uploadFile(`${CONFIG.ENDPOINTS.PHOTOS}/upload/${propertyId}`, file);
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

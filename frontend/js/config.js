/**
 * Configuracoes do Renova Campo
 */
const CONFIG = {
    // URL base da API - altere para producao quando necessario
    // Desenvolvimento: http://localhost:8080
    // Producao: http://sistema.renovacampo.com.br:8080
    API_BASE_URL: 'http://localhost:8080',

    // Endpoints da API
    ENDPOINTS: {
        // Propriedades (Terras)
        PROPERTIES: '/api/v1/property',

        // Projetos
        PROJECTS: '/api/v1/project',

        // Investidores
        INVESTORS: '/api/v1/investors',

        // Empreendimentos
        ENTERPRISES: '/api/v1/enterprises',

        // Arquivos
        PHOTOS: '/api/v1/photos',
        FILES: '/api/v1/files'
    },

    // Configuracoes de requisicao
    REQUEST: {
        TIMEOUT: 30000, // 30 segundos
        HEADERS: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    }
};

// Exportar para uso global
if (typeof window !== 'undefined') {
    window.CONFIG = CONFIG;
}

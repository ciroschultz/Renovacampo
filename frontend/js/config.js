/**
 * Configuracoes do Renova Campo
 */
const CONFIG = {
    // URL base da API - vazio para usar caminho relativo (funciona em dev e producao)
    API_BASE_URL: '',

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

/* ========================================
   RENOVA CAMPO - MAIN JAVASCRIPT
   ======================================== */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize all modules
    initMobileMenu();
    initHeroCarousel();
    initSimulador();
    initPeriodoButtons();
    initSmoothScroll();
    initHeaderScroll();
});

/* ========================================
   MOBILE MENU
   ======================================== */
function initMobileMenu() {
    const menuToggle = document.getElementById('menuToggle');
    const nav = document.querySelector('.nav');

    if (menuToggle) {
        menuToggle.addEventListener('click', function() {
            nav.classList.toggle('active');

            // Toggle icon
            const icon = this.querySelector('i');
            if (nav.classList.contains('active')) {
                icon.classList.remove('fa-bars');
                icon.classList.add('fa-times');
            } else {
                icon.classList.remove('fa-times');
                icon.classList.add('fa-bars');
            }
        });
    }
}

/* ========================================
   HERO CAROUSEL
   ======================================== */
function initHeroCarousel() {
    const slides = document.querySelectorAll('.carousel-slide');
    const indicators = document.querySelectorAll('.carousel-indicators .indicator');

    console.log('Carrossel iniciado - slides: ' + slides.length + ', indicadores: ' + indicators.length);

    if (slides.length === 0) return;

    let currentSlide = 0;
    const totalSlides = slides.length;

    function goToSlide(index) {
        console.log('Mudando para slide ' + index);

        // Remove active de todos
        for (let i = 0; i < slides.length; i++) {
            slides[i].classList.remove('active');
        }
        for (let i = 0; i < indicators.length; i++) {
            indicators[i].classList.remove('active');
        }

        // Adiciona active ao atual
        slides[index].classList.add('active');
        if (indicators[index]) {
            indicators[index].classList.add('active');
        }

        currentSlide = index;
    }

    function nextSlide() {
        const next = (currentSlide + 1) % totalSlides;
        goToSlide(next);
    }

    // Click nos indicadores
    for (let i = 0; i < indicators.length; i++) {
        indicators[i].addEventListener('click', function() {
            goToSlide(i);
        });
    }

    // Auto-play a cada 4 segundos
    setInterval(nextSlide, 4000);

    console.log('Carrossel configurado com sucesso');
}

/* ========================================
   SIMULADOR DE INVESTIMENTO
   ======================================== */
function initSimulador() {
    const calcularBtn = document.getElementById('calcularBtn');

    if (calcularBtn) {
        calcularBtn.addEventListener('click', calcularInvestimento);

        // Calculate on page load
        calcularInvestimento();
    }
}

function calcularInvestimento() {
    // Get input values
    const investimento = parseFloat(document.getElementById('investimento').value) || 50000;
    const tipoSelect = document.getElementById('tipo');
    const taxa = parseFloat(tipoSelect.value) / 100 || 0.18;

    // Get selected period
    const periodoBtn = document.querySelector('.periodo-btn.active');
    const meses = parseInt(periodoBtn?.dataset.meses) || 24;
    const anos = meses / 12;

    // Calculate returns (compound interest)
    const valorFinal = investimento * Math.pow(1 + taxa, anos);
    const rendimento = valorFinal - investimento;

    // Update display
    document.getElementById('resultInvestimento').textContent = formatCurrency(investimento);
    document.getElementById('resultTaxa').textContent = (taxa * 100).toFixed(0) + '%';
    document.getElementById('resultPeriodo').textContent = meses + ' meses';
    document.getElementById('resultRendimento').textContent = formatCurrency(rendimento);
    document.getElementById('resultFinal').textContent = formatCurrency(valorFinal);

    // Add animation
    animateValue('resultRendimento', rendimento);
    animateValue('resultFinal', valorFinal);
}

function initPeriodoButtons() {
    const periodoBtns = document.querySelectorAll('.periodo-btn');

    periodoBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            // Remove active from all
            periodoBtns.forEach(b => b.classList.remove('active'));
            // Add active to clicked
            this.classList.add('active');
            // Recalculate
            calcularInvestimento();
        });
    });
}

/* ========================================
   UTILITY FUNCTIONS
   ======================================== */
function formatCurrency(value) {
    return value.toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    });
}

function animateValue(elementId, finalValue) {
    const element = document.getElementById(elementId);
    if (!element) return;

    const duration = 500;
    const startValue = 0;
    const startTime = performance.now();

    function update(currentTime) {
        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);

        // Easing function
        const easeOutQuad = 1 - (1 - progress) * (1 - progress);
        const currentValue = startValue + (finalValue - startValue) * easeOutQuad;

        element.textContent = formatCurrency(currentValue);

        if (progress < 1) {
            requestAnimationFrame(update);
        }
    }

    requestAnimationFrame(update);
}

/* ========================================
   SMOOTH SCROLL
   ======================================== */
function initSmoothScroll() {
    const links = document.querySelectorAll('a[href^="#"]');

    links.forEach(link => {
        link.addEventListener('click', function(e) {
            const href = this.getAttribute('href');

            if (href === '#') return;

            const target = document.querySelector(href);

            if (target) {
                e.preventDefault();

                const headerHeight = document.querySelector('.header').offsetHeight;
                const targetPosition = target.offsetTop - headerHeight;

                window.scrollTo({
                    top: targetPosition,
                    behavior: 'smooth'
                });

                // Close mobile menu if open
                const nav = document.querySelector('.nav');
                if (nav.classList.contains('active')) {
                    nav.classList.remove('active');
                }
            }
        });
    });
}

/* ========================================
   HEADER SCROLL EFFECT
   ======================================== */
function initHeaderScroll() {
    const header = document.querySelector('.header');
    let lastScroll = 0;

    window.addEventListener('scroll', function() {
        const currentScroll = window.pageYOffset;

        // Add shadow on scroll
        if (currentScroll > 50) {
            header.style.boxShadow = '0 4px 20px rgba(0, 0, 0, 0.15)';
        } else {
            header.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.1)';
        }

        lastScroll = currentScroll;
    });
}

/* ========================================
   FILTER FUNCTIONALITY
   ======================================== */
function filterTerras(searchTerm) {
    const cards = document.querySelectorAll('.terra-card');

    cards.forEach(card => {
        const title = card.querySelector('h3').textContent.toLowerCase();
        const location = card.querySelector('.terra-location').textContent.toLowerCase();

        if (title.includes(searchTerm.toLowerCase()) || location.includes(searchTerm.toLowerCase())) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
}

function filterProjetos(searchTerm) {
    const cards = document.querySelectorAll('.projeto-card');

    cards.forEach(card => {
        const title = card.querySelector('h3').textContent.toLowerCase();

        if (title.includes(searchTerm.toLowerCase())) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
}

/* ========================================
   PROGRESS BAR ANIMATION
   ======================================== */
function animateProgressBars() {
    const progressBars = document.querySelectorAll('.progress-fill');

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const width = entry.target.style.width;
                entry.target.style.width = '0%';
                setTimeout(() => {
                    entry.target.style.width = width;
                }, 100);
            }
        });
    }, { threshold: 0.5 });

    progressBars.forEach(bar => observer.observe(bar));
}

// Initialize progress bar animation
document.addEventListener('DOMContentLoaded', animateProgressBars);

/* ========================================
   FORM VALIDATION
   ======================================== */
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return false;

    const inputs = form.querySelectorAll('input[required]');
    let isValid = true;

    inputs.forEach(input => {
        if (!input.value.trim()) {
            isValid = false;
            input.classList.add('error');
        } else {
            input.classList.remove('error');
        }

        if (input.type === 'email' && !validateEmail(input.value)) {
            isValid = false;
            input.classList.add('error');
        }
    });

    return isValid;
}

/* ========================================
   NEWSLETTER FORM
   ======================================== */
document.addEventListener('DOMContentLoaded', function() {
    const newsletterForm = document.querySelector('.newsletter-form');

    if (newsletterForm) {
        newsletterForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const email = this.querySelector('input[type="email"]').value;

            if (validateEmail(email)) {
                // Success - would send to API
                alert('Obrigado por se inscrever! Você receberá nossas novidades em breve.');
                this.reset();
            } else {
                alert('Por favor, insira um e-mail válido.');
            }
        });
    }
});

/* ========================================
   API INTEGRATION (Future)
   ======================================== */
const API = {
    baseURL: 'http://localhost:8080/api/v1',

    async get(endpoint) {
        try {
            const response = await fetch(`${this.baseURL}${endpoint}`);
            return await response.json();
        } catch (error) {
            console.error('API Error:', error);
            return null;
        }
    },

    async post(endpoint, data) {
        try {
            const response = await fetch(`${this.baseURL}${endpoint}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
            return await response.json();
        } catch (error) {
            console.error('API Error:', error);
            return null;
        }
    },

    // Properties
    async getProperties() {
        return this.get('/properties');
    },

    // Projects
    async getProjects() {
        return this.get('/projects');
    },

    // Investors
    async getInvestors() {
        return this.get('/investors');
    },

    // Simulator
    async calculateInvestment(data) {
        return this.post('/simulator/calculate', data);
    }
};

/* ========================================
   EXPORT FOR MODULES
   ======================================== */
window.RenovaCampo = {
    API,
    formatCurrency,
    validateEmail,
    validateForm
};

-- Dados de teste para desenvolvimento local

-- Propriedades (Terras)
INSERT INTO property (name, description, total_area, available_area, type, address, city, state, latitude, longitude) VALUES
('Fazenda Esperanca', 'Fazenda com excelente potencial para agricultura regenerativa. Solo fertil e boa disponibilidade hidrica.', 150, 100, 'Fazenda', 'Rodovia MG-050, KM 45', 'Pocos de Caldas', 'MG', -21.7872, -46.5635),
('Sitio Luz da Lua', 'Sitio com 50 hectares ideal para sistemas agroflorestais. Topografia plana e agua abundante.', 50, 50, 'Sitio', 'Estrada Rural, s/n', 'Andradas', 'MG', -21.9506, -46.5844),
('Chacara Verde Vale', 'Chacara com infraestrutura completa. Casa sede, galpao e sistema de irrigacao instalado.', 25, 20, 'Chacara', 'Rua das Palmeiras, 500', 'Caldas', 'MG', -21.9192, -46.3856);

-- Projetos
INSERT INTO project (name, category, description, start_date, estimated_end_date, priority, status, total_estimated_costs, total_costs, total_investment, estimated_return_over_investment) VALUES
('Soja Regenerativa', 'Graos', 'Projeto de producao de soja com praticas regenerativas e certificacao organica.', '2025-01-01', '2025-12-31', 'HIGH', 'IN_PROGRESS', 500000, 150000, 400000, 18),
('Sistema Agroflorestal', 'Agrofloresta', 'Implantacao de SAF com frutiferas, madeiras nobres e culturas anuais.', '2025-02-01', '2028-02-01', 'MEDIUM', 'PLANNING', 300000, 50000, 250000, 22),
('Pecuaria Regenerativa', 'Pecuaria', 'Conversao de pastagem convencional para sistema rotacional regenerativo.', '2025-03-01', '2026-03-01', 'HIGH', 'APPROVED', 200000, 0, 180000, 15);

-- Investidores
INSERT INTO investor (name, tax_id, email, phone, address, city, state, total_funds, invested_funds, description, active, create_date, update_date) VALUES
('Carlos Silva', '123.456.789-00', 'carlos@email.com', '(11) 99999-0001', 'Av. Paulista, 1000', 'Sao Paulo', 'SP', 500000, 200000, 'Investidor focado em projetos sustentaveis', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Maria Santos', '987.654.321-00', 'maria@email.com', '(11) 99999-0002', 'Rua Augusta, 500', 'Sao Paulo', 'SP', 300000, 100000, 'Investidora com interesse em agricultura organica', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Empreendimentos
INSERT INTO enterprise (name, description, property_id, project_id, status, launch_date, funding_deadline, expected_completion_date, total_investment_required, total_investment_raised, minimum_investment, expected_return_percentage, active, create_date) VALUES
('Empreendimento Soja Verde', 'Producao de soja regenerativa na Fazenda Esperanca com retorno estimado de 18% ao ano.', 1, 1, 'ACTIVE', '2025-01-15', '2025-03-15', '2025-12-31', 400000, 280000, 5000, 18, true, CURRENT_TIMESTAMP),
('Agrofloresta Luz da Lua', 'Sistema agroflorestal diversificado com frutiferas e madeiras nobres. Retorno de longo prazo.', 2, 2, 'ACTIVE', '2025-02-01', '2025-05-01', '2028-02-01', 250000, 75000, 2000, 22, true, CURRENT_TIMESTAMP),
('Pecuaria Sustentavel', 'Conversao para pecuaria regenerativa com certificacao de bem-estar animal.', 3, 3, 'PLANNING', '2025-04-01', '2025-06-01', '2026-03-01', 180000, 0, 3000, 15, true, CURRENT_TIMESTAMP);

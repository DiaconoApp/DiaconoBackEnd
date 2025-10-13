-- ========================================
-- IGREJAS
-- ========================================
INSERT INTO igreja (id_interno, id_externo, nome, cnpj)
VALUES
  (1, '550e8400-e29b-41d4-a716-446655440000', 'Igreja Central da Fé', '12.345.678/0001-90'),
  (2, '550e8400-e29b-41d4-a716-446655440001', 'Assembleia da Graça Eterna', '98.765.432/0001-10');

-- ========================================
-- MEMBROS
-- ========================================
INSERT INTO membro (id_interno, id_externo, nome, email, data_nascimento, cpf, cep, numero_casa, senha_temporaria, senha)
VALUES
  (1, '550e8400-e29b-41d4-a716-446655440010', 'João da Silva', 'joao.silva@exemplo.com', '1985-10-15', '123.456.789-01', '01000-000', 105, 'temp123', '$2a$10$HASHED_SENHA_1'),
  (2, '550e8400-e29b-41d4-a716-446655440011', 'Maria Oliveira', 'maria.oliver@exemplo.com', '1992-05-20', '987.654.321-02', '02000-000', 25, 'temp123', '$2a$10$HASHED_SENHA_2'),
  (3, '550e8400-e29b-41d4-a716-446655440012', 'Pedro Santos', 'pedro.santos@exemplo.com', '1989-07-30', '741.852.963-00', '03000-000', 12, 'temp123', '$2a$10$HASHED_SENHA_3'),
  (4, '550e8400-e29b-41d4-a716-446655440013', 'Ana Pereira', 'ana.pereira@exemplo.com', '1995-02-14', '369.258.147-00', '04000-000', 99, 'temp123', '$2a$10$HASHED_SENHA_4'),
  (5, '550e8400-e29b-41d4-a716-446655440014', 'Carla Souza', 'carla.souza@exemplo.com', '1990-09-10', '159.753.486-00', '05000-000', 500, 'temp123', '$2a$10$HASHED_SENHA_5');

-- ========================================
-- MINISTÉRIOS
-- ========================================
INSERT INTO ministerio (id_interno, id_externo, nome, data_criacao, nome_lider, status)
VALUES
  (1, '550e8400-e29b-41d4-a716-446655440020', 'Ministério de Louvor Adoração', '2022-01-01', 'Maria Oliveira', 'ATIVO'),
  (2, '550e8400-e29b-41d4-a716-446655440021', 'Kids Church', '2021-05-15', 'Carla Souza', 'ATIVO'),
  (3, '550e8400-e29b-41d4-a716-446655440022', 'Missão Esperança', '2023-11-20', 'Pedro Santos', 'ATIVO'),
  (4, '550e8400-e29b-41d4-a716-446655440023', 'Streaming & Mídia', '2024-03-10', 'Ana Pereira', 'ATIVO');

-- ========================================
-- ENDEREÇOS DE EVENTOS
-- ========================================
--INSERT INTO endereco_evento (id_interno, id_externo, cep, rua, cidade, bairro, complemento, numero, apelido)
--VALUES
--  (1, '550e8400-e29b-41d4-a716-446655440030', '13000-000', 'Rua das Graças', 'Campinas', 'Centro', 'Ao lado do teatro', '1234', 'Salão Principal'),
--  (2, '550e8400-e29b-41d4-a716-446655440031', '13100-000', 'Av. da Esperança', 'Campinas', 'Jardim das Flores', 'Próximo à praça', '567', 'Anexo Jovem');
--
---- ========================================
---- EVENTOS (Corrigido: Adicionado fk_igreja e fk_organizador, removido ministerio_id)
---- ========================================
--INSERT INTO evento (id_interno, id_externo, nome, fk_igreja, fk_organizador, fk_endereco, tipo_recorrencia, descricao, publico_alvo, data, hora_inicio, hora_fim, custo, data_inicio_recorrencia, data_termino_recorrencia, intervalo_recorrencia)
--VALUES
--  -- Evento 1: Ensaio de Louvor (Recorrente)
--  (1, '550e8400-e29b-41d4-a716-446655440040', 'Ensaio de Louvor', 1, 1, 1, 'SEMANAL', 'Ensaio semanal do ministério de louvor.', 'Músicos', '2025-10-14', '19:00:00', '21:00:00', 0.00, '2025-10-14', '2025-12-31', 1),
--  -- Evento 2: Culto Kids (Recorrente)
--  (2, '550e8400-e29b-41d4-a716-446655440041', 'Culto Kids', 2, 2, 2, 'SEMANAL', 'Culto especial para crianças.', 'Crianças 5-10 anos', '2025-10-19', '09:00:00', '11:00:00', 0.00, '2025-10-19', '2026-06-30', 1),
--  -- Evento 3: Ação Social (Recorrente)
--  (3, '550e8400-e29b-41d4-a716-446655440042', 'Ação Social - Doação de Roupas', 1, 1, 1, 'MENSAL', 'Arrecadação de roupas e alimentos.', 'Voluntários e comunidade', '2025-10-26', '08:00:00', '14:00:00', 0.00, '2025-10-26', '2026-10-26', 1),
--  -- Evento 4: Treinamento de Mídia (Único)
--  (4, '550e8400-e29b-41d4-a716-446655440043', 'Treinamento de Mídia', 2, 2, 2, 'UNICO', 'Treinamento para a equipe de streaming e multimídia.', 'Equipe de Mídia', '2025-11-02', '10:00:00', '13:00:00', 50.00, NULL, NULL, 0);
--
---- ========================================
---- TABELA DE ASSOCIAÇÃO EVENTO_MINISTERIO (ManyToMany)
---- ========================================
--INSERT INTO evento_ministerio (fk_evento, fk_ministerio)
--VALUES
--  (1, 1), -- Ensaio de Louvor -> Louvor Adoração
--  (2, 2), -- Culto Kids -> Kids Church
--  (3, 3), -- Ação Social -> Missão Esperança
--  (4, 4); -- Treinamento de Mídia -> Streaming & Mídia
--
---- ========================================
---- DIAS DA SEMANA (coleção de enums dos eventos recorrentes)
---- ========================================
--INSERT INTO evento_dias_semana (evento_id, dias_semana)
--VALUES
--  (1, 'TUESDAY'),  -- Ensaio de Louvor: Terça
--  (1, 'THURSDAY'), -- Ensaio de Louvor: Quinta
--  (2, 'SUNDAY'),   -- Culto Kids: Domingo
--  (3, 'SATURDAY'); -- Ação Social: Sábado
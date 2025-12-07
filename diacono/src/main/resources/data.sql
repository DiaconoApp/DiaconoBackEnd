-- ENDERECO IGREJA
INSERT INTO endereco_igreja (
    id_externo, bairro, cep, cidade, complemento, estado, numero, rua
)
VALUES
('d1f5e8b2-3c4a-4f6b-9e2d-5f7b8c9d0e1f', 'Centro', '12345-678', 'São Paulo', 'Apto 101', 'SP', '250', 'Rua das Flores'), -- id_interno 1
('a2b4c6d8-e0f1-2345-6789-0a1b2c3d4e5f', 'Jardim América', '87654-321', 'São Paulo', '', 'SP', '75', 'Avenida Brasil'), -- id_interno 2
('b7c3e2c3-8c37-43c9-9d0e-1f5b4c8b2c32', 'Centro', '01000-000', 'São Paulo', '', 'SP', '100', 'Rua Principal'); -- id_interno 3

-- IGREJA
INSERT INTO igreja (
    id_externo, fk_endereco, cnpj, nome
)
VALUES
('550e8400-e29b-41d4-a716-446655440000', 1, '12.345.678/0001-90', 'Igreja Central da Fé'), -- id_interno 1
('550e8400-e29b-41d4-a716-446655440001', 2, '98.765.432/0001-10', 'Assembleia da Graça Eterna'), -- id_interno 2
('f84aa4ac-0158-4ef0-9b5f-1762c4a86e53', 3, '12.345.678/0001-10', 'Igreja Congregação'); -- id_interno 3

-- ==================================================================================
-- 1. ENDEREÇOS (Corrigido para hexadecimais válidos: prefixo 1000...)
-- ==================================================================================

INSERT INTO endereco_membro (id_externo, rua, numero, bairro, cidade, estado, cep, complemento) VALUES
('10000000-0000-0000-0000-000000000001', 'Rua das Palmeiras', '10', 'Centro', 'São Paulo', 'SP', '01001-000', 'Casa'),
('10000000-0000-0000-0000-000000000002', 'Av. Paulista', '200', 'Bela Vista', 'São Paulo', 'SP', '01311-000', 'Apto 42'),
('10000000-0000-0000-0000-000000000003', 'Rua Augusta', '50', 'Consolação', 'São Paulo', 'SP', '01305-000', NULL),
('10000000-0000-0000-0000-000000000004', 'Rua da Consolação', '99', 'Consolação', 'São Paulo', 'SP', '01301-000', 'Bloco B'),
('10000000-0000-0000-0000-000000000005', 'Rua Oscar Freire', '1000', 'Jardins', 'São Paulo', 'SP', '01426-000', NULL),
('10000000-0000-0000-0000-000000000006', 'Rua Haddock Lobo', '500', 'Cerqueira César', 'São Paulo', 'SP', '01414-000', 'Fundos'),
('10000000-0000-0000-0000-000000000007', 'Alameda Santos', '85', 'Paraíso', 'São Paulo', 'SP', '01419-000', NULL),
('10000000-0000-0000-0000-000000000008', 'Rua Vergueiro', '300', 'Liberdade', 'São Paulo', 'SP', '01504-000', 'Apto 12'),
('10000000-0000-0000-0000-000000000009', 'Rua Domingos de Morais', '120', 'Vila Mariana', 'São Paulo', 'SP', '04009-000', NULL),
('10000000-0000-0000-0000-000000000010', 'Av. Jabaquara', '45', 'Mirandópolis', 'São Paulo', 'SP', '04045-000', 'Sobrado'),
('10000000-0000-0000-0000-000000000011', 'Rua Sena Madureira', '88', 'Vila Clementino', 'São Paulo', 'SP', '04021-000', NULL),
('10000000-0000-0000-0000-000000000012', 'Rua Botucatu', '70', 'Vila Clementino', 'São Paulo', 'SP', '04023-000', 'Casa 2'),
('10000000-0000-0000-0000-000000000013', 'Rua Pedro de Toledo', '22', 'Vila Clementino', 'São Paulo', 'SP', '04039-000', NULL),
('10000000-0000-0000-0000-000000000014', 'Rua Borges Lagoa', '900', 'Vila Clementino', 'São Paulo', 'SP', '04038-000', 'Apto 101'),
('10000000-0000-0000-0000-000000000015', 'Av. Ibirapuera', '3100', 'Moema', 'São Paulo', 'SP', '04029-000', NULL),
('10000000-0000-0000-0000-000000000016', 'Alameda dos Maracatins', '55', 'Moema', 'São Paulo', 'SP', '04089-000', NULL),
('10000000-0000-0000-0000-000000000017', 'Av. Moema', '12', 'Moema', 'São Paulo', 'SP', '04077-000', 'Cob 01'),
('10000000-0000-0000-0000-000000000018', 'Av. República do Líbano', '505', 'Ibirapuera', 'São Paulo', 'SP', '04501-000', NULL),
('10000000-0000-0000-0000-000000000019', 'Rua Bento de Andrade', '80', 'Jardim Paulista', 'São Paulo', 'SP', '04503-000', 'Casa Amarela');

-- Endereços dos Eventos (prefixo 1100...)
INSERT INTO endereco_evento (id_externo, rua, numero, bairro, cidade, estado, cep) VALUES
('11000000-0000-0000-0000-000000000001', 'Sitio do Recreio', 'Km 30', 'Interior', 'Atibaia', 'SP', '12940-000'),
('11000000-0000-0000-0000-000000000002', 'Parque Ibirapuera', 'S/N', 'Vila Mariana', 'São Paulo', 'SP', '04094-000');

-- ==================================================================================
-- 2. MEMBROS (Corrigido para hexadecimais válidos: prefixo 2000...)
-- ==================================================================================

INSERT INTO membro (
    id_externo, fk_igreja, fk_endereco, discipulador_id,
    nome, cpf, data_nascimento, data_registro, email, celular, senha,
    status, cargo_membro, genero_membro
) VALUES
('20000000-0000-0000-0000-000000000001', 1, 1, NULL, 'Pr. João Almeida', '11122233300', '1975-05-20', '2010-01-01', 'joao.pastor@email.com', '11999990001', 'senha123', 'ATIVO', 'LIDER_MINISTERIO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000002', 1, 2, 1, 'Pra. Maria Souza', '22233344400', '1980-08-15', '2012-06-15', 'maria.pra@email.com', '11999990002', 'senha123', 'ATIVO', 'LIDER_MINISTERIO', 'FEMININO'),
('20000000-0000-0000-0000-000000000003', 1, 3, 1, 'Dc. Pedro Santos', '33344455500', '1985-03-10', '2015-02-20', 'pedro.dc@email.com', '11999990003', 'senha123', 'ATIVO', 'LIDER_MINISTERIO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000004', 1, 4, 2, 'Ana Clara Silva', '44455566600', '1990-12-25', '2020-01-10', 'ana.clara@email.com', '11999990004', 'senha123', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000005', 1, 5, 2, 'Lucas Oliveira', '55566677700', '1992-07-07', '2021-03-15', 'lucas.oli@email.com', '11999990005', 'senha123', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000006', 1, 6, 2, 'Ester Rocha', '66677788800', '1995-11-30', '2021-05-20', 'ester.rocha@email.com', '11999990006', 'senha123', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000007', 1, 7, 2, 'Mateus Ferreira', '77788899900', '1998-01-05', '2022-01-12', 'mateus.fer@email.com', '11999990007', 'senha123', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000008', 1, 8, 2, 'Rebeca Lima', '88899900000', '2000-04-18', '2022-06-30', 'rebeca.lima@email.com', '11999990008', 'senha123', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000009', 1, 9, 2, 'Tiago Costa', '99900011100', '1988-09-09', '2019-11-05', 'tiago.costa@email.com', '11999990009', 'senha123', 'INATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000010', 1, 10, 2, 'Sarah Mendes', '00011122200', '1993-02-14', '2023-01-15', 'sarah.mendes@email.com', '11999990010', 'senha123', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000011', 1, 11, 3, 'Davi Barbosa', '12312312300', '1991-06-22', '2020-08-10', 'davi.barb@email.com', '11999990011', 'senha123', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000012', 1, 12, 3, 'Ruth Pereira', '32132132100', '1987-10-10', '2018-04-01', 'ruth.per@email.com', '11999990012', 'senha123', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000013', 1, 13, 3, 'Samuel Alves', '45645645600', '1996-12-01', '2023-02-28', 'samuel.alv@email.com', '11999990013', 'senha123', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000014', 1, 14, 3, 'Paulo Ribeiro', '65465465400', '1989-05-05', '2019-09-15', 'paulo.rib@email.com', '11999990014', 'senha123', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000015', 1, 15, 3, 'Silas Martins', '78978978900', '1994-08-08', '2021-12-20', 'silas.mar@email.com', '11999990015', 'senha123', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000016', 1, 16, 3, 'Debora Nunes', '98798798700', '1997-03-17', '2022-10-10', 'debora.nun@email.com', '11999990016', 'senha123', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000017', 1, 17, 3, 'Josué Campos', '15915915900', '1999-11-11', '2023-03-01', 'josue.cam@email.com', '11999990017', 'senha123', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000018', 1, 18, 3, 'Raquel Dias', '75375375300', '2001-01-20', '2023-04-05', 'raquel.dias@email.com', '11999990018', 'senha123', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000019', 1, 19, 3, 'Levi Cardoso', '95195195100', '1990-07-30', '2020-02-15', 'levi.car@email.com', '11999990019', 'senha123', 'INATIVO', 'MEMBRO', 'MASCULINO');

-- ==================================================================================
-- 3. MINISTÉRIOS (Corrigido para hexadecimais válidos: prefixo 3000...)
-- ==================================================================================
INSERT INTO ministerio (
    id_externo, fk_igreja, nome, data_criacao, nome_lider, status
) VALUES
('30000000-0000-0000-0000-000000000001', 1, 'Louvor e Adoração', '2015-01-01', 'Ester Rocha', 'ATIVO'),
('30000000-0000-0000-0000-000000000002', 1, 'Kids e Juniores', '2016-03-10', 'Ana Clara Silva', 'ATIVO'),
('30000000-0000-0000-0000-000000000003', 1, 'Recepção e Acolhimento', '2015-02-01', 'Rebeca Lima', 'ATIVO'),
('30000000-0000-0000-0000-000000000004', 1, 'Multimídia e Tech', '2018-06-15', 'Lucas Oliveira', 'ATIVO'),
('30000000-0000-0000-0000-000000000005', 1, 'Intercessão', '2015-01-01', 'Pra. Maria Souza', 'ATIVO');

-- ==================================================================================
-- 4. MEMBROS NOS MINISTÉRIOS (Corrigido para hexadecimais válidos: prefixo 4000...)
-- ==================================================================================

INSERT INTO membro_ministerio (
    id_externo, fk_membro, fk_ministerio, cargo_membro, nome_ministerio, data_registro
) VALUES
-- Ministério de Louvor
('40000000-0000-0000-0000-000000000001', 6, 1, 'LIDER_MINISTERIO', 'Louvor e Adoração', '2021-06-01'),
('40000000-0000-0000-0000-000000000002', 7, 1, 'MEMBRO_MINISTERIO', 'Louvor e Adoração', '2022-02-01'),
('40000000-0000-0000-0000-000000000003', 14, 1, 'MEMBRO_MINISTERIO', 'Louvor e Adoração', '2019-10-01'),
('40000000-0000-0000-0000-000000000004', 15, 1, 'MEMBRO_MINISTERIO', 'Louvor e Adoração', '2021-12-25'),

-- Ministério Kids
('40000000-0000-0000-0000-000000000005', 4, 2, 'LIDER_MINISTERIO', 'Kids e Juniores', '2020-02-01'),
('40000000-0000-0000-0000-000000000006', 10, 2, 'MEMBRO_MINISTERIO', 'Kids e Juniores', '2023-02-01'),
('40000000-0000-0000-0000-000000000007', 16, 2, 'MEMBRO_MINISTERIO', 'Kids e Juniores', '2022-11-01'),

-- Ministério Recepção
('40000000-0000-0000-0000-000000000008', 8, 3, 'LIDER_MINISTERIO', 'Recepção e Acolhimento', '2022-07-01'),
('40000000-0000-0000-0000-000000000009', 13, 3, 'MEMBRO_MINISTERIO', 'Recepção e Acolhimento', '2023-03-05'),
('40000000-0000-0000-0000-000000000010', 18, 3, 'MEMBRO_MINISTERIO', 'Recepção e Acolhimento', '2023-04-10'),

-- Ministério Multimídia
('40000000-0000-0000-0000-000000000011', 5, 4, 'LIDER_MINISTERIO', 'Multimídia e Tech', '2021-04-01'),
('40000000-0000-0000-0000-000000000012', 11, 4, 'MEMBRO_MINISTERIO', 'Multimídia e Tech', '2020-09-01'),
('40000000-0000-0000-0000-000000000013', 17, 4, 'MEMBRO_MINISTERIO', 'Multimídia e Tech', '2023-03-10'),

-- Ministério Intercessão
('40000000-0000-0000-0000-000000000014', 2, 5, 'LIDER_MINISTERIO', 'Intercessão', '2015-02-01'),
('40000000-0000-0000-0000-000000000015', 9, 5, 'MEMBRO_MINISTERIO', 'Intercessão', '2019-12-01'),
('40000000-0000-0000-0000-000000000016', 12, 5, 'MEMBRO_MINISTERIO', 'Intercessão', '2018-05-01'),
('40000000-0000-0000-0000-000000000017', 19, 5, 'MEMBRO_MINISTERIO', 'Intercessão', '2020-03-01'),
('40000000-0000-0000-0000-000000000018', 3, 5, 'MEMBRO_MINISTERIO', 'Intercessão', '2015-03-01');

-- ==================================================================================
-- 5. EVENTOS (Corrigido para hexadecimais válidos: prefixo 5000...)
-- ==================================================================================

INSERT INTO evento (
    id_externo, fk_igreja, fk_organizador, fk_endereco,
    nome, descricao, publico_alvo, data_hora_inicio, data_hora_fim, custo
) VALUES
('50000000-0000-0000-0000-000000000001', 1, 1, NULL, 'Culto da Família', 'Celebração principal de domingo', 'Toda a Família', '2024-05-12 18:00:00', '2024-05-12 20:00:00', 0.00),
('50000000-0000-0000-0000-000000000002', 1, 6, NULL, 'Sábado Jovem', 'Encontro da juventude com muito louvor', 'Jovens', '2024-05-18 19:30:00', '2024-05-18 21:30:00', 0.00),
('50000000-0000-0000-0000-000000000003', 1, 4, NULL, 'Tarde da Alegria', 'Brincadeiras e palavra para as crianças', 'Crianças 2-10 anos', '2024-05-25 14:00:00', '2024-05-25 17:00:00', 10.00),
('50000000-0000-0000-0000-000000000004', 1, 1, 1, 'Acampamento Renascer', 'Retiro anual de carnaval', 'Geral', '2024-02-10 08:00:00', '2024-02-13 12:00:00', 350.00),
('50000000-0000-0000-0000-000000000005', 1, 5, NULL, 'Workshop de Transmissão', 'Treinamento para equipe de vídeo', 'Voluntários Tech', '2024-06-01 09:00:00', '2024-06-01 12:00:00', 0.00),
('50000000-0000-0000-0000-000000000006', 1, 2, 2, 'Ide pelas Praças', 'Evangelismo e ação social no parque', 'Aberto', '2024-06-15 10:00:00', '2024-06-15 13:00:00', 0.00),
('50000000-0000-0000-0000-000000000007', 1, 2, NULL, 'Vigília da Virada', 'Oração pela madrugada', 'Intercessores', '2024-07-01 23:00:00', '2024-07-02 03:00:00', 0.00),
('50000000-0000-0000-0000-000000000008', 1, 8, NULL, 'Café de Boas Vindas', 'Recepção para novos visitantes', 'Visitantes', '2024-05-12 10:00:00', '2024-05-12 11:30:00', 0.00);

-- ==================================================================================
-- 6. VINCULO EVENTO x MINISTERIO (Este permaneceu igual pois usa FK interna)
-- ==================================================================================

-- ==================================================================================
-- 6. VINCULO EVENTO x MINISTERIO (CORRIGIDO: Inclusão da coluna 'IS_CONFIRMADO')
-- ==================================================================================

-- ==================================================================================
-- 6. VINCULO EVENTO x MINISTERIO (CORRIGIDO: Inclusão das colunas ID_EXTERNO e IS_CONFIRMADO)
-- ==================================================================================

INSERT INTO evento_ministerio (id_externo, fk_evento, fk_ministerio, is_confirmado) VALUES
('60000000-0000-0000-0000-000000000001', 1, 1, TRUE),  -- Culto Família / Louvor
('60000000-0000-0000-0000-000000000002', 1, 2, TRUE),  -- Culto Família / Kids
('60000000-0000-0000-0000-000000000003', 1, 3, TRUE),  -- Culto Família / Recepção
('60000000-0000-0000-0000-000000000004', 1, 4, TRUE),  -- Culto Família / Multimídia
('60000000-0000-0000-0000-000000000005', 1, 5, TRUE),  -- Culto Família / Intercessão
('60000000-0000-0000-0000-000000000006', 2, 1, TRUE),  -- Sábado Jovem / Louvor
('60000000-0000-0000-0000-000000000007', 2, 3, TRUE),  -- Sábado Jovem / Recepção
('60000000-0000-0000-0000-000000000008', 2, 4, TRUE),  -- Sábado Jovem / Multimídia
('60000000-0000-0000-0000-000000000009', 3, 2, TRUE),  -- Tarde Alegria / Kids
('60000000-0000-0000-0000-000000000010', 4, 1, TRUE),  -- Acampamento / Louvor
('60000000-0000-0000-0000-000000000011', 4, 2, TRUE),  -- Acampamento / Kids
('60000000-0000-0000-0000-000000000012', 4, 3, TRUE),  -- Acampamento / Recepção
('60000000-0000-0000-0000-000000000013', 4, 4, TRUE),  -- Acampamento / Multimídia
('60000000-0000-0000-0000-000000000014', 4, 5, TRUE),  -- Acampamento / Intercessão
('60000000-0000-0000-0000-000000000015', 5, 4, TRUE),  -- Workshop / Multimídia
('60000000-0000-0000-0000-000000000016', 6, 1, TRUE),  -- Ide Praças / Louvor
('60000000-0000-0000-0000-000000000017', 6, 5, TRUE),  -- Ide Praças / Intercessão
('60000000-0000-0000-0000-000000000018', 7, 1, TRUE),  -- Vigília / Louvor
('60000000-0000-0000-0000-000000000019', 7, 5, TRUE),  -- Vigília / Intercessão
('60000000-0000-0000-0000-000000000020', 8, 2, TRUE),  -- Café Boas Vindas / Kids
('60000000-0000-0000-0000-000000000021', 8, 3, TRUE); -- Café Boas Vindas / Recepção
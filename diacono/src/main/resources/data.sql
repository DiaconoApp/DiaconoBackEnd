-- ENDERECO IGREJA
INSERT INTO endereco_igreja (
    id_externo, bairro, cep, cidade, complemento, estado, numero, rua
)
VALUES
('d1f5e8b2-3c4a-4f6b-9e2d-5f7b8c9d0e1f', 'Centro', '12345-678', 'São Paulo', 'Apto 101', 'SP', '250', 'Rua das Flores'), -- id_interno 1
('a2b4c6d8-e0f1-2345-6789-0a1b2c3d4e5f', 'Jardim América', '87654-321', 'São Paulo', '', 'SP', '75', 'Avenida Brasil'), -- id_interno 2
('b7c3e2c3-8c37-43c9-9d0e-1f5b4c8b2c32', 'Centro', '01000-000', 'São Paulo', '', 'SP', '100', 'Rua Principal'),
('11111111-1111-1111-1111-111111111111', 'Jardim Elba', '03980070', 'São Paulo', '', 'São Paulo', '584', 'Rua Palmeira de Vinho'); -- id_interno 3

-- IGREJA
INSERT INTO igreja (
    id_externo, fk_endereco, cnpj, nome
)
VALUES
('550e8400-e29b-41d4-a716-446655440000', 1, '96.966.954/0001-22', 'ICF'),
('550e8400-e29b-41d4-a716-446655440001', 2, '98.765.432/0001-10', 'Assembleia da Graça Eterna'), -- id_interno 2
('f84aa4ac-0158-4ef0-9b5f-1762c4a86e53', 3, '12.345.678/0001-10', 'Igreja Congregação');


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
('10000000-0000-0000-0000-000000000019', 'Rua Bento de Andrade', '80', 'Jardim Paulista', 'São Paulo', 'SP', '04503-000', 'Casa Amarela'),
('10000000-0000-0000-0000-000000000020', 'Rua Afonso Braz', '150', 'Vila Nova Conceição', 'São Paulo', 'SP', '04511-000', NULL),
('10000000-0000-0000-0000-000000000021', 'Rua João Cachoeira', '900', 'Itaim Bibi', 'São Paulo', 'SP', '04535-000', 'Apto 55'),
('10000000-0000-0000-0000-000000000022', 'Rua Tabapuã', '400', 'Itaim Bibi', 'São Paulo', 'SP', '04533-000', NULL),
('10000000-0000-0000-0000-000000000023', 'Av. Santo Amaro', '2500', 'Brooklin', 'São Paulo', 'SP', '04555-000', NULL),
('10000000-0000-0000-0000-000000000024', 'Rua Joaquim Floriano', '100', 'Itaim Bibi', 'São Paulo', 'SP', '04534-000', 'Sala 10'),
('10000000-0000-0000-0000-000000000025', 'Av. Brigadeiro Faria Lima', '3000', 'Jardim Paulistano', 'São Paulo', 'SP', '01451-000', NULL),
('10000000-0000-0000-0000-000000000026', 'Rua Iguatemi', '350', 'Itaim Bibi', 'São Paulo', 'SP', '01451-010', NULL),
('10000000-0000-0000-0000-000000000027', 'Rua Amauri', '200', 'Jardim Europa', 'São Paulo', 'SP', '01448-000', 'Casa'),
('10000000-0000-0000-0000-000000000028', 'Av. Cidade Jardim', '800', 'Jardim Paulistano', 'São Paulo', 'SP', '01454-000', NULL),
('10000000-0000-0000-0000-000000000029', 'Rua Colômbia', '120', 'Jardim América', 'São Paulo', 'SP', '01438-000', NULL),
('10000000-0000-0000-0000-000000000030', 'Rua Estados Unidos', '1500', 'Jardim América', 'São Paulo', 'SP', '01427-000', NULL),
('10000000-0000-0000-0000-000000000031', 'Rua Groelândia', '450', 'Jardim Europa', 'São Paulo', 'SP', '01434-000', 'Sobrado'),
('10000000-0000-0000-0000-000000000032', 'Av. Europa', '600', 'Jardim Europa', 'São Paulo', 'SP', '01449-000', NULL),
('10000000-0000-0000-0000-000000000033', 'Rua da Mooca', '2000', 'Mooca', 'São Paulo', 'SP', '03104-000', NULL),
('10000000-0000-0000-0000-000000000034', 'Av. Paes de Barros', '500', 'Mooca', 'São Paulo', 'SP', '03114-000', 'Apto 88'),
('10000000-0000-0000-0000-000000000035', 'Rua do Oratório', '1200', 'Mooca', 'São Paulo', 'SP', '03116-000', NULL),
('10000000-0000-0000-0000-000000000036', 'Rua Juventus', '300', 'Parque da Mooca', 'São Paulo', 'SP', '03124-000', NULL),
('10000000-0000-0000-0000-000000000037', 'Rua Tuiuti', '1500', 'Tatuapé', 'São Paulo', 'SP', '03307-000', 'Bloco C'),
('10000000-0000-0000-0000-000000000038', 'Rua Coelho Lisboa', '400', 'Tatuapé', 'São Paulo', 'SP', '03323-000', NULL),
('10000000-0000-0000-0000-000000000039', 'Rua Serra de Bragança', '800', 'Tatuapé', 'São Paulo', 'SP', '03318-000', NULL),
('10000000-0000-0000-0000-000000000040', 'Rua Itapura', '950', 'Vila Gomes Cardim', 'São Paulo', 'SP', '03310-000', 'Apto 101'),
('10000000-0000-0000-0000-000000000041', 'Rua Emilia Marengo', '600', 'Vila Regente Feijó', 'São Paulo', 'SP', '03336-000', NULL),
('10000000-0000-0000-0000-000000000042', 'Av. Salim Farah Maluf', '3000', 'Tatuapé', 'São Paulo', 'SP', '03194-000', NULL),
('10000000-0000-0000-0000-000000000043', 'Rua Cantagalo', '550', 'Tatuapé', 'São Paulo', 'SP', '03319-000', NULL),
('10000000-0000-0000-0000-000000000044', 'Rua Antonio de Barros', '1200', 'Carrão', 'São Paulo', 'SP', '03401-000', 'Casa 1'),
('10000000-0000-0000-0000-000000000045', 'Av. Conselheiro Carrão', '2000', 'Vila Carrão', 'São Paulo', 'SP', '03402-000', NULL),
('10000000-0000-0000-0000-000000000046', 'Rua Evangelina', '400', 'Vila Carrão', 'São Paulo', 'SP', '03421-000', NULL),
('10000000-0000-0000-0000-000000000047', 'Rua Francisca de Paula', '300', 'Vila Carrão', 'São Paulo', 'SP', '03436-000', NULL),
('10000000-0000-0000-0000-000000000048', 'Rua Xiririca', '500', 'Vila Carrão', 'São Paulo', 'SP', '03441-000', NULL),
('10000000-0000-0000-0000-000000000049', 'Rua Lutécia', '800', 'Vila Carrão', 'São Paulo', 'SP', '03423-000', 'Casa de esquina'),
('10000000-0000-0000-0000-000000000050', 'Rua do Governo', '1', 'Centro', 'São Paulo', 'SP', '01000-001', NULL);

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
('20000000-0000-0000-0000-000000000001', 1, 1, NULL, 'Pr. João Almeida', '11122233300', '1975-05-20', '2010-01-01', 'joao.pastor@email.com', '11999990001', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'LIDER_MINISTERIO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000002', 1, 2, 1, 'Pra. Maria Souza', '22233344400', '1980-08-15', '2012-06-15', 'maria.pra@email.com', '11999990002', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'LIDER_MINISTERIO', 'FEMININO'),
('20000000-0000-0000-0000-000000000003', 1, 3, 1, 'Dc. Pedro Santos', '33344455500', '1985-03-10', '2015-02-20', 'pedro.dc@email.com', '11999990003', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000004', 1, 4, 2, 'Ana Clara Silva', '44455566600', '1990-12-25', '2020-01-10', 'ana.clara@email.com', '11999990004', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'LIDER_MINISTERIO', 'FEMININO'),
('20000000-0000-0000-0000-000000000005', 1, 5, 2, 'Lucas Oliveira', '55566677700', '1992-07-07', '2021-03-15', 'lucas.oli@email.com', '11999990005', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'LIDER_MINISTERIO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000006', 1, 6, 2, 'Ester Rocha', '66677788800', '1995-11-30', '2021-05-20', 'ester.rocha@email.com', '11999990006', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000007', 1, 7, 2, 'Mateus Ferreira', '77788899900', '1998-01-05', '2022-01-12', 'mateus.fer@email.com', '11999990007', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000008', 1, 8, 2, 'Rebeca Lima', '88899900000', '2000-04-18', '2022-06-30', 'rebeca.lima@email.com', '11999990008', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'LIDER_MINISTERIO', 'FEMININO'),
('20000000-0000-0000-0000-000000000009', 1, 9, 2, 'Tiago Costa', '99900011100', '1988-09-09', '2019-11-05', 'tiago.costa@email.com', '11999990009', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'INATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000010', 1, 10, 2, 'Sarah Mendes', '00011122200', '1993-02-14', '2023-01-15', 'sarah.mendes@email.com', '11999990010', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000011', 1, 11, 3, 'Davi Barbosa', '12312312300', '1991-06-22', '2020-08-10', 'davi.barb@email.com', '11999990011', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000012', 1, 12, 3, 'Ruth Pereira', '32132132100', '1987-10-10', '2018-04-01', 'ruth.per@email.com', '11999990012', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000013', 1, 13, 3, 'Samuel Alves', '45645645600', '1996-12-01', '2023-02-28', 'samuel.alv@email.com', '11999990013', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000014', 1, 14, 3, 'Paulo Ribeiro', '65465465400', '1989-05-05', '2019-09-15', 'paulo.rib@email.com', '11999990014', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000015', 1, 15, 3, 'Silas Martins', '78978978900', '1994-08-08', '2021-12-20', 'silas.mar@email.com', '11999990015', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000016', 1, 16, 3, 'Debora Nunes', '98798798700', '1997-03-17', '2022-10-10', 'debora.nun@email.com', '11999990016', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000017', 1, 17, 3, 'Josué Campos', '15915915900', '1999-11-11', '2023-03-01', 'josue.cam@email.com', '11999990017', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000018', 1, 18, 3, 'Raquel Dias', '75375375300', '2001-01-20', '2023-04-05', 'raquel.dias@email.com', '11999990018', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000019', 1, 19, 3, 'Levi Cardoso', '95195195100', '1990-07-30', '2020-02-15', 'levi.car@email.com', '11999990019', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'INATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000020', 1, 20, 2, 'Miriam Castro', '11122299900', '1992-04-12', '2023-05-10', 'miriam.cas@email.com', '11999990020', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000021', 1, 21, 3, 'Gabriel Araujo', '22233388800', '1990-09-25', '2021-08-15', 'gabriel.araujo@email.com', '11999990021', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000022', 1, 22, 1, 'Lívia Azevedo', '33344477700', '1985-12-05', '2019-03-20', 'livia.aze@email.com', '11999990022', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000023', 1, 23, 2, 'Rodrigo Freitas', '44455588800', '1998-07-18', '2023-06-01', 'rodrigo.frei@email.com', '11999990023', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000024', 1, 24, 3, 'Camila Batista', '55566699900', '1996-02-28', '2022-11-10', 'camila.bat@email.com', '11999990024', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000025', 1, 25, 2, 'Gustavo Moreira', '66677700011', '1989-10-10', '2020-04-15', 'gustavo.mor@email.com', '11999990025', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'INATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000026', 1, 26, 3, 'Juliana Melo', '77788811122', '1994-06-15', '2021-09-05', 'juliana.melo@email.com', '11999990026', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000027', 1, 27, 2, 'Rafael Correia', '88899922233', '1991-03-30', '2022-02-20', 'rafael.cor@email.com', '11999990027', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000028', 1, 28, 1, 'Fernanda Sales', '99900033344', '1987-11-22', '2018-07-12', 'fernanda.sales@email.com', '11999990028', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000029', 1, 29, 3, 'Daniel Cardoso', '00011144455', '1993-01-08', '2023-01-20', 'daniel.car@email.com', '11999990029', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000030', 1, 30, 2, 'Mariana Teixeira', '12345678910', '2000-05-14', '2023-07-01', 'mariana.teix@email.com', '11999990030', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000031', 1, 31, 3, 'André Neves', '23456789011', '1986-08-19', '2017-05-30', 'andre.neves@email.com', '11999990031', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000032', 1, 32, 2, 'Larissa Moraes', '34567890122', '1999-12-03', '2022-08-14', 'larissa.mor@email.com', '11999990032', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000033', 1, 33, 1, 'Eduardo Reis', '45678901233', '1982-04-27', '2016-10-10', 'eduardo.reis@email.com', '11999990033', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000034', 1, 34, 3, 'Beatriz Cunha', '56789012344', '1997-09-09', '2023-03-15', 'beatriz.cunha@email.com', '11999990034', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000035', 1, 35, 2, 'Thiago Machado', '67890123455', '1995-06-21', '2021-01-10', 'thiago.mac@email.com', '11999990035', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'INATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000036', 1, 36, 2, 'Gabriela Pinto', '78901234566', '1991-02-14', '2020-12-05', 'gabriela.pin@email.com', '11999990036', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000037', 1, 37, 3, 'Leonardo Santanna', '89012345677', '1988-11-30', '2019-06-25', 'leo.santanna@email.com', '11999990037', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000038', 1, 38, 2, 'Patrícia Guimarães', '90123456788', '1984-07-07', '2015-09-01', 'patricia.gui@email.com', '11999990038', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000039', 1, 39, 3, 'Ricardo Assis', '01234567899', '1996-03-12', '2022-04-18', 'ricardo.assis@email.com', '11999990039', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000040', 1, 40, 2, 'Vanessa Cruz', '12345098700', '1993-10-25', '2021-11-20', 'vanessa.cruz@email.com', '11999990040', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000041', 1, 41, 3, 'Marcelo Barros', '23456087611', '1990-05-05', '2019-08-08', 'marcelo.bar@email.com', '11999990041', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000042', 1, 42, 1, 'Renata Farias', '34567076522', '2001-01-15', '2023-02-10', 'renata.far@email.com', '11999990042', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000043', 1, 43, 2, 'Leandro Fonseca', '45678065433', '1985-09-28', '2018-01-15', 'leandro.fon@email.com', '11999990043', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000044', 1, 44, 3, 'Claudio Dantas', '56789054344', '1992-06-12', '2020-03-03', 'claudio.dan@email.com', '11999990044', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000045', 1, 45, 2, 'Helena Braga', '67890043255', '1998-08-30', '2022-09-25', 'helena.braga@email.com', '11999990045', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000046', 1, 46, 3, 'Otávio Lins', '78901032166', '1994-04-02', '2021-06-12', 'otavio.lins@email.com', '11999990046', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000047', 1, 47, 2, 'Isabela Campos', '89012021077', '1995-12-25', '2023-01-05', 'isabela.cam@email.com', '11999990047', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000048', 1, 48, 3, 'Vinícius Moura', '90123010988', '2002-02-18', '2023-08-20', 'vini.moura@email.com', '11999990048', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000049', 1, 49, 2, 'Cecília Pires', '01234009899', '1983-03-08', '2015-05-20', 'cecilia.pires@email.com', '11999990049', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000050', 1, 50, NULL, 'Admin Governo', '00000000000', '1990-01-01', '2024-01-01', 'admin@diacono.com', '11000000000', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'GOVERNO', 'MASCULINO');

-- ==================================================================================
-- 3. MINISTÉRIOS (Corrigido para hexadecimais válidos: prefixo 3000...)
-- ==================================================================================
INSERT INTO ministerio (
    id_externo, fk_igreja, nome, data_criacao, nome_lider, status
) VALUES
('30000000-0000-0000-0000-000000000001', 1, 'Louvor e Adoração', '2015-01-01', 'Pr. João Almeida', 'ATIVO'),
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
('40000000-0000-0000-0000-000000000001', 1, 1, 'LIDER_MINISTERIO', 'Louvor e Adoração', '2021-06-01'),
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
('40000000-0000-0000-0000-000000000018', 3, 5, 'MEMBRO_MINISTERIO', 'Intercessão', '2015-03-01'),

-- Continuação Louvor e Adoração (fk_ministerio 1)
('40000000-0000-0000-0000-000000000019', 20, 1, 'MEMBRO_MINISTERIO', 'Louvor e Adoração', '2023-06-15'),
('40000000-0000-0000-0000-000000000020', 25, 1, 'MEMBRO_MINISTERIO', 'Louvor e Adoração', '2020-06-01'),
('40000000-0000-0000-0000-000000000021', 30, 1, 'MEMBRO_MINISTERIO', 'Louvor e Adoração', '2023-08-01'),
('40000000-0000-0000-0000-000000000022', 35, 1, 'MEMBRO_MINISTERIO', 'Louvor e Adoração', '2021-03-15'),
('40000000-0000-0000-0000-000000000023', 40, 1, 'MEMBRO_MINISTERIO', 'Louvor e Adoração', '2022-01-10'),
('40000000-0000-0000-0000-000000000024', 45, 1, 'MEMBRO_MINISTERIO', 'Louvor e Adoração', '2022-11-20'),

-- Continuação Kids e Juniores (fk_ministerio 2)
('40000000-0000-0000-0000-000000000025', 21, 2, 'MEMBRO_MINISTERIO', 'Kids e Juniores', '2021-10-01'),
('40000000-0000-0000-0000-000000000026', 26, 2, 'MEMBRO_MINISTERIO', 'Kids e Juniores', '2021-11-15'),
('40000000-0000-0000-0000-000000000027', 31, 2, 'MEMBRO_MINISTERIO', 'Kids e Juniores', '2017-08-01'),
('40000000-0000-0000-0000-000000000028', 36, 2, 'MEMBRO_MINISTERIO', 'Kids e Juniores', '2021-02-01'),
('40000000-0000-0000-0000-000000000029', 41, 2, 'MEMBRO_MINISTERIO', 'Kids e Juniores', '2019-10-10'),
('40000000-0000-0000-0000-000000000030', 46, 2, 'MEMBRO_MINISTERIO', 'Kids e Juniores', '2021-08-12'),

-- Continuação Recepção e Acolhimento (fk_ministerio 3)
('40000000-0000-0000-0000-000000000031', 22, 3, 'MEMBRO_MINISTERIO', 'Recepção e Acolhimento', '2019-05-01'),
('40000000-0000-0000-0000-000000000032', 27, 3, 'MEMBRO_MINISTERIO', 'Recepção e Acolhimento', '2022-04-01'),
('40000000-0000-0000-0000-000000000033', 32, 3, 'MEMBRO_MINISTERIO', 'Recepção e Acolhimento', '2022-10-01'),
('40000000-0000-0000-0000-000000000034', 37, 3, 'MEMBRO_MINISTERIO', 'Recepção e Acolhimento', '2019-09-01'),
('40000000-0000-0000-0000-000000000035', 42, 3, 'MEMBRO_MINISTERIO', 'Recepção e Acolhimento', '2023-04-15'),
('40000000-0000-0000-0000-000000000036', 47, 3, 'MEMBRO_MINISTERIO', 'Recepção e Acolhimento', '2023-03-01'),

-- Continuação Multimídia e Tech (fk_ministerio 4)
('40000000-0000-0000-0000-000000000037', 23, 4, 'MEMBRO_MINISTERIO', 'Multimídia e Tech', '2023-07-15'),
('40000000-0000-0000-0000-000000000038', 28, 4, 'MEMBRO_MINISTERIO', 'Multimídia e Tech', '2018-09-01'),
('40000000-0000-0000-0000-000000000039', 33, 4, 'MEMBRO_MINISTERIO', 'Multimídia e Tech', '2017-02-15'),
('40000000-0000-0000-0000-000000000040', 38, 4, 'MEMBRO_MINISTERIO', 'Multimídia e Tech', '2015-11-01'),
('40000000-0000-0000-0000-000000000041', 43, 4, 'MEMBRO_MINISTERIO', 'Multimídia e Tech', '2018-05-20'),
('40000000-0000-0000-0000-000000000042', 48, 4, 'MEMBRO_MINISTERIO', 'Multimídia e Tech', '2023-09-01'),

-- Continuação Intercessão (fk_ministerio 5)
('40000000-0000-0000-0000-000000000043', 24, 5, 'MEMBRO_MINISTERIO', 'Intercessão', '2023-01-10'),
('40000000-0000-0000-0000-000000000044', 29, 5, 'MEMBRO_MINISTERIO', 'Intercessão', '2023-03-01'),
('40000000-0000-0000-0000-000000000045', 34, 5, 'MEMBRO_MINISTERIO', 'Intercessão', '2023-05-15'),
('40000000-0000-0000-0000-000000000046', 39, 5, 'MEMBRO_MINISTERIO', 'Intercessão', '2022-06-01'),
('40000000-0000-0000-0000-000000000047', 44, 5, 'MEMBRO_MINISTERIO', 'Intercessão', '2020-05-01'),
('40000000-0000-0000-0000-000000000048', 49, 5, 'MEMBRO_MINISTERIO', 'Intercessão', '2016-02-01');

-- ==================================================================================
-- 5. EVENTOS (Corrigido para hexadecimais válidos: prefixo 5000...)
-- ==================================================================================

INSERT INTO evento (
    id_externo, fk_igreja, fk_organizador, fk_endereco,
    nome, descricao, publico_alvo, data_hora_inicio, data_hora_fim, custo
) VALUES
('50000000-0000-0000-0000-000000000001', 1, 1, NULL, 'Culto da Família', 'Celebração principal de domingo', 'Toda a Família', '2025-05-12 18:00:00', '2025-05-12 20:00:00', 0.00),
('50000000-0000-0000-0000-000000000002', 1, 6, NULL, 'Sábado Jovem', 'Encontro da juventude com muito louvor', 'Jovens', '2025-05-18 19:30:00', '2025-05-18 21:30:00', 0.00),
('50000000-0000-0000-0000-000000000003', 1, 4, NULL, 'Tarde da Alegria', 'Brincadeiras e palavra para as crianças', 'Crianças 2-10 anos', '2025-05-25 14:00:00', '2025-05-25 17:00:00', 10.00),
('50000000-0000-0000-0000-000000000004', 1, 1,  NULL, 'Acampamento Renascer', 'Retiro anual de carnaval', 'Geral', '2025-02-10 08:00:00', '2025-02-13 12:00:00', 350.00),
('50000000-0000-0000-0000-000000000005', 1, 5, NULL, 'Workshop de Transmissão', 'Treinamento para equipe de vídeo', 'Voluntários Tech', '2025-06-01 09:00:00', '2025-06-01 12:00:00', 0.00),
('50000000-0000-0000-0000-000000000006', 1, 2,  NULL, 'Ide pelas Praças', 'Evangelismo e ação social no parque', 'Aberto', '2025-06-15 10:00:00', '2025-06-15 13:00:00', 0.00),
('50000000-0000-0000-0000-000000000007', 1, 2, NULL, 'Vigília da Virada', 'Oração pela madrugada', 'Intercessores', '2025-07-01 23:00:00', '2025-07-02 03:00:00', 0.00),
('50000000-0000-0000-0000-000000000008', 1, 8, NULL, 'Café de Boas Vindas', 'Recepção para novos visitantes', 'Visitantes', '2025-05-12 10:00:00', '2025-05-12 11:30:00', 0.00),
('50000000-0000-0000-0000-000000000009', 1, 1,  NULL, 'Retiro de Verão: Profundidade', 'Foco em oração e aprofundamento bíblico', 'Geral', '2026-01-17 18:00:00', '2026-01-20 16:00:00', 400.00),
('50000000-0000-0000-0000-000000000010', 1, 15, NULL, 'Reunião dos Diáconos', 'Planejamento e coordenação de serviços', 'Diáconos', '2026-01-29 20:00:00', '2026-01-29 21:30:00', 0.00),
('50000000-0000-0000-0000-000000000011', 1, 10, NULL, 'Culto de Missões', 'Foco em evangelismo transcultural', 'Geral', '2026-02-09 18:00:00', '2026-02-09 20:00:00', 0.00),
('50000000-0000-0000-0000-000000000012', 1, 18, NULL, 'Noite do Cinema Kids', 'Filme e pipoca para a criançada', 'Crianças', '2026-02-22 19:00:00', '2026-02-22 21:00:00', 5.00),
('50000000-0000-0000-0000-000000000013', 1, 12, NULL, 'Treinamento para Novos Membros', 'História e doutrina da igreja', 'Novos Membros', '2026-03-08 09:00:00', '2026-03-08 13:00:00', 0.00),
('50000000-0000-0000-0000-000000000014', 1, 14,  NULL, 'Jantar de Casais: Renovação', 'Palestra e jantar especial para casais', 'Casais', '2026-03-29 20:00:00', '2026-03-29 23:00:00', 120.00),
('50000000-0000-0000-0000-000000000015', 1, 6, NULL, 'Culto Especial de Páscoa', 'Celebração da Ressurreição', 'Geral', '2026-04-05 18:00:00', '2026-04-05 20:30:00', 0.00),
('50000000-0000-0000-0000-000000000016', 1, 7, NULL, 'Estudo Bíblico Semanal', 'Estudo aprofundado do livro de Atos', 'Geral', '2026-05-07 20:00:00', '2026-05-07 21:30:00', 0.00),
('50000000-0000-0000-0000-000000000017', 1, 2, NULL, 'Chá de Oração das Mulheres', 'Momento de comunhão e intercessão feminina', 'Mulheres', '2026-05-24 16:00:00', '2026-05-24 18:00:00', 15.00),
('50000000-0000-0000-0000-000000000018', 1, 11, NULL, 'Reunião de Líderes de Célula', 'Capacitação e alinhamento da visão', 'Líderes de Célula', '2026-06-04 20:00:00', '2026-06-04 21:30:00', 0.00),
('50000000-0000-0000-0000-000000000019', 1, 3,  NULL, 'Conferência de Louvor e Adoração', 'Palestra e prática para músicos e vocalistas', 'Equipes de Louvor', '2026-07-04 19:00:00', '2026-07-06 21:00:00', 80.00),
('50000000-0000-0000-0000-000000000020', 1, 9, NULL, 'Dia do Amigo Kids', 'Convite especial para os amiguinhos das crianças', 'Crianças e Convidados', '2026-07-19 14:00:00', '2026-07-19 17:00:00', 0.00),
('50000000-0000-0000-0000-000000000021', 1, 1, NULL, 'Semana de Santa Ceia', 'Momentos devocionais e Ceia do Senhor', 'Membros Batizados', '2026-08-06 20:00:00', '2026-08-06 21:30:00', 0.00),
('50000000-0000-0000-0000-000000000022', 1, 13,  NULL, 'Feira Vocacional Cristã', 'Orientação sobre carreira e vocação', 'Jovens e Adolescentes', '2026-08-23 09:00:00', '2026-08-23 17:00:00', 25.00),
('50000000-0000-0000-0000-000000000023', 1, 5, NULL, 'Atualização de Software e Streaming', 'Treinamento de novas ferramentas multimídia', 'Voluntários Tech', '2026-09-13 09:00:00', '2026-09-13 12:00:00', 0.00),
('50000000-0000-0000-0000-000000000024', 1, 16, NULL, 'Culto de Ação de Graças', 'Celebração pela colheita e provisão de Deus', 'Geral', '2026-10-12 18:00:00', '2026-10-12 20:00:00', 0.00),
('50000000-0000-0000-0000-000000000025', 1, 17, NULL, 'Campanha de Arrecadação de Roupas', 'Arrecadação para famílias carentes', 'Aberto', '2026-10-25 09:00:00', '2026-10-25 14:00:00', 0.00),
('50000000-0000-0000-0000-000000000026', 1, 4, NULL, 'Festa do Dia das Crianças', 'Celebração com gincanas e doces', 'Crianças', '2026-11-01 15:00:00', '2026-11-01 18:00:00', 5.00),
('50000000-0000-0000-0000-000000000027', 1, 8, NULL, 'Treinamento de Acolhimento', 'Capacitação para equipe de recepção', 'Voluntários de Recepção', '2026-11-22 09:00:00', '2026-11-22 12:00:00', 0.00),
('50000000-0000-0000-0000-000000000028', 1, 1, NULL, 'Ceia de Natal Comunitária', 'Celebração e confraternização de Natal', 'Geral', '2026-12-24 21:00:00', '2026-12-24 23:30:00', 50.00);

-- ==================================================================================
-- 6. VINCULO EVENTO x MINISTERIO (Este permaneceu igual pois usa FK interna)
-- ==================================================================================

-- ==================================================================================
-- 6. VINCULO EVENTO x MINISTERIO (CORRIGIDO: Inclusão da coluna 'IS_CONFIRMADO')
-- ==================================================================================

-- ==================================================================================
-- 6. VINCULO EVENTO x MINISTERIO (CORRIGIDO: Inclusão das colunas ID_EXTERNO e IS_CONFIRMADO)
-- ==================================================================================

INSERT INTO evento_ministerio (fk_evento, fk_ministerio) VALUES
(1, 1),  -- Culto Família / Louvor
(1, 2),  -- Culto Família / Kids
(1, 3),  -- Culto Família / Recepção
(1, 4),  -- Culto Família / Multimídia
(1, 5),  -- Culto Família / Intercessão
(2, 1),  -- Sábado Jovem / Louvor
(2, 3),  -- Sábado Jovem / Recepção
(2, 4),  -- Sábado Jovem / Multimídia
(3, 2),  -- Tarde Alegria / Kids
(4, 1),  -- Acampamento / Louvor
(4, 2),  -- Acampamento / Kids
(4, 3),  -- Acampamento / Recepção
(4, 4),  -- Acampamento / Multimídia
(4, 5),  -- Acampamento / Intercessão
(5, 4),  -- Workshop / Multimídia
(6, 1),  -- Ide Praças / Louvor
(6, 5),  -- Ide Praças / Intercessão
(7, 1),  -- Vigília / Louvor
(7, 5),  -- Vigília / Intercessão
(8, 2),  -- Café Boas Vindas / Kids
(8, 3), -- Café Boas Vindas / Recepção
-- Retiro de Verão: Profundidade (fk_evento 9) - Grande evento, envolve todos
(9, 1),
(9, 2),
(9, 3),
(9, 4),
(9, 5),

-- Reunião dos Diáconos (fk_evento 10) - Não requer muitos ministérios de suporte
(10, 5), -- Intercessão pela reunião

-- Culto de Missões (fk_evento 11) - Culto regular, foco em Louvor/Recepção/Multimídia
(11, 1),
(11, 3),
(11, 4),

-- Noite do Cinema Kids (fk_evento 12) - Foco em Kids e Multimídia
(12, 2),
(12, 4),

-- Treinamento para Novos Membros (fk_evento 13) - Suporte básico
(13, 3),
(13, 4),

-- Jantar de Casais: Renovação (fk_evento 14) - Requer Recepção e Louvor para ambiente
(14, 1),
(14, 3),

-- Culto Especial de Páscoa (fk_evento 15) - Grande evento, como o Culto da Família
(15, 1),
(15, 2),
(15, 3),
(15, 4),

-- Estudo Bíblico Semanal (fk_evento 16) - Suporte mínimo
(16, 4),
(16, 5),

-- Chá de Oração das Mulheres (fk_evento 17) - Foco em Intercessão e Recepção
(17, 3),
(17, 5),

-- Reunião de Líderes de Célula (fk_evento 18) - Suporte básico
(18, 4),

-- Conferência de Louvor e Adoração (fk_evento 19) - Foco total em Louvor e Multimídia
(19, 1),
(19, 3),
(19, 4),

-- Dia do Amigo Kids (fk_evento 20) - Foco em Kids e Recepção
(20, 2),
(20, 3),

-- Semana de Santa Ceia (fk_evento 21) - Culto principal, envolve todos
(21, 1),
(21, 3),
(21, 4),
(21, 5),

-- Feira Vocacional Cristã (fk_evento 22) - Grande evento aberto, foca em Recepção e Multimídia
(22, 3),
(22, 4),

-- Atualização de Software e Streaming (fk_evento 23) - Foco exclusivo em Multimídia
(23, 4),

-- Culto de Ação de Graças (fk_evento 24) - Culto principal
(24, 1),
(24, 3),
(24, 4),

-- Campanha de Arrecadação de Roupas (fk_evento 25) - Ação social, foca em Recepção/Acolhimento
(25, 3),

-- Festa do Dia das Crianças (fk_evento 26) - Foco em Kids e Recepção
(26, 2),
(26, 3),

-- Treinamento de Acolhimento (fk_evento 27) - Foco em Recepção
(27, 3),

-- Ceia de Natal Comunitária (fk_evento 28) - Grande evento de comunhão
(28, 1),
(28, 3),
(28, 4);
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
('10000000-0000-0000-0000-000000000049', 'Rua Lutécia', '800', 'Vila Carrão', 'São Paulo', 'SP', '03423-000', 'Casa de esquina');

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
('20000000-0000-0000-0000-000000000049', 1, 49, 2, 'Cecília Pires', '01234009899', '1983-03-08', '2015-05-20', 'cecilia.pires@email.com', '11999990049', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO');

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
    nome, descricao, publico_alvo, data_hora_inicio, data_hora_fim, custo, status
) VALUES
('50000000-0000-0000-0000-000000000001', 1, 1, NULL, 'Culto da Família', 'Celebração principal de domingo', 'Toda a Família', '2025-05-12 18:00:00', '2025-05-12 20:00:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000002', 1, 6, NULL, 'Sábado Jovem', 'Encontro da juventude com muito louvor', 'Jovens', '2025-05-18 19:30:00', '2025-05-18 21:30:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000003', 1, 4, NULL, 'Tarde da Alegria', 'Brincadeiras e palavra para as crianças', 'Crianças 2-10 anos', '2025-05-25 14:00:00', '2025-05-25 17:00:00', 10.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000004', 1, 1,  NULL, 'Acampamento Renascer', 'Retiro anual de carnaval', 'Geral', '2025-02-10 08:00:00', '2025-02-13 12:00:00', 350.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000005', 1, 5, NULL, 'Workshop de Transmissão', 'Treinamento para equipe de vídeo', 'Voluntários Tech', '2025-06-01 09:00:00', '2025-06-01 12:00:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000006', 1, 2,  NULL, 'Ide pelas Praças', 'Evangelismo e ação social no parque', 'Aberto', '2025-06-15 10:00:00', '2025-06-15 13:00:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000007', 1, 2, NULL, 'Vigília da Virada', 'Oração pela madrugada', 'Intercessores', '2025-07-01 23:00:00', '2025-07-02 03:00:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000008', 1, 8, NULL, 'Café de Boas Vindas', 'Recepção para novos visitantes', 'Visitantes', '2025-05-12 10:00:00', '2025-05-12 11:30:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000009', 1, 1,  NULL, 'Retiro de Verão: Profundidade', 'Foco em oração e aprofundamento bíblico', 'Geral', '2026-01-17 18:00:00', '2026-01-20 16:00:00', 400.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000010', 1, 15, NULL, 'Reunião dos Diáconos', 'Planejamento e coordenação de serviços', 'Diáconos', '2026-01-29 20:00:00', '2026-01-29 21:30:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000011', 1, 10, NULL, 'Culto de Missões', 'Foco em evangelismo transcultural', 'Geral', '2026-02-09 18:00:00', '2026-02-09 20:00:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000012', 1, 18, NULL, 'Noite do Cinema Kids', 'Filme e pipoca para a criançada', 'Crianças', '2026-02-22 19:00:00', '2026-02-22 21:00:00', 5.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000013', 1, 12, NULL, 'Treinamento para Novos Membros', 'História e doutrina da igreja', 'Novos Membros', '2026-03-08 09:00:00', '2026-03-08 13:00:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000014', 1, 14,  NULL, 'Jantar de Casais: Renovação', 'Palestra e jantar especial para casais', 'Casais', '2026-03-29 20:00:00', '2026-03-29 23:00:00', 120.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000015', 1, 6, NULL, 'Culto Especial de Páscoa', 'Celebração da Ressurreição', 'Geral', '2026-04-05 18:00:00', '2026-04-05 20:30:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000016', 1, 7, NULL, 'Estudo Bíblico Semanal', 'Estudo aprofundado do livro de Atos', 'Geral', '2026-05-07 20:00:00', '2026-05-07 21:30:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000017', 1, 2, NULL, 'Chá de Oração das Mulheres', 'Momento de comunhão e intercessão feminina', 'Mulheres', '2026-05-24 16:00:00', '2026-05-24 18:00:00', 15.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000018', 1, 11, NULL, 'Reunião de Líderes de Célula', 'Capacitação e alinhamento da visão', 'Líderes de Célula', '2026-06-04 20:00:00', '2026-06-04 21:30:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000019', 1, 3,  NULL, 'Conferência de Louvor e Adoração', 'Palestra e prática para músicos e vocalistas', 'Equipes de Louvor', '2026-07-04 19:00:00', '2026-07-06 21:00:00', 80.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000020', 1, 9, NULL, 'Dia do Amigo Kids', 'Convite especial para os amiguinhos das crianças', 'Crianças e Convidados', '2026-07-19 14:00:00', '2026-07-19 17:00:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000021', 1, 1, NULL, 'Semana de Santa Ceia', 'Momentos devocionais e Ceia do Senhor', 'Membros Batizados', '2026-08-06 20:00:00', '2026-08-06 21:30:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000022', 1, 13,  NULL, 'Feira Vocacional Cristã', 'Orientação sobre carreira e vocação', 'Jovens e Adolescentes', '2026-08-23 09:00:00', '2026-08-23 17:00:00', 25.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000023', 1, 5, NULL, 'Atualização de Software e Streaming', 'Treinamento de novas ferramentas multimídia', 'Voluntários Tech', '2026-09-13 09:00:00', '2026-09-13 12:00:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000024', 1, 16, NULL, 'Culto de Ação de Graças', 'Celebração pela colheita e provisão de Deus', 'Geral', '2026-10-12 18:00:00', '2026-10-12 20:00:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000025', 1, 17, NULL, 'Campanha de Arrecadação de Roupas', 'Arrecadação para famílias carentes', 'Aberto', '2026-10-25 09:00:00', '2026-10-25 14:00:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000026', 1, 4, NULL, 'Festa do Dia das Crianças', 'Celebração com gincanas e doces', 'Crianças', '2026-11-01 15:00:00', '2026-11-01 18:00:00', 5.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000027', 1, 8, NULL, 'Treinamento de Acolhimento', 'Capacitação para equipe de recepção', 'Voluntários de Recepção', '2026-11-22 09:00:00', '2026-11-22 12:00:00', 0.00, 'CONFIRMADO'),
('50000000-0000-0000-0000-000000000028', 1, 1, NULL, 'Ceia de Natal Comunitária', 'Celebração e confraternização de Natal', 'Geral', '2026-12-24 21:00:00', '2026-12-24 23:30:00', 50.00, 'CONFIRMADO');

-- ==================================================================================
-- 6. VINCULO EVENTO x MINISTERIO
-- ==================================================================================

INSERT INTO escala_evento (id_externo, fk_evento, fk_ministerio, status_escala_evento) VALUES
('60000000-0000-0000-0000-000000000001', 1, 1, 'CONFIRMADO'),  -- Culto Família / Louvor
('60000000-0000-0000-0000-000000000002', 1, 2, 'CONFIRMADO'),  -- Culto Família / Kids
('60000000-0000-0000-0000-000000000003', 1, 3, 'CONFIRMADO'),  -- Culto Família / Recepção
('60000000-0000-0000-0000-000000000004', 1, 4, 'CONFIRMADO'),  -- Culto Família / Multimídia
('60000000-0000-0000-0000-000000000005', 1, 5, 'CONFIRMADO'),  -- Culto Família / Intercessão
('60000000-0000-0000-0000-000000000006', 2, 1, 'CONFIRMADO'),  -- Sábado Jovem / Louvor
('60000000-0000-0000-0000-000000000007', 2, 3, 'CONFIRMADO'),  -- Sábado Jovem / Recepção
('60000000-0000-0000-0000-000000000008', 2, 4, 'CONFIRMADO'),  -- Sábado Jovem / Multimídia
('60000000-0000-0000-0000-000000000009', 3, 2, 'CONFIRMADO'),  -- Tarde Alegria / Kids
('60000000-0000-0000-0000-000000000010', 4, 1, 'CONFIRMADO'),  -- Acampamento / Louvor
('60000000-0000-0000-0000-000000000011', 4, 2, 'CONFIRMADO'),  -- Acampamento / Kids
('60000000-0000-0000-0000-000000000012', 4, 3, 'CONFIRMADO'),  -- Acampamento / Recepção
('60000000-0000-0000-0000-000000000013', 4, 4, 'CONFIRMADO'),  -- Acampamento / Multimídia
('60000000-0000-0000-0000-000000000014', 4, 5, 'CONFIRMADO'),  -- Acampamento / Intercessão
('60000000-0000-0000-0000-000000000015', 5, 4, 'CONFIRMADO'),  -- Workshop / Multimídia
('60000000-0000-0000-0000-000000000016', 6, 1, 'CONFIRMADO'),  -- Ide Praças / Louvor
('60000000-0000-0000-0000-000000000017', 6, 5, 'CONFIRMADO'),  -- Ide Praças / Intercessão
('60000000-0000-0000-0000-000000000018', 7, 1, 'CONFIRMADO'),  -- Vigília / Louvor
('60000000-0000-0000-0000-000000000019', 7, 5, 'CONFIRMADO'),  -- Vigília / Intercessão
('60000000-0000-0000-0000-000000000020', 8, 2, 'CONFIRMADO'),  -- Café Boas Vindas / Kids
('60000000-0000-0000-0000-000000000021', 8, 3, 'CONFIRMADO'), -- Café Boas Vindas / Recepção
-- Retiro de Verão: Profundidade (fk_evento 9) - Grande evento, envolve todos
('60000000-0000-0000-0000-000000000022', 9, 1, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000023', 9, 2, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000024', 9, 3, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000025', 9, 4, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000026', 9, 5, 'CONFIRMADO'),

-- Reunião dos Diáconos (fk_evento 10) - Não requer muitos ministérios de suporte
('60000000-0000-0000-0000-000000000027', 10, 5, 'CONFIRMADO'), -- Intercessão pela reunião

-- Culto de Missões (fk_evento 11) - Culto regular, foco em Louvor/Recepção/Multimídia
('60000000-0000-0000-0000-000000000028', 11, 1, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000029', 11, 3, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000030', 11, 4, 'CONFIRMADO'),

-- Noite do Cinema Kids (fk_evento 12) - Foco em Kids e Multimídia
('60000000-0000-0000-0000-000000000031', 12, 2, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000032', 12, 4, 'CONFIRMADO'),

-- Treinamento para Novos Membros (fk_evento 13) - Suporte básico
('60000000-0000-0000-0000-000000000033', 13, 3, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000034', 13, 4, 'CONFIRMADO'),

-- Jantar de Casais: Renovação (fk_evento 14) - Requer Recepção e Louvor para ambiente
('60000000-0000-0000-0000-000000000035', 14, 1, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000036', 14, 3, 'CONFIRMADO'),

-- Culto Especial de Páscoa (fk_evento 15) - Grande evento, como o Culto da Família
('60000000-0000-0000-0000-000000000037', 15, 1, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000038', 15, 2, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000039', 15, 3, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000040', 15, 4, 'CONFIRMADO'),

-- Estudo Bíblico Semanal (fk_evento 16) - Suporte mínimo
('60000000-0000-0000-0000-000000000041', 16, 4, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000042', 16, 5, 'CONFIRMADO'),

-- Chá de Oração das Mulheres (fk_evento 17) - Foco em Intercessão e Recepção
('60000000-0000-0000-0000-000000000043', 17, 3, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000044', 17, 5, 'CONFIRMADO'),

-- Reunião de Líderes de Célula (fk_evento 18) - Suporte básico
('60000000-0000-0000-0000-000000000045', 18, 4, 'CONFIRMADO'),

-- Conferência de Louvor e Adoração (fk_evento 19) - Foco total em Louvor e Multimídia
('60000000-0000-0000-0000-000000000046', 19, 1, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000047', 19, 3, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000048', 19, 4, 'CONFIRMADO'),

-- Dia do Amigo Kids (fk_evento 20) - Foco em Kids e Recepção
('60000000-0000-0000-0000-000000000049', 20, 2, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000050', 20, 3, 'CONFIRMADO'),

-- Semana de Santa Ceia (fk_evento 21) - Culto principal, envolve todos
('60000000-0000-0000-0000-000000000051', 21, 1, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000052', 21, 3, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000053', 21, 4, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000054', 21, 5, 'CONFIRMADO'),

-- Feira Vocacional Cristã (fk_evento 22) - Grande evento aberto, foca em Recepção e Multimídia
('60000000-0000-0000-0000-000000000055', 22, 3, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000056', 22, 4, 'CONFIRMADO'),

-- Atualização de Software e Streaming (fk_evento 23) - Foco exclusivo em Multimídia
('60000000-0000-0000-0000-000000000057', 23, 4, 'CONFIRMADO'),

-- Culto de Ação de Graças (fk_evento 24) - Culto principal
('60000000-0000-0000-0000-000000000058', 24, 1, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000059', 24, 3, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000060', 24, 4, 'CONFIRMADO'),

-- Campanha de Arrecadação de Roupas (fk_evento 25) - Ação social, foca em Recepção/Acolhimento
('60000000-0000-0000-0000-000000000061', 25, 3, 'CONFIRMADO'),

-- Festa do Dia das Crianças (fk_evento 26) - Foco em Kids e Recepção
('60000000-0000-0000-0000-000000000062', 26, 2, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000063', 26, 3, 'CONFIRMADO'),

-- Treinamento de Acolhimento (fk_evento 27) - Foco em Recepção
('60000000-0000-0000-0000-000000000064', 27, 3, 'CONFIRMADO'),

-- Ceia de Natal Comunitária (fk_evento 28) - Grande evento de comunhão
('60000000-0000-0000-0000-000000000065', 28, 1, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000066', 28, 3, 'CONFIRMADO'),
('60000000-0000-0000-0000-000000000067', 28, 4, 'CONFIRMADO');

-- ==================================================================================
-- 7. VINCULO ESCALA_EVENTO x MEMBRO_MINISTERIO
-- Regra: para cada escala_evento, incluir todos os membros do mesmo ministerio.
-- ==================================================================================

INSERT INTO escala_ministerio (id_externo, fk_escala_evento, fk_membro_ministerio, status_escala_ministerio) VALUES
(RANDOM_UUID(), 1, 1,  'CONFIRMADO'),  -- escala_evento 1 / ministerio 1
(RANDOM_UUID(), 1, 2, 'CONFIRMADO'),
(RANDOM_UUID(), 1, 3, 'CONFIRMADO'),
(RANDOM_UUID(), 1, 4,  'CONFIRMADO'),
(RANDOM_UUID(), 1, 19, 'CONFIRMADO'),
(RANDOM_UUID(), 1, 20, 'CONFIRMADO'),
(RANDOM_UUID(), 1, 21, 'CONFIRMADO'),
(RANDOM_UUID(), 1, 22, 'CONFIRMADO'),
(RANDOM_UUID(), 1, 23, 'CONFIRMADO'),
(RANDOM_UUID(), 1, 24, 'CONFIRMADO'),
(RANDOM_UUID(), 2, 5, 'CONFIRMADO'),  -- escala_evento 2 / ministerio 2
(RANDOM_UUID(), 2, 6, 'CONFIRMADO'),
(RANDOM_UUID(), 2, 7, 'CONFIRMADO'),
(RANDOM_UUID(), 2, 25, 'CONFIRMADO'),
(RANDOM_UUID(), 2, 26, 'CONFIRMADO'),
(RANDOM_UUID(), 2, 27, 'CONFIRMADO'),
(RANDOM_UUID(), 2, 28, 'CONFIRMADO'),
(RANDOM_UUID(), 2, 29, 'CONFIRMADO'),
(RANDOM_UUID(), 2, 30, 'CONFIRMADO'),
(RANDOM_UUID(), 3, 8, 'CONFIRMADO'),  -- escala_evento 3 / ministerio 3
(RANDOM_UUID(), 3, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 3, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 3, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 3, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 3, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 3, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 3, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 3, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 4, 11, 'CONFIRMADO'),  -- escala_evento 4 / ministerio 4
(RANDOM_UUID(), 4, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 4, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 4, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 4, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 4, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 4, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 4, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 4, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 5, 14, 'CONFIRMADO'),  -- escala_evento 5 / ministerio 5
(RANDOM_UUID(), 5, 15, 'CONFIRMADO'),
(RANDOM_UUID(), 5, 16, 'CONFIRMADO'),
(RANDOM_UUID(), 5, 17, 'CONFIRMADO'),
(RANDOM_UUID(), 5, 18, 'CONFIRMADO'),
(RANDOM_UUID(), 5, 43, 'CONFIRMADO'),
(RANDOM_UUID(), 5, 44, 'CONFIRMADO'),
(RANDOM_UUID(), 5, 45, 'CONFIRMADO'),
(RANDOM_UUID(), 5, 46, 'CONFIRMADO'),
(RANDOM_UUID(), 5, 47, 'CONFIRMADO'),
(RANDOM_UUID(), 5, 48, 'CONFIRMADO'),
(RANDOM_UUID(), 6, 1, 'CONFIRMADO'),  -- escala_evento 6 / ministerio 1
(RANDOM_UUID(), 6, 2, 'CONFIRMADO'),
(RANDOM_UUID(), 6, 3, 'CONFIRMADO'),
(RANDOM_UUID(), 6, 4, 'CONFIRMADO'),
(RANDOM_UUID(), 6, 19, 'CONFIRMADO'),
(RANDOM_UUID(), 6, 20, 'CONFIRMADO'),
(RANDOM_UUID(), 6, 21, 'CONFIRMADO'),
(RANDOM_UUID(), 6, 22, 'CONFIRMADO'),
(RANDOM_UUID(), 6, 23, 'CONFIRMADO'),
(RANDOM_UUID(), 6, 24, 'CONFIRMADO'),
(RANDOM_UUID(), 7, 8, 'CONFIRMADO'),  -- escala_evento 7 / ministerio 3
(RANDOM_UUID(), 7, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 7, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 7, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 7, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 7, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 7, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 7, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 7, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 8, 11, 'CONFIRMADO'),  -- escala_evento 8 / ministerio 4
(RANDOM_UUID(), 8, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 8, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 8, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 8, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 8, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 8, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 8, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 8, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 9, 5, 'CONFIRMADO'),  -- escala_evento 9 / ministerio 2
(RANDOM_UUID(), 9, 6, 'CONFIRMADO'),
(RANDOM_UUID(), 9, 7, 'CONFIRMADO'),
(RANDOM_UUID(), 9, 25, 'CONFIRMADO'),
(RANDOM_UUID(), 9, 26, 'CONFIRMADO'),
(RANDOM_UUID(), 9, 27, 'CONFIRMADO'),
(RANDOM_UUID(), 9, 28, 'CONFIRMADO'),
(RANDOM_UUID(), 9, 29, 'CONFIRMADO'),
(RANDOM_UUID(), 9, 30, 'CONFIRMADO'),
(RANDOM_UUID(), 10, 1, 'CONFIRMADO'),  -- escala_evento 10 / ministerio 1
(RANDOM_UUID(), 10, 2, 'CONFIRMADO'),
(RANDOM_UUID(), 10, 3, 'CONFIRMADO'),
(RANDOM_UUID(), 10, 4, 'CONFIRMADO'),
(RANDOM_UUID(), 10, 19, 'CONFIRMADO'),
(RANDOM_UUID(), 10, 20, 'CONFIRMADO'),
(RANDOM_UUID(), 10, 21, 'CONFIRMADO'),
(RANDOM_UUID(), 10, 22, 'CONFIRMADO'),
(RANDOM_UUID(), 10, 23, 'CONFIRMADO'),
(RANDOM_UUID(), 10, 24, 'CONFIRMADO'),
(RANDOM_UUID(), 11, 5, 'CONFIRMADO'),  -- escala_evento 11 / ministerio 2
(RANDOM_UUID(), 11, 6, 'CONFIRMADO'),
(RANDOM_UUID(), 11, 7, 'CONFIRMADO'),
(RANDOM_UUID(), 11, 25, 'CONFIRMADO'),
(RANDOM_UUID(), 11, 26, 'CONFIRMADO'),
(RANDOM_UUID(), 11, 27, 'CONFIRMADO'),
(RANDOM_UUID(), 11, 28, 'CONFIRMADO'),
(RANDOM_UUID(), 11, 29, 'CONFIRMADO'),
(RANDOM_UUID(), 11, 30, 'CONFIRMADO'),
(RANDOM_UUID(), 12, 8, 'CONFIRMADO'),  -- escala_evento 12 / ministerio 3
(RANDOM_UUID(), 12, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 12, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 12, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 12, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 12, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 12, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 12, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 12, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 13, 11, 'CONFIRMADO'),  -- escala_evento 13 / ministerio 4
(RANDOM_UUID(), 13, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 13, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 13, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 13, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 13, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 13, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 13, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 13, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 14, 14, 'CONFIRMADO'),  -- escala_evento 14 / ministerio 5
(RANDOM_UUID(), 14, 15, 'CONFIRMADO'),
(RANDOM_UUID(), 14, 16, 'CONFIRMADO'),
(RANDOM_UUID(), 14, 17, 'CONFIRMADO'),
(RANDOM_UUID(), 14, 18, 'CONFIRMADO'),
(RANDOM_UUID(), 14, 43, 'CONFIRMADO'),
(RANDOM_UUID(), 14, 44, 'CONFIRMADO'),
(RANDOM_UUID(), 14, 45, 'CONFIRMADO'),
(RANDOM_UUID(), 14, 46, 'CONFIRMADO'),
(RANDOM_UUID(), 14, 47, 'CONFIRMADO'),
(RANDOM_UUID(), 14, 48, 'CONFIRMADO'),
(RANDOM_UUID(), 15, 11, 'CONFIRMADO'),  -- escala_evento 15 / ministerio 4
(RANDOM_UUID(), 15, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 15, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 15, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 15, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 15, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 15, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 15, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 15, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 16, 1, 'CONFIRMADO'),  -- escala_evento 16 / ministerio 1
(RANDOM_UUID(), 16, 2, 'CONFIRMADO'),
(RANDOM_UUID(), 16, 3, 'CONFIRMADO'),
(RANDOM_UUID(), 16, 4, 'CONFIRMADO'),
(RANDOM_UUID(), 16, 19, 'CONFIRMADO'),
(RANDOM_UUID(), 16, 20, 'CONFIRMADO'),
(RANDOM_UUID(), 16, 21, 'CONFIRMADO'),
(RANDOM_UUID(), 16, 22, 'CONFIRMADO'),
(RANDOM_UUID(), 16, 23, 'CONFIRMADO'),
(RANDOM_UUID(), 16, 24, 'CONFIRMADO'),
(RANDOM_UUID(), 17, 14, 'CONFIRMADO'),  -- escala_evento 17 / ministerio 5
(RANDOM_UUID(), 17, 15, 'CONFIRMADO'),
(RANDOM_UUID(), 17, 16, 'CONFIRMADO'),
(RANDOM_UUID(), 17, 17, 'CONFIRMADO'),
(RANDOM_UUID(), 17, 18, 'CONFIRMADO'),
(RANDOM_UUID(), 17, 43, 'CONFIRMADO'),
(RANDOM_UUID(), 17, 44, 'CONFIRMADO'),
(RANDOM_UUID(), 17, 45, 'CONFIRMADO'),
(RANDOM_UUID(), 17, 46, 'CONFIRMADO'),
(RANDOM_UUID(), 17, 47, 'CONFIRMADO'),
(RANDOM_UUID(), 17, 48, 'CONFIRMADO'),
(RANDOM_UUID(), 18, 1, 'CONFIRMADO'),  -- escala_evento 18 / ministerio 1
(RANDOM_UUID(), 18, 2, 'CONFIRMADO'),
(RANDOM_UUID(), 18, 3, 'CONFIRMADO'),
(RANDOM_UUID(), 18, 4, 'CONFIRMADO'),
(RANDOM_UUID(), 18, 19, 'CONFIRMADO'),
(RANDOM_UUID(), 18, 20, 'CONFIRMADO'),
(RANDOM_UUID(), 18, 21, 'CONFIRMADO'),
(RANDOM_UUID(), 18, 22, 'CONFIRMADO'),
(RANDOM_UUID(), 18, 23, 'CONFIRMADO'),
(RANDOM_UUID(), 18, 24, 'CONFIRMADO'),
(RANDOM_UUID(), 19, 14, 'CONFIRMADO'),  -- escala_evento 19 / ministerio 5
(RANDOM_UUID(), 19, 15, 'CONFIRMADO'),
(RANDOM_UUID(), 19, 16, 'CONFIRMADO'),
(RANDOM_UUID(), 19, 17, 'CONFIRMADO'),
(RANDOM_UUID(), 19, 18, 'CONFIRMADO'),
(RANDOM_UUID(), 19, 43, 'CONFIRMADO'),
(RANDOM_UUID(), 19, 44, 'CONFIRMADO'),
(RANDOM_UUID(), 19, 45, 'CONFIRMADO'),
(RANDOM_UUID(), 19, 46, 'CONFIRMADO'),
(RANDOM_UUID(), 19, 47, 'CONFIRMADO'),
(RANDOM_UUID(), 19, 48, 'CONFIRMADO'),
(RANDOM_UUID(), 20, 5, 'CONFIRMADO'),  -- escala_evento 20 / ministerio 2
(RANDOM_UUID(), 20, 6, 'CONFIRMADO'),
(RANDOM_UUID(), 20, 7, 'CONFIRMADO'),
(RANDOM_UUID(), 20, 25, 'CONFIRMADO'),
(RANDOM_UUID(), 20, 26, 'CONFIRMADO'),
(RANDOM_UUID(), 20, 27, 'CONFIRMADO'),
(RANDOM_UUID(), 20, 28, 'CONFIRMADO'),
(RANDOM_UUID(), 20, 29, 'CONFIRMADO'),
(RANDOM_UUID(), 20, 30, 'CONFIRMADO'),
(RANDOM_UUID(), 21, 8, 'CONFIRMADO'),  -- escala_evento 21 / ministerio 3
(RANDOM_UUID(), 21, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 21, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 21, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 21, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 21, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 21, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 21, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 21, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 22, 1, 'CONFIRMADO'),  -- escala_evento 22 / ministerio 1
(RANDOM_UUID(), 22, 2, 'CONFIRMADO'),
(RANDOM_UUID(), 22, 3, 'CONFIRMADO'),
(RANDOM_UUID(), 22, 4, 'CONFIRMADO'),
(RANDOM_UUID(), 22, 19, 'CONFIRMADO'),
(RANDOM_UUID(), 22, 20, 'CONFIRMADO'),
(RANDOM_UUID(), 22, 21, 'CONFIRMADO'),
(RANDOM_UUID(), 22, 22, 'CONFIRMADO'),
(RANDOM_UUID(), 22, 23, 'CONFIRMADO'),
(RANDOM_UUID(), 22, 24, 'CONFIRMADO'),
(RANDOM_UUID(), 23, 5, 'CONFIRMADO'),  -- escala_evento 23 / ministerio 2
(RANDOM_UUID(), 23, 6, 'CONFIRMADO'),
(RANDOM_UUID(), 23, 7, 'CONFIRMADO'),
(RANDOM_UUID(), 23, 25, 'CONFIRMADO'),
(RANDOM_UUID(), 23, 26, 'CONFIRMADO'),
(RANDOM_UUID(), 23, 27, 'CONFIRMADO'),
(RANDOM_UUID(), 23, 28, 'CONFIRMADO'),
(RANDOM_UUID(), 23, 29, 'CONFIRMADO'),
(RANDOM_UUID(), 23, 30, 'CONFIRMADO'),
(RANDOM_UUID(), 24, 8, 'CONFIRMADO'),  -- escala_evento 24 / ministerio 3
(RANDOM_UUID(), 24, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 24, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 24, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 24, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 24, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 24, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 24, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 24, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 25, 11, 'CONFIRMADO'),  -- escala_evento 25 / ministerio 4
(RANDOM_UUID(), 25, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 25, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 25, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 25, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 25, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 25, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 25, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 25, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 26, 14, 'CONFIRMADO'),  -- escala_evento 26 / ministerio 5
(RANDOM_UUID(), 26, 15, 'CONFIRMADO'),
(RANDOM_UUID(), 26, 16, 'CONFIRMADO'),
(RANDOM_UUID(), 26, 17, 'CONFIRMADO'),
(RANDOM_UUID(), 26, 18, 'CONFIRMADO'),
(RANDOM_UUID(), 26, 43, 'CONFIRMADO'),
(RANDOM_UUID(), 26, 44, 'CONFIRMADO'),
(RANDOM_UUID(), 26, 45, 'CONFIRMADO'),
(RANDOM_UUID(), 26, 46, 'CONFIRMADO'),
(RANDOM_UUID(), 26, 47, 'CONFIRMADO'),
(RANDOM_UUID(), 26, 48, 'CONFIRMADO'),
(RANDOM_UUID(), 27, 14, 'CONFIRMADO'),  -- escala_evento 27 / ministerio 5
(RANDOM_UUID(), 27, 15, 'CONFIRMADO'),
(RANDOM_UUID(), 27, 16, 'CONFIRMADO'),
(RANDOM_UUID(), 27, 17, 'CONFIRMADO'),
(RANDOM_UUID(), 27, 18, 'CONFIRMADO'),
(RANDOM_UUID(), 27, 43, 'CONFIRMADO'),
(RANDOM_UUID(), 27, 44, 'CONFIRMADO'),
(RANDOM_UUID(), 27, 45, 'CONFIRMADO'),
(RANDOM_UUID(), 27, 46, 'CONFIRMADO'),
(RANDOM_UUID(), 27, 47, 'CONFIRMADO'),
(RANDOM_UUID(), 27, 48, 'CONFIRMADO'),
(RANDOM_UUID(), 28, 1, 'CONFIRMADO'),  -- escala_evento 28 / ministerio 1
(RANDOM_UUID(), 28, 2, 'CONFIRMADO'),
(RANDOM_UUID(), 28, 3, 'CONFIRMADO'),
(RANDOM_UUID(), 28, 4, 'CONFIRMADO'),
(RANDOM_UUID(), 28, 19, 'CONFIRMADO'),
(RANDOM_UUID(), 28, 20, 'CONFIRMADO'),
(RANDOM_UUID(), 28, 21, 'CONFIRMADO'),
(RANDOM_UUID(), 28, 22, 'CONFIRMADO'),
(RANDOM_UUID(), 28, 23, 'CONFIRMADO'),
(RANDOM_UUID(), 28, 24, 'CONFIRMADO'),
(RANDOM_UUID(), 29, 8, 'CONFIRMADO'),  -- escala_evento 29 / ministerio 3
(RANDOM_UUID(), 29, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 29, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 29, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 29, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 29, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 29, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 29, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 29, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 30, 11, 'CONFIRMADO'),  -- escala_evento 30 / ministerio 4
(RANDOM_UUID(), 30, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 30, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 30, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 30, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 30, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 30, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 30, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 30, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 31, 5, 'CONFIRMADO'),  -- escala_evento 31 / ministerio 2
(RANDOM_UUID(), 31, 6, 'CONFIRMADO'),
(RANDOM_UUID(), 31, 7, 'CONFIRMADO'),
(RANDOM_UUID(), 31, 25, 'CONFIRMADO'),
(RANDOM_UUID(), 31, 26, 'CONFIRMADO'),
(RANDOM_UUID(), 31, 27, 'CONFIRMADO'),
(RANDOM_UUID(), 31, 28, 'CONFIRMADO'),
(RANDOM_UUID(), 31, 29, 'CONFIRMADO'),
(RANDOM_UUID(), 31, 30, 'CONFIRMADO'),
(RANDOM_UUID(), 32, 11, 'CONFIRMADO'),  -- escala_evento 32 / ministerio 4
(RANDOM_UUID(), 32, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 32, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 32, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 32, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 32, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 32, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 32, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 32, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 33, 8, 'CONFIRMADO'),  -- escala_evento 33 / ministerio 3
(RANDOM_UUID(), 33, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 33, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 33, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 33, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 33, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 33, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 33, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 33, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 34, 11, 'CONFIRMADO'),  -- escala_evento 34 / ministerio 4
(RANDOM_UUID(), 34, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 34, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 34, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 34, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 34, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 34, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 34, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 34, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 35, 1, 'CONFIRMADO'),  -- escala_evento 35 / ministerio 1
(RANDOM_UUID(), 35, 2, 'CONFIRMADO'),
(RANDOM_UUID(), 35, 3, 'CONFIRMADO'),
(RANDOM_UUID(), 35, 4, 'CONFIRMADO'),
(RANDOM_UUID(), 35, 19, 'CONFIRMADO'),
(RANDOM_UUID(), 35, 20, 'CONFIRMADO'),
(RANDOM_UUID(), 35, 21, 'CONFIRMADO'),
(RANDOM_UUID(), 35, 22, 'CONFIRMADO'),
(RANDOM_UUID(), 35, 23, 'CONFIRMADO'),
(RANDOM_UUID(), 35, 24, 'CONFIRMADO'),
(RANDOM_UUID(), 36, 8, 'CONFIRMADO'),  -- escala_evento 36 / ministerio 3
(RANDOM_UUID(), 36, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 36, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 36, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 36, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 36, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 36, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 36, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 36, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 37, 1, 'CONFIRMADO'),  -- escala_evento 37 / ministerio 1
(RANDOM_UUID(), 37, 2, 'CONFIRMADO'),
(RANDOM_UUID(), 37, 3, 'CONFIRMADO'),
(RANDOM_UUID(), 37, 4, 'CONFIRMADO'),
(RANDOM_UUID(), 37, 19, 'CONFIRMADO'),
(RANDOM_UUID(), 37, 20, 'CONFIRMADO'),
(RANDOM_UUID(), 37, 21, 'CONFIRMADO'),
(RANDOM_UUID(), 37, 22, 'CONFIRMADO'),
(RANDOM_UUID(), 37, 23, 'CONFIRMADO'),
(RANDOM_UUID(), 37, 24, 'CONFIRMADO'),
(RANDOM_UUID(), 38, 5, 'CONFIRMADO'),  -- escala_evento 38 / ministerio 2
(RANDOM_UUID(), 38, 6, 'CONFIRMADO'),
(RANDOM_UUID(), 38, 7, 'CONFIRMADO'),
(RANDOM_UUID(), 38, 25, 'CONFIRMADO'),
(RANDOM_UUID(), 38, 26, 'CONFIRMADO'),
(RANDOM_UUID(), 38, 27, 'CONFIRMADO'),
(RANDOM_UUID(), 38, 28, 'CONFIRMADO'),
(RANDOM_UUID(), 38, 29, 'CONFIRMADO'),
(RANDOM_UUID(), 38, 30, 'CONFIRMADO'),
(RANDOM_UUID(), 39, 8, 'CONFIRMADO'),  -- escala_evento 39 / ministerio 3
(RANDOM_UUID(), 39, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 39, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 39, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 39, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 39, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 39, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 39, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 39, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 40, 11, 'CONFIRMADO'),  -- escala_evento 40 / ministerio 4
(RANDOM_UUID(), 40, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 40, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 40, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 40, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 40, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 40, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 40, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 40, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 41, 11, 'CONFIRMADO'),  -- escala_evento 41 / ministerio 4
(RANDOM_UUID(), 41, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 41, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 41, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 41, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 41, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 41, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 41, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 41, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 42, 14, 'CONFIRMADO'),  -- escala_evento 42 / ministerio 5
(RANDOM_UUID(), 42, 15, 'CONFIRMADO'),
(RANDOM_UUID(), 42, 16, 'CONFIRMADO'),
(RANDOM_UUID(), 42, 17, 'CONFIRMADO'),
(RANDOM_UUID(), 42, 18, 'CONFIRMADO'),
(RANDOM_UUID(), 42, 43, 'CONFIRMADO'),
(RANDOM_UUID(), 42, 44, 'CONFIRMADO'),
(RANDOM_UUID(), 42, 45, 'CONFIRMADO'),
(RANDOM_UUID(), 42, 46, 'CONFIRMADO'),
(RANDOM_UUID(), 42, 47, 'CONFIRMADO'),
(RANDOM_UUID(), 42, 48, 'CONFIRMADO'),
(RANDOM_UUID(), 43, 8, 'CONFIRMADO'),  -- escala_evento 43 / ministerio 3
(RANDOM_UUID(), 43, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 43, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 43, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 43, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 43, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 43, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 43, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 43, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 44, 14, 'CONFIRMADO'),  -- escala_evento 44 / ministerio 5
(RANDOM_UUID(), 44, 15, 'CONFIRMADO'),
(RANDOM_UUID(), 44, 16, 'CONFIRMADO'),
(RANDOM_UUID(), 44, 17, 'CONFIRMADO'),
(RANDOM_UUID(), 44, 18, 'CONFIRMADO'),
(RANDOM_UUID(), 44, 43, 'CONFIRMADO'),
(RANDOM_UUID(), 44, 44, 'CONFIRMADO'),
(RANDOM_UUID(), 44, 45, 'CONFIRMADO'),
(RANDOM_UUID(), 44, 46, 'CONFIRMADO'),
(RANDOM_UUID(), 44, 47, 'CONFIRMADO'),
(RANDOM_UUID(), 44, 48, 'CONFIRMADO'),
(RANDOM_UUID(), 45, 11, 'CONFIRMADO'),  -- escala_evento 45 / ministerio 4
(RANDOM_UUID(), 45, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 45, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 45, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 45, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 45, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 45, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 45, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 45, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 46, 1, 'CONFIRMADO'),  -- escala_evento 46 / ministerio 1
(RANDOM_UUID(), 46, 2, 'CONFIRMADO'),
(RANDOM_UUID(), 46, 3, 'CONFIRMADO'),
(RANDOM_UUID(), 46, 4, 'CONFIRMADO'),
(RANDOM_UUID(), 46, 19, 'CONFIRMADO'),
(RANDOM_UUID(), 46, 20, 'CONFIRMADO'),
(RANDOM_UUID(), 46, 21, 'CONFIRMADO'),
(RANDOM_UUID(), 46, 22, 'CONFIRMADO'),
(RANDOM_UUID(), 46, 23, 'CONFIRMADO'),
(RANDOM_UUID(), 46, 24, 'CONFIRMADO'),
(RANDOM_UUID(), 47, 8, 'CONFIRMADO'),  -- escala_evento 47 / ministerio 3
(RANDOM_UUID(), 47, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 47, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 47, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 47, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 47, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 47, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 47, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 47, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 48, 11, 'CONFIRMADO'),  -- escala_evento 48 / ministerio 4
(RANDOM_UUID(), 48, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 48, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 48, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 48, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 48, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 48, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 48, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 48, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 49, 5, 'CONFIRMADO'),  -- escala_evento 49 / ministerio 2
(RANDOM_UUID(), 49, 6, 'CONFIRMADO'),
(RANDOM_UUID(), 49, 7, 'CONFIRMADO'),
(RANDOM_UUID(), 49, 25, 'CONFIRMADO'),
(RANDOM_UUID(), 49, 26, 'CONFIRMADO'),
(RANDOM_UUID(), 49, 27, 'CONFIRMADO'),
(RANDOM_UUID(), 49, 28, 'CONFIRMADO'),
(RANDOM_UUID(), 49, 29, 'CONFIRMADO'),
(RANDOM_UUID(), 49, 30, 'CONFIRMADO'),
(RANDOM_UUID(), 50, 8, 'CONFIRMADO'),  -- escala_evento 50 / ministerio 3
(RANDOM_UUID(), 50, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 50, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 50, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 50, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 50, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 50, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 50, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 50, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 51, 1, 'CONFIRMADO'),  -- escala_evento 51 / ministerio 1
(RANDOM_UUID(), 51, 2, 'CONFIRMADO'),
(RANDOM_UUID(), 51, 3, 'CONFIRMADO'),
(RANDOM_UUID(), 51, 4, 'CONFIRMADO'),
(RANDOM_UUID(), 51, 19, 'CONFIRMADO'),
(RANDOM_UUID(), 51, 20, 'CONFIRMADO'),
(RANDOM_UUID(), 51, 21, 'CONFIRMADO'),
(RANDOM_UUID(), 51, 22, 'CONFIRMADO'),
(RANDOM_UUID(), 51, 23, 'CONFIRMADO'),
(RANDOM_UUID(), 51, 24, 'CONFIRMADO'),
(RANDOM_UUID(), 52, 8, 'CONFIRMADO'),  -- escala_evento 52 / ministerio 3
(RANDOM_UUID(), 52, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 52, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 52, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 52, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 52, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 52, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 52, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 52, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 53, 11, 'CONFIRMADO'),  -- escala_evento 53 / ministerio 4
(RANDOM_UUID(), 53, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 53, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 53, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 53, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 53, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 53, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 53, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 53, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 54, 14, 'CONFIRMADO'),  -- escala_evento 54 / ministerio 5
(RANDOM_UUID(), 54, 15, 'CONFIRMADO'),
(RANDOM_UUID(), 54, 16, 'CONFIRMADO'),
(RANDOM_UUID(), 54, 17, 'CONFIRMADO'),
(RANDOM_UUID(), 54, 18, 'CONFIRMADO'),
(RANDOM_UUID(), 54, 43, 'CONFIRMADO'),
(RANDOM_UUID(), 54, 44, 'CONFIRMADO'),
(RANDOM_UUID(), 54, 45, 'CONFIRMADO'),
(RANDOM_UUID(), 54, 46, 'CONFIRMADO'),
(RANDOM_UUID(), 54, 47, 'CONFIRMADO'),
(RANDOM_UUID(), 54, 48, 'CONFIRMADO'),
(RANDOM_UUID(), 55, 8, 'CONFIRMADO'),  -- escala_evento 55 / ministerio 3
(RANDOM_UUID(), 55, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 55, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 55, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 55, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 55, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 55, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 55, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 55, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 56, 11, 'CONFIRMADO'),  -- escala_evento 56 / ministerio 4
(RANDOM_UUID(), 56, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 56, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 56, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 56, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 56, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 56, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 56, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 56, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 57, 11, 'CONFIRMADO'),  -- escala_evento 57 / ministerio 4
(RANDOM_UUID(), 57, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 57, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 57, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 57, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 57, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 57, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 57, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 57, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 58, 1, 'CONFIRMADO'),  -- escala_evento 58 / ministerio 1
(RANDOM_UUID(), 58, 2, 'CONFIRMADO'),
(RANDOM_UUID(), 58, 3, 'CONFIRMADO'),
(RANDOM_UUID(), 58, 4, 'CONFIRMADO'),
(RANDOM_UUID(), 58, 19, 'CONFIRMADO'),
(RANDOM_UUID(), 58, 20, 'CONFIRMADO'),
(RANDOM_UUID(), 58, 21, 'CONFIRMADO'),
(RANDOM_UUID(), 58, 22, 'CONFIRMADO'),
(RANDOM_UUID(), 58, 23, 'CONFIRMADO'),
(RANDOM_UUID(), 58, 24, 'CONFIRMADO'),
(RANDOM_UUID(), 59, 8, 'CONFIRMADO'),  -- escala_evento 59 / ministerio 3
(RANDOM_UUID(), 59, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 59, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 59, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 59, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 59, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 59, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 59, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 59, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 60, 11, 'CONFIRMADO'),  -- escala_evento 60 / ministerio 4
(RANDOM_UUID(), 60, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 60, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 60, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 60, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 60, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 60, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 60, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 60, 42, 'CONFIRMADO'),
(RANDOM_UUID(), 61, 8, 'CONFIRMADO'),  -- escala_evento 61 / ministerio 3
(RANDOM_UUID(), 61, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 61, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 61, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 61, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 61, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 61, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 61, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 61, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 62, 5, 'CONFIRMADO'),  -- escala_evento 62 / ministerio 2
(RANDOM_UUID(), 62, 6, 'CONFIRMADO'),
(RANDOM_UUID(), 62, 7, 'CONFIRMADO'),
(RANDOM_UUID(), 62, 25, 'CONFIRMADO'),
(RANDOM_UUID(), 62, 26, 'CONFIRMADO'),
(RANDOM_UUID(), 62, 27, 'CONFIRMADO'),
(RANDOM_UUID(), 62, 28, 'CONFIRMADO'),
(RANDOM_UUID(), 62, 29, 'CONFIRMADO'),
(RANDOM_UUID(), 62, 30, 'CONFIRMADO'),
(RANDOM_UUID(), 63, 8, 'CONFIRMADO'),  -- escala_evento 63 / ministerio 3
(RANDOM_UUID(), 63, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 63, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 63, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 63, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 63, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 63, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 63, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 63, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 64, 8, 'CONFIRMADO'),  -- escala_evento 64 / ministerio 3
(RANDOM_UUID(), 64, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 64, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 64, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 64, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 64, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 64, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 64, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 64, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 65, 1, 'CONFIRMADO'),  -- escala_evento 65 / ministerio 1
(RANDOM_UUID(), 65, 2, 'CONFIRMADO'),
(RANDOM_UUID(), 65, 3, 'CONFIRMADO'),
(RANDOM_UUID(), 65, 4, 'CONFIRMADO'),
(RANDOM_UUID(), 65, 19, 'CONFIRMADO'),
(RANDOM_UUID(), 65, 20, 'CONFIRMADO'),
(RANDOM_UUID(), 65, 21, 'CONFIRMADO'),
(RANDOM_UUID(), 65, 22, 'CONFIRMADO'),
(RANDOM_UUID(), 65, 23, 'CONFIRMADO'),
(RANDOM_UUID(), 65, 24, 'CONFIRMADO'),
(RANDOM_UUID(), 66, 8, 'CONFIRMADO'),  -- escala_evento 66 / ministerio 3
(RANDOM_UUID(), 66, 9, 'CONFIRMADO'),
(RANDOM_UUID(), 66, 10, 'CONFIRMADO'),
(RANDOM_UUID(), 66, 31, 'CONFIRMADO'),
(RANDOM_UUID(), 66, 32, 'CONFIRMADO'),
(RANDOM_UUID(), 66, 33, 'CONFIRMADO'),
(RANDOM_UUID(), 66, 34, 'CONFIRMADO'),
(RANDOM_UUID(), 66, 35, 'CONFIRMADO'),
(RANDOM_UUID(), 66, 36, 'CONFIRMADO'),
(RANDOM_UUID(), 67, 11, 'CONFIRMADO'),  -- escala_evento 67 / ministerio 4
(RANDOM_UUID(), 67, 12, 'CONFIRMADO'),
(RANDOM_UUID(), 67, 13, 'CONFIRMADO'),
(RANDOM_UUID(), 67, 37, 'CONFIRMADO'),
(RANDOM_UUID(), 67, 38, 'CONFIRMADO'),
(RANDOM_UUID(), 67, 39, 'CONFIRMADO'),
(RANDOM_UUID(), 67, 40, 'CONFIRMADO'),
(RANDOM_UUID(), 67, 41, 'CONFIRMADO'),
(RANDOM_UUID(), 67, 42, 'CONFIRMADO');


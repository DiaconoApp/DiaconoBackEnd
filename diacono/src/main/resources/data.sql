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
('20000000-0000-0000-0000-000000000001', 1, 1, NULL, 'enc:v1:7DWyXWZgVVHA+M3i2Y0glIYbcy2WVOVzxEtRz8cA9xMM7+L4+bC5tRYrjqpj', 'enc:v1:XNDP6t7av7aG4LRe19rhjkbAPQbLQ2Zs3RZ6r13V1cEzv8NoTbfS', '1975-05-20', '2010-01-01', 'enc:v1:3iL+/z9lLd+PaLsl2DM/csRljp6k6/J379Qw8CYTbjdluXl0BgehttfpWr3bsMQSwA==', 'enc:v1:KJyBJft71sDD2iHZNzHtsLMvfJAL5pJS52+RFZda+b59gE1TIyfW', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'LIDER_MINISTERIO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000002', 1, 2, 1, 'enc:v1:C83nrlee38MGLB9AK5HCxg/42nhfMBcRpv+8YezsDvd61gwpR12kUrFMXqI=', 'enc:v1:jAYd1iykSsqsbgmuUFma8qUHKy5Mt9zW8rFOm/Fdxpydz47EyzEI', '1980-08-15', '2012-06-15', 'enc:v1:Yocy9eI37NxBNWCm5rCOiFZ9CXQiOsz6yfdLMtowr2VB0qrqW7Ds2+B/85gGJUA=', 'enc:v1:6ffDPgZvI0QeEt/vPAJZJ15n/91mBO8vHR5alVSL7mI18yL9jjg/', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'LIDER_MINISTERIO', 'FEMININO'),
('20000000-0000-0000-0000-000000000003', 1, 3, 1, 'enc:v1:0oIYHAVEsggQBcl5U4geu/32SUc/riiSpg1oBtLqQgN+dxE6LW5iVuezj7c=', 'enc:v1:rCG8djBBClv5VlxHvBwOW2+0iTeXZZXSE+XQYsCxsANR4SbX4a0p', '1985-03-10', '2015-02-20', 'enc:v1:qv3utxIZAeKq6ZkLV/U1hlQIHk5Ju/U3AfVyr6H65i/vB2rV8a030srKP6zo9w==', 'enc:v1:ZckmNv1bF1W70cPMHVsfnOf+OvCTrBZQfky7W207+p4FTL6/EYd/', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000004', 1, 4, 2, 'enc:v1:jrio4uvmk8D7eqpiEVYUMaUNQYol/ZXoNX9TRTQgYOCwX1kmWYprN2T6Yw==', 'enc:v1:inPbpBbNhHIO7ddOCP1WG3Xihfo1N5aB1ud+m05O9vZohnb2TDOX', '1990-12-25', '2020-01-10', 'enc:v1:oXJzPnd6CBIhnzUKkrRPtZ2gjPmRnuJGF90szQWKRKR7ejaERz/hO3UdPUUNu8c=', 'enc:v1:tgeRabAbFAHsJk8mD1KVO43RReTpvTX/JztK+DSPaYrcGJcPRQoa', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'LIDER_MINISTERIO', 'FEMININO'),
('20000000-0000-0000-0000-000000000005', 1, 5, 2, 'enc:v1:6+IYxhWM367Wv6xekc9aZG8WuHzs9qgSD6gxK8pPx2zZWCPOds7HG/66', 'enc:v1:nJzU58Z25hj35g66njOo7/8PDGJHjERM0ohx8jUTIRU76SJ5g/XN', '1992-07-07', '2021-03-15', 'enc:v1:XkSQdFoac1xoBcOWlxgKZkY5x/yIh14zIoHRGEzY2zPLVqTHPi7Xy/LUZchnud0=', 'enc:v1:L+2151mUu0Qw1qC+Keml4H0hrP90prOecYqIoZs/aNZ0w8DcUnAC', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'LIDER_MINISTERIO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000006', 1, 6, 2, 'enc:v1:s0ID6/0IpeAwXP0kINLJ4DZi+WeQRcXZmSKINa/01OM9KyTELZmD', 'enc:v1:xKQl+9peFPaQNPGvqb8xWjQEeqLjWEdW9ptx0zA9Vuiy5F1wEg2R', '1995-11-30', '2021-05-20', 'enc:v1:Rf+vaq2AG+uLI0ennSbjDovWonRU7liSleaqhy2TKimv48E3J2qptkJR2+Ov9MqY/A==', 'enc:v1:Ih66PA7Xf7VwsD6p5P8WT/Vj+9VbrjhlFk4pY498BGOyo3CqNryG', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000007', 1, 7, 2, 'enc:v1:gjhNFWP1fhz6tJgY/rwbwRLT+gI+tGmRpY4X2KvSDgU/TG2pC2cF5/dFXw==', 'enc:v1:ZzesZqykIuEFJSKb70aKfon93um75mgEvhhcm5BJi1XhmXIzeI72', '1998-01-05', '2022-01-12', 'enc:v1:lhkX3NCENWzerQCI6cmc0/ZAM1qoFiwcs5FY/V6k8inRJQ47qCqNr82ZqnCIjyI2', 'enc:v1:Vy+WJWD4/D0wOjMrbhQLQh+CAHOTbUSlD8VkMLftrVuDKyBXPX8p', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000008', 1, 8, 2, 'enc:v1:6EyzRzDF3Z3ewRKapPZ8NLETrSnxwoDReCG5uBi6n1UQ60vNs29U', 'enc:v1:Pft4CjJdxKomP9ae0IJp6NFwMHkolSRoaM6R2gVC1UBM7u1pDjlB', '2000-04-18', '2022-06-30', 'enc:v1:pjhmmot0O5f9/jrJFXSDD1AbYrF6iipCKC2IHJA+S3r5fJzbpyyLS1DWRovh/5Ntuw==', 'enc:v1:YjLWqlUuGbbiGRrN/Au3hhBYtY4jZR78rie5OJkia2nQcolWOLcc', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000009', 1, 9, 2, 'enc:v1:ex3Sgdi6DZhVAmbDPp1dWDK1+3VjfbkcvFbPFJYThLA0qnkrUakD', 'enc:v1:g2XxmiryFNFgMR23I6VU6ZVWACvhkoRuADZBGqdw6rzXyEVYU+64', '1988-09-09', '2019-11-05', 'enc:v1:7VQ73QGyRea7PfIyl9+HhjtVYoO/joP1hpUGHMT7CLqJ6EojmyeMfnf/qsAOc6/jCw==', 'enc:v1:bi2cP1nnxYZMuH4PqNHtM9FW6A7SZ0UFTUdB+bt+UbFzaZksBUg+', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'INATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000010', 1, 10, 2, 'enc:v1:FWsU3GjO0tNo6dQ2d6TFxjw5m/1K0srwyY6kfyC2JAaQSQbPypLP8w==', 'enc:v1:HpgLNJ2ffaoSCOSyUBTROHHGz3iKCbWBR7oUz21eCgy/TadLX+kb', '1993-02-14', '2023-01-15', 'enc:v1:7FYqTC1KybwKbBuN9qkfLzJhNUTa6TlyUwkbKb+ryqALegUtLKWYvJGR7eAhX0y7Mmc=', 'enc:v1:4CxzFmLu/nVLDsZDDRR6mjmfSJBLBZelBpQCrVaw6a5dRTodj7Aw', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000011', 1, 11, 3, 'enc:v1:aKq/EVpn3M5ej9GZWtVdSever9XC1oWu21yDCc0I32dH6OWIHzUgMg==', 'enc:v1:9RI2wdeg0CGGHjHxjSkV2tTkHk1gwyKbk8o9PLw2ZEr2y/uykUbd', '1991-06-22', '2020-08-10', 'enc:v1:muyrRevhKbvlJfsnAcuF17QS38Ay0Yfkd/vNf9WUf9F/V4O6pG097bxh1/9mfac=', 'enc:v1:pcPAGFDWyf6f1pi5yxga57QFi011B+TG+YYvqJY0gkzP3c8fFhU7', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000012', 1, 12, 3, 'enc:v1:BRmlw0PPLmzjMeunoxluGd+v65cMzXoDlA57awPcwy+knU/DmEQl1g==', 'enc:v1:RLLAiYLSWOlUq2hTVeoIehQdHLHtP5C02P/6+3gVCLWtnj8VaWX5', '1987-10-10', '2018-04-01', 'enc:v1:suJ44NXkyUTdoqzDngiSMRGahZ7tgxGJBs70l1QG9y0AIIG7f34aqGyHGGcKkQ==', 'enc:v1:EKd0Fq7ByPsnz6eImselKt9i64E6iHryRr8ksoEmmPHaHImO/Df1', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000013', 1, 13, 3, 'enc:v1:DwcBw79s38Bosu/fMQJKOUeVgmxcOeiJ9bA4k0unwBvBKl/i0C0p0Q==', 'enc:v1:i2jHKZirgMrHHXXNofq9ZYmjDT1BaBU9svShaurK5xcbtocbm4Hv', '1996-12-01', '2023-02-28', 'enc:v1:1S5HsDclinr6NpDPRkwFJsCbiakIdV+YTGvaRCz3yNLUJfPAQILuGxP5Y9k957/+', 'enc:v1:0Aj0190+zyRtbV9+PZRBV5gD2HLl+Z7NB3OpeiVmm1gqtIyNe+6i', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000014', 1, 14, 3, 'enc:v1:n30Xm/beEalj7FFz3VBBNWQ0B70bCWYuj3D57iQu2tPGnhPuMtAbdAw=', 'enc:v1:DLJJpT4cUCb1oYNxpEeSeGD285HrfZ6tU/GrIKh99cbtAkuAkI7J', '1989-05-05', '2019-09-15', 'enc:v1:laqufC4uo9jwLv4KkcDBxGaINjHvrNSRcXEgYwxbjlcmNhuUalPwDyJG7nmvkg4=', 'enc:v1:EncBcKjgiXsxIx0B2N6t0uwtOVKCx83pj9LsLtW4U6XL58sP78eg', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000015', 1, 15, 3, 'enc:v1:XG9tNAxftcSyAqwbHt19+kNVHjrcp8LktE9wgZwHwS+9efArXaiZPRw=', 'enc:v1:cnEFN7SvAwK+3Wzbwc4SkneEvArQWUEZRN8RB/RiA8zcHujpq6n0', '1994-08-08', '2021-12-20', 'enc:v1:N/3yk/p5TFqyNkXENQiE0uflTS7pUw8a9VlqqqipFUMNmlfD5fAEWwwftvuf5Zc=', 'enc:v1:Q57dQJxa1fEvILbLCuNNI92Guy/efhF2gpj974EhyTMYnXvTJJiX', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000016', 1, 16, 3, 'enc:v1:QL7LdIbXz65PWxTr27dHoBPpx+Mo2WaUKdvU8x330MMnxGKQtmVr7Q==', 'enc:v1:oWPlDcVeF6nszbHBXGIi8rrLZ33JAKj/ddTJxacOERBxEgzNZBWA', '1997-03-17', '2022-10-10', 'enc:v1:kJYvNyQ8ovST5Xp6234tRG9dJpVLljUzC9hvThhu3hLJg5mqIfv8SOHKvpcXTH5w', 'enc:v1:M9aN7mvnBPM8s8LQQ+yiunM5dM6FbU0XSsdttArpAWcMgdHPe4dd', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000017', 1, 17, 3, 'enc:v1:q5UX43AITEAGCwdVpaFaafJBljZKHl773jkLUvx/tv9DTeJSzWTIUdY=', 'enc:v1:o0uT2UD9Rr+BY8L/26odToPRMGenDLPEBO04a4n3C5xSa9S7bXYB', '1999-11-11', '2023-03-01', 'enc:v1:DlmHqkHm9+/vQA0aVVQyCGY6Yl2mw8OG3AWd4t+AM4Yuhdl7pibJ92pRl/b9Sk0=', 'enc:v1:TqE/oerk3EPFpujGEiFdbPTIlXQSczMLtKnuvm3ZY1s1k5SHdFik', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000018', 1, 18, 3, 'enc:v1:2o18shP1yboWSXstehY20BwTfzL7luBqFyEn6NC7FCjeP40wLtHP', 'enc:v1:fvh8wPuVY1/aQHyZYG1ek39BcWKN+u+yndFQG2R0OBt35S9E9MVa', '2001-01-20', '2023-04-05', 'enc:v1:XFIq+B+4Dsdw3PFBLP47DkulKLT3W4hGWOLL8AfHxl/YXFHXRl6W7iDi9fQ68gQHvg==', 'enc:v1:7NPIVcz2eSgbdNjRVOGTUgHdx5u+sTTeLq1M/InlV7yAhV6BjrZy', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000019', 1, 19, 3, 'enc:v1:Lieb5A/VzN0AzwC7UjzfsZyWMOam8Sw272XFsLu87WOy6HmJPk8Nyg==', 'enc:v1:HfcuGHFMosC2EjQ11/7vLeXut9AfjTnuZu+0z1yMFShlCA1ytZz7', '1990-07-30', '2020-02-15', 'enc:v1:3Myd1uy0oW2H8iOFMMEvIo/QhqJhTxZOg+TjmI2zWDkVUwtHgbEF1gNknNcxnA==', 'enc:v1:k8iGQ84KXg/YWEnxBpXiOgqh+SJb4e1XWsC46ca3hU/v4Qmbup29', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'INATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000020', 1, 20, 2, 'enc:v1:0XXaTkAhwHF4CPM2QPtAqygCiRiZDlqqancikCJtUTkLXlpl1ezi+yc=', 'enc:v1:0BVM//KJc4p8+v698lN9+d6EhQ/ZMBcsP+CIRPbtmZ+OuyPTfnKV', '1992-04-12', '2023-05-10', 'enc:v1:L386TTinxkjrXDSqyY4IMePLrl9VG8mcfF0aPSjSktXY1MO4S+xJl3yqQet4dDEk', 'enc:v1:9NvHv6LUYodMex4m7CS/osQAe5Sp0xvRjGkkmf7x5w7vW9IrmQbw', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000021', 1, 21, 3, 'enc:v1:OpDDTe4lxC5HlzzrybCxQlIe80esw/T842DStqR0BPOM+XU4YaCeaReY', 'enc:v1:ty5dUpCgwawlj29+tLvzcCPR+pIOhIBBfuDcylaJDQ9AT0mB68HR', '1990-09-25', '2021-08-15', 'enc:v1:UWHOuFHpZoj5ciOBqH7E+s3E9E0E/sZvQWFcUFnfvclNp1otkO0Il8bBpCbhUWrahTizAQ==', 'enc:v1:AOifhUavuqwz2Djfj8zB0BdZmOiOlPhyitcXoBZoXSwG0zbQqrf5', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000022', 1, 22, 1, 'enc:v1:7zTqkeksajMdrFxzMXTAvdslTPYTOpyVnLE2Gadg9LkmtkzV1HRTPbLs', 'enc:v1:GV3yNMLamUahh/qbrne7Abr595VzVWtamE4n/xyhkK7tOthYELB8', '1985-12-05', '2019-03-20', 'enc:v1:6EqtnsCZYIa3RISVX+dyapCZi/E9j/xl6AXsmO56mwsBjkyXsTenyZeSucNaDrk=', 'enc:v1:PlBbPrOouFPmenMHRn8WyUhXwDs/2fyBw7zb5/EO8umfzHTKSZqj', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000023', 1, 23, 2, 'enc:v1:CFrxIpzDLnL/C14eTh/msmGhGMBO/Xf6IenKxG++THMbghmhmNOAhHmJvA==', 'enc:v1:HeBsciCJac8rupQpauQF8n7ykTLz2ofO7epxuOhOh7q2yXN/3vdP', '1998-07-18', '2023-06-01', 'enc:v1:TMUmAMV2tJvGMtzxOJMB13ALoxFqqwGgb7Ct14KFZ/HpGCaFl/oPoq7KknDyiiJ22kc=', 'enc:v1:opQUaST+J5dF3WDAEgfxIfCshftQ9U1mCUN5VN0Dc5gLH8Q9jY5s', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000024', 1, 24, 3, 'enc:v1:rLswK4CWEpDQSE95aJvQux06hXidKf1/i++WMWmMTuX92AFBfKZSURWK', 'enc:v1:ijz7dvRmf60e1saPzj3c3GFb1EdazbM07JRSFs1YuPHfPQM3spSk', '1996-02-28', '2022-11-10', 'enc:v1:+zuEwZlW6/bv5Tb8vlg2CwuCig1ucNrX21k5Zm5uGomsiBsr0YKqsaMslHDUaig9', 'enc:v1:1m8KR0U+EnLAALcter6fJXFGYVXSzdIPZF9DOJ8ZP0aCvVF0n25Q', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000025', 1, 25, 2, 'enc:v1:LV4mbGNU3C5uGFw3rhWojmI+iw74xn6KAFcbwmVxWXZZ4utZdQP/Y1p0ng==', 'enc:v1:Zrm/AwN8FwWvhdZjJe4FIAGC88N86nRH8/lQDJ9Fyc1KWvG4BIOZ', '1989-10-10', '2020-04-15', 'enc:v1:6c2YVvEmwZ5hybz39bTjgnPQTLpRnSkp2eHoRl6kXGJonpJrGjk1id2jwDwXog9ArA==', 'enc:v1:XzZHshIGC6GCqI9E0tjlPa48ywboTCD19t5lhY/w+ceuLkuOKUJx', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'INATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000026', 1, 26, 3, 'enc:v1:nNE3C+cgZrY/SEOKKr2Q7mE2N7gqFEgFxsDPspfWtU0BOLo1nHeu9g==', 'enc:v1:1d8uXLa+xczn11UyfSufVGNX+p2YDFvZQ/HtBlC4oJx3Ry0hXwHt', '1994-06-15', '2021-09-05', 'enc:v1:poTjju2cYzZ0bCGOxPUOEytu7qOIiV1GEysbyaANtRwdC3OVdCmbaUy6A+aNqA/kYjk=', 'enc:v1:yUNR/xoap2OM6oYdyXW5ROU4WBG5KBKzn7fcR//2SB0W8RfjerXZ', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000027', 1, 27, 2, 'enc:v1:eBXI7YssTdu61bBOxipcPcDNQehZaM3cAzkzFEnaGulvT+25jdvQbwaL', 'enc:v1:ACK+Rb/qroO8woOtwN0RqI3raRne5/8qninStqgxeRttb3ADfIYW', '1991-03-30', '2022-02-20', 'enc:v1:4FYaIc/hdSIdOPcfQaNwoWZOgYySLjLwcCpypt9n0Ql9tDWkTwPfzAqMoc7hhbOa', 'enc:v1:Gg17p7maxC+0MOuXFCta4VOiEKrIziJEkogUvfjaEdWVADSvAXaI', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000028', 1, 28, 1, 'enc:v1:jcIqfE6k9RL60/C7jDWPFpgoGkG8EMy3XUrVPAo2ch40omSXX/vN4hQn', 'enc:v1:7x2dzZMiFdfNbqpP55msqYT6Rp0xf40duDnHYG3S9SO80M4/f4zC', '1987-11-22', '2018-07-12', 'enc:v1:he3BCUCAJu6nqFiAZ0tDlKpvv/f1ffG0bOeUhyTqAYWBvZ1eo4q94iiOsp2TZr0LR9mJhA==', 'enc:v1:0Ifxb7tXTe+KfzYXXeLLavLk3aQSLEvIafZl7QzJe9XODS1XLHgf', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000029', 1, 29, 3, 'enc:v1:A+J2Dw66M+4d6nd7si1KyhCy+aIJUp+HCcevqqYdQdf9x96ndPvfHNR8', 'enc:v1:SR5LtIdPgzfA/g4KaRoJSWLMqg/9HjnwpnDUvTcmewus4yk1Cgfj', '1993-01-08', '2023-01-20', 'enc:v1:mA8yLWM9uMxH3E795+0FkmSXbtbCZEJ9ppKGeWq2P7MyE7QWWsbqYGfYBeKWtK4Q', 'enc:v1:MIC60YCV5OjmGTz/2+dbQfSi850f+RkvtxsTBGv6HP9xx8dAHx5a', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000030', 1, 30, 2, 'enc:v1:99iM/7tlnM0AEVOsheAobVn+7WuBD1YwoyxMbvbInqhSVQYQqiqzbVPc94o=', 'enc:v1:PMssaxxduK66SKImEnXpffRsmbMLkNdCz9oSams3VL9f0+avvyFd', '2000-05-14', '2023-07-01', 'enc:v1:JSWPlfSJ32pyfc0wv9Gl+u4UVzzs/8JJOjacyep2RfkLNF4Ws+VQzdP8NAlqJc6SNoM=', 'enc:v1:cx24ohNVcm5N1Cp1/EFjKUUR5H6DO41AMFbbtaiDEXu5Q1HR21V3', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000031', 1, 31, 3, 'enc:v1:+sy57Y6pVN4Dus60NnytWmKooOCZL8J3OoPpHGvELKTTQALFFHO/PQ==', 'enc:v1:ylyKxmUPIuVeNnn2bX6IoxtlKUe6Ltx+gl/2zWoZbtiPMbYw9jrT', '1986-08-19', '2017-05-30', 'enc:v1:vDENZh4HMrmU7HvqVtgFldyWODgl/6VDDe+BBhpf4YiCsPirRXKcds5ksd7DAJYMWA==', 'enc:v1:akyBySSv0icOkzdhMAPT3/lv9bDtaGIPz3nYZFhCLyU47V5MnjHp', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000032', 1, 32, 2, 'enc:v1:JFqZJmY6EuYHLt7+lg8K7sCiFONQEP4mPrMUlj5zbIkYMkyPnzBuNqN3', 'enc:v1:mtZWApVyK3CtadILS7AtHgVitmZF8xIc8xdAbzMCkbFPpMkrBTte', '1999-12-03', '2022-08-14', 'enc:v1:lksQKjHrY9ziE8c7pIKsCxEN8iuQVNkGzmYwH5dTPw7JIK68j93fmJGIX2K2hlHY5Q==', 'enc:v1:jcX0GgIpxiP7kqI8SgnJQS3Ztg1iv5jXcj+BHkZeC+dbHaw1V+vM', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000033', 1, 33, 1, 'enc:v1:h52JvBoUrcPNYXpyHaOGepS47j3FL8n1/Z7v1vqE0FFkaYGTWIDk2Q==', 'enc:v1:ZcW5Ncb4sKSEjBf3Y79DnlfxEUOLZWZdJk8xQ3vl5XqUMWoktYEk', '1982-04-27', '2016-10-10', 'enc:v1:qeCBAcHlzF4rNnw/nP3ZPBLTQ7y76nUnC2QYoKMY7typSrZkGIsXKAFVLxADv9NAU/U=', 'enc:v1:h8X6t3C6qtXsDdfkUzE3o3rsslkqslLH36JqNWnngIWNKVEeVYui', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000034', 1, 34, 3, 'enc:v1:ndoVhmoioDD6RecvFnko2QnvAhtAPh6aCtACrVuWCxJPIo7YPZdUR3Q=', 'enc:v1:U6ohq1Wyd7KFBn742FsdKrS3DrZaPciI19RNNPbBn2ohPW8rN+Fd', '1997-09-09', '2023-03-15', 'enc:v1:YciSComk6SUq+cKEZjDCe7Y8GxPzUgBzznWDb3+e/9C8LgR4VGfXbwTAY2I854tgA4uZ', 'enc:v1:kE7AbAsUh5R5BGb5Km5X0vdJsXXS4db52M6IO+5hREXHW1bheR/P', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000035', 1, 35, 2, 'enc:v1:aFHVtobOEFTzUCIN40k9kfxoOk4/15xge3PUQDMJatRqsm8UX0TdAOvA', 'enc:v1:gQ4M+IP8HYaH1lsk3OJmX7hOq0UWDOniazb65Qe1oJsuZAsfRM3d', '1995-06-21', '2021-01-10', 'enc:v1:Dvx/8vcqFeIQ9Ds8hN6CWQyN0uQxGSHRyHYBnalVOXZysjgYvHzKCZchtFyL0OQf', 'enc:v1:a8k0KSt3vt3mywulrDfHtYSm6scxfwK8PIaFxvyaQAOcGUuP9aKm', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'INATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000036', 1, 36, 2, 'enc:v1:TIXGt6mTKACxAaZbg36hHYmipOcKOF71GyLU9kRKReFYdR4M0PbGmt+G', 'enc:v1:yoOrVgqJv1ioiSckaFZrAVhrHu2GY0avKmVSJsth2iuEXaVjTnBN', '1991-02-14', '2020-12-05', 'enc:v1:sZMrD1nrH6oZJT7dQDXnlJAddlKO2IGJbhrI/Cl6rYLBVbsO/sJki8T74wuVkNHfcPk=', 'enc:v1:+oqQKkWN49Bmz92NT9EDLp+jIx3IdVMlADsZaK0Apm8WscVB9mUD', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000037', 1, 37, 3, 'enc:v1:nXVi7Mo5bwsQjmoYZUMJVTh41RWWEH3JMJ4qR+UagkZtr5sZ1g79BlDAl6Bz', 'enc:v1:UdmFn7PvAFJJDIXqv7rT1xkQ4dB+Jkjfmu1xa5/rOCxcJCVKdf9C', '1988-11-30', '2019-06-25', 'enc:v1:go66oxRTrUmhPJ2P87pi67bToEuhDsshcrU4uSP0gW+0HdbouE3rv6anKW3YDB40LIM=', 'enc:v1:7NM6EK38zxyzJjhglt1/JbHAG/7vSuxaUMl069L4B+LRQQ1S3aHt', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000038', 1, 38, 2, 'enc:v1:TG67oqckFlTyH4U1fqISx3Ki1K/JOT3T6VelNjllkrdHT1dGDf2joNrOTAA6iLMc', 'enc:v1:YPzI3aXXSXbm8wNdnL63Sj9/qOupytzsLsGt6hBqBQfOEn+M2FRr', '1984-07-07', '2015-09-01', 'enc:v1:YTL9L9ZDhROGrcVmQ8HAH9ZrJ0N3HxTMJyri4/9HGctw8h2r3Domkwls7LuUPxbWpwY=', 'enc:v1:a+qfOkoHI+kiVKkCWr5tfXO2SBuYOiev7Cd8r6+hOlIIYXp1scdr', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000039', 1, 39, 3, 'enc:v1:W3h6dDhv7gqwGZDIOGoasXVMcGeLUGu6GMPEelSeObt6oazJcVU5r+w=', 'enc:v1:2JuKDa7rGNOzRCkJDAc9fte8gDUBQcMj6KHKT/h/XIdjQSx6qRZb', '1996-03-12', '2022-04-18', 'enc:v1:utI/YqmYwwgU0Q4mMQSnINMKksDWbnoHda5OsggK3Uzs08wxSvDE/zkQWHnfJbCiiKYt', 'enc:v1:PB8sGHWsQ5thkdeQ6OUlvhZsOeZYda0IlTgKAvm/c2ZM54tjmujo', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000040', 1, 40, 2, 'enc:v1:eaFLsM4iHZlgH4SfMHfWSqZalHPfNGQDanZZEqxsdD0wlKJImY3z8Q==', 'enc:v1:fiYk+7t8ADhuEEDw2vhIj/sT6RI4+t+f5BIZufCak6YhkaEdMkYw', '1993-10-25', '2021-11-20', 'enc:v1:effJ3LVJ475My5o105LBOuU4exPmqK+XOcwTDgfXmsThlBw82TbQNrUS0fA7zI6/Jy8=', 'enc:v1:AKaRSsePPS8ed3S8sm1v5fHuvrT0zaFFMvXGa+MPUuykXGmRqx8g', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000041', 1, 41, 3, 'enc:v1:1nWg9Ip4UJIDCGz5vxM2EDw/7pPUH9DcoWSOs/1GZ6WOMhKzm95P/Mzg', 'enc:v1:sd7f5DrrCEzHKcY7B0ecok/NY2yir5w+YCZ3qTEwBT9SR+Zfva0i', '1990-05-05', '2019-08-08', 'enc:v1:snlnHBQYhC3P2mcrf6UEGIXkt4oGha1pX+em7ar2DTZqPgptjfgMJfCWSW7cqw2EWg==', 'enc:v1:x3tPbjFK6L03jYvLrXsSI0eG+8Sq24n+P1YaHzcYpiQMFdvkeIfI', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000042', 1, 42, 1, 'enc:v1:TDojU5xz+dTRDQXyg7RJX3+S3FWEug03GyPDabBzD9r5sfpmZrAIaL4=', 'enc:v1:qg6cTYefdliD+JDM7xeszyDIU+nlXrqa96XW60vtKTagq2Bi7Hav', '2001-01-15', '2023-02-10', 'enc:v1:qjKK1o7xH0zfGFYKDSnulRGY3L1Q3mFKmtx0RkkQd3izgRjcM3vYJ5xvQ8AIL+JI', 'enc:v1:gUud1g4Ts4h7tBaRqPvRQrOnJnXNPx2Dixb3q3Bj/gew/oD5mE03', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000043', 1, 43, 2, 'enc:v1:BB86xsS/FwnJfLi+qYGGdlIaDNJ1f2+mhTH7TVxxjw5K3ABg2vZczlGktA==', 'enc:v1:c+Z9Eg/Uf3VWh7njL533UTDBbuzU1P+8Wa69VoOOmdUuopYIy9H8', '1985-09-28', '2018-01-15', 'enc:v1:woGWhqIBfQt0l79Zk3zYIa5cOFNyPI4OTYUAqwsqdv99GIV9zsDJuMd6wW+TayOvcQ==', 'enc:v1:atAqCRA9dcW+ZTwh4WInrF/L7xWXrm04xyszsKf51wc9OZ26DyHx', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000044', 1, 44, 3, 'enc:v1:mNA4O2Ik+3L/agLDGPlMHj2In6++X2GFPm1JXM0l163EzpmnVRX7ykoK', 'enc:v1:i4HDMZTLbjbvJxw9D/XFHRPhpt3WdrNxvGzWrXRTclx/1JoM4XZj', '1992-06-12', '2020-03-03', 'enc:v1:TARgFLFuqC20yS8wUmm0nHg9Jp1Fzfymksvguc24PkaDn6Ruvq9uhey0V7bHMhw1fA==', 'enc:v1:wI36vaY8Iik+mWyeQiCT5SYVsSx5SiARAVJkfP3LYfUqCiepYczn', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000045', 1, 45, 2, 'enc:v1:iqikdx4K6cRdGYm51lQV2gksdJwYT251GkzFLDPJA3wePhIt3HNRNw==', 'enc:v1:KUh+ft7zIPV3yRBYjzuHWW90Z6ACSeGVjW/lb+TbFHPLZ9pjDqGv', '1998-08-30', '2022-09-25', 'enc:v1:iZwk7OFlXEUP8zi30FdPrKtr+Dke+r0K92wpJGoPZPQLGdaUznOYS5hCRwpCqlGeOCY=', 'enc:v1:RF/DW5LM4WezcV0FaSszSSqVJQXbBL3Xa9DDtAeqcGQKLQp4RRqK', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000046', 1, 46, 3, 'enc:v1:+Ll9QkxEuNOFlsnedkgp+8BgcujWEGnWVzyxbtSFpjLWSebvoEVmSw==', 'enc:v1:k/Pw51uEX8OElEmcbNdCqOaZA6Xlr7UOFz7vllOoZx4a5v5AFHVU', '1994-04-02', '2021-06-12', 'enc:v1:q343z+K8iz1qs+mx64jkKxyRECKOzbj+d6W2ruoZ1ettnL6f7+K5KrvcEjrCJuDJBA==', 'enc:v1:sWkojQI9h1t8MGwpmD+WJKjndc0b7DIFuO2Wsx2+cqrwYrsMkzGb', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000047', 1, 47, 2, 'enc:v1:SR26ORqUNho5d3W3sgBpYPm4oDQ8mdiCpglxPrnDjrn0/NmgvRi7Onld', 'enc:v1:b354ffNCg1+fld02/khax46OfV5wLvFJVij38p9xObRAnGebyx2M', '1995-12-25', '2023-01-05', 'enc:v1:jAoYNWQv0JuRaZI34XZeSMgT5LBCub1eSu/z7nxexz60xKiGU6u1+g+NtKDcpV6Knw==', 'enc:v1:hx5hMpWZwReTpG5Ovq4wRkarbEKV8PWgMu/84t8NMYGWwZIfAiuU', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO'),
('20000000-0000-0000-0000-000000000048', 1, 48, 3, 'enc:v1:wj/iXZX/WM+sQMG/DUQnlnVa36OZSFRMjjb8r8e893EN/nv1Tfi16ng53g==', 'enc:v1:F63tf36g3+L32nOGtatQI6SYLov9nIJKnfGuntwQe0fgfadhVktC', '2002-02-18', '2023-08-20', 'enc:v1:3v9nI/VYNUpeAkhP+iicGEP+d1R4w6/O0lugdz0if37THys2gVL8+s2SwNvzYmZy', 'enc:v1:k3L7C7vOlztS7HgQGtDFx/QADvc7lXVuv6F4aPuw/m528rmCtfmK', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'MASCULINO'),
('20000000-0000-0000-0000-000000000049', 1, 49, 2, 'enc:v1:NO39yJ80uqbdiZTk/V6G25y7Nnd3v8L36fWC3okTKAOmHRtx9RCt4+Um', 'enc:v1:256Zu6+S9tpU+hBVmTN1TMNR0fBxzpHaOGMqtnM2qc3uXP5ceAZG', '1983-03-08', '2015-05-20', 'enc:v1:rRRL9p1NQcZRF9TugoD5+CtONNEUbFhDE/3rrAGsz2aGNFBk9CesHeCvSnsoR7ngOn7c', 'enc:v1:qg7Nv7ycTuKGw8I87tY7qKgJ6aL97M26hTe+zuntuxS/iNxno5S2', '$2a$10$agCzV50qLUM5cxjjiIbFi.WyjGf1lqWgGL.1HMD96aIOURmgF2aZW', 'ATIVO', 'MEMBRO', 'FEMININO');

-- ==================================================================================
-- 3. MINISTÉRIOS (Corrigido para hexadecimais válidos: prefixo 3000...)
-- ==================================================================================
INSERT INTO ministerio (
    id_externo, fk_igreja, nome, data_criacao, nome_lider, status
) VALUES
('30000000-0000-0000-0000-000000000001', 1, 'Louvor e Adoração', '2015-01-01', 'Pr. João Almeida', 'ATIVO'),
('30000000-0000-0000-0000-000000000002', 1, 'Kids e Juniores', '2016-03-10', 'Ana Clara Silva', 'ATIVO'),
('30000000-0000-0000-0000-000000000003', 1, 'Recepção e Acolhimento', '2015-02-01', 'Pr. João Almeida', 'ATIVO'),
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
('40000000-0000-0000-0000-000000000002', 6, 1, 'MEMBRO_MINISTERIO', 'Louvor e Adoração', '2022-02-01'),
('40000000-0000-0000-0000-000000000003', 14, 1, 'MEMBRO_MINISTERIO', 'Louvor e Adoração', '2019-10-01'),
('40000000-0000-0000-0000-000000000004', 15, 1, 'MEMBRO_MINISTERIO', 'Louvor e Adoração', '2021-12-25'),

-- Ministério Kids
('40000000-0000-0000-0000-000000000005', 4, 2, 'LIDER_MINISTERIO', 'Kids e Juniores', '2020-02-01'),
('40000000-0000-0000-0000-000000000006', 10, 2, 'MEMBRO_MINISTERIO', 'Kids e Juniores', '2023-02-01'),
('40000000-0000-0000-0000-000000000007', 16, 2, 'MEMBRO_MINISTERIO', 'Kids e Juniores', '2022-11-01'),

-- Ministério Recepção
('40000000-0000-0000-0000-000000000008', 6, 3, 'MEMBRO_MINISTERIO', 'Recepção e Acolhimento', '2022-07-01'),
('40000000-0000-0000-0000-000000000009', 13, 3, 'MEMBRO_MINISTERIO', 'Recepção e Acolhimento', '2023-03-05'),
('40000000-0000-0000-0000-000000000010', 18, 3, 'MEMBRO_MINISTERIO', 'Recepção e Acolhimento', '2023-04-10'),
('40000000-0000-0000-0000-000000000050', 1, 3, 'LIDER_MINISTERIO', 'Recepção e Acolhimento', '2024-01-01'),

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
('40000000-0000-0000-0000-000000000049', 7, 2, 'MEMBRO_MINISTERIO', 'Kids e Juniores', '2024-01-01'),

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
('70000000-0000-0000-0000-000000000001', 1, 1,  'CONFIRMADO'),  -- escala_evento 1 / ministerio 1
('70000000-0000-0000-0000-000000000002', 1, 2, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000003', 1, 3, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000004', 1, 4,  'CONFIRMADO'),
('70000000-0000-0000-0000-000000000005', 1, 19, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000006', 1, 20, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000007', 1, 21, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000008', 1, 22, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000009', 1, 23, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000010', 1, 24, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000011', 2, 5, 'CONFIRMADO'),  -- escala_evento 2 / ministerio 2
('70000000-0000-0000-0000-000000000012', 2, 6, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000013', 2, 7, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000014', 2, 25, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000015', 2, 26, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000016', 2, 27, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000017', 2, 28, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000018', 2, 29, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000019', 2, 30, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000020', 3, 8, 'CONFIRMADO'),  -- escala_evento 3 / ministerio 3
('70000000-0000-0000-0000-000000000021', 3, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000022', 3, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000023', 3, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000024', 3, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000025', 3, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000026', 3, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000027', 3, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000028', 3, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000029', 4, 11, 'CONFIRMADO'),  -- escala_evento 4 / ministerio 4
('70000000-0000-0000-0000-000000000030', 4, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000031', 4, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000032', 4, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000033', 4, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000034', 4, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000035', 4, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000036', 4, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000037', 4, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000038', 5, 14, 'CONFIRMADO'),  -- escala_evento 5 / ministerio 5
('70000000-0000-0000-0000-000000000039', 5, 15, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000040', 5, 16, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000041', 5, 17, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000042', 5, 18, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000043', 5, 43, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000044', 5, 44, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000045', 5, 45, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000046', 5, 46, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000047', 5, 47, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000048', 5, 48, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000049', 6, 1, 'CONFIRMADO'),  -- escala_evento 6 / ministerio 1
('70000000-0000-0000-0000-000000000050', 6, 2, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000051', 6, 3, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000052', 6, 4, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000053', 6, 19, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000054', 6, 20, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000055', 6, 21, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000056', 6, 22, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000057', 6, 23, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000058', 6, 24, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000059', 7, 8, 'CONFIRMADO'),  -- escala_evento 7 / ministerio 3
('70000000-0000-0000-0000-000000000060', 7, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000061', 7, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000062', 7, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000063', 7, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000064', 7, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000065', 7, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000066', 7, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000067', 7, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000068', 8, 11, 'CONFIRMADO'),  -- escala_evento 8 / ministerio 4
('70000000-0000-0000-0000-000000000069', 8, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000070', 8, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000071', 8, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000072', 8, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000073', 8, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000074', 8, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000075', 8, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000076', 8, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000077', 9, 5, 'CONFIRMADO'),  -- escala_evento 9 / ministerio 2
('70000000-0000-0000-0000-000000000078', 9, 6, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000079', 9, 7, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000080', 9, 25, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000081', 9, 26, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000082', 9, 27, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000083', 9, 28, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000084', 9, 29, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000085', 9, 30, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000638', 23, 49, 'CONFIRMADO'),  -- mesma pessoa escalada em outro ministério no mesmo evento
('70000000-0000-0000-0000-000000000086', 10, 1, 'CONFIRMADO'),  -- escala_evento 10 / ministerio 1
('70000000-0000-0000-0000-000000000087', 10, 2, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000088', 10, 3, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000089', 10, 4, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000090', 10, 19, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000091', 10, 20, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000092', 10, 21, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000093', 10, 22, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000094', 10, 23, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000095', 10, 24, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000096', 11, 5, 'CONFIRMADO'),  -- escala_evento 11 / ministerio 2
('70000000-0000-0000-0000-000000000097', 11, 6, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000098', 11, 7, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000099', 11, 25, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000100', 11, 26, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000101', 11, 27, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000102', 11, 28, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000103', 11, 29, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000104', 11, 30, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000105', 12, 8, 'CONFIRMADO'),  -- escala_evento 12 / ministerio 3
('70000000-0000-0000-0000-000000000106', 12, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000107', 12, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000108', 12, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000109', 12, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000110', 12, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000111', 12, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000112', 12, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000113', 12, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000114', 13, 11, 'CONFIRMADO'),  -- escala_evento 13 / ministerio 4
('70000000-0000-0000-0000-000000000115', 13, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000116', 13, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000117', 13, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000118', 13, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000119', 13, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000120', 13, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000121', 13, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000122', 13, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000123', 14, 14, 'CONFIRMADO'),  -- escala_evento 14 / ministerio 5
('70000000-0000-0000-0000-000000000124', 14, 15, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000125', 14, 16, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000126', 14, 17, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000127', 14, 18, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000128', 14, 43, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000129', 14, 44, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000130', 14, 45, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000131', 14, 46, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000132', 14, 47, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000133', 14, 48, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000134', 15, 11, 'CONFIRMADO'),  -- escala_evento 15 / ministerio 4
('70000000-0000-0000-0000-000000000135', 15, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000136', 15, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000137', 15, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000138', 15, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000139', 15, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000140', 15, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000141', 15, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000142', 15, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000143', 16, 1, 'CONFIRMADO'),  -- escala_evento 16 / ministerio 1
('70000000-0000-0000-0000-000000000144', 16, 2, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000145', 16, 3, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000146', 16, 4, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000147', 16, 19, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000148', 16, 20, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000149', 16, 21, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000150', 16, 22, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000151', 16, 23, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000152', 16, 24, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000153', 17, 14, 'CONFIRMADO'),  -- escala_evento 17 / ministerio 5
('70000000-0000-0000-0000-000000000154', 17, 15, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000155', 17, 16, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000156', 17, 17, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000157', 17, 18, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000158', 17, 43, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000159', 17, 44, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000160', 17, 45, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000161', 17, 46, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000162', 17, 47, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000163', 17, 48, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000164', 18, 1, 'CONFIRMADO'),  -- escala_evento 18 / ministerio 1
('70000000-0000-0000-0000-000000000165', 18, 2, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000166', 18, 3, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000167', 18, 4, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000168', 18, 19, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000169', 18, 20, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000170', 18, 21, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000171', 18, 22, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000172', 18, 23, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000173', 18, 24, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000174', 19, 14, 'CONFIRMADO'),  -- escala_evento 19 / ministerio 5
('70000000-0000-0000-0000-000000000175', 19, 15, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000176', 19, 16, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000177', 19, 17, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000178', 19, 18, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000179', 19, 43, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000180', 19, 44, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000181', 19, 45, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000182', 19, 46, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000183', 19, 47, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000184', 19, 48, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000185', 20, 5, 'CONFIRMADO'),  -- escala_evento 20 / ministerio 2
('70000000-0000-0000-0000-000000000186', 20, 6, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000187', 20, 7, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000188', 20, 25, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000189', 20, 26, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000190', 20, 27, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000191', 20, 28, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000192', 20, 29, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000193', 20, 30, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000194', 21, 8, 'CONFIRMADO'),  -- escala_evento 21 / ministerio 3
('70000000-0000-0000-0000-000000000195', 21, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000196', 21, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000197', 21, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000198', 21, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000199', 21, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000200', 21, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000201', 21, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000202', 21, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000203', 22, 1, 'CONFIRMADO'),  -- escala_evento 22 / ministerio 1
-- ('70000000-0000-0000-0000-000000000204', 22, 2, 'CONFIRMADO'),  -- Individuo indisponivel devido outro evento, conflitante
('70000000-0000-0000-0000-000000000205', 22, 3, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000206', 22, 4, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000207', 22, 19, 'CONFIRMADO'),
-- ('70000000-0000-0000-0000-000000000208', 22, 20, 'CONFIRMADO'), -- Individuo indisponivel devido outro evento, conflitante
('70000000-0000-0000-0000-000000000209', 22, 21, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000210', 22, 22, 'CONFIRMADO'),
-- ('70000000-0000-0000-0000-000000000211', 22, 23, 'CONFIRMADO'),  -- Individuo indisponivel devido outro evento, conflitante
('70000000-0000-0000-0000-000000000212', 22, 24, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000213', 23, 5, 'CONFIRMADO'),  -- escala_evento 23 / ministerio 2
('70000000-0000-0000-0000-000000000214', 23, 6, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000215', 23, 7, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000216', 23, 25, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000217', 23, 26, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000218', 23, 27, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000219', 23, 28, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000220', 23, 29, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000221', 23, 30, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000222', 24, 8, 'CONFIRMADO'),  -- escala_evento 24 / ministerio 3
('70000000-0000-0000-0000-000000000223', 24, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000224', 24, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000225', 24, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000226', 24, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000227', 24, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000228', 24, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000229', 24, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000230', 24, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000231', 25, 11, 'CONFIRMADO'),  -- escala_evento 25 / ministerio 4
('70000000-0000-0000-0000-000000000232', 25, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000233', 25, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000234', 25, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000235', 25, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000236', 25, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000237', 25, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000238', 25, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000239', 25, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000240', 26, 14, 'CONFIRMADO'),  -- escala_evento 26 / ministerio 5
('70000000-0000-0000-0000-000000000241', 26, 15, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000242', 26, 16, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000243', 26, 17, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000244', 26, 18, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000245', 26, 43, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000246', 26, 44, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000247', 26, 45, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000248', 26, 46, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000249', 26, 47, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000250', 26, 48, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000251', 27, 14, 'CONFIRMADO'),  -- escala_evento 27 / ministerio 5
('70000000-0000-0000-0000-000000000252', 27, 15, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000253', 27, 16, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000254', 27, 17, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000255', 27, 18, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000256', 27, 43, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000257', 27, 44, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000258', 27, 45, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000259', 27, 46, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000260', 27, 47, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000261', 27, 48, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000262', 28, 1, 'CONFIRMADO'),  -- escala_evento 28 / ministerio 1
('70000000-0000-0000-0000-000000000263', 28, 2, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000264', 28, 3, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000265', 28, 4, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000266', 28, 19, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000267', 28, 20, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000268', 28, 21, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000269', 28, 22, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000270', 28, 23, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000271', 28, 24, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000272', 29, 8, 'CONFIRMADO'),  -- escala_evento 29 / ministerio 3
('70000000-0000-0000-0000-000000000273', 29, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000274', 29, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000275', 29, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000276', 29, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000277', 29, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000278', 29, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000279', 29, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000280', 29, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000281', 30, 11, 'CONFIRMADO'),  -- escala_evento 30 / ministerio 4
('70000000-0000-0000-0000-000000000282', 30, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000283', 30, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000284', 30, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000285', 30, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000286', 30, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000287', 30, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000288', 30, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000289', 30, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000290', 31, 5, 'CONFIRMADO'),  -- escala_evento 31 / ministerio 2
('70000000-0000-0000-0000-000000000291', 31, 6, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000292', 31, 7, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000293', 31, 25, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000294', 31, 26, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000295', 31, 27, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000296', 31, 28, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000297', 31, 29, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000298', 31, 30, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000299', 32, 11, 'CONFIRMADO'),  -- escala_evento 32 / ministerio 4
('70000000-0000-0000-0000-000000000300', 32, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000301', 32, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000302', 32, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000303', 32, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000304', 32, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000305', 32, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000306', 32, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000307', 32, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000308', 33, 8, 'CONFIRMADO'),  -- escala_evento 33 / ministerio 3
('70000000-0000-0000-0000-000000000309', 33, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000310', 33, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000311', 33, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000312', 33, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000313', 33, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000314', 33, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000315', 33, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000316', 33, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000317', 34, 11, 'CONFIRMADO'),  -- escala_evento 34 / ministerio 4
('70000000-0000-0000-0000-000000000318', 34, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000319', 34, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000320', 34, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000321', 34, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000322', 34, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000323', 34, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000324', 34, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000325', 34, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000326', 35, 1, 'CONFIRMADO'),  -- escala_evento 35 / ministerio 1
('70000000-0000-0000-0000-000000000327', 35, 2, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000328', 35, 3, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000329', 35, 4, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000330', 35, 19, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000331', 35, 20, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000332', 35, 21, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000333', 35, 22, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000334', 35, 23, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000335', 35, 24, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000336', 36, 8, 'CONFIRMADO'),  -- escala_evento 36 / ministerio 3
('70000000-0000-0000-0000-000000000337', 36, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000338', 36, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000339', 36, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000340', 36, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000341', 36, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000342', 36, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000343', 36, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000344', 36, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000345', 37, 1, 'CONFIRMADO'),  -- escala_evento 37 / ministerio 1
('70000000-0000-0000-0000-000000000346', 37, 2, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000347', 37, 3, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000348', 37, 4, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000349', 37, 19, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000350', 37, 20, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000351', 37, 21, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000352', 37, 22, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000353', 37, 23, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000354', 37, 24, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000355', 38, 5, 'CONFIRMADO'),  -- escala_evento 38 / ministerio 2
('70000000-0000-0000-0000-000000000356', 38, 6, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000357', 38, 7, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000358', 38, 25, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000359', 38, 26, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000360', 38, 27, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000361', 38, 28, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000362', 38, 29, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000363', 38, 30, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000364', 39, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000365', 39, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000366', 39, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000367', 39, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000368', 39, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000369', 39, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000370', 39, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000371', 39, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000372', 40, 11, 'CONFIRMADO'),  -- escala_evento 40 / ministerio 4
('70000000-0000-0000-0000-000000000373', 40, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000374', 40, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000375', 40, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000376', 40, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000377', 40, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000378', 40, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000379', 40, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000380', 40, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000381', 41, 11, 'CONFIRMADO'),  -- escala_evento 41 / ministerio 4
('70000000-0000-0000-0000-000000000382', 41, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000383', 41, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000384', 41, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000385', 41, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000386', 41, 39, 'CONFIRMADO'), --
('70000000-0000-0000-0000-000000000387', 41, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000388', 41, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000389', 41, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000390', 42, 14, 'CONFIRMADO'),  -- escala_evento 42 / ministerio 5
('70000000-0000-0000-0000-000000000391', 42, 15, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000392', 42, 16, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000393', 42, 17, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000394', 42, 18, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000395', 42, 43, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000396', 42, 44, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000397', 42, 45, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000398', 42, 46, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000399', 42, 47, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000400', 42, 48, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000401', 43, 8, 'CONFIRMADO'),  -- escala_evento 43 / ministerio 3
('70000000-0000-0000-0000-000000000402', 43, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000403', 43, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000404', 43, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000405', 43, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000406', 43, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000407', 43, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000408', 43, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000409', 43, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000410', 44, 14, 'CONFIRMADO'),  -- escala_evento 44 / ministerio 5
('70000000-0000-0000-0000-000000000411', 44, 15, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000412', 44, 16, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000413', 44, 17, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000414', 44, 18, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000415', 44, 43, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000416', 44, 44, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000417', 44, 45, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000418', 44, 46, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000419', 44, 47, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000420', 44, 48, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000421', 45, 11, 'CONFIRMADO'),  -- escala_evento 45 / ministerio 4
('70000000-0000-0000-0000-000000000422', 45, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000423', 45, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000424', 45, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000425', 45, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000426', 45, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000427', 45, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000428', 45, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000429', 45, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000430', 46, 1, 'CONFIRMADO'),  -- escala_evento 46 / ministerio 1
('70000000-0000-0000-0000-000000000431', 46, 2, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000432', 46, 3, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000433', 46, 4, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000434', 46, 19, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000435', 46, 20, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000436', 46, 21, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000437', 46, 22, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000438', 46, 23, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000439', 46, 24, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000440', 47, 8, 'CONFIRMADO'),  -- escala_evento 47 / ministerio 3
('70000000-0000-0000-0000-000000000441', 47, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000442', 47, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000443', 47, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000444', 47, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000445', 47, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000446', 47, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000447', 47, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000448', 47, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000449', 48, 11, 'CONFIRMADO'),  -- escala_evento 48 / ministerio 4
('70000000-0000-0000-0000-000000000450', 48, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000451', 48, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000452', 48, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000453', 48, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000454', 48, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000455', 48, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000456', 48, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000457', 48, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000458', 49, 5, 'CONFIRMADO'),  -- escala_evento 49 / ministerio 2
('70000000-0000-0000-0000-000000000459', 49, 6, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000460', 49, 7, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000461', 49, 25, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000462', 49, 26, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000463', 49, 27, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000464', 49, 28, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000465', 49, 29, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000466', 49, 30, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000467', 50, 8, 'CONFIRMADO'),  -- escala_evento 50 / ministerio 3
('70000000-0000-0000-0000-000000000468', 50, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000469', 50, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000470', 50, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000471', 50, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000472', 50, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000473', 50, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000474', 50, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000475', 50, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000476', 51, 1, 'CONFIRMADO'),  -- escala_evento 51 / ministerio 1
('70000000-0000-0000-0000-000000000477', 51, 2, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000478', 51, 3, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000479', 51, 4, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000480', 51, 19, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000481', 51, 20, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000482', 51, 21, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000483', 51, 22, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000484', 51, 23, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000485', 51, 24, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000486', 52, 8, 'CONFIRMADO'),  -- escala_evento 52 / ministerio 3
('70000000-0000-0000-0000-000000000487', 52, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000488', 52, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000489', 52, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000490', 52, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000491', 52, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000492', 52, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000493', 52, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000494', 52, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000495', 53, 11, 'CONFIRMADO'),  -- escala_evento 53 / ministerio 4
('70000000-0000-0000-0000-000000000496', 53, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000497', 53, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000498', 53, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000499', 53, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000500', 53, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000501', 53, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000502', 53, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000503', 53, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000504', 54, 14, 'CONFIRMADO'),  -- escala_evento 54 / ministerio 5
('70000000-0000-0000-0000-000000000505', 54, 15, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000506', 54, 16, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000507', 54, 17, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000508', 54, 18, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000509', 54, 43, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000510', 54, 44, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000511', 54, 45, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000512', 54, 46, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000513', 54, 47, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000514', 54, 48, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000515', 55, 8, 'CONFIRMADO'),  -- escala_evento 55 / ministerio 3
('70000000-0000-0000-0000-000000000516', 55, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000517', 55, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000518', 55, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000519', 55, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000520', 55, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000521', 55, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000522', 55, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000523', 55, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000524', 56, 11, 'CONFIRMADO'),  -- escala_evento 56 / ministerio 4
('70000000-0000-0000-0000-000000000525', 56, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000526', 56, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000527', 56, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000528', 56, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000529', 56, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000530', 56, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000531', 56, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000532', 56, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000533', 57, 11, 'CONFIRMADO'),  -- escala_evento 57 / ministerio 4
('70000000-0000-0000-0000-000000000534', 57, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000535', 57, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000536', 57, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000537', 57, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000538', 57, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000539', 57, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000540', 57, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000541', 57, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000542', 58, 1, 'CONFIRMADO'),  -- escala_evento 58 / ministerio 1
('70000000-0000-0000-0000-000000000543', 58, 2, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000544', 58, 3, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000545', 58, 4, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000546', 58, 19, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000547', 58, 20, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000548', 58, 21, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000549', 58, 22, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000550', 58, 23, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000551', 58, 24, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000552', 59, 8, 'CONFIRMADO'),  -- escala_evento 59 / ministerio 3
('70000000-0000-0000-0000-000000000553', 59, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000554', 59, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000555', 59, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000556', 59, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000557', 59, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000558', 59, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000559', 59, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000560', 59, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000561', 60, 11, 'CONFIRMADO'),  -- escala_evento 60 / ministerio 4
('70000000-0000-0000-0000-000000000562', 60, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000563', 60, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000564', 60, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000565', 60, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000566', 60, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000567', 60, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000568', 60, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000569', 60, 42, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000570', 61, 8, 'CONFIRMADO'),  -- escala_evento 61 / ministerio 3
('70000000-0000-0000-0000-000000000571', 61, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000572', 61, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000573', 61, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000574', 61, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000575', 61, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000576', 61, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000577', 61, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000578', 61, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000579', 62, 5, 'CONFIRMADO'),  -- escala_evento 62 / ministerio 2
('70000000-0000-0000-0000-000000000580', 62, 6, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000581', 62, 7, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000582', 62, 25, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000583', 62, 26, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000584', 62, 27, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000585', 62, 28, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000586', 62, 29, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000587', 62, 30, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000588', 63, 8, 'CONFIRMADO'),  -- escala_evento 63 / ministerio 3
('70000000-0000-0000-0000-000000000589', 63, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000590', 63, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000591', 63, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000592', 63, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000593', 63, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000594', 63, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000595', 63, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000596', 63, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000597', 64, 8, 'CONFIRMADO'),  -- escala_evento 64 / ministerio 3
('70000000-0000-0000-0000-000000000598', 64, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000599', 64, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000600', 64, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000601', 64, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000602', 64, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000603', 64, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000604', 64, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000605', 64, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000606', 65, 1, 'CONFIRMADO'),  -- escala_evento 65 / ministerio 1
('70000000-0000-0000-0000-000000000607', 65, 2, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000608', 65, 3, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000609', 65, 4, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000610', 65, 19, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000611', 65, 20, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000612', 65, 21, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000613', 65, 22, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000614', 65, 23, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000615', 65, 24, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000616', 66, 8, 'CONFIRMADO'),  -- escala_evento 66 / ministerio 3
('70000000-0000-0000-0000-000000000617', 66, 9, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000618', 66, 10, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000619', 66, 31, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000620', 66, 32, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000621', 66, 33, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000622', 66, 34, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000623', 66, 35, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000624', 66, 36, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000625', 67, 11, 'CONFIRMADO'),  -- escala_evento 67 / ministerio 4
('70000000-0000-0000-0000-000000000626', 67, 12, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000627', 67, 13, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000628', 67, 37, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000629', 67, 38, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000630', 67, 39, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000631', 67, 40, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000632', 67, 41, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000633', 67, 42, 'CONFIRMADO');


-- ==================================================================================
-- 8. EVENTO SOBREPOSTO PARA GERAR MEMBROS OCUPADOS NO ENDPOINT
-- ==================================================================================

INSERT INTO evento (
    id_externo, fk_igreja, fk_organizador, fk_endereco,
    nome, descricao, publico_alvo, data_hora_inicio, data_hora_fim, custo, status
) VALUES
('50000000-0000-0000-0000-000000000029', 1, 1, NULL, 'Retiro de Verão: Profundidade - Encontro Paralelo', 'Evento criado para sobrepor o retiro e gerar conflito de agenda', 'Geral', '2026-01-17 18:00:00', '2026-01-20 16:00:00', 0.00, 'CONFIRMADO');

INSERT INTO escala_evento (id_externo, fk_evento, fk_ministerio, status_escala_evento) VALUES
('60000000-0000-0000-0000-000000000068', 29, 1, 'CONFIRMADO');

INSERT INTO escala_ministerio (id_externo, fk_escala_evento, fk_membro_ministerio, status_escala_ministerio) VALUES
('70000000-0000-0000-0000-000000000635', 68, 2, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000636', 68, 20, 'CONFIRMADO'),
('70000000-0000-0000-0000-000000000637', 68, 23, 'CONFIRMADO');



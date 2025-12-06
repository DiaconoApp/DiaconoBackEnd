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


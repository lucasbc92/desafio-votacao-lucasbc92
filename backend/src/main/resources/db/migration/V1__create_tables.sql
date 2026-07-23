--- backend/src/main/resources/db/migration/V1__create_tables.sql (原始)


+++ backend/src/main/resources/db/migration/V1__create_tables.sql (修改后)
-- Migration inicial: Criação das tabelas do sistema de votação
-- Versão 1.0.0

CREATE TABLE IF NOT EXISTS pauta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS sessao_votacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pauta_id BIGINT NOT NULL,
    data_abertura TIMESTAMP,
    data_fechamento TIMESTAMP,
    duracao_segundos INTEGER DEFAULT 60,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDENTE',
    CONSTRAINT fk_sessao_pauta FOREIGN KEY (pauta_id) REFERENCES pauta(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS voto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pauta_id BIGINT NOT NULL,
    cpf_associado VARCHAR(14) NOT NULL,
    voto_tipo VARCHAR(20) NOT NULL,
    data_voto TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_voto_pauta FOREIGN KEY (pauta_id) REFERENCES pauta(id) ON DELETE CASCADE,
    CONSTRAINT uk_voto_pauta_cpf UNIQUE (pauta_id, cpf_associado)
);

-- Índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_sessao_pauta ON sessao_votacao(pauta_id);
CREATE INDEX IF NOT EXISTS idx_sessao_status ON sessao_votacao(status);
CREATE INDEX IF NOT EXISTS idx_voto_pauta ON voto(pauta_id);
CREATE INDEX IF NOT EXISTS idx_voto_cpf ON voto(cpf_associado);
CREATE TABLE solicitante (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf_cnpj VARCHAR(18) UNIQUE NOT NULL
);

CREATE TABLE categoria (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE solicitacao (
    id BIGSERIAL PRIMARY KEY,
    solicitante_id BIGINT NOT NULL REFERENCES solicitante(id),
    categoria_id BIGINT NOT NULL REFERENCES categoria(id),
    descricao TEXT,
    valor NUMERIC(15,2) NOT NULL,
    data_solicitacao DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SOLICITADO'
);
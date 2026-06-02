CREATE TABLE historico_solicitacao (
    id BIGSERIAL PRIMARY KEY,
    solicitacao_id BIGINT NOT NULL REFERENCES solicitacao(id),
    status_anterior VARCHAR(20),
    status_novo VARCHAR(20) NOT NULL,
    data_alteracao TIMESTAMP NOT NULL DEFAULT NOW()
);
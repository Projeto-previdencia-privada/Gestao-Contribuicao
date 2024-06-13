CREATE SCHEMA IF NOT EXISTS contribuicao;

CREATE TABLE IF NOT EXISTS contribuicao.aliquota
(
    id bigserial NOT NULL,
    categoria character varying(255) NOT NULL,
    salario_inicio numeric(10, 2) NOT NULL,
    salario_fim numeric(10, 2) NOT NULL,
    valor_aliquota numeric(5, 2) NOT NULL,
    CONSTRAINT aliquota_pkey PRIMARY KEY (id),
    CONSTRAINT uniq_aliquota UNIQUE (categoria, salario_inicio, salario_fim, valor_aliquota)
    );

CREATE SEQUENCE contribuicao.salario_minimo_historico_id_seq;

CREATE TABLE IF NOT EXISTS contribuicao.salario_minimo_historico
(
    id bigint NOT NULL DEFAULT nextval('contribuicao.salario_minimo_historico_id_seq'),
    data_minimo date NOT NULL,
    valor_salario_minimo_ano numeric(10, 2) NOT NULL,
    CONSTRAINT salario_minimo_historico_pkey PRIMARY KEY (id)
    );
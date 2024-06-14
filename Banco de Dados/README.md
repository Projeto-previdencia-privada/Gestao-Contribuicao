# Gestão de Contribuições - Banco de Dados

Este documento descreve a estrutura e a configuração do banco de dados utilizado na aplicação "Gestão de Contribuições". Este banco de dados armazena informações sobre as alíquotas aplicadas às contribuições e o histórico dos valores do salário mínimo.

## Tecnologias Utilizadas

- **PostgreSQL**: Sistema de gerenciamento de banco de dados relacional.
- **Docker**: Para containerização e orquestração do banco de dados.

## Estrutura do Banco de Dados

### Esquema

O banco de dados utiliza o esquema `contribuicao` para organizar suas tabelas.

### Tabelas

#### Aliquota

A tabela `aliquota` armazena informações sobre as alíquotas aplicadas às contribuições baseadas em categorias e faixas salariais.

- **id**: Identificador único da alíquota (chave primária).
- **categoria**: Categoria do contribuinte a qual a alíquota se aplica.
- **salario_inicio**: Salário inicial da faixa salarial para a qual a alíquota é aplicável.
- **salario_fim**: Salário final da faixa salarial para a qual a alíquota é aplicável.
- **valor_aliquota**: Valor percentual da alíquota a ser aplicada.

```sql
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
```
#### SalarioMinimoHistorico

A tabela `SalarioMinimoHistorico` a registra o histórico de valores do salário mínimo ao longo dos anos.

- **id**: Identificador único do registro do salário mínimo (chave primária).
- **data_minimo**: Data de referência para o valor do salário mínimo.
- **valor_salario_minimo_ano:**: Valor do salário mínimo no ano correspondente.


```sql
CREATE SEQUENCE IF NOT EXISTS contribuicao.salario_minimo_historico_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS contribuicao.salario_minimo_historico
(
    id bigint NOT NULL DEFAULT nextval('contribuicao.salario_minimo_historico_id_seq'),
    data_minimo date NOT NULL,
    valor_salario_minimo_ano numeric(10, 2) NOT NULL,
    CONSTRAINT salario_minimo_historico_pkey PRIMARY KEY (id)
);
```

### Relacionamento Lógico

As tabelas Aliquota e SalarioMinimoHistorico são usadas em conjunto para calcular o valor da contribuição ajustada dos contribuintes. Esses cálculos são realizados pelo sistema de "Gestão de Contribuições" e não são representados como relacionamentos físicos (chaves estrangeiras) no banco de dados. Em vez disso, o sistema realiza consultas cruzadas entre estas tabelas baseadas em regras de negócio para:

- Determinar a alíquota aplicável com base na categoria e no salário do contribuinte.
- Ajustar o valor da contribuição com base na variação do salário mínimo ao longo do tempo.

### Função do Sistema

Função no Sistema
A aplicação "Gestão de Contribuições" serve como um intermediário entre contribuintes e o sistema de benefícios. Ela utiliza as informações armazenadas na API de “Gestão de Contribuinte” e nas tabelas Aliquota e SalarioMinimoHistorico para:

- Calcular o valor das contribuições dos contribuintes.
- Ajustar o valor das contribuições com base em parâmetros econômicos (como o salário mínimo).
- Fornecer dados essenciais para o sistema de benefícios, permitindo a avaliação de elegibilidade e o cálculo dos benefícios com base nas contribuições dos indivíduos.
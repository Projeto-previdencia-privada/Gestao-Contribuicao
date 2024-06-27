# Gestão de Contribuições Backend

Este projeto, Gestão de Contribuições, é uma aplicação web composta por uma API REST desenvolvida para gerenciar contribuições de contribuintes, possibilitando cálculo do tempo total de contribuição e o valor total atualizado das contribuições com base nas alíquotas aplicáveis.

## Tecnologias Utilizadas

1. **Backend**:
    - Spring Boot 3.2.4: Para construção da API REST.
    - PostgreSQL 16: Como banco de dados.
    - Docker Compose 2.25: Para containerização da aplicação e dos serviços dependentes.

## Estrutura do Backend

```bash
Projeto-Gestao-Contribuicao/
│
├── backend/
│   ├── Dockerfile
│   ├── src/
│   ├── docker-compose.yml
│   ├── Dockerfile
│   ├── pom.xml
│   ├── .env
```

### Adicione as seguintes variáveis ao arquivo .env:

    DB_HOST: Host do banco de dados.
    DB_NAME: Nome do banco de dados.
    DB_USER: Usuário do banco de dados.
    DB_PASS: Senha do banco de dados.
    APP_PORT: Porta em que a aplicação será exposta.
    DB_PORT: Porta em que o banco de dados será exposto.
    CONTRIBUINTES_API_HOST: Host do serviço de contribuintes.
    CONTRIBUINTES_API_PORT: Porta do serviço de contribuintes.

## Execução com Docker Compose

Utilize o seguinte comando na raiz  do projeto  para subir a aplicação do Backend:

```bash
docker-compose up --build
```

## Portas Utilizadas

1. Aplicação: `8080`
2. Aplicação Frontend: `3000`
3. PostgreSQL: `5432`

## Uso dos endpoints Aliquotas
As aliquotas são utilizadas para calcular o valor da contribuição com base na categoria do contribuinte e seu salário. Abaixo estão os endpoints disponíveis para gerenciar as aliquotas.

- Listar Todas as Aliquotas
    * GET `/aliquotas`
    * Descrição: Retorna todas as aliquotas cadastradas no sistema.


- Criar Nova Aliquota
    * POST `/aliquotas/`
    * Descrição: Cria uma nova aliquota no sistema. É necessário enviar no corpo da requisição os dados da aliquota a ser criada.

- Atualizar Aliquota
    * PUT `/aliquotas/{categoria}`
    * Descrição: Atualiza os dados de uma aliquota existente. É necessário enviar no corpo da requisição os dados atualizados da aliquota.
- Deletar Aliquota
    * Delete `/aliquotas/{categoria}`
    * Descrição: Remove uma aliquota do sistema pelo seu Categoria.

## Obter  os dados do Contribuinte via API Contribuintes

-  consulta contribuinte(API GESTÃO CONTRIBUINTES).
    * GET `/contribuintes/contribuinte/{cpf}`
    * Descrição: Retorna detalhes sobre o contribuinte da Api dos Contribuintes, exemplo;
- Exemplo de exemplo:
  
```bash
{
  "cpf": "84688392052",
  "categoria": "MEI",
  "salario": 7500.00,
  
}

```

- Exemplo de Dados do contribuinte calculados
    * POST `/contribuintes/calcularc/contribuiçoes`
    * Descrição: Recebe CPF e períodos de contribuição para calcular o total contribuído.
- Dados de exemplo:

```bash
{
  "cpf": "84688392052",
  "periodos": ["2020-01-01", "2021-01-01"]
}  



- Dados de resposta exemplo:


{
  "CPF": "84688392052",
  "Categoria": "MEI",
  "Salário": "7500",
  "Alíquota": "20",
  "periodosContribuicao": ["2020-01-01", "2021-01-01"]
  "tempoContribuicaoMeses": 53,
  "ValorContribuilçãoMensal": "1500",
  "TotalContribuido(Sem Ajuste)": "79.500",
  "ValorAjusteAplicado": "28540.42",
  "Total Contribuido (Ajustado)": 144308.18
}

```
- Exemplo de Dados do contribuinte calculados
    * GET `/contribuintes/consultar/{cpf}`
    * Descrição: Retorna detalhes sobre o contribuinte, incluindo tempo total
      de contribuição em meses e o total contribuído ajustado, considerando o
      salário, categoria, e variações do salário mínimo.
- Dados de exemplo:
```bash
{
  "CPF": "84688392052",
  "Categoria": "MEI",
  "Salário": "7500",
  "Alíquota": "20%",
  "tempoContribuicaoMeses": 53,
  "Valor Contribuilção Mensal": "1500",
  "Total Contribuido (Sem Ajuste)": "79.500",
  "Valor Ajuste Aplicado": "28540.42",
  "Total Contribuido (Ajustado)": 144308.18
}
```

## Documentação da API com Swagger

A documentação da API está disponível através do Swagger UI, que fornece uma interface interativa para explorar e testar os endpoints da API.

Para acessar a documentação da API via Swagger UI, após iniciar os serviços com Docker Compose, navegue para:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Na interface do Swagger UI, você encontrará a lista de todos os endpoints disponíveis. Você também pode executar chamadas à API diretamente da interface do Swagger para testar os comportamentos. 
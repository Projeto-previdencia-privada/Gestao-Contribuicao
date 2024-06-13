# Gestão de Contribuições Frontend

Este projeto frontend permite a interação com a API e visualização dos dados.

## Tecnologias Utilizadas

1. **Frontend**:
    - Spring Boot 3.2.4: Para construção da API REST.
    - PostgreSQL 16: Como banco de dados.
    - Docker Compose 2.25: Para containerização da aplicação e dos serviços dependentes.

## Estrutura do Backend

```bash
Projeto-Gestao-Contribuicao/
│
├── Frontend/
│   ├── Dockerfile
│   ├── src/
│   ├── conf/
│   ├── docker-compose.yml
│   ├── Dockerfile
│   ├── dist.xml
│   ├── .env
│   ├── index.html
│   ├── vite.config.js
```

### Adicione as seguintes variáveis ao arquivo .env:

    VITE_API_BASE_URL= URL do serviço backend

## Execução com Docker Compose

Utilize o seguinte comando na raiz  do projeto  para subir a aplicação do Frontend:

```bash
docker-compose up --build
```

## Portas Utilizadas

1. Aplicação: `8080`
2. Aplicação Frontend: `3000`
3. PostgreSQL: `5432`
   
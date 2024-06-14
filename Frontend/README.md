# Gestão de Contribuições Frontend

Este projeto frontend permite a interação com a API e visualização dos dados.

## Guia de Instalação

Para iniciar o Projeto deverá ser instalado o pacote @gov-ds/core do site do [Gov.br](https://www.gov.br/ds/como-comecar/instalacao)

## Tecnologias Utilizadas

1. **Frontend**:
    - React: Para a construção da interface de usuário.
    - JavaScript: Como linguagem desenvolvimento.
    - Nginx: Para servir a aplicação.
    - Vite: Para o build e desenvolvimento rápido.
    - Docker e Docker Compose: Para containerização da aplicação e orquestração dos serviços.

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

    VITE_API_BASE_URL=http://URL_DO_SERVICO_BACKEND:PORTA

## Execução com Docker Compose

Utilize o seguinte comando na raiz  do projeto  para subir a aplicação do Frontend:

```bash
docker-compose up --build
```

## Portas Utilizadas

1. Aplicação: `8080`
2. Aplicação Frontend: `3000`
3. PostgreSQL: `5432`
   
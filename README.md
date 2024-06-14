# Gestão de Contribuições

Este repositório contém os componentes do sistema de Gestão de Contribuições, um sistema que gerencia alíquotas e contribuições de contribuintes em um regime de previdência privada.

## Estrutura do Repositório

- **backend**: Contém o código-fonte do serviço backend que lida com as operações relacionadas às alíquotas e contribuições.
- **frontend**: Contém o código-fonte do frontend da aplicação, que fornece uma interface de usuário para interação com o sistema.
- **banco_de_dados**: Contém os scripts de banco de dados e as configurações relacionadas ao PostgreSQL.

## Instruções de Configuração e Execução

### Backend

O backend é uma aplicação Spring Boot que se comunica com um banco de dados PostgreSQL. Para configurar e executar o backend:

1. Navegue até a pasta `backend`.
2. Consulte o README.md dentro da pasta `backend` para obter instruções detalhadas sobre como configurar e executar o serviço.

### Frontend

O frontend é uma aplicação React que se comunica com o backend para exibir e gerenciar os dados dos contribuintes e suas contribuições. Para configurar e executar o frontend:

1. Navegue até a pasta `frontend`.
2. Consulte o README.md dentro da pasta `frontend` para obter instruções detalhadas sobre como configurar e executar o serviço.

### Banco de Dados

O banco de dados é gerenciado por um container Docker que utiliza PostgreSQL. Para configurar e executar o banco de dados:

1. Navegue até a pasta `banco_de_dados`.
2. Consulte o README.md dentro da pasta `banco_de_dados` para obter instruções detalhadas sobre como configurar e executar o serviço.


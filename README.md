[FastSolution] 🎯 Proposta do Projeto Assistência organizacional para uma empresa de grande porte e alta relevância no mercado.

Funcionalidades Principais:

Módulo de Autenticação Seguro: Cadastro e Login de Usuários com criptografia de senha (BCrypt) e controle de acesso via Spring Security.

[CRUD de Produtos/Serviços]: Funcionalidade completa de Criação, Leitura, Atualização e Deleção de [Produto/Serviço/Entidade Principal].

[Funcionalidade X]: Ex: Gerenciamento de Carrinho de Compras.

[Funcionalidade Y]: Ex: Visualização de pedidos por status.

⚙️ Estrutura da Aplicação (Back-end) A aplicação Back-end foi desenvolvida utilizando Spring Boot (Java 17) e segue a arquitetura em camadas, garantindo a separação de responsabilidades (Clean Code):

com.example.challenge.controller: Responsável por receber requisições HTTP e retornar respostas. Inclui a lógica de navegação e tratamento de formulários.

com.example.challenge.service: Contém a lógica de negócios (business rules) da aplicação.

com.example.challenge.repository: Interface de comunicação com o banco de dados (JPA/Hibernate).

com.example.challenge.domain: Classes de domínio/entidades de banco de dados (@Entity).

com.example.challenge.dto: Objetos de Transferência de Dados, utilizados para comunicação entre camadas e validação de formulários (@Valid).

Tecnologias Utilizadas:

Linguagem: Java 17

Framework: Spring Boot 3

Segurança: Spring Security (BCrypt)

Banco de Dados: [Ex: H2 Database ou PostgreSQL]

Testes: JUnit 5 e Mockito (com cobertura em UserService)

👥 Integrantes Nome Completo

RM

Felipe Orikasa

557435

Marcelo Bonfim

558254

Antonio Cauê

558891

🛠️ Como Rodar o Projeto Clonar o Repositório: git clone https://youtu.be/Vs-nrSVce-g.

Abrir: Importar o projeto em sua IDE preferida (IntelliJ, Eclipse, VS Code).

Configuração (Opcional): Se usar um banco externo (PostgreSQL/MySQL), configure o application.properties.

Executar: Inicie a classe principal ChallengeApplication.

O aplicativo estará acessível em http://localhost:8080.


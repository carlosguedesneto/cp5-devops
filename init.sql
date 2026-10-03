-- DDL: Criação das Tabelas
CREATE TABLE profissoes (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255) NOT NULL
);

CREATE TABLE pessoas (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    idade INT NOT NULL,
    profissao_id BIGINT NOT NULL,
    CONSTRAINT fk_pessoa_profissao
        FOREIGN KEY (profissao_id)
        REFERENCES profissoes(id)
);

-- DML: Inserção de Dados
INSERT INTO profissoes (nome, descricao) VALUES ('Desenvolvedor', 'Cria e mantém sistemas');
INSERT INTO profissoes (nome, descricao) VALUES ('Engenheiro de Dados', 'Constrói arquiteturas de dados');
INSERT INTO pessoas (nome, idade, profissao_id) VALUES ('Carlos Silva', 28, 1);
INSERT INTO pessoas (nome, idade, profissao_id) VALUES ('Ana Souza', 32, 2);

use projetocms;

select * from pessoas;
select * from usuarios;
select * from authorities;
select * from sites;
select * from paginas;

/* TODAS AS SENHAS SÃO 1 até 8 12345678 */ 

INSERT INTO pessoas (cpf, data_nascimento, nome) 
VALUES ('12345678901', '1990-01-01', 'João'),
       ('98765432109', '1985-05-15', 'Maria'),
       ('45678912304', '1998-09-30', 'Pedro'),
       ('78901234567', '1992-12-10', 'Ana'),
       ('54321098760', '1987-07-20', 'Sofia');

INSERT INTO usuarios (admin, data_reg, email, senha, usuario_ativo)
VALUES (true, '2023-05-01', 'usuario1@example.com', '$2a$10$QwuPq5hTzuAZY3pKLN7QSOOPwxF4WzDn2pY/jrnVmW/NW2Jk35ScW', true),
       (false, '2023-05-02','usuario2@example.com', '$2a$10$QwuPq5hTzuAZY3pKLN7QSOOPwxF4WzDn2pY/jrnVmW/NW2Jk35ScW', true),
       (false, '2023-05-03','usuario3@example.com', '$2a$10$QwuPq5hTzuAZY3pKLN7QSOOPwxF4WzDn2pY/jrnVmW/NW2Jk35ScW', true),
       (false, '2023-05-04','usuario4@example.com', '$2a$10$QwuPq5hTzuAZY3pKLN7QSOOPwxF4WzDn2pY/jrnVmW/NW2Jk35ScW', true),
       (true, '2023-05-05', 'usuario5@example.com', '$2a$10$QwuPq5hTzuAZY3pKLN7QSOOPwxF4WzDn2pY/jrnVmW/NW2Jk35ScW', true);

INSERT INTO authorities (authority, email, appuser_fk)
VALUES
    ('ADM', 'usuario1@example.com', 1),
    ('USER', 'usuario2@example.com', 2),
    ('USER', 'usuario3@example.com', 3),
    ('USER', 'usuario4@example.com', 4),
    ('ADM', 'usuario5@example.com', 5);
    
INSERT INTO sites (nome, url, usuario_id)
VALUES
  ('Site 1', 'https://www.site1.com', 1),
  ('Site 2', 'https://www.site2.com', 2),
  ('Site 3', 'https://www.site3.com', 3),
  ('Site 4', 'https://www.site4.com', 4),
  ('Site 5', 'https://www.site5.com', 5);


INSERT INTO paginas (conteudo, titulo, site_id)
VALUES
    ('Conteúdo da página 1', 'Título da página 1', 1),
    ('Conteúdo da página 2', 'Título da página 2', 2),
    ('Conteúdo da página 3', 'Título da página 3', 3),
    ('Conteúdo da página 4', 'Título da página 4', 4),
    ('Conteúdo da página 5', 'Título da página 5', 5);

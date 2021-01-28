SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS roles;

CREATE TABLE roles (
                       id int(11) NOT NULL AUTO_INCREMENT,
                       name varchar(50) DEFAULT NULL,
                       PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO roles (name)
VALUES
('ROLE_ADMIN'),('ROLE_MANAGER'),('ROLE_USER'),('ROLE_GUEST');

DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id int(11) NOT NULL AUTO_INCREMENT,
                       username varchar(50) NOT NULL,
                       pass char(100) NOT NULL,
                       first_name varchar(50) NOT NULL,
                       last_name varchar(50) DEFAULT NULL,
                       email varchar(50) NOT NULL,
                       PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO users (username,pass,first_name,last_name,email)
VALUES
('alex','alex','Александр','Иванов','alex@stk.com'),
('andre','andre','Андрей','Петров','andre@stk.com'),
('serge','serge','Сергей','Сидоров','serge@stk.com');

DROP TABLE IF EXISTS users_roles;

CREATE TABLE user_roles (
                             user_id int(11) NOT NULL,
                             role_id int(11) NOT NULL,

                             PRIMARY KEY (user_id, role_id),

                             KEY FK_ROLE_idx (role_id),

                             CONSTRAINT FK_USER FOREIGN KEY (user_id)
                                 REFERENCES users (id)
                                 ON DELETE NO ACTION ON UPDATE NO ACTION,

                             CONSTRAINT FK_ROLE FOREIGN KEY (role_id)
                                 REFERENCES roles (id)
                                 ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO user_roles (user_id, role_id)
VALUES
(1, 1),
(2, 3),
(3, 2);

DROP TABLE IF EXISTS products;

CREATE TABLE products (
                            id bigint NOT NULL AUTO_INCREMENT,
                            name varchar(100) NOT NULL,
                            description varchar(255) DEFAULT NULL,
                            price decimal(19,2) NOT NULL,
                            stock bigint NOT NULL,
                            PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
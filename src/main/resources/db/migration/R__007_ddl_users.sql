DROP TABLE IF EXISTS users;

CREATE TABLE users (
  u_id bigint NOT NULL AUTO_INCREMENT,
  u_login varchar(50) NOT NULL,
  u_pass char(80) NOT NULL,
  u_first_name varchar(50) NOT NULL,
  u_last_name varchar(50) DEFAULT NULL,
  u_email varchar(50) NOT NULL,
  PRIMARY KEY (u_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


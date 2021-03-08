SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS user_roles;

CREATE TABLE user_roles (
  user_id bigint NOT NULL,
  role_id int NOT NULL,
  PRIMARY KEY (user_id, role_id),
  KEY fk_role_idx (role_id),
  CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (r_id),
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (u_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
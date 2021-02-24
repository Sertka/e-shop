SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
  o_id bigint NOT NULL AUTO_INCREMENT,
  user_id bigint NOT NULL,
  o_status varchar(15) NOT NULL,
  o_price decimal(10,2) NOT NULL,
  o_delivery_date datetime NOT NULL,
  o_create_date datetime NOT NULL,
  o_update_date datetime DEFAULT NULL,
  o_phone varchar(20) NOT NULL,
  o_address varchar(150) NOT NULL,
  o_details varchar(255) DEFAULT NULL,
  PRIMARY KEY (o_id),
  KEY fk_order_user_idx (user_id),
  CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users (u_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
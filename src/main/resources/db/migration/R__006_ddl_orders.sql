SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
  o_id bigint NOT NULL AUTO_INCREMENT,
  user_id bigint NOT NULL,
  status_code char(3) COLLATE utf8_bin NOT NULL,
  o_price decimal(10,2) NOT NULL,
  o_delivery_date datetime NOT NULL,
  o_create_date datetime NOT NULL,
  o_update_date datetime DEFAULT NULL,
  o_details varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (o_id),
  KEY fk_order_user_idx (user_id),
  CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users (u_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;
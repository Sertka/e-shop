SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS order_items;

CREATE TABLE order_items (
  oi_id bigint NOT NULL AUTO_INCREMENT,
  product_id bigint NOT NULL,
  order_id bigint NOT NULL,
  oi_quantity int NOT NULL,
  oi_price decimal(10,2) NOT NULL,
  oi_details varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (oi_id),
  KEY fk_item_oder_idx (order_id),
  KEY fk_item_product_idx (product_id),
  CONSTRAINT fk_item_oder FOREIGN KEY (order_id) REFERENCES orders (o_id),
  CONSTRAINT fk_item_product FOREIGN KEY (product_id) REFERENCES products (p_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;
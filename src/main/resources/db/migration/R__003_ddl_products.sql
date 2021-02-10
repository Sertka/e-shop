DROP TABLE IF EXISTS products;

CREATE TABLE products (
  p_id bigint NOT NULL AUTO_INCREMENT,
  p_name varchar(100) NOT NULL,
  p_description varchar(300) DEFAULT NULL,
  p_price decimal(10,2) NOT NULL,
  p_stock int NOT NULL,
  p_size varchar (50),
  p_weight INT NOT NULL,
  brand_id int NOT NULL,
  type_id int NOT NULL,
  PRIMARY KEY (p_id),
  KEY fk_product_brand_idx (brand_id),
  KEY fk_product_type_idx (type_id),
  CONSTRAINT fk_product_brand FOREIGN KEY (brand_id) REFERENCES product_brands (b_id),
  CONSTRAINT fk_product_type FOREIGN KEY (type_id) REFERENCES product_types (pt_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
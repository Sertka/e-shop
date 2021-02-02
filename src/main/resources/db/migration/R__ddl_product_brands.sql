DROP TABLE IF EXISTS product_brands;

CREATE TABLE product_brands (
  b_id int NOT NULL AUTO_INCREMENT,
  b_name varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (b_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

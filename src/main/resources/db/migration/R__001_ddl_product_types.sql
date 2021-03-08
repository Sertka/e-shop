DROP TABLE IF EXISTS product_types;

CREATE TABLE product_types (
  pt_id int NOT NULL AUTO_INCREMENT,
  pt_name varchar(50) NOT NULL,
  PRIMARY KEY (pt_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
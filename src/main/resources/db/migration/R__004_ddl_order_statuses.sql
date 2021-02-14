DROP TABLE IF EXISTS order_codes;

CREATE TABLE order_statuses (
  os_id bigint NOT NULL AUTO_INCREMENT,
  os_name varchar(30) NOT NULL,
  PRIMARY KEY (os_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
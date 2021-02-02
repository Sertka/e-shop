DROP TABLE IF EXISTS order_codes;

CREATE TABLE order_codes (
  oc_code char(3) COLLATE utf8_bin NOT NULL,
  oc_name varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (oc_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
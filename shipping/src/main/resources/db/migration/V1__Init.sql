CREATE TABLE IF NOT EXISTS shipping(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    external_id VARCHAR(32) NOT NULL,
    order_id VARCHAR(50) NOT NULL,
    amount FLOAT,
    status SMALLINT NOT NULL,
    shipping_date TIMESTAMP,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_by BIGINT NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY(id)
);

INSERT INTO shipping(external_id, order_id, amount, status, shipping_date, created_by, created_at, updated_by, updated_at)
    SELECT 'd707ada36e6644ddaec63a52e7a40d56', 'c611d780541541f69c1e1e80b966527a', 100, 1, '2024-11-23', 1, '2023-05-23', 1, '2023-06-30'
WHERE NOT EXISTS (
    SELECT 1 FROM shipping WHERE id = 1
);

INSERT INTO shipping(external_id, order_id, amount, status, shipping_date, created_by, created_at, updated_by, updated_at)
    SELECT '3b08299ce1fc4d5ea40e634f96169150', '850829b025704efe995d29661a3a1220', 200, 2, '2024-12-25', 2, '2023-06-21', 2, '2023-08-29'
WHERE NOT EXISTS (
    SELECT 1 FROM shipping WHERE id = 2
);

INSERT INTO shipping(external_id, order_id, amount, status, shipping_date, created_by, created_at, updated_by, updated_at)
    SELECT '52419eda2e5e4ec391fcfddd766ad407', '0a5eb04756f54776ac7752d3c8fae45b', 300, 2, '2024-12-25', 2, '2023-06-21', 2, '2023-08-29'
WHERE NOT EXISTS (
    SELECT 1 FROM shipping WHERE id = 2
);

CREATE TABLE IF NOT EXISTS shipping_detail(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    external_id VARCHAR(32) NOT NULL,
    shipping_id BIGINT NOT NULL,
    item_name VARCHAR(50) NOT NULL,
    item_description VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    amount FLOAT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_by BIGINT NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY(id),
    CONSTRAINT shipping_detail_shipping_id_foreign_key
          FOREIGN KEY(shipping_id)
    	  REFERENCES shipping(id)
    	  ON DELETE CASCADE
);

INSERT INTO shipping_detail(external_id, shipping_id, item_name, item_description, quantity, amount, created_by, created_at, updated_by, updated_at)
    SELECT 'ae61181973fd4896a99ecb4089005197', 1, 'Pizza', 'Pizza is good', 5, 2, 1, '2023-05-23', 1, '2023-06-30'
WHERE NOT EXISTS (
    SELECT 1 FROM shipping_detail WHERE id = 1
);

INSERT INTO shipping_detail(external_id, shipping_id, item_name, item_description, quantity, amount, created_by, created_at, updated_by, updated_at)
    SELECT 'f2953c61c5c841969054bae6b8eb1f4f', 1, 'Burger', 'Pizza is popular', 10, 3, 1, '2023-07-05', 1, '2023-09-15'
WHERE NOT EXISTS (
    SELECT 1 FROM shipping_detail WHERE id = 2
);

INSERT INTO shipping_detail(external_id, shipping_id, item_name, item_description, quantity, amount, created_by, created_at, updated_by, updated_at)
    SELECT '2fe2daff67e04b45937c296facc31e96', 2, 'Pasta', 'Pasta is delicious', 3, 5, 2, '2023-07-05', 2, '2023-09-15'
WHERE NOT EXISTS (
    SELECT 1 FROM shipping_detail WHERE id = 3
);

INSERT INTO shipping_detail(external_id, shipping_id, item_name, item_description, quantity, amount, created_by, created_at, updated_by, updated_at)
    SELECT '02c47fd887984607a6bf7f1549fe96ff', 3, 'Pasta', 'Pasta is delicious', 3, 5, 2, '2023-07-05', 2, '2023-09-15'
WHERE NOT EXISTS (
    SELECT 1 FROM shipping_detail WHERE id = 3
);
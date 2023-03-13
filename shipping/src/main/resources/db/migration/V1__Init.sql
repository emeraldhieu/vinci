CREATE TABLE IF NOT EXISTS shipping(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    reference_number VARCHAR(100),
    order_id BIGINT NOT NULL,
    amount FLOAT NOT NULL,
    status SMALLINT NOT NULL,
    shipping_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY(id)
);

INSERT INTO shipping(reference_number, order_id, amount, status, shipping_date, created_at, updated_at)
    SELECT '7e183413fdb04437be4495b98d36d32f', 1, 100, 1, '2024-11-23', '2023-05-23', '2023-06-30'
WHERE NOT EXISTS (
    SELECT 1 FROM shipping WHERE id = 1
);

INSERT INTO shipping(reference_number, order_id, amount, status, shipping_date, created_at, updated_at)
    SELECT '03893107945047d6a9f8c33a45c93fdb', 2, 200, 2, '2024-12-25', '2023-06-21', '2023-08-29'
WHERE NOT EXISTS (
    SELECT 1 FROM shipping WHERE id = 2
);

CREATE TABLE IF NOT EXISTS shipping_detail(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    shipping_id BIGINT NOT NULL,
    item_name VARCHAR(50) NOT NULL,
    item_description VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    amount FLOAT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY(id),
    CONSTRAINT shipping_detail_shipping_id_foreign_key
          FOREIGN KEY(shipping_id)
    	  REFERENCES shipping(id)
    	  ON DELETE CASCADE
);

INSERT INTO shipping_detail(shipping_id, item_name, item_description, quantity, amount, created_at, updated_at)
    SELECT 1, 'Pizza', 'Pizza is good', 5, 2, '2023-05-23', '2023-06-30'
WHERE NOT EXISTS (
    SELECT 1 FROM shipping_detail WHERE id = 1
);

INSERT INTO shipping_detail(shipping_id, item_name, item_description, quantity, amount, created_at, updated_at)
    SELECT 1, 'Burger', 'Pizza is popular', 10, 3, '2023-07-05', '2023-09-15'
WHERE NOT EXISTS (
    SELECT 1 FROM shipping_detail WHERE id = 2
);

INSERT INTO shipping_detail(shipping_id, item_name, item_description, quantity, amount, created_at, updated_at)
    SELECT 2, 'Pasta', 'Pasta is delicious', 3, 5, '2023-07-05', '2023-09-15'
WHERE NOT EXISTS (
    SELECT 1 FROM shipping_detail WHERE id = 3
);

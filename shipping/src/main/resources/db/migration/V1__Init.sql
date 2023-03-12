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

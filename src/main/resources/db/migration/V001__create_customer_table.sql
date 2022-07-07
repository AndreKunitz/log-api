CREATE TABLE customer (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,

    PRIMARY KEY (id)
);

INSERT INTO customer (name, email, phone) VALUES ('André', 'andrekunitz@gmail.com', '+00 00 0000 0000');
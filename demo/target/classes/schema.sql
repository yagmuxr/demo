-- schema.sql
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(25) NOT NULL,
    password VARCHAR(250) NOT NULL,
    version INT
    );

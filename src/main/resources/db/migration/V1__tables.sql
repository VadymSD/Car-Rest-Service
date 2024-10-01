DROP TABLE IF EXISTS cars;

CREATE TABLE cars(
    car_id SERIAL PRIMARY KEY,
    make VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    year_column INT NOT NULL,
    category TEXT
);




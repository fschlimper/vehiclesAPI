CREATE TABLE IF NOT EXISTS manufacturer (
    code int NOT NULL PRIMARY KEY,
    name VARCHAR(40)
);


CREATE TABLE IF NOT EXISTS car (
    id int NOT NULL PRIMARY KEY,
    createdAt TIMESTAMP,
    modifiedAt TIMESTAMP,
    condition VARCHAR(10),
    lat DOUBLE,
    lon DOUBLE,
    price DOUBLE,
    body VARCHAR(20),
    model VARCHAR(20),
    manufacturer_code int,
    FOREIGN KEY (manufacturer_code) REFERENCES manufacturer(code),
    numberOfDoors int,
    fuelType VARCHAR(20),
    engine VARCHAR(20),
    mileage int,
    modelYear int,
    productionYear int,
    externalColor VARCHAR(20)
);
DROP DATABASE IF EXISTS logininformation;
CREATE DATABASE logininformation;

USE logininformation;

CREATE TABLE Users (
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
);

INSERT INTO Users(username, password)
	VALUES	('user1', 'user1pw'),
			('user2', 'user2pw');

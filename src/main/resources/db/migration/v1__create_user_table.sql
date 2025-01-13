CREATE TABLE users (
    id serial primary key,
	name varchar(100),
	email varchar(100),
	password varchar(255),
	role varchar(50),
	created_at timestamp default current_timestamp,
	updated_at timestamp default Current_timestamp
);
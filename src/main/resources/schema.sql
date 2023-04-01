DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friendship CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS mpa_rating CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS film_genres CASCADE;

create table if not exists users(
user_id int generated by default as identity primary key,
email varchar NOT NULL,
login varchar(255) NOT NULL,
name varchar(255),
birthday date NOT NULL
);

create table if not exists friendship(
user_id int,
friend_id int
);

create table if not exists mpa_rating(
rating_id int auto_increment,
name varchar(255)
);

create table if not exists films(
film_id int generated by default as identity primary key,
name varchar(255) NOT NULL,
description varchar(200) NOT NULL,
releaseDate date NOT NULL,
duration int,
rating_id int
);

create table if not exists likes(
film_id int,
user_id int
);

create table if not exists genres(
genre_id int auto_increment primary key,
name varchar(255)
);

create table if not exists film_genres(
film_id int,
genre_id int
);
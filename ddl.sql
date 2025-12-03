-- queries to set up the database go here
create database if not exists gamegram;

use gamegram;

CREATE TABLE if not exists games(
   game_id      INTEGER  NOT NULL PRIMARY KEY 
  ,name         VARCHAR(132) NOT NULL
  ,platform     VARCHAR(4) NOT NULL
  ,year         NUMERIC(6,1)
  ,genre        VARCHAR(12) NOT NULL
  ,publisher    VARCHAR(38)
  ,na_sales     NUMERIC(5,2)
  ,eu_sales     NUMERIC(5,2)
  ,jp_sales     NUMERIC(5,2)
  ,other_sales  NUMERIC(5,2)
  ,global_sales NUMERIC(5,2)
);

create table if not exists user (
    userId int auto_increment,
    username varchar(255) not null,
    password varchar(255) not null,
    firstName varchar(255) not null,
    lastName varchar(255) not null,
    primary key (userId),
    unique (username),
    constraint userName_min_length check (char_length(trim(userName)) >= 2),
    constraint firstName_min_length check (char_length(trim(firstName)) >= 2),
    constraint lastName_min_length check (char_length(trim(lastName)) >= 2)
);

create table if not exists follows (
    followingId int not null,
    followedId int not null,
    primary key (followingId, followedId),
    key followedId (followedId),
    constraint follows_ibfk_1 foreign key (followingId) references user (userId),
    constraint follows_ibfk_2 foreign key (followedId) references user (userId)
);

create table if not exists review (
    reviewId int auto_increment,
    game_id int not null,
    hoursPlayed int not null,
    content varchar(1000) not null,
    reviewRating int check (reviewRating >= 0 AND reviewRating <= 10) not null,
    reviewDate datetime not null,
    userId int not null,
    heartsCount int default 0,
    commentsCount int default 0,
    isHearted int default 0,
    isBookmarked int default 0,
    primary key (reviewId),
    foreign key (userId) references user (userId),
    foreign key (game_id) references games (game_id)
);
-- queries to set up the database go here
use gamegram;

CREATE TABLE if not exists games(
   game_id      INTEGER  NOT NULL PRIMARY KEY 
  ,name         VARCHAR(132) NOT NULL
  ,genre        VARCHAR(12) NOT NULL
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

create table if not exists platforms (
    game_id int not null,
    platform varchar(4) not null,
    year numeric(6,1),
    publisher varchar(38)
    ,na_sales     NUMERIC(5,2)
    ,eu_sales     NUMERIC(5,2)
    ,jp_sales     NUMERIC(5,2)
    ,other_sales  NUMERIC(5,2)
    ,global_sales NUMERIC(5,2)
    ,primary key (game_id, platform),
    constraint platforms_ibfk_1 foreign key (game_id) references games (game_id)
);

create table if not exists review (
    reviewId int auto_increment,
    userId int not null,
    game_id int not null,
    hoursPlayed int not null,
    reviewRating int not null,
    content varchar(1000) not null,
    postDate datetime default current_timestamp,
    heartsCount int default 0,
    commentsCount int default 0,
    isHearted boolean default false,
    isBookmarked boolean default false,
    primary key (reviewId),
    constraint review_ibfk_1 foreign key (userId) references user (userId),
    constraint review_ibfk_2 foreign key (game_id) references games (game_id)
);

create table if not exists review_hearts (
    userId int not null,
    reviewId int not null,
    primary key (userId, reviewId),
    foreign key (userId) references user(userId),
    foreign key (reviewId) references review(reviewId)
);

create table if not exists review_comments (
    commentId int auto_increment,
    reviewId int not null,
    userId int not null,
    commentText varchar(255) not null,
    commentDate datetime not null,
    primary key (commentId),
    foreign key (userId) references user(userId),
    foreign key (reviewId) references review(reviewId)
);
-- queries to set up the database go here

CREATE TABLE games(
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
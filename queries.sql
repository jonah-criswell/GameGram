-- every query used in the code to retrieve stuff on the site goes here, 
-- along with a comment explaining what each one does and where

-- Homepage 
-- Finds the 10 most popular games worldwide based on sales. 
SELECT * FROM games ORDER BY global_sales DESC LIMIT 10;
-- Finds the 10 most popular games that have a particular platform
SELECT * FROM games WHERE platform = ? ORDER BY global_sales DESC LIMIT 10;
-- Finds the 10 most popular games that have a particular publisher
SELECT * FROM games WHERE publisher = ? ORDER BY global_sales DESC LIMIT 10;
-- Finds the 10 most popular games that were published in a particular decade
SELECT * FROM games WHERE FLOOR(year / 10) = FLOOR(? / 10) ORDER BY global_sales DESC LIMIT 10;
-- Finds the 10 most popular games among the user's friends






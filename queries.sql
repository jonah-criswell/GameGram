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

-- Friends / Follows Page

-- Games Page (for browsing and searching games)
-- Using the search bar, get games with the title that the user inputs
SELECT * FROM games WHERE name like ?;

-- Game Page (for whatever game is being looked at by the user)
-- Get information about the specific game
SELECT * FROM games WHERE game_id = ?;
-- Get the game info and reviews for the game
SELECT * FROM games g LEFT JOIN reviews r ON g.game_id = r.review_of WHERE g.game_id = ?;

-- Reviews
-- Get the info for a review and its comments
SELECT * FROM reviews r LEFT JOIN review_comments c ON r.review_id = c.posted_on WHERE r.review_id = ?;

-- Profile Page
-- Get user info and all their reviews
SELECT * FROM user u LEFT JOIN reviews r on u.userId = r.review_by WHERE u.userId = ?;


-- Bookmark tab: shows games you want to play, and basic info about each game
-- Retrieves games that the user has bookmarked 




-- every query used in the code to retrieve stuff on the site goes here, 
-- along with a comment explaining what each one does and where

-- game summary creation to run after inserting all the data
CREATE VIEW game_summary AS
SELECT
    g.game_id,
    g.name,
    g.genre,
    GROUP_CONCAT(DISTINCT p.platform ORDER BY p.platform) AS platforms,
    GROUP_CONCAT(DISTINCT p.publisher ORDER BY p.publisher) AS publishers,
    GROUP_CONCAT(DISTINCT p.year ORDER BY p.year) AS years,
    SUM(p.global_sales) AS total_global_sales
FROM games g
JOIN platforms p ON g.game_id = p.game_id
GROUP BY g.game_id, g.name, g.genre;

-- User registration
-- insert a new user and their information into the database
insert into user (username, password, firstName, lastName) values (?, ?, ?, ?)

-- Homepage 
-- Finds the 10 most popular games worldwide based on sales. 
SELECT * FROM game_summary ORDER BY total_global_sales DESC LIMIT 10;
-- Finds the users not followed by the user
WITH followed_users as (SELECT f.followedId as friend_id FROM follows f WHERE f.followingId = ?)
SELECT * FROM user u WHERE u.user_id NOT IN (SELECT friend_id FROM followed_users) AND u.user_id <> ?;
-- Finds the 10 most popular games among the user's friends
WITH followed_users AS (SELECT f.followedId AS friend_id FROM follows f WHERE f.followingId = ?),
followed_reviews AS (SELECT r.review_id, r.review_of FROM reviews r JOIN followed_users fu ON r.review_by = fu.friend_id)
SELECT gs.*, COUNT(fr.review_id) AS r_count FROM game_summary gs LEFT JOIN followed_reviews fr ON fr.review_of = gs.game_id
GROUP BY gs.game_id ORDER BY r_count DESC LIMIT 10;

-- - don't know if we're actually using these - ---------------------------------------------------------------------------------------
-- Finds the 10 most popular games that have a particular platform
SELECT * FROM game_summary WHERE FIND_IN_SET(?, platforms) ORDER BY total_global_sales DESC LIMIT 10;
-- Finds the 10 most popular games that have a particular publisher
SELECT * FROM game_summary WHERE FIND_IN_SET(?, publishers) ORDER BY total_global_sales DESC LIMIT 10;
-- Finds the 10 most popular games that were published in a particular decade
SELECT * FROM game_summary WHERE EXISTS (SELECT 1 FROM platforms p WHERE p.game_id = game_summary.game_id 
AND FLOOR(p.year / 10) = FLOOR(? / 10)) ORDER BY total_global_sales DESC LIMIT 10;
----------------------------------------------------------------------------------------------------------------------------------------

-- Games Page (for browsing and searching games)
-- Using the search bar, get games with the title that the user inputs
SELECT g.game_id, g.name, g.genre, p.platform, p.year, p.publisher, p.na_sales, p.eu_sales, p.jp_sales, p.other_sales, p.global_sales
        FROM games g
        JOIN platforms p ON p.game_id = g.game_id
        WHERE g.name LIKE ?;
-- get the average rating for a game
SELECT AVG(rating) as avg_rating FROM reviews WHERE review_of = ?;

-- Profile Page
-- Get user info and all their reviews 
SELECT * FROM user u LEFT JOIN reviews r on u.userId = r.review_by WHERE u.userId = ?;
-- Get the user's average review rating
SELECT AVG(rating) as avg_rating FROM reviews WHERE review_by = ?;

-- Friends / Follows Page
-- Get all users that the current user follows
SELECT * FROM user u LEFT JOIN follows f ON u.userId = f.followedId WHERE f.followingId = ?; 

-- Reviews
-- Insert a review into the database when a user makes a review
--INSERT INTO reviews ()

-- Hearts and Comments for Reviews
-- When a user hearts a post this stores that in the database. Used on any page displaying posts 
INSERT INTO review_hearts (review_id, userId) VALUES (?, ?);
--When a user unhearts a post this removes the heart from the database. Used on any page displaying posts 
DELETE FROM review_hearts WHERE review_id = ? AND userId = ?;
-- Access a review's number of hearts
SELECT COUNT(userId) as heart_count FROM review_hearts WHERE review_id = ?;
-- When a user posts a comment this stores the comment's information in the database.  
INSERT INTO review_comments (comment_id, comment_text, comment_date, posted_on, posted_by) VALUES (?, ?, now(), ?, ?);
-- Access a review's number of comments
SELECT COUNT(comment_id) as comment_count FROM review_comments WHERE posted_on = ?;




-- Bookmark tab: shows games you want to play, and basic info about each game
-- Retrieves games that the user has bookmarked 

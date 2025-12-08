-- every query used in the code to retrieve stuff on the site goes here, 
-- along with a comment explaining what each one does and where

-- User registration and authentication
-- insert a new user and their information into the database
insert into user (username, password, firstName, lastName) values (?, ?, ?, ?);
-- Authentication
select * from user where username = ?;
-- Get the info of a specific user 
select * from user where userId = ?;
-- Get the users a user follows
SELECT * FROM user u LEFT JOIN follows f ON u.userId = f.followedId WHERE f.followingId = ?;

-- Homepage 
-- Get the 10 most popular games based on global sales
SELECT g.game_id, g.name, g.genre, p.platform, p.year, p.publisher,
                           p.na_sales, p.eu_sales, p.jp_sales, p.other_sales, p.global_sales
                    FROM games g
                    JOIN platforms p ON g.game_id = p.game_id
                    ORDER BY p.global_sales DESC
                    LIMIT 10;
-- Finds the users not followed by the user
WITH followed_users as (SELECT f.followedId as friend_id FROM follows f WHERE f.followingId = ?)
SELECT * FROM user u WHERE u.user_id NOT IN (SELECT friend_id FROM followed_users) AND u.user_id <> ?;

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
SELECT 
                 r.reviewId, r.content, DATE_FORMAT(r.postDate, '%M %d, %Y') as formattedDate,
                 r.game_id, r.hoursPlayed, r.reviewRating, r.heartsCount, r.commentsCount, 
                 r.isHearted, r.isBookmarked, 
                 u.userId, u.firstName, u.lastName, 
                 g.name as gameName 
                 FROM review r 
                 JOIN user u ON r.userId = u.userId 
                 JOIN games g ON r.game_id = g.game_id 
                 WHERE u.userId = ? 
                 ORDER BY r.postDate DESC;

-- Friends / Follows Page
-- Get all users that the current user follows
SELECT * FROM user u LEFT JOIN follows f ON u.userId = f.followedId WHERE f.followingId = ?; 
-- When a user follows another user, update the database to reflect this
INSERT INTO follows (followingId, followedId) VALUES (?, ?);
-- When a user unfollows another user 
DELETE FROM follows WHERE followingId = ? AND followedId = ?;

-- Reviews
-- Insert a review into the database when a user makes a review
insert into review (game_id, hoursPlayed, content, reviewRating, postDate, userId) values (?, ?, ?, ?, now(), ?);
-- Get all reviews for a game
SELECT * FROM review WHERE game_id = ?;

-- Hearts and Comments for Reviews
-- When a user hearts a post this stores that in the database. Used on any page displaying posts 
insert into review_hearts (reviewId, userId) values (?, ?);
--When a user unhearts a post this removes the heart from the database. Used on any page displaying posts 
delete from review_hearts where reviewId = ? and userId = ?;
-- Everytime a review is liked or unliked, the review's like count is updated
update review set heartsCount = (
                        SELECT COUNT(userId) as heart_count FROM review_hearts WHERE reviewId = ?)  
                        WHERE reviewId = ?;
-- Access a review's number of hearts
SELECT heartsCount FROM review WHERE reviewId = ?;
-- When a user posts a comment this stores the comment's information in the database.  
INSERT INTO review_comments (commentText, commentDate, reviewId, userId) VALUES (?, now(), ?, ?)
-- When a user posts a comment this updates the comment count variable for the review
update review set commentsCount = (
                        SELECT COUNT(userId) as comment_count FROM review_comments WHERE reviewId = ?)  
                        WHERE reviewId = ?;
-- Access a review's number of comments
SELECT commentsCount FROM review WHERE reviewId = ?;


CSCI4370 final project - GameGram
Group 16: Aidan Murray, Mason Gindlesperger, Christopher McIntosh, Jonah Criswell, Joey Vos


List what each team member did under their name:

    Mason: Login, Authentication, Profile Page, Front End Programming
    Jonah: Game Search Page, Review object creation
    Christopher: Likes, Comments, Most Popular Games impl, home page
    Joey: Front End Programming, User Profile Pages
    Aidan: Review object creation, friends page, home page, Review Details Page

Include all the technologies you used including 3rd party libraries:

    We used the 3rd Party Dataset, from Kaggle with the most popular video games found here:
    https://www.kaggle.com/datasets/ulrikthygepedersen/video-games-sales

    We also used BCrypt to make make our passcodes secure

Include the database name, database username and the password you used for the JDBC connection.

    The database name is gamegram, the username is root and the passcode is mysqlpass. 
    This infomation and more can be found in the ddl.sql file and application.properties files.

Provide three test username and password pairs from your demo data.

<<<<<<< HEAD
    Since the username and passcodes are encrypted using BCrypt, there are INACCESSIBLE to the another user, when inserted via SQL commands.
    This means you MUST CREATE A NEW USER when logging in. For you convienience, here are 3 sample users you can input on the registration page.

    firstName: Ross
    lastName: Geller
    username: rossgeller
    passcode: fossilsrock

    firstName: Monica
    lastName: Geller
    username: monicageller
    passcode: chef123

    firstName: Chandler
    lastName: Bing
    username: chandlerbing
    passcode: whatsmyjob

Run the project: ./mvnw spring-boot:run
=======
Run the project: ./mvnw spring-boot:run
>>>>>>> a499a2fa129a119a0e598bec2b8aae9ba08b60d2

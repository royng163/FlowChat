-- Create and use the database
CREATE DATABASE IF NOT EXISTS `flowchat_db`;
USE `flowchat_db`;

-- Drop existing tables to ensure a clean slate
DROP TABLE IF EXISTS `Follow`, `Block`, `User_Profile`, `Message_Image`, `Message`, `Recommendation`, `Post_Image`, `Post_Tag`, `Tag_Data`, `View`, `Dislike`, `Like`, `Post`, `Image_Data`, `Authentication`, `User_Account`, `Role`;

-- Create Tables
CREATE TABLE `Role` (
  `role_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE `User_Account` (
  `user_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) UNIQUE NOT NULL,
  `email` VARCHAR(100) UNIQUE NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `is_active` BOOLEAN NOT NULL DEFAULT 0,
  `role_id` INT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL
);

CREATE TABLE `Authentication` (
  `key_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `key_code` VARCHAR(16) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `expires_at` DATETIME,
  `is_available` BOOLEAN NOT NULL DEFAULT 1
);

CREATE TABLE `Image_Data` (
  `image_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `image_name` VARCHAR(255) NOT NULL,
  `image_data` LONGBLOB NOT NULL,
  `image_format` VARCHAR(15) NOT NULL
);

CREATE TABLE `Post` (
  `post_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `title` VARCHAR(100),
  `content` VARCHAR(1000) NOT NULL,
  `view_count` INT NOT NULL DEFAULT 0,
  `like_count` INT NOT NULL DEFAULT 0,
  `dislike_count` INT NOT NULL DEFAULT 0,
  `comment_count` INT NOT NULL DEFAULT 0,
  `popularity_score` INT NOT NULL DEFAULT 0,
  `attach_to` INT NOT NULL DEFAULT 0,
  `is_active` BOOLEAN NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL
);

CREATE TABLE `Like` (
  `post_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`post_id`, `user_id`)
);

CREATE TABLE `Dislike` (
  `post_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`post_id`, `user_id`)
);

CREATE TABLE `View` (
  `post_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`post_id`, `user_id`)
);

CREATE TABLE `Tag_Data` (
  `tag_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `tag_name` VARCHAR(100) NOT NULL,
  `keyword` VARCHAR(500)
);

CREATE TABLE `Post_Tag` (
  `post_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  PRIMARY KEY (`post_id`, `tag_id`)
);

CREATE TABLE `Post_Image` (
  `post_id` INT NOT NULL,
  `image_id` INT NOT NULL,
  PRIMARY KEY (`post_id`, `image_id`)
);

CREATE TABLE `Recommendation` (
  `user_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  `score` INT NOT NULL DEFAULT 0,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`user_id`, `tag_id`)
);

CREATE TABLE `Message` (
  `message_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `user_id_from` INT NOT NULL,
  `user_id_to` INT NOT NULL,
  `content` VARCHAR(1000) NOT NULL,
  `attach_to` INT DEFAULT 0,
  `sent_at` DATETIME NOT NULL,
  `read_at` DATETIME,
  `is_active` BOOLEAN NOT NULL DEFAULT 1
);

CREATE TABLE `Message_Image` (
  `message_id` INT NOT NULL,
  `image_id` INT NOT NULL,
  PRIMARY KEY (`message_id`, `image_id`)
);

CREATE TABLE `User_Profile` (
  `user_id` INT PRIMARY KEY NOT NULL,
  `username` VARCHAR(50) UNIQUE NOT NULL,
  `description` VARCHAR(300),
  `avatar` INT,
  `following_setting` VARCHAR(10) NOT NULL DEFAULT 'all',
  `is_posting_visible` BOOLEAN NOT NULL DEFAULT 1,
  `last_update` DATETIME NOT NULL
);

CREATE TABLE `Follow` (
  `user_id_from` INT NOT NULL,
  `user_id_to` INT NOT NULL,
  PRIMARY KEY (`user_id_from`, `user_id_to`)
);

CREATE TABLE `Block` (
  `user_id_from` INT NOT NULL,
  `user_id_to` INT NOT NULL,
  PRIMARY KEY (`user_id_from`, `user_id_to`)
);

-- Insert default roles
INSERT INTO `Role` (`role_id`, `role_name`) VALUES (1, 'USER'), (2, 'ADMIN');

-- Insert default tags
INSERT INTO `Tag_Data` (`tag_id`, `tag_name`, `keyword`) VALUES
(1, 'General', 'general,discussion,misc'),
(2, 'Technology', 'tech,programming,computers,gadgets'),
(3, 'Science', 'science,research,discovery'),
(4, 'Art', 'art,design,photography,drawing'),
(5, 'Music', 'music,songs,artists,bands'),
(6, 'Sports', 'sports,football,basketball,esports'),
(7, 'Gaming', 'gaming,video games,pc,console'),
(8, 'News', 'news,current events,world'),
(9, 'Movies', 'movies,films,cinema'),
(10, 'Books', 'books,reading,literature'),
(11, 'Food', 'food,cooking,recipes,restaurants'),
(12, 'Travel', 'travel,vacation,tourism,adventure'),
(13, 'Fashion', 'fashion,style,clothing,trends'),
(14, 'Health', 'health,fitness,wellness,medicine'),
(15, 'Finance', 'finance,investing,money,business');

-- Add Foreign Keys
ALTER TABLE `User_Account` ADD FOREIGN KEY (`role_id`) REFERENCES `Role` (`role_id`);
ALTER TABLE `Post` ADD FOREIGN KEY (`user_id`) REFERENCES `User_Account` (`user_id`);
ALTER TABLE `Like` ADD FOREIGN KEY (`post_id`) REFERENCES `Post` (`post_id`);
ALTER TABLE `Like` ADD FOREIGN KEY (`user_id`) REFERENCES `User_Account` (`user_id`);
ALTER TABLE `Dislike` ADD FOREIGN KEY (`post_id`) REFERENCES `Post` (`post_id`);
ALTER TABLE `Dislike` ADD FOREIGN KEY (`user_id`) REFERENCES `User_Account` (`user_id`);
ALTER TABLE `View` ADD FOREIGN KEY (`post_id`) REFERENCES `Post` (`post_id`);
ALTER TABLE `View` ADD FOREIGN KEY (`user_id`) REFERENCES `User_Account` (`user_id`);
ALTER TABLE `Post_Tag` ADD FOREIGN KEY (`post_id`) REFERENCES `Post` (`post_id`);
ALTER TABLE `Post_Tag` ADD FOREIGN KEY (`tag_id`) REFERENCES `Tag_Data` (`tag_id`);
ALTER TABLE `Post_Image` ADD FOREIGN KEY (`post_id`) REFERENCES `Post` (`post_id`);
ALTER TABLE `Post_Image` ADD FOREIGN KEY (`image_id`) REFERENCES `Image_Data` (`image_id`);
ALTER TABLE `Recommendation` ADD FOREIGN KEY (`user_id`) REFERENCES `User_Account` (`user_id`);
ALTER TABLE `Recommendation` ADD FOREIGN KEY (`tag_id`) REFERENCES `Tag_Data` (`tag_id`);
ALTER TABLE `Message` ADD FOREIGN KEY (`user_id_from`) REFERENCES `User_Account` (`user_id`);
ALTER TABLE `Message` ADD FOREIGN KEY (`user_id_to`) REFERENCES `User_Account` (`user_id`);
ALTER TABLE `Message_Image` ADD FOREIGN KEY (`message_id`) REFERENCES `Message` (`message_id`);
ALTER TABLE `Message_Image` ADD FOREIGN KEY (`image_id`) REFERENCES `Image_Data` (`image_id`);
ALTER TABLE `User_Profile` ADD FOREIGN KEY (`user_id`) REFERENCES `User_Account` (`user_id`);
ALTER TABLE `User_Profile` ADD FOREIGN KEY (`username`) REFERENCES `User_Account` (`username`);
ALTER TABLE `User_Profile` ADD FOREIGN KEY (`avatar`) REFERENCES `Image_Data` (`image_id`);
ALTER TABLE `Follow` ADD FOREIGN KEY (`user_id_from`) REFERENCES `User_Account` (`user_id`);
ALTER TABLE `Follow` ADD FOREIGN KEY (`user_id_to`) REFERENCES `User_Account` (`user_id`);
ALTER TABLE `Block` ADD FOREIGN KEY (`user_id_from`) REFERENCES `User_Account` (`user_id`);
ALTER TABLE `Block` ADD FOREIGN KEY (`user_id_to`) REFERENCES `User_Account` (`user_id`);
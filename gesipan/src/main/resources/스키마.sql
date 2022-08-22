-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema spring_cafe
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema spring_cafe
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `spring_cafe` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `spring_cafe` ;

-- -----------------------------------------------------
-- Table `spring_cafe`.`post_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spring_cafe`.`post_category` (
                                                             `post_category_id` BIGINT NOT NULL AUTO_INCREMENT,
                                                             `category_name` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`post_category_id`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 3
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `spring_cafe`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spring_cafe`.`users` (
                                                     `user_id` BIGINT NOT NULL AUTO_INCREMENT,
                                                     `user_email` VARCHAR(200) NOT NULL,
    `user_name` VARCHAR(10) NOT NULL,
    `user_nickname` VARCHAR(100) NOT NULL,
    `user_role` VARCHAR(45) NOT NULL DEFAULT 'normal',
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NOT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE INDEX `user_email_UNIQUE` (`user_email` ASC) VISIBLE)
    ENGINE = InnoDB
    AUTO_INCREMENT = 10006
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `spring_cafe`.`post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spring_cafe`.`post` (
                                                    `post_id` BIGINT NOT NULL AUTO_INCREMENT,
                                                    `post_subject` VARCHAR(1000) NOT NULL,
    `post_content` TEXT NULL DEFAULT NULL,
    `hit_count` BIGINT NOT NULL DEFAULT '0',
    `reference_category_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NOT NULL,
    PRIMARY KEY (`post_id`),
    INDEX `fk_post_post_category1_idx` (`reference_category_id` ASC) VISIBLE,
    INDEX `fk_post_users1_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_post_post_category1`
    FOREIGN KEY (`reference_category_id`)
    REFERENCES `spring_cafe`.`post_category` (`post_category_id`),
    CONSTRAINT `fk_post_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `spring_cafe`.`users` (`user_id`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 53055
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `spring_cafe`.`comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spring_cafe`.`comment` (
                                                       `comment_id` BIGINT NOT NULL AUTO_INCREMENT,
                                                       `reference_post_id` BIGINT NOT NULL,
                                                       `user_id` BIGINT NOT NULL,
                                                       `comment_content` VARCHAR(1000) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NOT NULL,
    PRIMARY KEY (`comment_id`),
    INDEX `fk_comment_post_idx` (`reference_post_id` ASC) VISIBLE,
    INDEX `fk_comment_users1_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_comment_post`
    FOREIGN KEY (`reference_post_id`)
    REFERENCES `spring_cafe`.`post` (`post_id`)
    ON DELETE CASCADE,
    CONSTRAINT `fk_comment_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `spring_cafe`.`users` (`user_id`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 103572
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `spring_cafe`.`post_recommend`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spring_cafe`.`post_recommend` (
                                                              `post_recommend_id` BIGINT NOT NULL AUTO_INCREMENT,
                                                              `created_at` DATETIME NOT NULL,
                                                              `user_id` BIGINT NOT NULL,
                                                              `post_id` BIGINT NOT NULL,
                                                              PRIMARY KEY (`post_recommend_id`),
    INDEX `fk_post_recommend_users1_idx` (`user_id` ASC) VISIBLE,
    INDEX `fk_post_recommend_post1_idx` (`post_id` ASC) VISIBLE,
    CONSTRAINT `fk_post_recommend_post1`
    FOREIGN KEY (`post_id`)
    REFERENCES `spring_cafe`.`post` (`post_id`),
    CONSTRAINT `fk_post_recommend_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `spring_cafe`.`users` (`user_id`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 3710
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `spring_cafe`.`temp_post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spring_cafe`.`temp_post` (
                                                         `temp_post_id` BIGINT NOT NULL AUTO_INCREMENT,
                                                         `reference_user_id` BIGINT NOT NULL,
                                                         `post_subject` VARCHAR(1000) NOT NULL,
    `post_content` TEXT NOT NULL,
    `created_at` DATETIME NOT NULL,
    PRIMARY KEY (`temp_post_id`),
    INDEX `fk_temp_post_users1_idx` (`reference_user_id` ASC) VISIBLE,
    CONSTRAINT `fk_temp_post_users1`
    FOREIGN KEY (`reference_user_id`)
    REFERENCES `spring_cafe`.`users` (`user_id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

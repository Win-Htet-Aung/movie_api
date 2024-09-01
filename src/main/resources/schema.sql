DROP TABLE IF EXISTS `movie_genre`;
DROP TABLE IF EXISTS `movie_cast`;
DROP TABLE IF EXISTS `movie_production`;
DROP TABLE IF EXISTS `series_cast`;
DROP TABLE IF EXISTS `series_production`;
DROP TABLE IF EXISTS `series_genre`;
DROP TABLE IF EXISTS `productions`;
DROP TABLE IF EXISTS `casts`;
DROP TABLE IF EXISTS `genres`;
DROP TABLE IF EXISTS `movie_review`;
DROP TABLE IF EXISTS `series_review`;
DROP TABLE IF EXISTS `season_review`;
DROP TABLE IF EXISTS `episode_review`;
DROP TABLE IF EXISTS `reviews`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `movies`;
DROP TABLE IF EXISTS `episodes`;
DROP TABLE IF EXISTS `seasons`;
DROP TABLE IF EXISTS `series`;

CREATE TABLE IF NOT EXISTS `movies` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `slug` VARCHAR(255) UNIQUE,
  `title` VARCHAR(255),
  `summary` TEXT NOT NULL,
  `release_year` INT NOT NULL,
  `duration` INT NOT NULL,
  `country` VARCHAR(100),
  `imdb_rating` DECIMAL(8,6),
  `user_rating` DECIMAL(8,6),
  `review_count` INT NOT NULL,
  `cover` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `series` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `slug` VARCHAR(255) UNIQUE,
  `title` VARCHAR(255),
  `summary` TEXT NOT NULL,
  `release_year` INT NOT NULL,
  `duration` INT NOT NULL,
  `country` VARCHAR(100),
  `imdb_rating` DECIMAL(8,6),
  `user_rating` DECIMAL(8,6),
  `review_count` INT NOT NULL,
  `cover` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `genres` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `casts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100),
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `productions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `movie_genre` (
  `movie_id` INT NOT NULL,
  `genre_id` INT NOT NULL,

  PRIMARY KEY (`movie_id`, `genre_id`),
  FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  FOREIGN KEY (`genre_id`) REFERENCES `genres` (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `movie_cast` (
  `movie_id` INT NOT NULL,
  `cast_id` INT NOT NULL,

  PRIMARY KEY (`movie_id`, `cast_id`),
  FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  FOREIGN KEY (`cast_id`) REFERENCES `casts` (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `movie_production` (
  `movie_id` INT NOT NULL,
  `production_id` INT NOT NULL,

  PRIMARY KEY (`movie_id`, `production_id`),
  FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  FOREIGN KEY (`production_id`) REFERENCES `productions` (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `series_cast` (
  `series_id` INT NOT NULL,
  `cast_id` INT NOT NULL,

  PRIMARY KEY (`series_id`, `cast_id`),
  FOREIGN KEY (`series_id`) REFERENCES `series` (`id`),
  FOREIGN KEY (`cast_id`) REFERENCES `casts` (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `series_genre` (
  `series_id` INT NOT NULL,
  `genre_id` INT NOT NULL,

  PRIMARY KEY (`series_id`, `genre_id`),
  FOREIGN KEY (`series_id`) REFERENCES `series` (`id`),
  FOREIGN KEY (`genre_id`) REFERENCES `genres` (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `series_production` (
  `series_id` INT NOT NULL,
  `production_id` INT NOT NULL,

  PRIMARY KEY (`series_id`, `production_id`),
  FOREIGN KEY (`series_id`) REFERENCES `series` (`id`),
  FOREIGN KEY (`production_id`) REFERENCES `productions` (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `seasons` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `series_id` INT NOT NULL,
  `season_number` INT NOT NULL,
  `summary` TEXT NOT NULL,
  `release_year` INT NOT NULL,
  `imdb_rating` DECIMAL(3,1),
  `user_rating` DECIMAL(8,6),
  `review_count` INT NOT NULL,

  PRIMARY KEY `id`(`id`),
  CONSTRAINT `U_SEASON` UNIQUE (`series_id`, `season_number`),
  FOREIGN KEY (`series_id`) REFERENCES `series` (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `episodes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `season_id` INT NOT NULL,
  `title` VARCHAR(255),
  `episode_number` INT NOT NULL,
  `air_date` DATE NOT NULL,
  `imdb_rating` DECIMAL(3,1),
  `user_rating` DECIMAL(8,6),
  `review_count` INT NOT NULL,

  PRIMARY KEY `id`(`id`),
  CONSTRAINT `U_EPISODE` UNIQUE (`season_id`, `episode_number`),
  FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50),
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) UNIQUE,
  `displayname` VARCHAR(255),
  `email` VARCHAR(255) UNIQUE,
  `password` VARCHAR(255),
  `role_id` INT NOT NULL,

  PRIMARY KEY `id`(`id`),
  FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `reviews` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `comment` TEXT,
  `rating` INT NOT NULL,

  PRIMARY KEY `id`(`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `movie_review` (
  `movie_id` INT NOT NULL,
  `review_id` INT NOT NULL,

  PRIMARY KEY (`movie_id`, `review_id`),
  FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  FOREIGN KEY (`review_id`) REFERENCES `reviews` (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `series_review` (
  `series_id` INT NOT NULL,
  `review_id` INT NOT NULL,

  PRIMARY KEY (`series_id`, `review_id`),
  FOREIGN KEY (`series_id`) REFERENCES `series` (`id`),
  FOREIGN KEY (`review_id`) REFERENCES `reviews` (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `season_review` (
  `season_id` INT NOT NULL,
  `review_id` INT NOT NULL,

  PRIMARY KEY (`season_id`, `review_id`),
  FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`),
  FOREIGN KEY (`review_id`) REFERENCES `reviews` (`id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `episode_review` (
  `episode_id` INT NOT NULL,
  `review_id` INT NOT NULL,

  PRIMARY KEY (`episode_id`, `review_id`),
  FOREIGN KEY (`episode_id`) REFERENCES `episodes` (`id`),
  FOREIGN KEY (`review_id`) REFERENCES `reviews` (`id`)
) ENGINE = InnoDB;

-- roles
INSERT INTO `roles` (`name`) VALUES
('admin'),
('user'),
('testrole')
;

-- users
INSERT INTO `users` (`username`, `displayname`, `email`, `password`, `role_id`) VALUES
('admin', 'Admin', 'admin@example.com', '{bcrypt}$2a$10$ot2t6kCvGvaqLqUaysfble3m2jiRwoElVH5BHH37foRqouhRAHHsu', 1),
('user', 'User', 'user@example.com', '{bcrypt}$2a$10$PM25pPgcMP40kwHVFsu.Quo4mTEUbSihA4ItljWKF3/WqrqNuXPAi', 2)
;

-- genres
INSERT INTO `genres` (`name`) VALUES
('Action'),
('Comedy'),
('Drama'),
('Crime')
;

-- casts
INSERT INTO `casts` (`name`) VALUES
('Tim Robbins'),
('Morgan Freeman'),
('Al Pacino'),
('Marlon Brando'),
('Tom Cruise'),
('Tom Hanks')
;

-- productions
INSERT INTO `productions` (`name`) VALUES
('Warner Bros. Pictures'),
('Paramount Pictures'),
('20th Century Fox'),
('Universal Pictures'),
('Columbia Pictures'),
('Walt Disney Pictures')
;

-- movies
INSERT INTO `movies` (`title`, `slug`, `summary`, `release_year`, `duration`, `country`, `imdb_rating`, `user_rating`, `review_count`, `cover`) VALUES
('The Godfather', 'godfather', 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.', 1972, 175, 'USA', 9.2, 8.5, 2, 'godfather.jpg'),
('The Shawshank Redemption', 'the-shawshank-redemption', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.', 1994, 142, 'USA', 9.3, 7.0, 1, 'shawshank.jpg'),
('The Big Lebowski', 'the-big-lebowski', 'The Dude finds himself in the middle of a crooked police investigation.', 1998, 117, 'USA', 8.2, 0.0, 0, 'big_lebowski.jpg'),
('Big Hero 6', 'big-hero-6', 'The special bond that develops between plus-sized inflatable robot Baymax, and prodigy Hiro Hamada, who team up with mad scientists.', 2014, 102, 'Japan', 7.8, 0.0, 0, 'big_hero.jpg')
;

-- movie_genre
INSERT INTO `movie_genre` (`movie_id`, `genre_id`) VALUES
(3, 1),
(3, 2)
;

-- movie_cast
INSERT INTO `movie_cast` (`movie_id`, `cast_id`) VALUES
(3, 1),
(3, 2)
;

-- movie_production
INSERT INTO `movie_production` (`movie_id`, `production_id`) VALUES
(3, 1),
(3, 2)
;

-- series
INSERT INTO `series` (`title`, `slug`, `summary`, `release_year`, `duration`, `country`, `imdb_rating`, `user_rating`, `review_count`, `cover`) VALUES
('The Big Bang Theory', 'the-big-bang-theory', 'A woman and her family are stuck in the time paradox.', 2007, 22, 'USA', 9.5, 8.5, 2, 'big_bang_theory.jpg'),
('Game of Thrones', 'game-of-thrones', 'Nine noble families fight for control over the mythical lands of Westeros.', 2011, 57, 'USA', 9.3, 10.0, 1, 'game_of_thrones.jpg'),
('House of Cards', 'house-of-cards', 'An antisocial maverick doctor who specializes in cardiology.', 2013, 45, 'USA', 9.1, 0.0, 0, 'house_of_cards.jpg')
;

-- series_genre
INSERT INTO `series_genre` (`series_id`, `genre_id`) VALUES
(3, 1),
(3, 2)
;

-- series_cast
INSERT INTO `series_cast` (`series_id`, `cast_id`) VALUES
(3, 1),
(3, 2)
;

-- series_production
INSERT INTO `series_production` (`series_id`, `production_id`) VALUES
(3, 1),
(3, 2)
;

-- seasons
INSERT INTO `seasons` (`series_id`, `season_number`, `summary`, `release_year`, `imdb_rating`, `user_rating`, `review_count`) VALUES
(3, 1, 'The series starts with the first season of House of Cards.', 2013, 9.1, 9.5, 2),
(3, 2, 'The series starts with the second season of House of Cards.', 2014, 9.0, 8.0, 1)
;

-- episodes
INSERT INTO `episodes` (`season_id`, `episode_number`, `title`, `air_date`, `imdb_rating`, `user_rating`, `review_count`) VALUES
(2, 1, 'Episode 1 of House of Cards Season 2.', '2014-11-23', 9.0, 9.5, 2),
(2, 2, 'Episode 2 of House of Cards Season 2.', '2014-11-30', 8.9, 8.0, 1)
;

-- reviews
INSERT INTO `reviews` (`comment`, `rating`, `user_id`) VALUES
('This film is a masterful blend of captivating storytelling.', 9, 1),
('This film is a stunning visuals, and outstanding performances.', 8, 2),
('This film is an interesting adaptation of the novel.', 7, 2),
('Very very good!', 10, 1),
('Very good!', 9, 2),
('Good!', 8, 1)
;

-- movie_reviews
INSERT INTO `movie_review` (`movie_id`, `review_id`) VALUES
(1, 1),
(1, 2),
(2, 3)
;

-- series_reviews
INSERT INTO `series_review` (`series_id`, `review_id`) VALUES
(1, 6),
(1, 5),
(2, 4)
;

-- season_reviews
INSERT INTO `season_review` (`season_id`, `review_id`) VALUES
(1, 4),
(1, 5),
(2, 6)
;

-- episode_reviews
INSERT INTO `episode_review` (`episode_id`, `review_id`) VALUES
(1, 4),
(1, 5),
(2, 6)
;

-- roles
INSERT INTO `roles` (`name`) VALUES
('admin'),
('user')
;

-- users
INSERT INTO `users` (`username`, `displayname`, `email`, `password`, `role_id`) VALUES
('admin', 'Admin', 'admin@example.com', 'admin123', 1),
('user', 'User', 'user@example.com', 'user123', 2)
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
INSERT INTO `movies` (`title`, `summary`, `release_year`, `duration`, `country`, `imdb_rating`, `cover`) VALUES
('The Godfather', 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.', 1972, 175, 'USA', 9.2, 'godfather.jpg'),
('The Shawshank Redemption', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.', 1994, 142, 'USA', 9.3, 'shawshank.jpg'),
('The Big Lebowski', 'The Dude finds himself in the middle of a crooked police investigation.', 1998, 117, 'USA', 8.2, 'big_lebowski.jpg')
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
INSERT INTO `series` (`title`, `summary`, `release_year`, `duration`, `country`, `imdb_rating`, `cover`) VALUES
('The Big Bang Theory', 'A woman and her family are stuck in the time paradox.', 2007, 22, 'USA', 9.5, 'big_bang_theory.jpg'),
('Game of Thrones', 'Nine noble families fight for control over the mythical lands of Westeros.', 2011, 57, 'USA', 9.3, 'game_of_thrones.jpg'),
('House of Cards', 'An antisocial maverick doctor who specializes in cardiology.', 2013, 45, 'USA', 9.1, 'house_of_cards.jpg')
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
INSERT INTO `seasons` (`series_id`, `season_number`, `summary`, `release_year`, `imdb_rating`) VALUES
(3, 1, 'The series starts with the first season of House of Cards.', 2013, 9.1),
(3, 2, 'The series starts with the second season of House of Cards.', 2014, 9.0)
;

-- episodes
INSERT INTO `episodes` (`season_id`, `episode_number`, `title`, `air_date`, `imdb_rating`) VALUES
(2, 1, 'Episode 1 of House of Cards Season 2.', '2014-11-23', 9.0),
(2, 2, 'Episode 2 of House of Cards Season 2.', '2014-11-30', 8.9)
;

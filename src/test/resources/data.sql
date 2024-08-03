-- roles
INSERT INTO `roles` (`name`) VALUES
('admin'),
('user')
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

-- movie_genres
INSERT INTO `movie_genre` (`movie_id`, `genre_id`) VALUES
(3, 1),
(3, 2)
;

-- movie_casts
INSERT INTO `movie_cast` (`movie_id`, `cast_id`) VALUES
(3, 1),
(3, 2)
;

-- movie_productions
INSERT INTO `movie_production` (`movie_id`, `production_id`) VALUES
(3, 1),
(3, 2)
;
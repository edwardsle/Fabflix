DROP PROCEDURE IF EXISTS add_movie;
DELIMITER //
CREATE PROCEDURE add_movie (IN movie_title varchar(100), IN movie_year int(11), IN movie_director varchar(100), IN star_name varchar(100), IN genre_name varchar(32))
BEGIN
	DECLARE star_id varchar(10);
    DECLARE genre_id int(11);
    DECLARE movie_id varchar(10);
                             
	IF NOT EXISTS (SELECT id from movies WHERE title = movie_title AND year = movie_year AND director = movie_director)
    THEN
    	BEGIN
            DECLARE max_movie_id varchar(10);
            DECLARE prefix varchar(10);
            DECLARE num_chars varchar(8);
            DECLARE num int(11);
            
            SELECT max(movies.id) INTO max_movie_id from movies;
            SET prefix = SUBSTRING(max_movie_id,1,2);
            SET num_chars = SUBSTRING(max_movie_id,3,8);
            SET num = CONVERT(num_chars, UNSIGNED INTEGER) + 1;
            SET movie_id = CONCAT(prefix, CONVERT(num, CHAR));
            
            INSERT INTO movies (id, title, year, director) VALUES (movie_id, movie_title, movie_year, movie_director);
        	
            IF NOT EXISTS (SELECT id from stars WHERE name = star_name)
            THEN
                BEGIN
                    DECLARE max_star_id varchar(10);
                    DECLARE prefix varchar(10);
                    DECLARE num_chars varchar(8);
                    DECLARE num int(11);
                    
                    SELECT max(stars.id) INTO max_star_id from stars;
                    SET prefix = SUBSTRING(max_star_id,1,2);
                    SET num_chars = SUBSTRING(max_star_id,3,8);
                    SET num = CONVERT(num_chars, UNSIGNED INTEGER) + 1;
                    SET star_id = CONCAT(prefix, CONVERT(num, CHAR));
                    
                    INSERT INTO stars (id, name, birthYear) VALUES (star_id, star_name, NULL);
                END;
            ELSE
                SELECT id INTO star_id from stars WHERE name = star_name ORDER BY id DESC LIMIT 1;
            END IF;
            
            IF NOT EXISTS (SELECT id from genres WHERE name = genre_name)
            THEN
                BEGIN
                    DECLARE max_genre_id int(11);
                    
                    SELECT max(genres.id) INTO max_genre_id from genres;
                    SET genre_id = max_genre_id + 1;
                    
                    INSERT INTO genres (id, name) VALUES (genre_id, genre_name);
                END;
            ELSE
                SELECT id INTO genre_id from genres WHERE name = genre_name ORDER BY id DESC LIMIT 1;
            END IF;
            INSERT INTO stars_in_movies (starId, movieId) VALUES (star_id, movie_id);
            INSERT INTO genres_in_movies (genreId, movieId) VALUES (genre_id, movie_id);   
        END;
    END IF;

END;
//                            
DELIMITER ;
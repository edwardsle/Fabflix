package fabflix.android;

import java.util.ArrayList;

public class Movie {
    private String id;
    private String title;
    private int year;
    private String director;
    private ArrayList<Star> stars;
    private ArrayList<Genre> genres;
    private String star;
    private String genre;
    
    public Movie(String id, String title, int year, String director, ArrayList<Star> stars, ArrayList<Genre> genres) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.stars = stars;
        this.genres = genres;
    }

    public Movie(String id, String title) {
        super();
        this.id = id;
        this.title = title;
    }

    public Movie(String id, String title, int year, String director, String star, String genre) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.star = star;
        this.genre = genre;
    }

    public ArrayList<Star> getStars() {
        return stars;
    }

    public void setStars(ArrayList<Star> stars) {
        this.stars = stars;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public String getStar() {
        return star;
    }

    public String getGenre() {
        return genre;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
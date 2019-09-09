package XMLParsing;

public class MovieWithGenre {
	//Movie
	private String id;
	private String title;
	private int year;
	private String director;
	private String genre;
	private String star;
	
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}

	//GenreInMovie
	private int genreId;
	private String movieId;
	public MovieWithGenre() {
		// TODO Auto-generated constructor stub
	}
	public MovieWithGenre(String id, String title, int year, String director, String genre) {
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
	}
	public MovieWithGenre(String genre)
	{
		this.genre = genre;
	}
	public MovieWithGenre(int genreId, String movieId)
	{
		this.genreId = genreId;
		this.movieId = movieId;
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
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public int getGenreId() {
		return genreId;
	}
	public void setGenreId(int genreId) {
		this.genreId = genreId;
	}
	public String getMovieId() {
		return movieId;
	}
	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}
	
//	For Testing Parsing
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Movie Details - ");
		sb.append("Director Name: " + getDirector());
		sb.append(" | ");
		sb.append("Title: " + getTitle());
		sb.append(" | ");
		sb.append("star: " + getStar());
		sb.append(" | ");
		sb.append("Year: " + getYear());
		sb.append(" | ");
		sb.append("Genre: " + getGenre());
		
		
		return sb.toString();
	}
}

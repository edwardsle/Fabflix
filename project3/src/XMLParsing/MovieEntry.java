package XMLParsing;

public class MovieEntry {

	private String id;
	private String title;
	private String year;
	private String director;
	private String starName;
	private String genreName;
	
	public MovieEntry(String id, String title, String year, String director) {
		super();
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
	}
	
	public MovieEntry(String id, String title, String year, String director, String starName, String genreName) {
		super();
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
		this.starName = starName;
		this.genreName = genreName;
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
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getStarName() {
		return starName;
	}
	public void setStarName(String starName) {
		this.starName = starName;
	}
	public String getGenreName() {
		return genreName;
	}
	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}
	
	
	
}

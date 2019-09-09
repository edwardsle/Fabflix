package Models;

import java.sql.Date;
public class Sale {
	private int id;
	private int customerId;
	private String movieId;
	private String movieName;
	private Date saleDate;
	public Sale() {
		// TODO Auto-generated constructor stub
	}
	public Sale(int id, int customerId, String movieId, Date saleDate) {
		this.id = id;
		this.customerId = customerId;
		this.movieId = movieId;
		this.saleDate = saleDate;
	}
	
	
	public Sale(int id, String movieId, String movieName) {
		super();
		this.id = id;
		this.movieId = movieId;
		this.movieName = movieName;
	}
	public Sale(int id, int customerId, String movieId) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.movieId = movieId;
		this.saleDate = null;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getMovieId() {
		return movieId;
	}
	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}
	public Date getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	
}

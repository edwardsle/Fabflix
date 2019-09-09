package Models;

public class CartItem {
	protected String id;
	protected Movie movie;
	protected int quantity;
	
	
	public CartItem(String id, int quantity) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.movie = null;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Movie getMovie() {
		return movie;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}

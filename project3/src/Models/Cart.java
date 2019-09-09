package Models;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
public class Cart {

	private ArrayList<CartItem> items;
	

	public Cart() {
		super();
		items = new ArrayList<CartItem>();
	}


	public Cart(ArrayList<CartItem> items) {
		super();
		this.items = items;
	}
	
	private CartItem find(String id) {
		if (items.isEmpty())
			return null;
		for (CartItem item : items)
		{
			if (item.id.equals(id))
				return item;
		}
		return null;
	}
	
	public ArrayList<CartItem> getItems() {
		return items;
	}

	public String toJsonString() {
		Gson gson = new Gson();
		return gson.toJson(items);
	}
	
	public void add(String movieId)
	{
		CartItem item = find(movieId);
		if (item != null)
		{
			++item.quantity;
		}
		else
		{
			items.add(new CartItem(movieId, 1));
		}
		
	}
	
	public void remove(String movieId)
	{
		CartItem item = find(movieId);
		if (item != null)
		{
			items.remove(item);
		}
	}
	
	public void clear()
	{
		items.clear();
	}
	
	public void increase(String movieId)
	{
		CartItem item = find(movieId);
		if (item != null)
		{
			++item.quantity;
		}
	}
	
	public void decrease(String movieId)
	{
		CartItem item = find(movieId);
		if (item != null && item.quantity > 1)
			--item.quantity;
	}
}

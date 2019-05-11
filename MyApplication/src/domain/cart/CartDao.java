package domain.cart;

import java.util.List;

import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public interface CartDao {
	
	/*
	 *  Retrieves cart items for a customer
	 */
	public List<Cart> fetchCartItems(int customerId);
	
	/*
	 *  Add item to cart
	 */
	public List<Cart> addItemToCart(JsonObject subscriptionDetails) throws MySQLIntegrityConstraintViolationException;
	
	/*
	 *  Update item in cart
	 */
	public List<Cart> updateCartItem(JsonObject subscriptionDetails);
	
	/*
	 *  Delete item from cart
	 */
	public List<Cart> deleteCartItem(JsonObject subscriptionDetails);
	
}

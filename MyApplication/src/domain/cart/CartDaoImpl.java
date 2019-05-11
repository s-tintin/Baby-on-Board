package domain.cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import db.DbManager;

public class CartDaoImpl implements CartDao{

	static Connection conn;
	static PreparedStatement ps;
	DbManager db = new DbManager();
	
	@Override
	public List<Cart> fetchCartItems(int customerId) {
		
		List<Cart> cartItems = new ArrayList<Cart>();
		
		try {
			conn = db.getConnection();
			ps = conn.prepareStatement("SELECT c.id, c.subscription_id, c.customer_id, c.price, c.quantity, s.name, ag.id, ag.name FROM cart c JOIN subscription s ON c.subscription_id = s.id JOIN age_group ag ON s.age_group = ag.id WHERE c.customer_id=?");
			ps.setInt(1, customerId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Cart cartItem = new Cart();
				cartItem.setId(rs.getInt(1));
				cartItem.setSubscriptionId(rs.getInt(2));
				cartItem.setCustomerId(rs.getInt(3));
				cartItem.setPrice(rs.getDouble(4));
				cartItem.setQuantity(rs.getInt(5));
				cartItem.setSubscriptionName(rs.getString(6));
				cartItem.setAgeGroupId(rs.getInt(7));
				cartItem.setAgeGroupName(rs.getString(8));
				cartItems.add(cartItem);
			}
			
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
		return cartItems;
	}

	@Override
	public List<Cart> addItemToCart(JsonObject subscriptionDetails) throws MySQLIntegrityConstraintViolationException {
		
		int status = 0;
		
		try {
			conn = db.getConnection();
			ps = conn.prepareStatement("INSERT INTO `cart`(`subscription_id`,`customer_id`,`price`,`quantity`) VALUES (?,?,?,?)");
			ps.setInt(1, subscriptionDetails.get("sub_id").getAsInt());
			ps.setInt(2, subscriptionDetails.get("cust_id").getAsInt());
			ps.setFloat(3, subscriptionDetails.get("price").getAsFloat());
			ps.setInt(4, subscriptionDetails.get("quantity").getAsInt());
			status = ps.executeUpdate();
			
			conn.close();
		}
		
		catch(MySQLIntegrityConstraintViolationException e){
			throw e;
		}
		
		catch(Exception e){
			System.out.println(e);
		}
		
		return status == 1 ? this.fetchCartItems(subscriptionDetails.get("cust_id").getAsInt()) : null;
	}

	@Override
	public List<Cart> updateCartItem(JsonObject subscriptionDetails) {
		
		int status = 0;
		try {
			conn = db.getConnection();
			ps = conn.prepareStatement("UPDATE cart SET price = ?, quantity = ? WHERE customer_id = ? AND subscription_id = ?");
			ps.setFloat(1, subscriptionDetails.get("price").getAsFloat());
			ps.setInt(2, subscriptionDetails.get("quantity").getAsInt());
			ps.setInt(3, subscriptionDetails.get("cust_id").getAsInt());
			ps.setInt(4, subscriptionDetails.get("sub_id").getAsInt());
			status = ps.executeUpdate();
			
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
		return status == 1 ? this.fetchCartItems(subscriptionDetails.get("cust_id").getAsInt()) : null;
	}

	@Override
	public List<Cart> deleteCartItem(JsonObject subscriptionDetails) {
		int status = 0;
		
		try {
			conn = db.getConnection();
			if(subscriptionDetails.has("sub_id") && subscriptionDetails.has("cust_id")){
				ps = conn.prepareStatement("DELETE FROM cart WHERE customer_id = ? AND subscription_id = ?");
				ps.setInt(1, subscriptionDetails.get("cust_id").getAsInt());
				ps.setInt(2, subscriptionDetails.get("sub_id").getAsInt());
			}
			else if(subscriptionDetails.has("cust_id")) {
				ps = conn.prepareStatement("DELETE FROM cart WHERE customer_id = ?");
				ps.setInt(1, subscriptionDetails.get("cust_id").getAsInt());
			}
			status = ps.executeUpdate();
			
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
		return status > 0 ? this.fetchCartItems(subscriptionDetails.get("cust_id").getAsInt()) : null;
	}
	
}

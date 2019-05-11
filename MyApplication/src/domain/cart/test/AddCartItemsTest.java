package domain.cart.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import domain.cart.Cart;
import domain.cart.CartDao;
import domain.cart.CartDaoImpl;

public class AddCartItemsTest {

	private static CartDao cartDao;
	private static Gson gson;
	
	@Before
	public void setUp() throws Exception {
		gson = new Gson();
		cartDao = new CartDaoImpl();
		cartDao.deleteCartItem(gson.fromJson("{\"sub_id\":\"9\",\"cust_id\":\"1\",\"price\":\"19\",\"quantity\":\"1\"}", JsonObject.class));
	}

	@Test
	public void addToCartSuccessTest() throws MySQLIntegrityConstraintViolationException{
		String cartData = "{\"sub_id\":\"9\",\"cust_id\":\"1\",\"price\":\"19\",\"quantity\":\"1\"}";
		JsonObject cartDetails = gson.fromJson(cartData, JsonObject.class);
		assertNotNull(cartDetails);
		List<Cart> cartItems = cartDao.addItemToCart(cartDetails);
		assertFalse(cartItems.size()==0);
		
	}
	
	@Test(expected = MySQLIntegrityConstraintViolationException.class)
	public void addToCartFailureTest() throws MySQLIntegrityConstraintViolationException{
		String cartData1 = "{\"sub_id\":\"9\",\"cust_id\":\"1\",\"price\":\"19\",\"quantity\":\"1\"}";
		JsonObject cartDetails1 = gson.fromJson(cartData1, JsonObject.class);
		assertNotNull(cartDetails1);
		List<Cart> cartItems = cartDao.addItemToCart(cartDetails1);

		String cartData2 = "{\"sub_id\":\"9\",\"cust_id\":\"1\",\"price\":\"19\",\"quantity\":\"1\"}";
		JsonObject cartDetails2 = gson.fromJson(cartData2, JsonObject.class);
		assertNotNull(cartDetails2);
		cartItems = cartDao.addItemToCart(cartDetails2);
		assertTrue(cartItems.size()>0);
	}
	
	@Test
	public void deleteFromCartSuccessTest() throws MySQLIntegrityConstraintViolationException {
		String cartData = "{\"sub_id\":\"9\",\"cust_id\":\"1\",\"price\":\"19\",\"quantity\":\"1\"}";
		JsonObject cartDetails = gson.fromJson(cartData, JsonObject.class);
		assertNotNull(cartDetails);
		List<Cart> cartItems = cartDao.addItemToCart(cartDetails);
		cartItems = cartDao.deleteCartItem(cartDetails);
		assertTrue(cartItems.size()== 0);
	}
	
	@Test
	public void deleteFromCartFailureTest() throws MySQLIntegrityConstraintViolationException {
		String cartData = "{\"sub_id\":\"9\",\"cust_id\":\"1\",\"price\":\"19\",\"quantity\":\"1\"}";
		JsonObject cartDetails = gson.fromJson(cartData, JsonObject.class);
		assertNotNull(cartDetails);
		List<Cart> cartItems = cartDao.addItemToCart(cartDetails);
		assertFalse(cartItems.size() == 0);
	}
	
	@After
	public void tearDown() throws Exception {
		gson = null;
		//cartDao.deleteCartItem(gson.fromJson("{\"sub_id\":\"9\",\"cust_id\":\"1\",\"price\":\"19\",\"quantity\":\"1\"}", JsonObject.class));
		cartDao=null;
	}

	

}

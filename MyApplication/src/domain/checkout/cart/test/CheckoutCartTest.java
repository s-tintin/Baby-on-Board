package domain.checkout.cart.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import domain.cart.Cart;
import domain.cart.CartDao;
import domain.cart.CartDaoImpl;
import domain.subscription.Subscription;
import domain.subscription.SubscriptionProductDao;
import domain.subscription.SubscriptionProductDaoImpl;
import domain.transaction.TransactionDao;
import domain.transaction.TransactionDaoImpl;

public class CheckoutCartTest {
	
	private static CartDao cartDao;
	private static SubscriptionProductDao subProdDao;
	private static TransactionDao transactionDao;
	private static Gson gson;
	
	@BeforeClass
	public static void setUp() {
		gson = new Gson();
		cartDao = new CartDaoImpl();
		subProdDao = new SubscriptionProductDaoImpl();
		transactionDao = new TransactionDaoImpl();
	}
	
	@Test
	public void fetchItemsFromCartSuccessTest() throws MySQLIntegrityConstraintViolationException {
		// Delete all cart items for customer
		String cartItemString = "{\"cust_id\":1}";
		JsonObject cartDetails = gson.fromJson(cartItemString, JsonObject.class);
		List<Cart> cartItems = cartDao.deleteCartItem(cartDetails);
		
		// Add Item to cart
		cartItemString = "{\"cust_id\":1,\"sub_id\":\"4\",\"quantity\":1,\"price\":86}";
		cartDetails = gson.fromJson(cartItemString, JsonObject.class);
		cartItems = cartDao.addItemToCart(cartDetails);
		assertNotNull(cartItems);
		assertTrue(cartItems.size() == 1);
		
		// Add Item to cart
		cartItemString = "{\"cust_id\":1,\"sub_id\":\"9\",\"quantity\":1,\"price\":19}";
		cartDetails = gson.fromJson(cartItemString, JsonObject.class);
		cartItems = cartDao.addItemToCart(cartDetails);
		assertNotNull(cartItems);
		assertTrue(cartItems.size() == 2);
		
		// Get cart items
		cartItems = cartDao.fetchCartItems(1);
		assertNotNull(cartItems);
		assertTrue(cartItems.size() == 2);
		
		// Delete all cart items
		cartItemString = "{\"cust_id\":1}";
		cartDetails = gson.fromJson(cartItemString, JsonObject.class);
		cartItems = cartDao.deleteCartItem(cartDetails);
		assertNotNull(cartItems);
		assertTrue(cartItems.size() == 0);
	}
	
	@Test
	public void fetchItemsFromCartWithoutCustomerIdTest() {
		// Get cart items
		List<Cart> cartItems = cartDao.fetchCartItems(999);
		assertNotNull(cartItems);
		assertTrue(cartItems.size() == 0);
	}
	
	@Test
	public void getSubscriptionsSuccessTest() {
		String subscriptionIds = "2,6";
		List<Subscription> subscriptionDetails = subProdDao.getSubscriptionInfoById(subscriptionIds);
		assertNotNull(subscriptionDetails);
		assertTrue(subscriptionDetails.size() == 2);
		
		Subscription sub = subscriptionDetails.get(0);
		assertNotNull(sub);
		assertEquals(sub.getName(), "Premium");
		assertEquals(sub.getAgeGroup().getId(), 2);
		assertNotNull(sub.getProducts());
		assertEquals(sub.getProducts().size(), 2);
		
		sub = subscriptionDetails.get(1);
		assertNotNull(sub);
		assertEquals(sub.getName(), "Standard");
		assertEquals(sub.getAgeGroup().getId(), 3);
		assertNotNull(sub.getProducts());
		assertEquals(sub.getProducts().size(), 1);
	}
	
	@Test
	public void getSubscriptionsEmptyTest() {
		String subscriptionIds = "55,75";
		List<Subscription> subscriptionDetails = subProdDao.getSubscriptionInfoById(subscriptionIds);
		assertNotNull(subscriptionDetails);
		assertTrue(subscriptionDetails.size() == 0);
	}
	
	@Test
	public void saveCustomerSubscriptionsSuccessTest() throws MySQLIntegrityConstraintViolationException {
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/27/2018\",\"address\":\"3488 Alp Rd Apt 66, Hyd, India, 600192.\",\"name_on_card\":\"Greg Jones\",\"card_no\":\"1290-6745-8989-0010\",\"exp_month\":\"11\",\"exp_year\":\"2027\",\"card_cvv\":\"778\",\"amount\":191,\"subscribed_items\":[{\"customer_id\":1,\"subscription_id\":4,\"frequency\":\"bi-weekly\",\"duration\":12,\"quantity\":2,\"start_date\":\"11/27/2018\",\"total\":86},{\"customer_id\":1,\"subscription_id\":9,\"frequency\":\"weekly\",\"duration\":6,\"quantity\":1,\"start_date\":\"11/27/2018\",\"total\":19}]}";
		JsonObject transactionDetails = gson.fromJson(transactionData, JsonObject.class);
		assertNotNull(transactionDetails);
		assertTrue(transactionDetails.has("subscribed_items"));
		assertTrue(transactionDetails.get("subscribed_items").isJsonArray());
		
		JsonArray customerSubDetails = transactionDetails.getAsJsonArray("subscribed_items");
		ArrayList<Integer> custSubscriptionIds = subProdDao.saveCustomerSubscriptions(customerSubDetails);
		assertNotNull(custSubscriptionIds);
		assertTrue(custSubscriptionIds.size() == customerSubDetails.size());
	}
	
	@Test(expected = MySQLIntegrityConstraintViolationException.class)
	public void saveCustomerSubscriptionsWithSubIdNotPresentTest() throws MySQLIntegrityConstraintViolationException{
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/27/2018\",\"address\":\"3488 Alp Rd Apt 66, Hyd, India, 600192.\",\"name_on_card\":\"Greg Jones\",\"card_no\":\"1290-6745-8989-0010\",\"exp_month\":\"11\",\"exp_year\":\"2027\",\"card_cvv\":\"778\",\"amount\":191,\"subscribed_items\":[{\"customer_id\":1,\"subscription_id\":41,\"frequency\":\"bi-weekly\",\"duration\":12,\"quantity\":2,\"start_date\":\"11/27/2018\",\"total\":86},{\"customer_id\":1,\"subscription_id\":90,\"frequency\":\"weekly\",\"duration\":6,\"quantity\":1,\"start_date\":\"11/27/2018\",\"total\":19}]}";
		JsonObject transactionDetails = gson.fromJson(transactionData, JsonObject.class);
		assertTrue(transactionDetails.get("subscribed_items").isJsonArray());
		
		JsonArray customerSubDetails = transactionDetails.getAsJsonArray("subscribed_items");
		ArrayList<Integer> custSubscriptionIds = subProdDao.saveCustomerSubscriptions(customerSubDetails);
		assertNull(custSubscriptionIds);
	}
	
	@Test(expected = MySQLIntegrityConstraintViolationException.class)
	public void saveCustomerSubscriptionsWithCustomerIdsNotPresentTest() throws MySQLIntegrityConstraintViolationException{
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/27/2018\",\"address\":\"3488 Alp Rd Apt 66, Hyd, India, 600192.\",\"name_on_card\":\"Greg Jones\",\"card_no\":\"1290-6745-8989-0010\",\"exp_month\":\"11\",\"exp_year\":\"2027\",\"card_cvv\":\"778\",\"amount\":191,\"subscribed_items\":[{\"customer_id\":999,\"subscription_id\":4,\"frequency\":\"bi-weekly\",\"duration\":12,\"quantity\":2,\"start_date\":\"11/27/2018\",\"total\":86},{\"customer_id\":1000,\"subscription_id\":9,\"frequency\":\"weekly\",\"duration\":6,\"quantity\":1,\"start_date\":\"11/27/2018\",\"total\":19}]}";
		JsonObject transactionDetails = gson.fromJson(transactionData, JsonObject.class);
		assertTrue(transactionDetails.get("subscribed_items").isJsonArray());
		
		JsonArray customerSubDetails = transactionDetails.getAsJsonArray("subscribed_items");
		ArrayList<Integer> custSubscriptionIds = subProdDao.saveCustomerSubscriptions(customerSubDetails);
		assertNull(custSubscriptionIds);
	}
	
	@Test
	public void saveCustomerSubscriptionsWithEmptySubsTest() throws MySQLIntegrityConstraintViolationException {
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/27/2018\",\"address\":\"3488 Alp Rd Apt 66, Hyd, India, 600192.\",\"name_on_card\":\"Greg Jones\",\"card_no\":\"1290-6745-8989-0010\",\"exp_month\":\"11\",\"exp_year\":\"2027\",\"card_cvv\":\"778\",\"amount\":191,\"subscribed_items\":[]}";
		JsonObject transactionDetails = gson.fromJson(transactionData, JsonObject.class);
		assertNotNull(transactionDetails);
		assertTrue(transactionDetails.has("subscribed_items"));
		assertTrue(transactionDetails.get("subscribed_items").isJsonArray());
		
		JsonArray customerSubDetails = transactionDetails.getAsJsonArray("subscribed_items");
		ArrayList<Integer> custSubscriptionIds = subProdDao.saveCustomerSubscriptions(customerSubDetails);
		assertNotNull(custSubscriptionIds);
		assertTrue(custSubscriptionIds.size() == customerSubDetails.size());
	}
	
	@Test
	public void createTransactionWithCardSuccessTest() {
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/27/2018\",\"address\":\"3488 Alp Rd Apt 66, Hyd, India, 600192.\",\"name_on_card\":\"Greg Jones\",\"card_no\":\"1290-6745-8989-0010\",\"exp_month\":\"11\",\"exp_year\":\"2027\",\"card_cvv\":\"778\",\"amount\":191,\"subscribed_items\":[{\"customer_id\":1,\"subscription_id\":4,\"frequency\":\"bi-weekly\",\"duration\":12,\"quantity\":2,\"start_date\":\"11/27/2018\",\"total\":86},{\"customer_id\":1,\"subscription_id\":9,\"frequency\":\"weekly\",\"duration\":6,\"quantity\":1,\"start_date\":\"11/27/2018\",\"total\":19}]}";
		JsonObject transactionDetails = gson.fromJson(transactionData, JsonObject.class);
		assertNotNull(transactionDetails);
		
		int transactionId = transactionDao.createTransaction(transactionDetails);
		assertTrue(transactionId > 0);
	}
	
	@Test
	public void createTransactionWithCashSuccessTest() {
		String transactionData = "{\"payment_mode\":\"cash\",\"date\":\"11/27/2018\",\"address\":\"3488 Alp Rd Apt 66, Hyd, India, 600192.\",\"amount\":191,\"subscribed_items\":[{\"customer_id\":1,\"subscription_id\":4,\"frequency\":\"bi-weekly\",\"duration\":12,\"quantity\":2,\"start_date\":\"11/27/2018\",\"total\":86},{\"customer_id\":1,\"subscription_id\":9,\"frequency\":\"weekly\",\"duration\":6,\"quantity\":1,\"start_date\":\"11/27/2018\",\"total\":19}]}";
		JsonObject transactionDetails = gson.fromJson(transactionData, JsonObject.class);
		assertNotNull(transactionDetails);
		
		int transactionId = transactionDao.createTransaction(transactionDetails);
		assertTrue(transactionId > 0);
	}
	
	@Test
	public void saveCustomerSubsciptionsTransactionsSuccessTest() throws MySQLIntegrityConstraintViolationException {
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/27/2018\",\"address\":\"3488 Alp Rd Apt 66, Hyd, India, 600192.\",\"name_on_card\":\"Greg Jones\",\"card_no\":\"1290-6745-8989-0010\",\"exp_month\":\"11\",\"exp_year\":\"2027\",\"card_cvv\":\"778\",\"amount\":191,\"subscribed_items\":[{\"customer_id\":1,\"subscription_id\":4,\"frequency\":\"bi-weekly\",\"duration\":12,\"quantity\":2,\"start_date\":\"11/27/2018\",\"total\":86},{\"customer_id\":1,\"subscription_id\":9,\"frequency\":\"weekly\",\"duration\":6,\"quantity\":1,\"start_date\":\"11/27/2018\",\"total\":19}]}";
		JsonObject transactionDetails = gson.fromJson(transactionData, JsonObject.class);
		assertNotNull(transactionDetails);
		
		JsonArray customerSubDetails = transactionDetails.getAsJsonArray("subscribed_items");
		assertNotNull(customerSubDetails);
		
		ArrayList<Integer> custSubscriptionIds = subProdDao.saveCustomerSubscriptions(customerSubDetails);
		assertNotNull(custSubscriptionIds);
		assertTrue(customerSubDetails.size() == custSubscriptionIds.size());
		
		int transactionId = transactionDao.createTransaction(transactionDetails);
		assertTrue(transactionId != -1);
		
		int subTransStatus = transactionDao.saveCustomerSubsciptionTransactions(transactionId, custSubscriptionIds);
		assertTrue(subTransStatus == 2);
	}
	
	@Test
	public void saveCustomerSubsciptionsTransactionsEmptyTest() throws MySQLIntegrityConstraintViolationException {
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/27/2018\",\"address\":\"3488 Alp Rd Apt 66, Hyd, India, 600192.\",\"name_on_card\":\"Greg Jones\",\"card_no\":\"1290-6745-8989-0010\",\"exp_month\":\"11\",\"exp_year\":\"2027\",\"card_cvv\":\"778\",\"amount\":191,\"subscribed_items\":[{\"customer_id\":1,\"subscription_id\":4,\"frequency\":\"bi-weekly\",\"duration\":12,\"quantity\":2,\"start_date\":\"11/27/2018\",\"total\":86},{\"customer_id\":1,\"subscription_id\":9,\"frequency\":\"weekly\",\"duration\":6,\"quantity\":1,\"start_date\":\"11/27/2018\",\"total\":19}]}";
		JsonObject transactionDetails = gson.fromJson(transactionData, JsonObject.class);
		assertNotNull(transactionDetails);
		
		ArrayList<Integer> custSubscriptionIds = new ArrayList<Integer>();
		assertNotNull(custSubscriptionIds);
		
		int transactionId = transactionDao.createTransaction(transactionDetails);
		assertTrue(transactionId != -1);
		
		int subTransStatus = transactionDao.saveCustomerSubsciptionTransactions(transactionId, custSubscriptionIds);
		assertTrue(subTransStatus == 0);
	}
	
	@AfterClass
	public static void tearDown() {
		cartDao = null;
		transactionDao = null;
		subProdDao = null;
		gson = null;
	}
}

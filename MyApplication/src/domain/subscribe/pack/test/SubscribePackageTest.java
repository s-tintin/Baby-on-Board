package domain.subscribe.pack.test;

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

import domain.subscription.Subscription;
import domain.subscription.SubscriptionProductDao;
import domain.subscription.SubscriptionProductDaoImpl;
import domain.transaction.TransactionDao;
import domain.transaction.TransactionDaoImpl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class SubscribePackageTest {
	
	private static SubscriptionProductDao subProdDao;
	private static TransactionDao transactionDao;
	private static Gson gson;
	
	@BeforeClass
	public static void setUp() {
		gson = new Gson();
		subProdDao = new SubscriptionProductDaoImpl();
		transactionDao = new TransactionDaoImpl();
	}
	
	@Test
	public void getSubscriptionSuccessTest() {
		String subscriptionIds = "2";
		List<Subscription> subscriptionDetails = subProdDao.getSubscriptionInfoById(subscriptionIds);
		assertNotNull(subscriptionDetails);
		assertTrue(subscriptionDetails.size() == 1);
		
		Subscription sub = subscriptionDetails.get(0);
		assertNotNull(sub);
		assertEquals(sub.getName(), "Premium");
		assertEquals(sub.getAgeGroup().getId(), 2);
		assertNotNull(sub.getProducts());
		assertEquals(sub.getProducts().size(), 2);
	}
	
	@Test
	public void getSubscriptionEmptyTest() {
		String subscriptionIds = "55";
		List<Subscription> subscriptionDetails = subProdDao.getSubscriptionInfoById(subscriptionIds);
		assertNotNull(subscriptionDetails);
		assertTrue(subscriptionDetails.size() == 0);
	}
	
	@Test
	public void saveCustomerSubscriptionsSuccessTest() throws MySQLIntegrityConstraintViolationException {
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/15/2018\",\"address\":\"777 Ape Dr, Dallas TX 75252\",\"name_on_card\":\"Greg Wash\",\"card_no\":\"3451-2221-7878-9056\",\"exp_month\":\"12\",\"exp_year\":\"2022\",\"card_cvv\":\"500\",\"amount\":220,\"subscribed_items\":[{\"customer_id\":1,\"subscription_id\":5,\"frequency\":\"bi-weekly\",\"duration\":9,\"quantity\":2,\"start_date\":\"11/15/2018\",\"total\":110}]}";
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
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/15/2018\",\"address\":\"777 Ape Dr, Dallas TX 75252\",\"name_on_card\":\"Greg Wash\",\"card_no\":\"3451-2221-7878-9056\",\"exp_month\":\"12\",\"exp_year\":\"2022\",\"card_cvv\":\"500\",\"amount\":220,\"subscribed_items\":[{\"customer_id\":1,\"subscription_id\":15,\"frequency\":\"bi-weekly\",\"duration\":9,\"quantity\":2,\"start_date\":\"11/15/2018\",\"total\":110}]}";
		JsonObject transactionDetails = gson.fromJson(transactionData, JsonObject.class);
		assertTrue(transactionDetails.get("subscribed_items").isJsonArray());
		
		JsonArray customerSubDetails = transactionDetails.getAsJsonArray("subscribed_items");
		ArrayList<Integer> custSubscriptionIds = subProdDao.saveCustomerSubscriptions(customerSubDetails);
		assertNull(custSubscriptionIds);
	}
	
	@Test(expected = MySQLIntegrityConstraintViolationException.class)
	public void saveCustomerSubscriptionsWithCustomerIdNotPresentTest() throws MySQLIntegrityConstraintViolationException{
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/15/2018\",\"address\":\"777 Ape Dr, Dallas TX 75252\",\"name_on_card\":\"Greg Wash\",\"card_no\":\"3451-2221-7878-9056\",\"exp_month\":\"12\",\"exp_year\":\"2022\",\"card_cvv\":\"500\",\"amount\":220,\"subscribed_items\":[{\"customer_id\":999999,\"subscription_id\":5,\"frequency\":\"bi-weekly\",\"duration\":9,\"quantity\":2,\"start_date\":\"11/15/2018\",\"total\":110}]}";
		JsonObject transactionDetails = gson.fromJson(transactionData, JsonObject.class);
		assertTrue(transactionDetails.get("subscribed_items").isJsonArray());
		
		JsonArray customerSubDetails = transactionDetails.getAsJsonArray("subscribed_items");
		ArrayList<Integer> custSubscriptionIds = subProdDao.saveCustomerSubscriptions(customerSubDetails);
		assertNull(custSubscriptionIds);
	}
	
	@Test
	public void saveCustomerSubscriptionsWithEmptySubTest() throws MySQLIntegrityConstraintViolationException {
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/15/2018\",\"address\":\"777 Ape Dr, Dallas TX 75252\",\"name_on_card\":\"Greg Wash\",\"card_no\":\"3451-2221-7878-9056\",\"exp_month\":\"12\",\"exp_year\":\"2022\",\"card_cvv\":\"500\",\"amount\":220,\"subscribed_items\":[]}";
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
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/15/2018\",\"address\":\"777 Ape Dr, Dallas TX 75252\",\"name_on_card\":\"Greg Wash\",\"card_no\":\"3451-2221-7878-9056\",\"exp_month\":\"12\",\"exp_year\":\"2022\",\"card_cvv\":\"500\",\"amount\":220,\"subscribed_items\":[]}";
		JsonObject transactionDetails = gson.fromJson(transactionData, JsonObject.class);
		assertNotNull(transactionDetails);
		
		int transactionId = transactionDao.createTransaction(transactionDetails);
		assertTrue(transactionId > 0);
	}
	
	@Test
	public void createTransactionWithCashSuccessTest() {
		String transactionData = "{\"payment_mode\":\"cash\",\"date\":\"11/15/2018\",\"address\":\"431 Lemon Rd, Dallas TX 75432\",\"amount\":110,\"subscribed_items\":[{\"customer_id\":1,\"subscription_id\":5,\"frequency\":\"weekly\",\"duration\":9,\"quantity\":1,\"start_date\":\"11/15/2018\",\"total\":110}]}";
		JsonObject transactionDetails = gson.fromJson(transactionData, JsonObject.class);
		assertNotNull(transactionDetails);
		
		int transactionId = transactionDao.createTransaction(transactionDetails);
		assertTrue(transactionId > 0);
	}
	
	@Test
	public void saveCustomerSubsciptionTransactionsSuccessTest() throws MySQLIntegrityConstraintViolationException {
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/15/2018\",\"address\":\"777 Ape Dr, Dallas TX 75252\",\"name_on_card\":\"Greg Wash\",\"card_no\":\"3451-2221-7878-9056\",\"exp_month\":\"12\",\"exp_year\":\"2022\",\"card_cvv\":\"500\",\"amount\":220,\"subscribed_items\":[{\"customer_id\":1,\"subscription_id\":5,\"frequency\":\"bi-weekly\",\"duration\":9,\"quantity\":2,\"start_date\":\"11/15/2018\",\"total\":110}]}";
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
		assertTrue(subTransStatus == 1);
	}
	
	@Test
	public void saveCustomerSubsciptionTransactionsEmptyTest() throws MySQLIntegrityConstraintViolationException {
		String transactionData = "{\"payment_mode\":\"card\",\"date\":\"11/15/2018\",\"address\":\"777 Ape Dr, Dallas TX 75252\",\"name_on_card\":\"Greg Wash\",\"card_no\":\"3451-2221-7878-9056\",\"exp_month\":\"12\",\"exp_year\":\"2022\",\"card_cvv\":\"500\",\"amount\":220,\"subscribed_items\":[{\"customer_id\":1,\"subscription_id\":5,\"frequency\":\"bi-weekly\",\"duration\":9,\"quantity\":2,\"start_date\":\"11/15/2018\",\"total\":110}]}";
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
		transactionDao = null;
		subProdDao = null;
		gson = null;
	}
	
}

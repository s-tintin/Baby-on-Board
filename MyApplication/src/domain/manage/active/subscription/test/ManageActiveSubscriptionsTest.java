package domain.manage.active.subscription.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import domain.subscription.CustomerSubscriptionMap;
import domain.subscription.SubscriptionProductDao;
import domain.subscription.SubscriptionProductDaoImpl;


public class ManageActiveSubscriptionsTest {
	
	private static SubscriptionProductDao subProdDao;
	private static Gson gson;
	
	@BeforeClass
	public static void setUp() {
		gson = new Gson();
		subProdDao = new SubscriptionProductDaoImpl();
	}
	
	@Test
	public void viewActiveSubscriptionSuccessTest() throws MySQLIntegrityConstraintViolationException{
		
		String subscriptionData = "{\"customer_id\":1,\"subscription_id\":5,\"frequency\":\"bi-weekly\",\"duration\":9,\"quantity\":2,\"start_date\":\"11/15/2018\",\"total\":110}";
		JsonObject subscriptionDetail = gson.fromJson(subscriptionData, JsonObject.class);
		JsonArray subscriptionDetails = new JsonArray();
		subscriptionDetails.add(subscriptionDetail);
		assertNotNull(subscriptionDetails);
		assertTrue(subscriptionDetails.isJsonArray());
		
		ArrayList<Integer> custSubscriptionInfo = subProdDao.saveCustomerSubscriptions(subscriptionDetails);
		assertNotNull(custSubscriptionInfo);
		
		List<CustomerSubscriptionMap> subscriptionList =  subProdDao.fetchActiveSubscriptions(1);
		assertFalse(subscriptionList.size()==0);
	}
	
	
	@Test(expected = MySQLIntegrityConstraintViolationException.class)
	public void viewActiveSubscriptionFailureWithSubscriptionIdOutOfRangeTest() throws MySQLIntegrityConstraintViolationException{
		
		String subscriptionData = "{\"customer_id\":1,\"subscription_id\":15,\"frequency\":\"bi-weekly\",\"duration\":9,\"quantity\":2,\"start_date\":\"11/15/2018\",\"total\":110}";
		JsonObject subscriptionDetail = gson.fromJson(subscriptionData, JsonObject.class);
		JsonArray subscriptionDetails = new JsonArray();
		subscriptionDetails.add(subscriptionDetail);
		assertNotNull(subscriptionDetails);
		assertTrue(subscriptionDetails.isJsonArray());
		
		ArrayList<Integer> custSubscriptionInfo = subProdDao.saveCustomerSubscriptions(subscriptionDetails);
		assertNull(custSubscriptionInfo);
		
		List<CustomerSubscriptionMap> subscriptionList =  subProdDao.fetchActiveSubscriptions(1);
		assertTrue(subscriptionList.size()==0);
	}
	
	@Test(expected = MySQLIntegrityConstraintViolationException.class)
	public void viewActiveSubscriptionFailureWithCustomerIdNotPresentTest() throws MySQLIntegrityConstraintViolationException{
		
		String subscriptionData = "{\"customer_id\":99999,\"subscription_id\":5,\"frequency\":\"bi-weekly\",\"duration\":9,\"quantity\":2,\"start_date\":\"11/15/2018\",\"total\":110}";
		JsonObject subscriptionDetail = gson.fromJson(subscriptionData, JsonObject.class);
		JsonArray subscriptionDetails = new JsonArray();
		subscriptionDetails.add(subscriptionDetail);
		assertNotNull(subscriptionDetails);
		assertTrue(subscriptionDetails.isJsonArray());
		
		ArrayList<Integer> custSubscriptionInfo = subProdDao.saveCustomerSubscriptions(subscriptionDetails);
		assertNull(custSubscriptionInfo);
		
		List<CustomerSubscriptionMap> subscriptionList =  subProdDao.fetchActiveSubscriptions(99999);
		assertTrue(subscriptionList.size()==0);
	}
	
	@Test
	public void viewActiveSubscriptionFailureWithNoSubscription() throws MySQLIntegrityConstraintViolationException{
		
		String subscriptionData = "{}";
		JsonObject subscriptionDetail = gson.fromJson(subscriptionData, JsonObject.class);
		JsonArray subscriptionDetails = new JsonArray();
		subscriptionDetails.add(subscriptionDetail);
		assertNotNull(subscriptionDetails);
		assertTrue(subscriptionDetails.isJsonArray());
		
		ArrayList<Integer> custSubscriptionInfo = subProdDao.saveCustomerSubscriptions(subscriptionDetails);
		assertNotNull(custSubscriptionInfo);
		
		List<CustomerSubscriptionMap> subscriptionList =  subProdDao.fetchActiveSubscriptions(0);
		assertTrue(subscriptionList.size()==0);
	
	}
	
	@Test
	public void deleteActiveSubscriptionSuccessTest() throws MySQLIntegrityConstraintViolationException{
		
		String subscriptionData = "{\"customer_id\":1,\"subscription_id\":5,\"frequency\":\"bi-weekly\",\"duration\":9,\"quantity\":2,\"start_date\":\"11/15/2018\",\"total\":110}";
		JsonObject subscriptionDetail = gson.fromJson(subscriptionData, JsonObject.class);
		JsonArray subscriptionDetails = new JsonArray();
		subscriptionDetails.add(subscriptionDetail);
		assertNotNull(subscriptionDetails);
		assertTrue(subscriptionDetails.isJsonArray());
		
		ArrayList<Integer> custSubscriptionInfo = subProdDao.saveCustomerSubscriptions(subscriptionDetails);
		assertNotNull(custSubscriptionInfo);
		
		int updateStatus = subProdDao.cancelSubscription(custSubscriptionInfo.get(0));
		assertTrue(updateStatus == 1);
	}
	
	@Test
	public void deleteActiveSubscriptionFailureTest() throws MySQLIntegrityConstraintViolationException{
		
		int custSubId = 0;
		
		int updateStatus = subProdDao.cancelSubscription(custSubId);
		assertTrue(updateStatus == 0);
	}
	
	@AfterClass
	public static void tearDown() {
		subProdDao = null;
		gson = null;
	}
	
	
}




























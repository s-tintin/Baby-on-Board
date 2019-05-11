package domain.transaction.test;

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
import domain.transaction.*;

public class TransactionHistoryTest {
	
	private static SubscriptionProductDao Product;
	private static TransactionDao ProdDao;
	private static Gson gson;
	
	@BeforeClass
	public static void setUp() {
		gson = new Gson();
		Product = new SubscriptionProductDaoImpl();
		ProdDao=new TransactionDaoImpl();
	}
	
	@Test
	public void viewTransactionHistorySuccessTest() throws MySQLIntegrityConstraintViolationException{
	/*2.1 */	
		String subscriptionData = "{\"customer_id\":1,\"subscription_id\":5,\"frequency\":\"bi-weekly\",\"duration\":9,\"quantity\":2,\"start_date\":\"11/15/2018\",\"total\":110}";
		JsonObject subscriptionDetail = gson.fromJson(subscriptionData, JsonObject.class);
		JsonArray subscriptionDetails = new JsonArray();
		subscriptionDetails.add(subscriptionDetail);
		assertNotNull(subscriptionDetails);
		assertTrue(subscriptionDetails.isJsonArray());
		
		ArrayList<Integer> custSubscriptionInfo = Product.saveCustomerSubscriptions(subscriptionDetails);
		assertNotNull(custSubscriptionInfo);		
		List<CustomerSubscriptionMap> subscriptionList =  Product.fetchActiveSubscriptions(1);
		List<Transaction>transhistory=ProdDao.fetchTransactionsById(1);
		assertTrue(subscriptionList.get(0).getStartDate().equals(transhistory.get(1).getTransactionDate()));
	}
	
	
	@Test(expected = MySQLIntegrityConstraintViolationException.class)
	public void FailureWithSubscriptionIdOutOfRangeTest() throws MySQLIntegrityConstraintViolationException{
	/*2.2 */
		String subscriptionData = "{\"customer_id\":1,\"subscription_id\":15,\"frequency\":\"bi-weekly\",\"duration\":9,\"quantity\":2,\"start_date\":\"11/15/2018\",\"total\":110}";
		JsonObject subscriptionDetail = gson.fromJson(subscriptionData, JsonObject.class);
		JsonArray subscriptionDetails = new JsonArray();
		subscriptionDetails.add(subscriptionDetail);
		assertNotNull(subscriptionDetails);
		assertTrue(subscriptionDetails.isJsonArray());
		
		ArrayList<Integer> custSubscriptionInfo = Product.saveCustomerSubscriptions(subscriptionDetails);
		assertNull(custSubscriptionInfo);
		List<Transaction>transhistory=ProdDao.fetchTransactionsById(1);
		assertTrue(transhistory.size()==0);
	}		

	
	@Test
	public void deleteTransactionHistorySuccessTest() throws MySQLIntegrityConstraintViolationException{
	/*2.3 */	
		String subscriptionData = "{\"customer_id\":1,\"subscription_id\":5,\"frequency\":\"bi-weekly\",\"duration\":9,\"quantity\":2,\"start_date\":\"11/15/2018\",\"total\":110}";
		JsonObject subscriptionDetail = gson.fromJson(subscriptionData, JsonObject.class);
		JsonArray subscriptionDetails = new JsonArray();
		subscriptionDetails.add(subscriptionDetail);
		assertNotNull(subscriptionDetails);
		assertTrue(subscriptionDetails.isJsonArray());
		
		ArrayList<Integer> custSubscriptionInfo = Product.saveCustomerSubscriptions(subscriptionDetails);
		assertNotNull(custSubscriptionInfo);
		
		List<CustomerSubscriptionMap> subscri =  Product.fetchActiveSubscriptions(1);
		int Status_value=subscri.get(0).getStatus() ? 0 : 1;
		assertTrue(Status_value==1);
	}
	
	@Test
	public void deleteTransactionHistoryFailureTest() throws MySQLIntegrityConstraintViolationException{
	/*2.4*/	
		int custSubId = 0;
		
		int updateStatus = Product.cancelSubscription(custSubId);
		assertTrue(updateStatus == 0);

	}
	
	@AfterClass
	public static void tearDown() {
		Product = null;
		gson = null;
	}
	
	
}

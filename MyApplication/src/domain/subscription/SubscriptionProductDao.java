package domain.subscription;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public interface SubscriptionProductDao {
	
	/*
	 *  Fetches predefined subscriptions along with their products and quantities
	 */
	public List<Subscription> fetchPredefinedSubscriptions();
	
	/*
	 *  Fetches each subscription information with products
	 */
	public List<Subscription> getSubscriptionInfoById(String subscriptionIds);
	
	/*
	 *  Inserts subscriptions for customers
	 */
	public ArrayList<Integer> saveCustomerSubscriptions(JsonArray subscriptionDetails) throws MySQLIntegrityConstraintViolationException;
	
	/*
	 *  Fetches all subscriptions information for a customer
	 */
	public List<CustomerSubscriptionMap> fetchActiveSubscriptions(int userId);
	
	/*
	 *  Fetches all subscriptions information for a customer
	 */
	public int cancelSubscription(int custSubId);
}

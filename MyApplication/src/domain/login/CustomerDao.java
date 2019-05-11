package domain.login;

import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * 
 * @author karthik
 * The methods that we need to save and retrieve customer from the database
 */
public interface CustomerDao {

	/*
	 * Register customer to the system 
	 */
	public int register(Customer c) throws MySQLIntegrityConstraintViolationException;
	
	/*
	 * Retrieve the Customer object from the database
	 */
	public Customer validateCustomer(Login login);
	
	/*
	 * Delete customer if exists
	 */
	public int deleteCustomer(String userName);
	
	/*
	 * Update customer details
	 */
	public int updateCustomer(JsonObject user);
	
	public int getUserIdByUsername(String username);
}


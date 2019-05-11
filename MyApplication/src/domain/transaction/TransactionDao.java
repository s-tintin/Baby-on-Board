package domain.transaction;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;


/**
 *  @author Diksha
 * These methods are used to access and modify Transaction details from the database. 
 */

public interface TransactionDao {
	/*
	 * Retrieves transaction details from the database 
	 */
	public List<Transaction> fetchTransactionsById(int userId);
	
	/*
	 *  Insert a transaction record into the database
	 */
	public int createTransaction(JsonObject transactionDetails);
	
	/*
	 *  Create transaction and customer subscription mappings
	 */
	public int saveCustomerSubsciptionTransactions(int transactionId, ArrayList<Integer> custSubscriptionIds);
}

package domain.subscription;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import domain.transaction.TransactionDao;
import domain.transaction.TransactionDaoImpl;

@WebServlet("/CustomerSubscriptions")
public class CustomerSubscriptionController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private static Gson gson = new Gson();
	
	public CustomerSubscriptionController() {}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(req.getInputStream()));
		String inputLine;
		String data = "";
		while ((inputLine = in.readLine()) != null) {
			data += inputLine;
		}
		in.close();
		
		JsonObject transactionDetails = gson.fromJson(data, JsonObject.class);
		JsonArray customerSubDetails = transactionDetails.getAsJsonArray("subscribed_items");
		
		SubscriptionProductDao subProdDao = new SubscriptionProductDaoImpl();
		ArrayList<Integer> custSubscriptionIds = new ArrayList<Integer>();
		try {
			custSubscriptionIds = subProdDao.saveCustomerSubscriptions(customerSubDetails);
		} catch (MySQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
		}
		
		int custSubstatus = -1;
		if(customerSubDetails.size() == custSubscriptionIds.size()) {
			custSubstatus = 1;
		}
		
		TransactionDao transactionDao = new TransactionDaoImpl();
		int transactionId = transactionDao.createTransaction(transactionDetails);
		
		int status = 0;
		if(custSubstatus != -1 && transactionId != -1) {
			int subTransStatus = transactionDao.saveCustomerSubsciptionTransactions(transactionId, custSubscriptionIds);
			if(subTransStatus == custSubscriptionIds.size()) {
				status = 1;
			}
		}
		
		resp.setContentType("application/json");
    	resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("{\"status\":" + status + "}");
        resp.flushBuffer();
	}
}

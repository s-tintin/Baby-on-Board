package domain.transaction;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@WebServlet("/GetTransactionInfo")

public class TransactionController extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static Gson gson = new Gson();
	
	public TransactionController() {}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		TransactionDao transactionDao = new TransactionDaoImpl();
		int userId = Integer.parseInt(req.getParameter("id"));
	
		List<Transaction> transactionList =  transactionDao.fetchTransactionsById(userId);
		
		String transactionListString = gson.toJson(transactionList, new TypeToken<List<Transaction>>(){}.getType());
	
		resp.setContentType("application/json");
    	resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(transactionListString);
        resp.flushBuffer();
	}
}

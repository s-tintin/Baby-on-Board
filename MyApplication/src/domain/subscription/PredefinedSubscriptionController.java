package domain.subscription;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@WebServlet("/PredefinedSubscriptions")
public class PredefinedSubscriptionController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private static Gson gson = new Gson();
	
	public PredefinedSubscriptionController() {}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		SubscriptionProductDao subProdDao = new SubscriptionProductDaoImpl();
		List<Subscription> predefinedSubscriptionsList = subProdDao.fetchPredefinedSubscriptions();
		
		String predefinedSubscriptionListString = gson.toJson(predefinedSubscriptionsList, new TypeToken<List<Subscription>>(){}.getType());
		
		resp.setContentType("application/json");
    	resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(predefinedSubscriptionListString);
        resp.flushBuffer();
	}
	
}

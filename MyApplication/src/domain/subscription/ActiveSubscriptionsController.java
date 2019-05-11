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

@WebServlet("/GetActiveSubscriptions")

public class ActiveSubscriptionsController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static Gson gson = new Gson();
	
	public ActiveSubscriptionsController() {}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		SubscriptionProductDao subscriptionDao = new SubscriptionProductDaoImpl();
		int userId = Integer.parseInt(req.getParameter("id"));
	
		List<CustomerSubscriptionMap> subscriptionList =  subscriptionDao.fetchActiveSubscriptions(userId);
		
		String subscriptionListString = gson.toJson(subscriptionList, new TypeToken<List<CustomerSubscriptionMap>>(){}.getType());
	
		resp.setContentType("application/json");
    	resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(subscriptionListString);
        resp.flushBuffer();
	}
	
}

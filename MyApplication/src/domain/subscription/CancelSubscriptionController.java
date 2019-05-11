package domain.subscription;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CancelSubscription")
public class CancelSubscriptionController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public CancelSubscriptionController() {}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		SubscriptionProductDao subscriptionDao = new SubscriptionProductDaoImpl();
		int custSubId = Integer.parseInt(req.getParameter("id"));
	
		int status = subscriptionDao.cancelSubscription(custSubId);
		
		resp.setContentType("application/json");
    	resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("{\"status\": " + status + "}");
        resp.flushBuffer();
	}
	
}

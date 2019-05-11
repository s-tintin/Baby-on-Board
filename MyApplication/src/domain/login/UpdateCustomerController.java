package domain.login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@WebServlet("/UpdateCustomer")
public class UpdateCustomerController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private static Gson gson = new Gson();
	
	public UpdateCustomerController() {}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		JsonObject userDetails = new JsonObject();
		userDetails.addProperty("password", req.getParameter("password"));
		userDetails.addProperty("fullname", req.getParameter("name"));
		userDetails.addProperty("email", req.getParameter("email"));
		userDetails.addProperty("phone", req.getParameter("phone"));
		userDetails.addProperty("id", Integer.parseInt(req.getParameter("id")));
		
		CustomerDao customerDao = new CustomerDaoImpl();
		int status = customerDao.updateCustomer(userDetails);
		
		HttpSession session = req.getSession(false);
		
		if(status == 1 && session != null) {
			Customer c = customerDao.validateCustomer(new Login(req.getParameter("username"), req.getParameter("password")));
			session.setAttribute("customerDetails", gson.toJson(c, new TypeToken<Customer>(){}.getType()));
		}
		resp.sendRedirect(req.getContextPath() + "/userProfile");
	}
}
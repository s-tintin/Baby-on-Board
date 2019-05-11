package domain.login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * Servlet implementation class Login
 */
@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	Gson gson = new Gson();

    public LoginController() {}
    
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		CustomerDao customerDao = new CustomerDaoImpl();
		
		String username = request.getParameter("username");
		String pass = request.getParameter("password");
		String submitType = request.getParameter("submit");
		Login login = new Login(username, pass);
		Customer c = customerDao.validateCustomer(login);
		
		HttpSession session = request.getSession();
		
		if(submitType.equals("login") && c != null && c.getFullName() != null && c.getUsername() != null){
			if(c.getPassword().equals(login.getPassword())) {
				session.setAttribute("loginStatus", "true");
				session.removeAttribute("errorMessage");
				session.setAttribute("customerDetails", gson.toJson(c, new TypeToken<Customer>(){}.getType()));
			}
			else {
				session.setAttribute("loginStatus", "false");
				session.setAttribute("errorMessage", "invalid-login");
			}
			response.sendRedirect(request.getContextPath()+"/index");
		}else if(submitType.equals("register")){
			c.setFullName(request.getParameter("name"));
			c.setUsername(request.getParameter("username"));
			c.setPassword(request.getParameter("password"));
			c.setEmail(request.getParameter("email"));
			c.setPhone(request.getParameter("phone"));
			try {
				int value = customerDao.register(c);
				if(value == 0) {
					session.setAttribute("loginStatus", "false");
					session.setAttribute("errorMessage", "invalid-registration");
				}
				else {
					c.setId(value);
					session.setAttribute("loginStatus", "true");
					session.removeAttribute("errorMessage");
					session.setAttribute("customerDetails", gson.toJson(c, new TypeToken<Customer>(){}.getType()));
				}
			}
			catch(MySQLIntegrityConstraintViolationException e) {
				session.setAttribute("loginStatus", "false");
				session.setAttribute("errorMessage", "duplicate-username");
			}
			response.sendRedirect(request.getContextPath()+"/index");
		}else{
			session.setAttribute("loginStatus", "false");
			session.setAttribute("errorMessage", "invalid-registration");
			response.sendRedirect(request.getContextPath()+"/index");
		}

	}

}

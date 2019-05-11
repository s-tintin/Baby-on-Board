package domain.cart;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@WebServlet("/FetchCartItems")
public class FetchCartItemsController extends HttpServlet {
		
		private static final long serialVersionUID = 1L;
		private static Gson gson = new Gson();
		
		public FetchCartItemsController() {}
		
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			
			int customerId = Integer.parseInt(req.getParameter("id"));
			CartDao cartDao = new CartDaoImpl();
			List<Cart> cartItems = cartDao.fetchCartItems(customerId);
			
			String cartListString = gson.toJson(cartItems, new TypeToken<List<Cart>>(){}.getType());
			
			resp.setContentType("application/json");
	    	resp.setCharacterEncoding("UTF-8");
	        resp.getWriter().write(cartListString);
	        resp.flushBuffer();
		}
}

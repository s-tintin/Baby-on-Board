package domain.cart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@WebServlet("/DeleteCartItem")
public class DeleteCartItemController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private static Gson gson = new Gson();
	
	public DeleteCartItemController() {}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(req.getInputStream()));
		String inputLine;
		String data = "";
		while ((inputLine = in.readLine()) != null) {
			data += inputLine;
		}
		in.close();
		
		JsonObject cartDetails = gson.fromJson(data, JsonObject.class);
		CartDao cartDao = new CartDaoImpl();
		List<Cart> cartItems = cartDao.deleteCartItem(cartDetails);
		
		String cartItemsString = "";
		
		if(cartItems != null) {
			cartItemsString = gson.toJson(cartItems, new TypeToken<List<Cart>>(){}.getType());
		}
		
		resp.setContentType("application/json");
    	resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(cartItemsString);
        resp.flushBuffer();
	}
}

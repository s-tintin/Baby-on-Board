package domain.agegroup;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@WebServlet("/AgeGroups")
public class AgeGroupController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private static Gson gson = new Gson();
	
    public AgeGroupController() {}
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	AgeGroupDao agDao = new AgeGroupDaoImpl();
    	List<AgeGroup> ageGroupsList = agDao.fetchAgeGroups();
    	
    	String ageGroupsListString = gson.toJson(ageGroupsList, new TypeToken<List<AgeGroup>>(){}.getType());
    	
    	resp.setContentType("application/json");
    	resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(ageGroupsListString);
        resp.flushBuffer();
    	
    }
	
}

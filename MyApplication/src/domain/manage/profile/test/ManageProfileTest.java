package domain.manage.profile.test;

import static org.junit.Assert.assertTrue;

import org.junit.*;
import com.google.gson.JsonObject;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import domain.login.Customer;
import domain.login.CustomerDao;
import domain.login.CustomerDaoImpl;


public class ManageProfileTest {
	private static CustomerDao customerDao;
	private static Customer c;
	
	@BeforeClass
	public static void setup() throws MySQLIntegrityConstraintViolationException {
		customerDao = new CustomerDaoImpl();
		c = new Customer();
		
		c.setFullName("Test Dummy");
		c.setUsername("test-dummy");
		c.setPassword("test-dummy");
		c.setEmail("test@dummy.com");
		c.setPhone("+1-352-949-9999");
		customerDao.register(c);
	}
	
	@Test
	public void updateProfileSuccessTest() throws MySQLIntegrityConstraintViolationException{
		int userId = customerDao.getUserIdByUsername("test-dummy");
		
		JsonObject userDetails = new JsonObject();
		userDetails.addProperty("password", "testedPassword");
		userDetails.addProperty("fullname", "Tested Dummy");
		userDetails.addProperty("email", "tested@gmail.com");
		userDetails.addProperty("phone", "469-899-0000");
		userDetails.addProperty("id", userId);
		
		int status = customerDao.updateCustomer(userDetails);
		assertTrue(status == 1);
	}
	
	@Test
	public void updateProfileNoPasswordTest() throws MySQLIntegrityConstraintViolationException{
		int userId = customerDao.getUserIdByUsername("test-dummy");
		String pass = new String();
		pass = null;
		JsonObject userDetails = new JsonObject();
		userDetails.addProperty("password", pass);
		userDetails.addProperty("fullname", "Tested Dummy");
		userDetails.addProperty("email", "tested@gmail.com");
		userDetails.addProperty("phone", "469-899-0000");
		userDetails.addProperty("id", userId);
		
		int status = customerDao.updateCustomer(userDetails);
		assertTrue(status == 0);
	}
	
	@AfterClass
	public static void tearDown() {
		customerDao.deleteCustomer("test-dummy");
		customerDao = null;
	}
	
}

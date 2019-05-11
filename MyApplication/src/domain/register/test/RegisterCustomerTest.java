package domain.register.test;

import static org.junit.Assert.assertTrue;

import org.junit.*;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import domain.login.Customer;
import domain.login.CustomerDao;
import domain.login.CustomerDaoImpl;

public class RegisterCustomerTest {
	
	private static CustomerDao customerDao;
	
	@BeforeClass
	public static void setUp() {
		customerDao = new CustomerDaoImpl();
		customerDao.deleteCustomer("test-dummy");
	}
	
	@Test
	public void registerCustomerSuccessTest() throws MySQLIntegrityConstraintViolationException {
		Customer c = new Customer();
		c.setFullName("Test Dummy");
		c.setUsername("test-dummy");
		c.setPassword("test-dummy");
		c.setEmail("test@dummy.com");
		c.setPhone("+1-352-949-9999");
		int status = customerDao.register(c);
		assertTrue(status > 0);
	}
	
	@Test(expected = MySQLIntegrityConstraintViolationException.class)
	public void registerCustomerNoUserNameTest() throws MySQLIntegrityConstraintViolationException {
		Customer c = new Customer();
		c.setFullName("Test Dummy");
		c.setPassword("test-dummy");
		c.setEmail("test@dummy.com");
		c.setPhone("+1-352-949-9999");
		customerDao.register(c);
	}
	
	@Test(expected = MySQLIntegrityConstraintViolationException.class)
	public void registerCustomerDuplicateUsernameTest() throws MySQLIntegrityConstraintViolationException {
		Customer c = new Customer();
		c.setUsername("admin");
		c.setFullName("Test Dummy");
		c.setPassword("test-dummy");
		c.setEmail("test@dummy.com");
		c.setPhone("+1-352-949-9999");
		customerDao.register(c);
	}
	
	@AfterClass
	public static void tearDown() {
		customerDao.deleteCustomer("test-dummy");
		customerDao = null;
	}
}

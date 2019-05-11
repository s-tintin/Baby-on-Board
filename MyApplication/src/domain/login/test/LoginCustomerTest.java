package domain.login.test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import domain.login.Customer;
import domain.login.CustomerDao;
import domain.login.CustomerDaoImpl;
import domain.login.Login;

public class LoginCustomerTest {

	private static CustomerDao customerDao;
	private static Customer customer;
	private static Login login;

	@BeforeClass
	public static void setUp(){
		customerDao = new CustomerDaoImpl();
	}

	@Test
	public void loginCustomerSuccessTest() throws MySQLIntegrityConstraintViolationException{
		login = new Login("admin", "admin");
		customer = customerDao.validateCustomer(login);
		assertNotNull(customer);
		assertEquals(customer.getUsername(),login.getUsername());
		assertEquals(customer.getPassword(),login.getPassword());
	}

	@Test
	public void loginCustomerUnsuccessfulTest() throws MySQLIntegrityConstraintViolationException{
		login = new Login("nimda", "nimda");
		customer = customerDao.validateCustomer(login);
		assertNotNull(customer);
		assertNotEquals(customer.getUsername(),login.getUsername());
		assertNotEquals(customer.getPassword(),login.getPassword());
	}

	@Test
	public void loginCustomerIncorrectPasswordTest() throws MySQLIntegrityConstraintViolationException{
		login = new Login("admin", "nimda");
		customer = customerDao.validateCustomer(login);
		assertNotNull(customer);
		assertEquals(customer.getUsername(),login.getUsername());
		assertNotEquals(customer.getPassword(),login.getPassword());
	}
	@AfterClass
	public static void tearDown() {
		customerDao = null;
		customer = null;
		login = null;
	}





}

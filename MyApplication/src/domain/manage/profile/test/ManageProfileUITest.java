package domain.manage.profile.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import domain.login.Customer;
import domain.login.CustomerDao;
import domain.login.CustomerDaoImpl;

public class ManageProfileUITest {

	WebDriver driver;
	private static Customer c;
	
	@BeforeClass
	public static void setUp() throws MySQLIntegrityConstraintViolationException {
		CustomerDao customerDao = new CustomerDaoImpl();
		
		c = new Customer();
		
		c.setFullName("Test Dummy");
		c.setUsername("test-dummy");
		c.setPassword("test-dummy");
		c.setEmail("test@dummy.com");
		c.setPhone("+1-352-949-9999");
		customerDao.register(c);	
		
	}
	
	@Before
	public void setUpPage() throws InterruptedException{
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");
		driver = new ChromeDriver();
		
		driver.get("http://localhost:8080/MyApplication/index");
		assertEquals("Baby On Board | Landing", driver.getTitle());
		
		WebElement loginForm = driver.findElement(By.id("login"));
		assertEquals(loginForm.getAttribute("name"), "loginform");
		assertEquals(loginForm.getCssValue("display"), "block");
		
		loginForm.findElement(By.name("username")).sendKeys("test-dummy");
		loginForm.findElement(By.name("password")).sendKeys("test-dummy");
		loginForm.findElement(By.name("submit")).click();
		
		Thread.sleep(2000);
		
		List<WebElement> formContainer = driver.findElements(By.className("home"));
		assertTrue(formContainer.size() == 1);
		assertEquals(formContainer.get(0).getAttribute("style"), "display: none;");
	}
	
	@Test
	public void manageProfileSuccessTest() throws InterruptedException {
		
		assertEquals("Baby On Board | Landing", driver.getTitle());
		
		// Checking if all age groups are present
		WebElement link = driver.findElement(By.className("header"));
		link.click();
		Thread.sleep(500);
			
		WebElement profileLink = link.findElement(By.className("main-nav"));
		
		
		WebElement dropLink1 = profileLink.findElement(By.id("user-button"));
		dropLink1.click();
		Thread.sleep(1000);
		
		WebElement dropLink2 = dropLink1.findElement(By.className("dropdown_nav"));
		
		WebElement dropLink3 = dropLink2.findElement(By.id("user_profile"));
		dropLink3.click();
		Thread.sleep(1000);
		
		WebElement editCustomerForm = driver.findElement(By.id("edit-customer"));
		
		assertEquals(editCustomerForm.getAttribute("name"), "editcustomer");
		
		
		
		editCustomerForm.findElement(By.name("name")).clear();
		editCustomerForm.findElement(By.name("name")).sendKeys("Tested Dummy");
		
		editCustomerForm.findElement(By.name("email")).clear();
		editCustomerForm.findElement(By.name("email")).sendKeys("tested@dummy.com");
		
		editCustomerForm.findElement(By.name("phone")).clear();
		editCustomerForm.findElement(By.name("phone")).sendKeys("352-949-9999");
		
		editCustomerForm.findElement(By.name("password")).clear();
		editCustomerForm.findElement(By.name("password")).sendKeys("test-dummy");
		
		editCustomerForm.findElement(By.name("retry-password")).clear();
		editCustomerForm.findElement(By.name("retry-password")).sendKeys("test-dummy");
		
		editCustomerForm.findElement(By.name("submit")).click();
		Thread.sleep(5000);
		
		List<WebElement> formContainer = driver.findElements(By.id("edit-customer"));
		assertTrue(formContainer.size() == 1);
		
	}
	
	@Test
	public void manageProfileValidationTest() throws InterruptedException  {
		driver.get("http://localhost:8080/MyApplication/userProfile");
		assertEquals("Baby On Board | User Profile", driver.getTitle());
		Thread.sleep(1000);
		
		WebElement editCustomerForm = driver.findElement(By.id("edit-customer"));
		
		assertEquals(editCustomerForm.getAttribute("name"), "editcustomer");
		
		
		// Submit empty form
		editCustomerForm.findElement(By.name("name")).clear();
		editCustomerForm.findElement(By.name("email")).clear();
		editCustomerForm.findElement(By.name("phone")).clear();
		editCustomerForm.findElement(By.name("password")).clear();
		editCustomerForm.findElement(By.name("retry-password")).clear();
		
		editCustomerForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		WebElement popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Password must be filled out");
		Thread.sleep(4000);
		
		// Submit form with name only
		editCustomerForm.findElement(By.name("name")).clear();
		editCustomerForm.findElement(By.name("email")).clear();
		editCustomerForm.findElement(By.name("phone")).clear();
		editCustomerForm.findElement(By.name("password")).clear();
		editCustomerForm.findElement(By.name("retry-password")).clear();
		
		editCustomerForm.findElement(By.name("name")).sendKeys("Test dummy");
		editCustomerForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Password must be filled out");
		Thread.sleep(4000);
		
		// Submit form with password only
		editCustomerForm.findElement(By.name("name")).clear();
		editCustomerForm.findElement(By.name("email")).clear();
		editCustomerForm.findElement(By.name("phone")).clear();
		editCustomerForm.findElement(By.name("password")).clear();
		editCustomerForm.findElement(By.name("retry-password")).clear();
		
		editCustomerForm.findElement(By.name("password")).sendKeys("test-dummy");
		editCustomerForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Retype Password must be filled out");
		Thread.sleep(4000);
		
		// Submit form with confirmed password
		editCustomerForm.findElement(By.name("name")).clear();
		editCustomerForm.findElement(By.name("email")).clear();
		editCustomerForm.findElement(By.name("phone")).clear();
		editCustomerForm.findElement(By.name("password")).clear();
		editCustomerForm.findElement(By.name("retry-password")).clear();
		
		editCustomerForm.findElement(By.name("retry-password")).sendKeys("test-dummy1");
		editCustomerForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Password must be filled out");
		Thread.sleep(4000);
		
		// Submit form with full name
		editCustomerForm.findElement(By.name("name")).clear();
		editCustomerForm.findElement(By.name("email")).clear();
		editCustomerForm.findElement(By.name("phone")).clear();
		editCustomerForm.findElement(By.name("password")).clear();
		editCustomerForm.findElement(By.name("retry-password")).clear();
		
		
		editCustomerForm.findElement(By.name("name")).sendKeys("Test Dummy");
		editCustomerForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(),  "Password must be filled out");
		Thread.sleep(4000);
		
		// Submit form with invalid email
		editCustomerForm.findElement(By.name("name")).clear();
		editCustomerForm.findElement(By.name("email")).clear();
		editCustomerForm.findElement(By.name("phone")).clear();
		editCustomerForm.findElement(By.name("password")).clear();
		editCustomerForm.findElement(By.name("retry-password")).clear();
		
		editCustomerForm.findElement(By.name("name")).sendKeys("Tested Dummy");
		editCustomerForm.findElement(By.name("email")).sendKeys("hahahaha");
		editCustomerForm.findElement(By.name("phone")).sendKeys("352-949-9999");
		editCustomerForm.findElement(By.name("password")).sendKeys("test-dummy");
		editCustomerForm.findElement(By.name("retry-password")).sendKeys("test-dummy");
		
		editCustomerForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Please enter a valid email id");
		Thread.sleep(4000);
		
		// Submit form with valid email
		editCustomerForm.findElement(By.name("email")).clear();
		editCustomerForm.findElement(By.name("phone")).clear();
		editCustomerForm.findElement(By.name("email")).sendKeys("test@gmail.com");
		editCustomerForm.findElement(By.name("submit")).click();
		
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Phone must be filled out");
		Thread.sleep(4000);
		
		// Submit form with invalid phone number
		editCustomerForm.findElement(By.name("phone")).clear();
		editCustomerForm.findElement(By.name("phone")).sendKeys("+invalidno");
		editCustomerForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Phone number is invalid");
		Thread.sleep(4000);
		
		// Submit form with retype password not equals password
		editCustomerForm.findElement(By.name("phone")).clear();
		editCustomerForm.findElement(By.name("phone")).sendKeys("3528889999");
		editCustomerForm.findElement(By.name("password")).clear();
		editCustomerForm.findElement(By.name("retry-password")).clear();
		
		editCustomerForm.findElement(By.name("password")).sendKeys("test-dummy");
		editCustomerForm.findElement(By.name("retry-password")).sendKeys("tested-dummy");
		editCustomerForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Passwords do not match");
		
	}
	
	
	
	@AfterClass
	public static void tearDown() {
		CustomerDao customerDao = new CustomerDaoImpl();
		customerDao.deleteCustomer("test-dummy");
		customerDao = null;
	}
	
	
}

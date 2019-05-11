package domain.register.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import domain.login.CustomerDao;
import domain.login.CustomerDaoImpl;

public class RegisterCustomerUITest {
	
	WebDriver driver;
	
	@BeforeClass
	public static void setUp() {
		CustomerDao customerDao = new CustomerDaoImpl();
		customerDao.deleteCustomer("test-dummy");
	}
	
	@Before
	public void setUpPage() throws InterruptedException{
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");
		driver = new ChromeDriver();
	}
	
	@Test
	public void registrationSuccessTest() throws InterruptedException {
		driver.get("http://localhost:8080/MyApplication/index");
		assertEquals("Baby On Board | Landing", driver.getTitle());

		WebElement link = driver.findElement(By.id("sign-up-button"));
		link.click();
		Thread.sleep(1000);
		
		WebElement registrationForm = driver.findElement(By.id("register"));  
		assertEquals(registrationForm.getAttribute("name"), "regform");
		assertEquals(registrationForm.getAttribute("style"), "display: inline;");
		
		registrationForm.findElement(By.name("name")).sendKeys("Test Dummy");
		registrationForm.findElement(By.name("email")).sendKeys("test@gmail.com");
		registrationForm.findElement(By.name("phone")).sendKeys("3528889999");
		registrationForm.findElement(By.name("username")).sendKeys("test-dummy");
		registrationForm.findElement(By.name("password")).sendKeys("test-dummy");
		registrationForm.findElement(By.name("retry-password")).sendKeys("test-dummy");
		registrationForm.findElement(By.name("submit")).click();
		Thread.sleep(5000);
		
		List<WebElement> formContainer = driver.findElements(By.className("home"));
		assertTrue(formContainer.size() == 1);
		
		assertEquals(formContainer.get(0).getAttribute("style"), "display: none;");
	}
	
	@Test
	public void registrationDuplicateUsernameTest() throws InterruptedException {
		driver.get("http://localhost:8080/MyApplication/index");
		assertEquals("Baby On Board | Landing", driver.getTitle());

		WebElement link = driver.findElement(By.id("sign-up-button"));
		link.click();
		Thread.sleep(1000);
		
		WebElement registrationForm = driver.findElement(By.id("register"));  
		assertEquals(registrationForm.getAttribute("name"), "regform");
		assertEquals(registrationForm.getAttribute("style"), "display: inline;");
		
		registrationForm.findElement(By.name("name")).sendKeys("Test Dummy");
		registrationForm.findElement(By.name("email")).sendKeys("test@gmail.com");
		registrationForm.findElement(By.name("phone")).sendKeys("3528889999");
		registrationForm.findElement(By.name("username")).sendKeys("admin");
		registrationForm.findElement(By.name("password")).sendKeys("test-dummy");
		registrationForm.findElement(By.name("retry-password")).sendKeys("test-dummy");
		registrationForm.findElement(By.name("submit")).click();
		Thread.sleep(5000);
		
		List<WebElement> formContainer = driver.findElements(By.className("home"));
		assertTrue(formContainer.size() == 1);
		assertEquals(formContainer.get(0).getCssValue("display"), "block");
		
		registrationForm = driver.findElement(By.id("register"));
		assertEquals(registrationForm.getAttribute("style"), "display: inline;");
		WebElement errorElement = registrationForm.findElement(By.id("incorrect-register"));
		assertTrue(!errorElement.getAttribute("class").contains("hide"));
		assertEquals(errorElement.getText(), "Username already exists!");
	}
	
	@Test
	public void registrationFormValidationsTest() throws InterruptedException {
		driver.get("http://localhost:8080/MyApplication/index");
		assertEquals("Baby On Board | Landing", driver.getTitle());

		WebElement link = driver.findElement(By.id("sign-up-button"));
		link.click();
		Thread.sleep(1000);
		
		WebElement registrationForm = driver.findElement(By.id("register"));  
		assertEquals(registrationForm.getAttribute("name"), "regform");
		assertEquals(registrationForm.getAttribute("style"), "display: inline;");
		
		// Submit empty form
		registrationForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		WebElement popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Username must be filled out");
		Thread.sleep(4000);
		
		// Submit form with user name only
		registrationForm.findElement(By.name("username")).sendKeys("test-dummy");
		registrationForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Password must be filled out");
		Thread.sleep(4000);
		
		// Submit form with password
		registrationForm.findElement(By.name("password")).sendKeys("test-dummy");
		registrationForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Confirm Password must be filled out");
		Thread.sleep(4000);
		
		// Submit form with confirmed password
		registrationForm.findElement(By.name("retry-password")).sendKeys("test-dummy1");
		registrationForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Full Name must be filled out");
		Thread.sleep(4000);
		
		// Submit form with full name
		registrationForm.findElement(By.name("name")).sendKeys("Test Dummy");
		registrationForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Email must be filled out");
		Thread.sleep(4000);
		
		// Submit form with invalid email
		registrationForm.findElement(By.name("email")).sendKeys("hahahaha");
		registrationForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Please enter a valid email id");
		Thread.sleep(4000);
		
		// Submit form with valid email
		registrationForm.findElement(By.name("email")).clear();
		registrationForm.findElement(By.name("email")).sendKeys("test@gmail.com");
		registrationForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Phone must be filled out");
		Thread.sleep(4000);
		
		// Submit form with invalid phone number
		registrationForm.findElement(By.name("phone")).sendKeys("+invalidno");
		registrationForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Phone number is invalid");
		Thread.sleep(4000);
		
		// Submit form with valid phone number
		registrationForm.findElement(By.name("phone")).clear();
		registrationForm.findElement(By.name("phone")).sendKeys("3528889999");
		registrationForm.findElement(By.name("submit")).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Passwords do not match");
	}
	
	@After
	public void tearDownPage(){
		driver.quit();
	}
	
	@AfterClass
	public static void tearDown() {
		CustomerDao customerDao = new CustomerDaoImpl();
		customerDao.deleteCustomer("test-dummy");
		customerDao = null;
	}
}


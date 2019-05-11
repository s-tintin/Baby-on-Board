/**
 * 
 */
package domain.login.test;

import static org.junit.Assert.*;

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

/**
 * @author Roopa
 *
 */
public class LoginCustomerUITest {

	WebDriver driver;

	@BeforeClass
	public static void setUp() {
	
	}
	
	@Before
	public void setUpPage() throws InterruptedException{
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");
		driver = new ChromeDriver();
	}
	
	@Test
	public void loginSuccessTest() throws InterruptedException{
		
		driver.get("http://localhost:8080/MyApplication/index");
		assertEquals("Baby On Board | Landing", driver.getTitle());
		
		WebElement loginForm = driver.findElement(By.id("login"));
		assertEquals(loginForm.getAttribute("name"), "loginform");
		assertEquals(loginForm.getCssValue("display"), "block");
		
		loginForm.findElement(By.name("username")).sendKeys("admin");
		loginForm.findElement(By.name("password")).sendKeys("admin");
		loginForm.findElement(By.name("submit")).click();
		
		Thread.sleep(5000);
		
		List<WebElement> formContainer = driver.findElements(By.className("home"));
		assertTrue(formContainer.size() == 1);
		
		assertEquals(formContainer.get(0).getAttribute("style"), "display: none;");
		
	}
	
	@Test
	public void loginFailureTest() throws InterruptedException{
		driver.get("http://localhost:8080/MyApplication/index");
		assertEquals("Baby On Board | Landing", driver.getTitle());
		
		Thread.sleep(5000);
		
		WebElement loginForm = driver.findElement(By.id("login"));
		assertEquals(loginForm.getAttribute("name"), "loginform");
		assertEquals(loginForm.getCssValue("display"), "block");
		
		loginForm.findElement(By.name("username")).sendKeys("admin");
		loginForm.findElement(By.name("password")).sendKeys("nimda");
		loginForm.findElement(By.name("submit")).click();
		
		Thread.sleep(5000);
		
		List<WebElement> formContainer = driver.findElements(By.className("home"));
		assertTrue(formContainer.size() == 1);
		
		assertEquals(formContainer.get(0).getCssValue("display"), "block");
		
		loginForm = driver.findElement(By.id("login"));
		assertEquals(loginForm.getCssValue("display"), "block");
		WebElement errorElement = loginForm.findElement(By.id("incorrect-login"));
		assertTrue(!errorElement.getAttribute("class").contains("hide"));
		assertEquals(errorElement.getText(), "Password invalid!");
	}
	
	@Test
	public void loginClick() throws InterruptedException{
		driver.get("http://localhost:8080/MyApplication/index");
		assertEquals("Baby On Board | Landing", driver.getTitle());
	
		WebElement loginForm = driver.findElement(By.id("login"));
		assertEquals(loginForm.getAttribute("name"), "loginform");
		assertEquals(loginForm.getCssValue("display"), "block");
		loginForm.findElement(By.name("submit")).click();
		Thread.sleep(1000);
		//submit empty form
		WebElement popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Username must be filled out");
		Thread.sleep(4000);
	 //Submit with only username filled
		loginForm.findElement(By.name("username")).sendKeys("admin");
		loginForm.findElement(By.name("submit")).click();
		Thread.sleep(1000);
		 popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Password must be filled out");
		//submit with wrong password
		loginForm.findElement(By.name("password")).sendKeys("nimda");
		loginForm.findElement(By.name("submit")).click();
		
        Thread.sleep(5000);
		
		List<WebElement> formContainer = driver.findElements(By.className("home"));
		assertTrue(formContainer.size() == 1);
		
		assertEquals(formContainer.get(0).getCssValue("display"), "block");
		
		loginForm = driver.findElement(By.id("login"));
		assertEquals(loginForm.getCssValue("display"), "block");
		WebElement errorElement = loginForm.findElement(By.id("incorrect-login"));
		assertTrue(!errorElement.getAttribute("class").contains("hide"));
		assertEquals(errorElement.getText(), "Password invalid!");
		//submit with all valid credentials
		loginForm.findElement(By.name("username")).sendKeys("admin");
		loginForm.findElement(By.name("password")).sendKeys("admin");
		loginForm.findElement(By.name("submit")).click();
         Thread.sleep(5000);
		
		formContainer = driver.findElements(By.className("home"));
		assertTrue(formContainer.size() == 1);
		
		assertEquals(formContainer.get(0).getAttribute("style"), "display: none;");
		
		
	}

	@After
	public void tearDownPage() throws InterruptedException{

		driver.quit();
	}
	
	@AfterClass
	public static void tearDown() {
	
	}

}

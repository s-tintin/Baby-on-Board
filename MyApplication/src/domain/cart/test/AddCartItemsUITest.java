package domain.cart.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class AddCartItemsUITest {

	WebDriver driver;

	@Before
	public void setUp() throws Exception {
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
		
		loginForm.findElement(By.name("username")).sendKeys("admin");
		loginForm.findElement(By.name("password")).sendKeys("admin");
		loginForm.findElement(By.name("submit")).click();
		
		Thread.sleep(2000);
		
		List<WebElement> formContainer = driver.findElements(By.className("home"));
		assertTrue(formContainer.size() == 1);
		assertEquals(formContainer.get(0).getAttribute("style"), "display: none;");
	}
	
	@Test
	public void addToCartSuccess() throws InterruptedException{
		
       assertEquals("Baby On Board | Landing", driver.getTitle());
       Thread.sleep(5000);
		// Checking if all age groups are present
		WebElement ageGroupList = driver.findElement(By.id("age-group-list"));
		assertNotNull(ageGroupList);
		
		List<WebElement> ageGroups = ageGroupList.findElements(By.tagName("li"));
		assertNotNull(ageGroups);
		assertTrue(ageGroups.size() == 3);
		
		// Checking if the correct subscription container is selected
		List<WebElement> subscriptionContainers = driver.findElements(By.className("inner-subscription-container"));
		assertNotNull(subscriptionContainers);
		assertTrue(subscriptionContainers.size() == 3);
		assertFalse(subscriptionContainers.get(0).getAttribute("class").contains("hide"));
		
		// Changing age group
		ageGroups.get(1).findElement(By.tagName("button")).click();
		Thread.sleep(5000);
		// Checking if correct age group is selected
		ageGroupList = driver.findElement(By.id("age-group-list"));
		assertNotNull(ageGroupList);
		
		ageGroups = ageGroupList.findElements(By.tagName("li"));
		assertNotNull(ageGroups);
		assertTrue(ageGroups.size() == 3);
		
		WebElement selectedAgeGroup = ageGroups.get(1);
		assertTrue(selectedAgeGroup.getAttribute("class").length() > 0);
		assertTrue(selectedAgeGroup.getAttribute("class").contains("selected-age"));
		
		// Checking if the corresponding container is selected
		subscriptionContainers = driver.findElements(By.className("inner-subscription-container"));
		assertNotNull(subscriptionContainers);
		assertTrue(subscriptionContainers.size() == 3);
		assertTrue(subscriptionContainers.get(0).getAttribute("class").contains("hide"));
		Thread.sleep(2000);
		WebElement selectedSubContainer = subscriptionContainers.get(1);
		assertFalse(selectedSubContainer.getAttribute("class").contains("hide"));
		
		// Selecting addToCart Button
		List<WebElement> subscriptions = selectedSubContainer.findElements(By.className("subscription-box"));
		assertNotNull(subscriptions);
		assertTrue(subscriptions.size() == 3);
		subscriptions.get(2).findElements(By.className("addToCart_button")).get(0).click();
		Thread.sleep(2000);
		WebElement cartModal=driver.findElement(By.className("header"));
		cartModal.findElement(By.id("cart_btn")).click();
		Thread.sleep(2000);
	}
	
	@Test
	public void deleteFromCartSuccess() throws InterruptedException{
		WebElement cartModal=driver.findElement(By.className("header"));
		cartModal.findElement(By.id("cart_btn")).click();
		Thread.sleep(5000);
		
		WebElement cartTable=driver.findElement(By.id("cart-table1"));
		//cartTable.findElement(By.id("cart_btn")).click();
		List<WebElement> deleteBtns = cartTable.findElements(By.className("delete_cart"));
		assertNotNull(deleteBtns);
		deleteBtns.get(0).click();
	}
	
	@After
	public void tearDown() throws Exception {
		Thread.sleep(500);
		driver.quit();
	}

	

}

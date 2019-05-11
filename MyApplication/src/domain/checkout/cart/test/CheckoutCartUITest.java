package domain.checkout.cart.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import domain.cart.Cart;
import domain.cart.CartDao;
import domain.cart.CartDaoImpl;

public class CheckoutCartUITest {
	
	WebDriver driver;
	private static CartDao cartDao;
	private static Gson gson;
	
	@BeforeClass
	public static void setUp() {
		gson = new Gson();
		cartDao = new CartDaoImpl();
	}
	
	@Before
	public void setUpPage() throws InterruptedException, MySQLIntegrityConstraintViolationException{
		// Delete all cart items for customer
		String cartItemString = "{\"cust_id\":1}";
		JsonObject cartDetails = gson.fromJson(cartItemString, JsonObject.class);
		List<Cart> cartItems = cartDao.deleteCartItem(cartDetails);

		// Add Item to cart
		cartItemString = "{\"cust_id\":1,\"sub_id\":\"4\",\"quantity\":1,\"price\":86}";
		cartDetails = gson.fromJson(cartItemString, JsonObject.class);
		cartItems = cartDao.addItemToCart(cartDetails);
		assertNotNull(cartItems);
		assertTrue(cartItems.size() == 1);

		// Add Item to cart
		cartItemString = "{\"cust_id\":1,\"sub_id\":\"9\",\"quantity\":1,\"price\":19}";
		cartDetails = gson.fromJson(cartItemString, JsonObject.class);
		cartItems = cartDao.addItemToCart(cartDetails);
		assertNotNull(cartItems);
		assertTrue(cartItems.size() == 2);
		
		Thread.sleep(200);
		
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
	public void checkoutCartByCardSuccessTest() throws InterruptedException {
		assertEquals("Baby On Board | Landing", driver.getTitle());
		Thread.sleep(2000);
		
		// Open cart
		WebElement cartButton = driver.findElement(By.id("cart_btn"));
		assertNotNull(cartButton);
		assertTrue(cartButton.getAttribute("class").contains("cart_btn_class"));
		assertTrue(cartButton.findElement(By.id("cart-count")).getText().equalsIgnoreCase("2"));
		cartButton.click();
		
		// Cart Modal
		WebElement cartModal = driver.findElement(By.id("cart-modal"));
		assertNotNull(cartModal);
		assertEquals(cartModal.getAttribute("style"), "display: block;");
		
		// Check cart table elements
		List<WebElement> cartTableElements = driver.findElement(By.id("cart-table1")).findElements(By.tagName("tr"));
		assertNotNull(cartTableElements);
		assertTrue(cartTableElements.size() == 2);
		
		// Proceed to checkout
		driver.findElements(By.className("cartCheckout_btn")).get(0).click();
		Thread.sleep(1000);
		
		assertEquals("Baby On Board | Check Out", driver.getTitle());
		
		// Check number of subscription containers
		List<WebElement> subscriptionConts = driver.findElements(By.className("subscription-item"));
		assertNotNull(subscriptionConts);
		assertTrue(subscriptionConts.size() == 2);
		
		// Payment container
		WebElement creditPayCont = driver.findElements(By.className("resp-tab-content-active")).get(0);
		assertNotNull(creditPayCont);
		
		// Enter payment details and check appropriate error messages
		// Check billing address
		creditPayCont.findElements(By.className("submit-payment")).get(0).click();
		Thread.sleep(200);
		WebElement popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Billing address is empty!");
		Thread.sleep(1000);
		
		// Check card name
		creditPayCont.findElement(By.id("card_addr")).sendKeys("7777 Ape Dr, Dallas TX 75643");
		creditPayCont.findElements(By.className("submit-payment")).get(0).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Name on card is empty!");
		Thread.sleep(1000);
		
		// Check card number
		creditPayCont.findElement(By.id("card_name")).sendKeys("Greg Jones");
		creditPayCont.findElements(By.className("submit-payment")).get(0).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Card number is empty!");
		Thread.sleep(1000);
		
		// Check valid card
		creditPayCont.findElement(By.id("card_no")).sendKeys("alpha-121-delta");
		Thread.sleep(200);
		creditPayCont.findElements(By.className("submit-payment")).get(0).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Card number format is invalid");
		Thread.sleep(1000);
		
		// Check exp month
		creditPayCont.findElement(By.id("card_no")).clear();
		creditPayCont.findElement(By.id("card_no")).sendKeys("7643-5544-0101-1541");
		creditPayCont.findElements(By.className("submit-payment")).get(0).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Expiry month is empty!");
		Thread.sleep(1000);
		
		// Check valid exp month
		creditPayCont.findElement(By.id("card_exp_month")).sendKeys("99");
		creditPayCont.findElements(By.className("submit-payment")).get(0).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Expiry month is out of range!");
		Thread.sleep(1000);
		
		// Check exp year
		creditPayCont.findElement(By.id("card_exp_month")).clear();
		creditPayCont.findElement(By.id("card_exp_month")).sendKeys("12");
		Thread.sleep(200);
		creditPayCont.findElements(By.className("submit-payment")).get(0).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Expiry year is empty!");
		Thread.sleep(1000);
		
		// Check valid exp year
		creditPayCont.findElement(By.id("card_exp_year")).sendKeys("1200");
		creditPayCont.findElements(By.className("submit-payment")).get(0).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Expiry year is out of range! Allowed range [1900-2100]");
		Thread.sleep(1000);
		
		// Check cvv
		creditPayCont.findElement(By.id("card_exp_year")).clear();
		creditPayCont.findElement(By.id("card_exp_year")).sendKeys("2020");
		creditPayCont.findElements(By.className("submit-payment")).get(0).click();
		Thread.sleep(200);
		popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "CVV Number is empty!");
		Thread.sleep(1000);
		
		// Submit payment
		creditPayCont.findElement(By.id("card_cvv")).sendKeys("574");
		creditPayCont.findElements(By.className("submit-payment")).get(0).click();
		
		Thread.sleep(2000);
		
		assertEquals("Baby On Board | Order Success", driver.getTitle());
		
		// Check if order is successful
		WebElement successContainer = driver.findElements(By.className("success_msg")).get(0);
		assertNotNull(successContainer);
		assertFalse(successContainer.getAttribute("class").contains("hide"));
		
		// Open cart
		cartButton = driver.findElement(By.id("cart_btn"));
		assertNotNull(cartButton);
		assertTrue(cartButton.getAttribute("class").contains("cart_btn_class"));
		assertTrue(cartButton.findElement(By.id("cart-count")).getText().equalsIgnoreCase("0"));
	}
	
	@Test
	public void checkoutCartByCashSuccessTest() throws InterruptedException {
		assertEquals("Baby On Board | Landing", driver.getTitle());
		Thread.sleep(2000);
		
		// Open cart
		WebElement cartButton = driver.findElement(By.id("cart_btn"));
		assertNotNull(cartButton);
		assertTrue(cartButton.getAttribute("class").contains("cart_btn_class"));
		assertTrue(cartButton.findElement(By.id("cart-count")).getText().equalsIgnoreCase("2"));
		cartButton.click();
		
		// Cart Modal
		WebElement cartModal = driver.findElement(By.id("cart-modal"));
		assertNotNull(cartModal);
		assertEquals(cartModal.getAttribute("style"), "display: block;");
		
		// Check cart table elements
		List<WebElement> cartTableElements = driver.findElement(By.id("cart-table1")).findElements(By.tagName("tr"));
		assertNotNull(cartTableElements);
		assertTrue(cartTableElements.size() == 2);
		
		// Proceed to checkout
		driver.findElements(By.className("cartCheckout_btn")).get(0).click();
		Thread.sleep(1000);
		
		assertEquals("Baby On Board | Check Out", driver.getTitle());
		
		// Check number of subscription containers
		List<WebElement> subscriptionConts = driver.findElements(By.className("subscription-item"));
		assertNotNull(subscriptionConts);
		assertTrue(subscriptionConts.size() == 2);
		
		// Change payment method
		driver.findElements(By.className("resp-tab-item")).get(1).findElement(By.tagName("span")).click();
		assertTrue(driver.findElements(By.className("resp-tab-item")).get(1).getAttribute("class").contains("resp-tab-active"));

		// Payment container
		WebElement cashPayCont = driver.findElements(By.className("resp-tab-content-active")).get(0);
		assertNotNull(cashPayCont);
		
		// Enter payment details and check appropriate error messages
		// Check billing address
		cashPayCont.findElements(By.className("submit-payment")).get(0).click();
		Thread.sleep(200);
		WebElement popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("error"));
		assertEquals(popUpMessage.getText(), "Billing address is empty!");
		Thread.sleep(1000);
		
		// Submit Payment
		cashPayCont.findElement(By.id("cash_addr")).sendKeys("7777 Ape Dr, Dallas TX 75643");
		cashPayCont.findElements(By.className("submit-payment")).get(0).click();
		
		Thread.sleep(2000);
		
		assertEquals("Baby On Board | Order Success", driver.getTitle());
		
		// Check if order is successful
		WebElement successContainer = driver.findElements(By.className("success_msg")).get(0);
		assertNotNull(successContainer);
		assertFalse(successContainer.getAttribute("class").contains("hide"));
		
		// Open cart
		cartButton = driver.findElement(By.id("cart_btn"));
		assertNotNull(cartButton);
		assertTrue(cartButton.getAttribute("class").contains("cart_btn_class"));
		assertTrue(cartButton.findElement(By.id("cart-count")).getText().equalsIgnoreCase("0"));
	}
	
	@After
	public void tearDownPage() throws InterruptedException{
		// Delete all cart items
		String cartItemString = "{\"cust_id\":1}";
		JsonObject cartDetails = gson.fromJson(cartItemString, JsonObject.class);
		cartDao.deleteCartItem(cartDetails);

		driver.get("http://localhost:8080/MyApplication/logout");
		Thread.sleep(500);
		driver.quit();
	}
	
	@AfterClass
	public static void tearDown() {
		cartDao = null;
		gson = null;
	}
	
}	

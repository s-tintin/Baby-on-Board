package domain.subscribe.pack.test;

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

public class SubscribPackageUITest {
	
	WebDriver driver;
	
	@BeforeClass
	public static void setUp() {
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
	public void subscribePackageByCardSuccessTest() throws InterruptedException {
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
		
		WebElement selectedSubContainer = subscriptionContainers.get(1);
		assertFalse(selectedSubContainer.getAttribute("class").contains("hide"));
		
		// Selecting subscription
		List<WebElement> subscriptions = selectedSubContainer.findElements(By.className("subscription-box"));
		assertNotNull(subscriptions);
		assertTrue(subscriptions.size() == 3);
		subscriptions.get(2).findElements(By.className("subscription_button")).get(0).click();
		
		// Checking if the subscription is selected
		subscriptionContainers = driver.findElements(By.className("inner-subscription-container"));
		assertNotNull(subscriptionContainers);
		selectedSubContainer = subscriptionContainers.get(1);
		assertNotNull(selectedSubContainer);
		subscriptions = selectedSubContainer.findElements(By.className("subscription-box"));
		assertNotNull(subscriptions);
		assertTrue(subscriptions.get(2).findElement(By.tagName("ul")).getAttribute("class").contains("sub-selected"));
		
		// Selecting duration
		List<WebElement> durationButtonsList = driver.findElements(By.className("duration_list")).get(0).findElements(By.className("duration_button"));
		assertNotNull(durationButtonsList);
		assertTrue(durationButtonsList.size() == 4);
		durationButtonsList.get(3).click();
		
		// Check if correct duration is selected
		durationButtonsList = driver.findElements(By.className("duration_list")).get(0).findElements(By.className("duration_button"));
		assertNotNull(durationButtonsList);
		assertTrue(durationButtonsList.get(3).getAttribute("class").contains("selected-dur"));
		
		// Proceed to checkout
		driver.findElements(By.className("checkout_btn")).get(0).click();
		Thread.sleep(1000);
		
		assertEquals("Baby On Board | Check Out", driver.getTitle());
		
		WebElement subscriptionCont = driver.findElements(By.className("subscription-item")).get(0);
		assertNotNull(subscriptionCont);
		
		// Check if quantity is 1
		assertTrue(Integer.parseInt(subscriptionCont.findElements(By.className("quantity")).get(0).getAttribute("value")) == 1);
		
		// Set quantity to 2
		subscriptionCont.findElements(By.className("quantity")).get(0).clear();
		subscriptionCont.findElements(By.className("quantity")).get(0).sendKeys("2");
		
		// Check if quantity is set to 2
		subscriptionCont = driver.findElements(By.className("subscription-item")).get(0);
		assertTrue(Integer.parseInt(subscriptionCont.findElements(By.className("quantity")).get(0).getAttribute("value")) == 2);
		
		// Frequency drop down change
		subscriptionCont.findElement(By.id("frequency-dropdown")).findElements(By.className("dropbtn")).get(0).click();
		subscriptionCont.findElement(By.id("frequency-dropdown-content")).findElements(By.tagName("a")).get(2).click();
		
		// Check if frequency is changed
		subscriptionCont = driver.findElements(By.className("subscription-item")).get(0);
		WebElement frequencyVal = subscriptionCont.findElement(By.id("frequency-dropdown")).findElements(By.className("dropbtn")).get(0);
		assertNotNull(frequencyVal);
		assertTrue(frequencyVal.getText().equalsIgnoreCase("Frequency: Monthly"));
		
		// Duration drop down change
		subscriptionCont.findElement(By.id("duration-dropdown")).findElements(By.className("dropbtn")).get(0).click();
		subscriptionCont.findElement(By.id("duration-dropdown-content")).findElements(By.tagName("a")).get(2).click();
		
		// Check if duration is changed
		subscriptionCont = driver.findElements(By.className("subscription-item")).get(0);
		WebElement durationVal = subscriptionCont.findElement(By.id("duration-dropdown")).findElements(By.className("dropbtn")).get(0);
		assertNotNull(durationVal);
		assertTrue(durationVal.getText().equalsIgnoreCase("Duration: 9 Months"));
		
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
	}
	
	@Test
	public void subscribePackageByCashSuccessTest() throws InterruptedException {
		assertEquals("Baby On Board | Landing", driver.getTitle());
		
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
		ageGroups.get(2).findElement(By.tagName("button")).click();
		
		// Checking if correct age group is selected
		ageGroupList = driver.findElement(By.id("age-group-list"));
		assertNotNull(ageGroupList);
		
		ageGroups = ageGroupList.findElements(By.tagName("li"));
		assertNotNull(ageGroups);
		assertTrue(ageGroups.size() == 3);
		
		WebElement selectedAgeGroup = ageGroups.get(2);
		assertTrue(selectedAgeGroup.getAttribute("class").length() > 0);
		assertTrue(selectedAgeGroup.getAttribute("class").contains("selected-age"));
		
		// Checking if the corresponding container is selected
		subscriptionContainers = driver.findElements(By.className("inner-subscription-container"));
		assertNotNull(subscriptionContainers);
		assertTrue(subscriptionContainers.size() == 3);
		assertTrue(subscriptionContainers.get(0).getAttribute("class").contains("hide"));
		
		WebElement selectedSubContainer = subscriptionContainers.get(2);
		assertFalse(selectedSubContainer.getAttribute("class").contains("hide"));
		
		// Selecting subscription
		List<WebElement> subscriptions = selectedSubContainer.findElements(By.className("subscription-box"));
		assertNotNull(subscriptions);
		assertTrue(subscriptions.size() == 3);
		subscriptions.get(2).findElements(By.className("subscription_button")).get(0).click();
		
		// Checking if the subscription is selected
		subscriptionContainers = driver.findElements(By.className("inner-subscription-container"));
		assertNotNull(subscriptionContainers);
		selectedSubContainer = subscriptionContainers.get(2);
		assertNotNull(selectedSubContainer);
		subscriptions = selectedSubContainer.findElements(By.className("subscription-box"));
		assertNotNull(subscriptions);
		assertTrue(subscriptions.get(2).findElement(By.tagName("ul")).getAttribute("class").contains("sub-selected"));
		
		// Selecting duration
		List<WebElement> durationButtonsList = driver.findElements(By.className("duration_list")).get(0).findElements(By.className("duration_button"));
		assertNotNull(durationButtonsList);
		assertTrue(durationButtonsList.size() == 4);
		durationButtonsList.get(3).click();
		
		// Check if correct duration is selected
		durationButtonsList = driver.findElements(By.className("duration_list")).get(0).findElements(By.className("duration_button"));
		assertNotNull(durationButtonsList);
		assertTrue(durationButtonsList.get(3).getAttribute("class").contains("selected-dur"));
		
		// Proceed to checkout
		driver.findElements(By.className("checkout_btn")).get(0).click();
		Thread.sleep(1000);
		
		assertEquals("Baby On Board | Check Out", driver.getTitle());
		
		WebElement subscriptionCont = driver.findElements(By.className("subscription-item")).get(0);
		assertNotNull(subscriptionCont);
		
		// Check if quantity is 1
		assertTrue(Integer.parseInt(subscriptionCont.findElements(By.className("quantity")).get(0).getAttribute("value")) == 1);
		
		// Set quantity to 2
		subscriptionCont.findElements(By.className("quantity")).get(0).clear();
		subscriptionCont.findElements(By.className("quantity")).get(0).sendKeys("2");
		
		// Check if quantity is set to 2
		subscriptionCont = driver.findElements(By.className("subscription-item")).get(0);
		assertTrue(Integer.parseInt(subscriptionCont.findElements(By.className("quantity")).get(0).getAttribute("value")) == 2);
		
		// Frequency drop down change
		subscriptionCont.findElement(By.id("frequency-dropdown")).findElements(By.className("dropbtn")).get(0).click();
		subscriptionCont.findElement(By.id("frequency-dropdown-content")).findElements(By.tagName("a")).get(2).click();
		
		// Check if frequency is changed
		subscriptionCont = driver.findElements(By.className("subscription-item")).get(0);
		WebElement frequencyVal = subscriptionCont.findElement(By.id("frequency-dropdown")).findElements(By.className("dropbtn")).get(0);
		assertNotNull(frequencyVal);
		assertTrue(frequencyVal.getText().equalsIgnoreCase("Frequency: Monthly"));
		
		// Duration drop down change
		subscriptionCont.findElement(By.id("duration-dropdown")).findElements(By.className("dropbtn")).get(0).click();
		subscriptionCont.findElement(By.id("duration-dropdown-content")).findElements(By.tagName("a")).get(2).click();
		
		// Check if duration is changed
		subscriptionCont = driver.findElements(By.className("subscription-item")).get(0);
		WebElement durationVal = subscriptionCont.findElement(By.id("duration-dropdown")).findElements(By.className("dropbtn")).get(0);
		assertNotNull(durationVal);
		assertTrue(durationVal.getText().equalsIgnoreCase("Duration: 9 Months"));
		
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
	}
	
	@After
	public void tearDownPage() throws InterruptedException{
		driver.get("http://localhost:8080/MyApplication/logout");
		Thread.sleep(500);
		driver.quit();
	}
	
	@AfterClass
	public static void tearDown() {
	}
}

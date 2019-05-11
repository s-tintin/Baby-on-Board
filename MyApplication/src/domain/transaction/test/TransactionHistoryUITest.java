package domain.transaction.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TransactionHistoryUITest{
	
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
		
		//Selecting a subscription for the current customer (taken from Subscribe Package UI Test)
		
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
		
		// Change payment method
		driver.findElements(By.className("resp-tab-item")).get(1).findElement(By.tagName("span")).click();
		assertTrue(driver.findElements(By.className("resp-tab-item")).get(1).getAttribute("class").contains("resp-tab-active"));
		
		// Payment container
		WebElement cashPayCont = driver.findElements(By.className("resp-tab-content-active")).get(0);
		assertNotNull(cashPayCont);
		
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
	
	
	@Test
	public void viewTransactionHistoryTest() throws InterruptedException {
		
		assertEquals("Baby On Board | Order Success", driver.getTitle());
		
		WebElement link = driver.findElement(By.className("header"));
		link.click();
		Thread.sleep(500);
			
		WebElement profileLink = link.findElement(By.className("main-nav"));
		
		
		WebElement dropLink1 = profileLink.findElement(By.id("user-button"));
		dropLink1.click();
		Thread.sleep(1000);
		
		WebElement dropLink2 = dropLink1.findElement(By.className("dropdown_nav"));
		Thread.sleep(1000);
		
		WebElement dropLink3 = dropLink2.findElement(By.id("transaction"));
		dropLink3.click();
		
		Thread.sleep(1000);
		
		WebElement transaction=driver.findElement(By.id("transaction-label"));
		transaction.click();
		Thread.sleep(1000);
		/*3.1*/
		WebElement payment = driver.findElement(By.id("tab2"));
		assertEquals(payment.getAttribute("aria-controls"), "Transaction");
				
		WebElement active=driver.findElement(By.id("subscription-label"));
		active.click();
		
		WebElement mainTab = driver.findElement(By.className("tabset"));
		
		WebElement transactionTab = mainTab.findElement(By.className("tab-panels"));
		WebElement activeSubscriptionTab = transactionTab.findElement(By.id("Subscriptions"));
		assertEquals(activeSubscriptionTab.getAttribute("class"), "tab-panel");
		
        Thread.sleep(1000);
		/*3.2*/
		WebElement record = activeSubscriptionTab.findElement(By.className("item-container"));
		assertNotNull(record);
		WebElement deleteButton = record.findElement(By.className("cancel-sub"));
		assertNotNull(deleteButton);
		deleteButton.click();
		
		Thread.sleep(200);
		
		WebElement popUpMessage = driver.findElement(By.id("pop-up-message"));
		assertNotNull(popUpMessage);
		assertTrue(popUpMessage.getAttribute("class").contains("success"));
		assertEquals(popUpMessage.getText(), "Successfully cancelled subscription.");
		
		WebElement trans=driver.findElement(By.id("transaction-label"));
		trans.click();
		Thread.sleep(1000);
		
		
	}
	
	
	@After
	public void tearDownPage() throws InterruptedException{
		driver.get("http://localhost:8080/MyApplication/logout");
		Thread.sleep(500);
		driver.quit();
	}
	
	
}

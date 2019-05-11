/**
 * @author Diksha
 * Getters and setters for Transaction
 *
 *`id` int NOT NULL AUTO_INCREMENT,
	`transaction_date` timestamp NOT NULL,
	`mode` char(100) NOT NULL,
	`address` char(200) NOT NULL,
	`amount` float(10,2) NOT NULL,
	`card_no` INT,
 */

package domain.transaction;

import domain.subscription.*;
import java.util.Date;
import java.util.List;

public class Transaction {
	private int id;
	private String paymentMode;
	private String address;
	private float amount;
	private Date transactionDate;
	private String cardName;
	private String cardNumber;
	private String cardExpiration;
	private String cardCVV;
	
	List<CustomerSubscriptionMap> subscriptions;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardExpiration() {
		return cardExpiration;
	}
	public void setCardExpiration(String cardExpiration) {
		this.cardExpiration = cardExpiration;
	}
	public String getCardCVV() {
		return cardCVV;
	}
	public void setCardCVV(String cardCVV) {
		this.cardCVV = cardCVV;
	}
	public List<CustomerSubscriptionMap> getSubscriptions() {
		return subscriptions;
	}
	public void setSubscriptions(List<CustomerSubscriptionMap> subscriptions) {
		this.subscriptions = subscriptions;
	}
}

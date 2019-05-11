package domain.cart;

public class Cart {
	
	private int id;
	private int subscriptionId;
	private int customerId;
	private double price;
	private int quantity;
	private String subscriptionName;
	private int ageGroupId;
	private String ageGroupName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(int subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getSubscriptionName() {
		return subscriptionName;
	}
	public void setSubscriptionName(String subscriptionName) {
		this.subscriptionName = subscriptionName;
	}
	public int getAgeGroupId() {
		return ageGroupId;
	}
	public void setAgeGroupId(int ageGroupId) {
		this.ageGroupId = ageGroupId;
	}
	public String getAgeGroupName() {
		return ageGroupName;
	}
	public void setAgeGroupName(String ageGroupName) {
		this.ageGroupName = ageGroupName;
	}
	
}

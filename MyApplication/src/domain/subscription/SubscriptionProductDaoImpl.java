package domain.subscription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import db.DbManager;
import domain.agegroup.AgeGroup;

public class SubscriptionProductDaoImpl implements SubscriptionProductDao{

	static Connection conn;
	static PreparedStatement ps;
	DbManager db = new DbManager();
	
	@Override
	public List<Subscription> fetchPredefinedSubscriptions() {
		
		List<Subscription> predefinedSubscriptions = new ArrayList<Subscription>();
		
		try{
			conn = db.getConnection();
			ps = conn.prepareStatement("SELECT s.id, s.name, s.age_group, spm.quantity, p.id, p.name, p.brand, p.category, p.quantity, p.price FROM subscription s JOIN subscription_product_mapping spm ON s.id = spm.subscription_id JOIN product p ON p.id = spm.product_id WHERE s.created_by=1 ORDER BY s.id ASC");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int currentSubscriptionId = rs.getInt(1);
				
				AgeGroup ageGroup = new AgeGroup();
				ageGroup.setId(rs.getInt(3));
				
				Subscription subscription = new Subscription();
				subscription.setId(currentSubscriptionId);
				subscription.setName(rs.getString(2));
				subscription.setAgeGroup(ageGroup);
				
				List<Product> productLists = new ArrayList<Product>();
				
				Product p = new Product();
				p.setId(rs.getInt(5));
				p.setName(rs.getString(6));
				p.setBrand(rs.getString(7));
				p.setCategory(rs.getString(8));
				p.setQuantity(rs.getString(9));
				p.setAmount(rs.getInt(4));
				p.setPrice(rs.getDouble(10));
				productLists.add(p);
				
				while(rs.next()) {
					if(currentSubscriptionId == rs.getInt(1)) {
						Product product = new Product();
						product.setId(rs.getInt(5));
						product.setName(rs.getString(6));
						product.setBrand(rs.getString(7));
						product.setCategory(rs.getString(8));
						product.setQuantity(rs.getString(9));
						product.setAmount(rs.getInt(4));
						product.setPrice(rs.getDouble(10));
						productLists.add(product);
					}
					else {
						break;
					}
				}
				
				subscription.setProducts(productLists);
				predefinedSubscriptions.add(subscription);
				rs.previous();
			}
			
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
		return predefinedSubscriptions;
	}

	@Override
	public List<Subscription> getSubscriptionInfoById(String subscriptionIds) {
		
		List<Subscription> subscriptionList = new ArrayList<Subscription>();
		try{
			conn = db.getConnection();
			ps = conn.prepareStatement("SELECT s.id, s.name, s.age_group, spm.quantity, p.id, p.name, p.brand, p.category, p.quantity, p.price, ag.name, ag.description FROM subscription s JOIN subscription_product_mapping spm ON s.id = spm.subscription_id JOIN product p ON p.id = spm.product_id JOIN age_group ag ON ag.id = s.age_group WHERE s.id IN (" + subscriptionIds + ") ORDER BY FIELD(s.id," + subscriptionIds + ")");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int currentSubscriptionMappingId = rs.getInt(1);
				
				AgeGroup ageGroup = new AgeGroup();
				ageGroup.setId(rs.getInt(3));
				ageGroup.setName(rs.getString(11));
				ageGroup.setDescription(rs.getString(12));
				
				Subscription subscription = new Subscription();
				subscription.setId(rs.getInt(1));
				subscription.setName(rs.getString(2));
				subscription.setAgeGroup(ageGroup);
				
				List<Product> productLists = new ArrayList<Product>();
				
				Product p = new Product();
				p.setId(rs.getInt(5));
				p.setName(rs.getString(6));
				p.setBrand(rs.getString(7));
				p.setCategory(rs.getString(8));
				p.setQuantity(rs.getString(9));
				p.setAmount(rs.getInt(4));
				p.setPrice(rs.getDouble(10));
				productLists.add(p);
				
				while(rs.next()) {
					if(currentSubscriptionMappingId == rs.getInt(1)) {
						Product product = new Product();
						product.setId(rs.getInt(5));
						product.setName(rs.getString(6));
						product.setBrand(rs.getString(7));
						product.setCategory(rs.getString(8));
						product.setQuantity(rs.getString(9));
						product.setAmount(rs.getInt(4));
						product.setPrice(rs.getDouble(10));
						productLists.add(product);
					}
					else {
						break;
					}
				}
				
				subscription.setProducts(productLists);
				subscriptionList.add(subscription);
				rs.previous();
			}
			
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
		return subscriptionList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ArrayList<Integer> saveCustomerSubscriptions(JsonArray subscriptionDetails) throws MySQLIntegrityConstraintViolationException {
		
		ArrayList<Integer> generatedIds = new ArrayList<Integer>();
		
		for(JsonElement subDetail: subscriptionDetails) {
			JsonObject subDetailObj = subDetail.getAsJsonObject();
			
			try {
				conn = db.getConnection();
				ps = conn.prepareStatement("INSERT INTO customer_subscription_mapping (`customer_id`,`subscription_id`,`frequency`, `quantity`, `duration`, `start_date`,`status`) VALUES(?,?,?,?,?,?,1)", Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, subDetailObj.get("customer_id").getAsInt());
				ps.setInt(2, subDetailObj.get("subscription_id").getAsInt());
				ps.setString(3, subDetailObj.get("frequency").getAsString());
				ps.setInt(4, subDetailObj.get("quantity").getAsInt());
				ps.setInt(5, subDetailObj.get("duration").getAsInt());
				String[] date = subDetailObj.get("start_date").getAsString().split("/");
				ps.setTimestamp(6, new Timestamp(Integer.parseInt(date[2]) - 1900, Integer.parseInt(date[0]), Integer.parseInt(date[1]), 0, 0, 0, 0));
				ps.executeUpdate();
				
				ResultSet rs = ps.getGeneratedKeys();
				while(rs.next()) {
					generatedIds.add(rs.getInt(1));
				}
				
				conn.close();
			}
			catch(MySQLIntegrityConstraintViolationException e){
				throw e;
			}
			catch(Exception e){
				System.out.println(e);
			}
		}
		
		return generatedIds;
	}

	@Override
	public List<CustomerSubscriptionMap> fetchActiveSubscriptions(int userId) {
		
		List<CustomerSubscriptionMap> subList = new ArrayList<CustomerSubscriptionMap>();
		
		try{
			conn = db.getConnection();
			ps = conn.prepareStatement("SELECT csm.id, csm.frequency, csm.quantity, csm.duration, csm.start_date, s.age_group, ag.name, s.id, s.name, p.id, p.name, p.brand, p.category, p.quantity, p.price, spm.quantity FROM customer_subscription_mapping csm JOIN subscription s ON csm.subscription_id = s.id JOIN subscription_product_mapping spm ON spm.subscription_id = s.id JOIN product p ON p.id = spm.product_id JOIN age_group ag ON ag.id = s.age_group WHERE csm.customer_id = ? AND csm.status = 1 ORDER BY csm.start_date DESC, csm.id DESC");
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int currentSubscriptionMappingId = rs.getInt(1);
				
				AgeGroup ageGroup = new AgeGroup();
				ageGroup.setId(rs.getInt(6));
				ageGroup.setName(rs.getString(7));
				
				Subscription subscription = new Subscription();
				subscription.setId(rs.getInt(8));
				subscription.setName(rs.getString(9));
				subscription.setAgeGroup(ageGroup);
				
				CustomerSubscriptionMap custSubMap = new CustomerSubscriptionMap();
				custSubMap.setId(rs.getInt(1));
				custSubMap.setQuantity(rs.getInt(3));
				custSubMap.setFrequency(rs.getString(2));
				custSubMap.setDuration(rs.getInt(4));
				custSubMap.setStartDate(rs.getDate(5));
				custSubMap.setSubscription(subscription);
				
				List<Product> productLists = new ArrayList<Product>();
				
				Product p = new Product();
				p.setId(rs.getInt(10));
				p.setName(rs.getString(11));
				p.setBrand(rs.getString(12));
				p.setCategory(rs.getString(13));
				p.setQuantity(rs.getString(14));
				p.setAmount(rs.getInt(16));
				p.setPrice(rs.getDouble(15));
				productLists.add(p);
				
				while(rs.next()) {
					if(currentSubscriptionMappingId == rs.getInt(1)) {
						Product product = new Product();
						product.setId(rs.getInt(10));
						product.setName(rs.getString(11));
						product.setBrand(rs.getString(12));
						product.setCategory(rs.getString(13));
						product.setQuantity(rs.getString(14));
						product.setAmount(rs.getInt(16));
						product.setPrice(rs.getDouble(15));
						productLists.add(product);
					}
					else {
						break;
					}
				}
				
				subscription.setProducts(productLists);
				subList.add(custSubMap);
				rs.previous();
			}
			
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
		return subList;
	}

	@Override
	public int cancelSubscription(int custSubId) {
		
		int status = 0;
		
		try {
			conn = db.getConnection();
			ps = conn.prepareStatement("UPDATE customer_subscription_mapping SET status = 0 WHERE id = ?");
			ps.setInt(1, custSubId);
			status = ps.executeUpdate();
			
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
		return status;
	}
	
}

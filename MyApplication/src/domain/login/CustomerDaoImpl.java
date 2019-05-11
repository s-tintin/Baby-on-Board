package domain.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import db.DbManager;



public class CustomerDaoImpl implements CustomerDao {

	static Connection conn;
	static PreparedStatement ps;
	DbManager db = new DbManager();
	
	@Override
	public int register(Customer c) throws MySQLIntegrityConstraintViolationException {
		int status = 0;
		try{
			conn = db.getConnection();
			ps = conn.prepareStatement("INSERT INTO customer(user_name,password,full_name,email,phone) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, c.getUsername());
			ps.setString(2, c.getPassword());
			ps.setString(3, c.getFullName());
			ps.setString(4, c.getEmail());
			ps.setString(5, c.getPhone());
			status = ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				status = rs.getInt(1);
			}
			
			conn.close();
		}
		catch(MySQLIntegrityConstraintViolationException e){
			throw e;
		}
		catch(Exception e){
			System.out.println(e);
		}
		return status;
	}

	@Override
	public Customer validateCustomer(Login login) {
		Customer c = new Customer();
		try{
			conn = db.getConnection();
			ps = conn.prepareStatement("SELECT id, user_name, password, full_name, email, phone FROM customer WHERE user_name=?");
			ps.setString(1, login.getUsername());

			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				c.setId(rs.getInt(1));
				c.setUsername(rs.getString(2));
				c.setPassword(rs.getString(3));
				c.setFullName(rs.getString(4));
				c.setEmail(rs.getString(5));
				c.setPhone(rs.getString(6));
			}
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
		return c;
	}

	@Override
	public int deleteCustomer(String userName) {
		int status = 0;
		try{
			conn = db.getConnection();
			ps = conn.prepareStatement("DELETE FROM customer WHERE user_name = ?");
			ps.setString(1, userName);
			status = ps.executeUpdate();
			conn.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
		return status;
	}

	@Override
	public int updateCustomer(JsonObject user) {
		int status = 0;
		try{
			conn = db.getConnection();
			ps = conn.prepareStatement("UPDATE customer SET password = ?, full_name = ?, email = ?, phone = ? WHERE id = ?");
			ps.setString(1, user.get("password").getAsString());
			ps.setString(2, user.get("fullname").getAsString());
			ps.setString(3, user.get("email").getAsString());
			ps.setString(4, user.get("phone").getAsString());
			ps.setInt(5, user.get("id").getAsInt());
			status = ps.executeUpdate();
			conn.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
		return status;
	}
	
	@Override
	public int getUserIdByUsername(String username) {
		int id = 0;
		try{
			conn = db.getConnection();
			ps = conn.prepareStatement("SELECT id FROM customer WHERE user_name = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			//status = ps.executeUpdate();
			while(rs.next()) {
				id = rs.getInt(1);
			}
			conn.close();
			
		}
		catch(Exception e){
			
			System.out.println(e);
		}
		return id;
	
	}

}

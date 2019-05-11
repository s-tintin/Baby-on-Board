package domain.agegroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.DbManager;

public class AgeGroupDaoImpl implements AgeGroupDao {
	
	static Connection conn;
	static PreparedStatement ps;
	DbManager db = new DbManager();
	
	public List<AgeGroup> fetchAgeGroups(){
		
		List<AgeGroup> ageGroups = new ArrayList<AgeGroup>();
		
		try {
			conn = db.getConnection();
			ps = conn.prepareStatement("SELECT id, name FROM age_group");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				AgeGroup ageGroup = new AgeGroup();
				ageGroup.setId(rs.getInt(1));
				ageGroup.setName(rs.getString(2));
				ageGroups.add(ageGroup);
			}
			
			conn.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
		return ageGroups;
	}
	
}

package domain.agegroup;

import java.util.List;

/**
 * 
 * @author karthik
 * These methods are used to access and modify age group data from the database 
 */
public interface AgeGroupDao {
	
	/*
	 * Retrieves age groups from the database 
	 */
	public List<AgeGroup> fetchAgeGroups();
}

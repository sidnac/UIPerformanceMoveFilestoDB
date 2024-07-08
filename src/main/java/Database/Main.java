package Database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		//TestResultsTable.createTable();
		// get results
		
		String query =  "Select * from test_results";
		DatabaseConnection dbConnect = new DatabaseConnectionSQlite();
		
		
		try (ResultSet rs=  dbConnect.connect().createStatement().executeQuery(query)) {
			
			System.out.println("Reading......");
			while(rs.next()) {
				
				System.out.println(rs.getString("date"));
				
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	
	
	}

}

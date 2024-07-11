package Query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Database.DatabaseConnectionMariaDB;

public class Query {
	
	public static ResultSet getQuery() {
		Connection conn = new DatabaseConnectionMariaDB().connect();
		String query = """
				 select test_scope, AVG(load_time),MAX(load_time),MIN(load_time) from ui_test_results utr
				 where 
				 date = current_date()-1 and
				 test_scope in ("ADD_INF_TOTAL","EDIT_INF_TOTAL","DSC","ODIF TABLE","PIVOT TABLE","SCHEDULE_CHANGE", "HIGH LEVEL HISTORICAL FORECAST: LOAD CHART")
				 group by 
				 test_scope ,
				 date
				""";

		try (Statement statement = conn.createStatement()){

			return statement.executeQuery(query);
	
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

		
		
	}

}

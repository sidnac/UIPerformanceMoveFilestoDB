package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import CSVReader.TestResults;
import General.ConfigReader;

public class DatabaseConnectionMariaDB implements DatabaseConnection{
private static final String MARIADB_URL = ConfigReader.properties.getProperty("MARIADB_URL");
private static final String USER = ConfigReader.properties.getProperty("MARIADB_USER");
private static final String PASSWORD = ConfigReader.properties.getProperty("MARIADB_PASSWORD");

@Override
public Connection connect() {
    Connection conn = null;
    try {
    	try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	conn = DriverManager.getConnection(MARIADB_URL, USER, PASSWORD);
        System.out.println("Connection to MariaDB has been established.");
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return conn;
}

@Override
public void close(Connection conn) {
    if (conn != null) {
        try {
            conn.close();
            System.out.println("Connection to MariaDB has been closed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
//DATE_FORMAT(date, '%W')
@Override
public  void insertData(TestResults testResults) {
	 String sql = "INSERT INTO ui_test_results(test_scope, date, time, env, description, load_time, version,weekday) VALUES(?,  STR_TO_DATE(?, '%m/%d/%Y'), ?, ?, ?, ?, ?, DATE_FORMAT(STR_TO_DATE(?, '%m/%d/%Y'), '%W'))";

     try (Connection conn = connect();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setString(1, testResults.getTestScope());
         pstmt.setString(2, testResults.getDate().toString()); // Ensure this returns a suitable date format for MariaDB
         pstmt.setString(3, testResults.getTime());
         pstmt.setString(4, testResults.getEnv());
         pstmt.setString(5, testResults.getDescription());
         pstmt.setInt(6, testResults.getTimeTaken());
         pstmt.setString(7, testResults.getVersion());
         pstmt.setString(8, testResults.getDate().toString());
         pstmt.executeUpdate();
         System.out.println("Data inserted into 'test_results' table.");
     } catch (SQLException e) {
         System.out.println(e.getMessage());
     }
 
}

@Override
public void createTable() {
    String sql = "CREATE TABLE IF NOT EXISTS ui_test_results ("
            + "id INT PRIMARY KEY AUTO_INCREMENT,"
            + "test_scope VARCHAR(255) NOT NULL,"
            + "date DATE NOT NULL,"
            + "time TIME NOT NULL,"
            + "env VARCHAR(255) NOT NULL,"
            + "description TEXT,"
            + "load_time INT NOT NULL,"
            + "version VARCHAR(255),"
            +"weekday VARCHAR(255)"
            + ");";

 try (Connection conn = connect();
      Statement stmt = conn.createStatement()) {
     stmt.execute(sql);
     System.out.println("Table 'test_results' created or already exists.");
 } catch (SQLException e) {
     System.out.println(e.getMessage());
 }
}



}

package CSVReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Database.DatabaseConnection;
import Database.DatabaseConnectionMariaDB;
import Database.DatabaseConnectionSQlite;

public class TestResults {

	// TEST_SCOPE,DATE,TIME,ENV,DESCRIPTION,TEST_RESULT

	private String testScope;
	private String date;
	private String time;
	private String env;
	private String description;
	private String version;
	private int timeTaken;
	
	

	public TestResults(String testScope, String date, String time, String env, String description, int timeTaken, String version) {
		super();
		this.testScope = testScope;
		this.date = date;
		this.time = time;
		this.env = env;
		this.description = description;
		this.timeTaken = timeTaken;
		this.setVersion(version);
	}
	
	
	public static Date parseDate(String dateStr) {
        
		String[] dateFormats = { "MM/dd/yyyy", "yyyy-MM-dd" }; // Add more formats as needed
        for (String format : dateFormats) {
            try {
                return new SimpleDateFormat(format).parse(dateStr);
            } catch (ParseException e) {
                // Continue to the next format
            }
        }
        // Log an error or throw an exception if all formats fail
        System.err.println("Unparseable date: " + dateStr);
        return null;
    }
	
	
	
	// Getters Setters

	public String getTestScope() {
		return testScope;
	}

	public void setTestScope(String testScope) {
		this.testScope = testScope;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(int timeTaken) {
		this.timeTaken = timeTaken;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public static void insertData(TestResults testResults) {
		CSVReader.getConnectDB().insertData(testResults);
	}


	public static void createTable() {
		CSVReader.getConnectDB().createTable();
	}
	
	
	

}

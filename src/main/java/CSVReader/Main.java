package CSVReader;

import Database.DatabaseConnection;
import Database.DatabaseConnectionMariaDB;
import Database.DatabaseConnectionSQlite;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Step 1: mention csv files to read
		// Step 2: read file and store them into TestResults objects
		// Step 3: Write them to SQLite database
		// Step 4: Write them to MariaDB
		DatabaseConnection connectionSQlite = new DatabaseConnectionSQlite();		
		DatabaseConnection connectionMariaDB = new DatabaseConnectionMariaDB();		
		
		// first SQlite
		CSVReader.setConnectDB(connectionSQlite);
		CSVReader.csvReadandWriteDB();
		
		
		// second MariaDB
		CSVReader.setConnectDB(connectionMariaDB);
		CSVReader.csvReadandWriteDB();
		

		
		// Delete the files in the directory		
		CSVReader.removeFiles();
		
		
		
		
		
		
	}

	

}

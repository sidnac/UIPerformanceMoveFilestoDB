package Database;

import java.sql.Connection;

import CSVReader.TestResults;

public interface DatabaseConnection {

	Connection connect();

	void close(Connection conn);

	void insertData(TestResults testResults);

	void createTable();

}

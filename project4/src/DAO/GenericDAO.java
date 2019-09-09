package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GenericDAO {
	
    private static String dbUser = "root";
    private static String dbPassword = "fantasy";
    private static String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
    Connection connection;
    
	public GenericDAO() throws SQLException {
		connection = DriverManager.getConnection(loginUrl, dbUser, dbPassword);
	}
	
	public void close() throws SQLException {
		if (connection != null)
			connection.close();
	}
}

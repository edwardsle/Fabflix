package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.annotation.Resource;
import javax.sql.DataSource;

public class GenreDAO extends GenericDAO{

	public GenreDAO() throws SQLException {
		super();
	}

	public void close() throws SQLException {
		if (connection != null)
			connection.close();
	}
	
	public int getNextUnsedID() throws SQLException {
		PreparedStatement stmGetMaxID = connection.prepareStatement("SELECT max(id) as max from genres");

		ResultSet rs = stmGetMaxID.executeQuery();
		stmGetMaxID.close();
		
		int id = -1;
		if (rs.next()) {
			String maxID = rs.getString("max");
			id = Integer.parseInt(maxID);
		} 
		
		return id;
	}

	public int insert(String name) throws SQLException {
		int id = getNextUnsedID();		

		PreparedStatement statement = connection
				.prepareStatement("INSERT INTO genres (id, name) VALUES (?, ?)");

		statement.setInt(1, id);
		statement.setString(2, name);

		int count = statement.executeUpdate();
		statement.close();
		
		if (count > 0)
			return id;
		
		return -1;
	}
	
	public int findId(String name) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT id from genres WHERE name = ? LIMIT 1");
		statement.setString(1, name);

		ResultSet rs = statement.executeQuery();
		statement.close();
		
		int id = -1;
		if (rs.next()) {
			id = Integer.parseInt(rs.getString("id"));
		} 
		
		return id;
	}
	
	public boolean attachMovie(String movieID, int genreID) throws SQLException
	{
		PreparedStatement statement = connection
				.prepareStatement("INSERT INTO genres_in_movies (genreId, movieId) VALUES (?, ?)");
		
		statement.setInt(1, genreID);
		statement.setString(2, movieID);
		
		int count = statement.executeUpdate();
		statement.close();
		
		return (count > 0);
	}
}

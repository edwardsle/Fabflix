package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import Models.Movie;
import Models.Star;

public class MovieDAO extends GenericDAO{
	public MovieDAO() throws SQLException, NamingException {
		super();
	}

	public static String getNextID(String maxID) {
		// parse max ID
		String prefix = StringUtils.left(maxID, 2);
		int num = Integer.parseInt(StringUtils.mid(maxID, 2, maxID.length()));

		// get next ID
		String id = prefix + (++num);

		return id;
	}

	public String getNextUnsedID() throws SQLException {
		PreparedStatement stmGetMaxID = connection.prepareStatement("SELECT max(id) as max from movies");

		ResultSet rs = stmGetMaxID.executeQuery();
		stmGetMaxID.close();

		String id = null;
		if (rs.next()) {
			String maxID = rs.getString("max");
			id = getNextID(maxID);
		}

		return id;
	}

	public String findId(String title, int year, String director) throws SQLException {
		PreparedStatement stmGetCurrentRecord = connection
				.prepareStatement("SELECT * from movies where title = ? AND year = ? and director = ?");
		stmGetCurrentRecord.setString(1, title);
		stmGetCurrentRecord.setInt(2, year);
		stmGetCurrentRecord.setString(3, director);

		ResultSet rsCurrentRecord = stmGetCurrentRecord.executeQuery();
		stmGetCurrentRecord.close();

		if (rsCurrentRecord.next()) {
			return rsCurrentRecord.getString("id");
		}

		return null;
	}

	public boolean exists(String title, int year, String director) throws SQLException {
		return (findId(title, year, director) != null);
	}

	public String insert(String title, int year, String director) throws SQLException {

		String id = getNextUnsedID();

		PreparedStatement statement = connection
				.prepareStatement("INSERT INTO movies (id, title, year, director) VALUES (?, ?, ?, ?)");

		statement.setString(1, id);
		statement.setString(2, title);
		statement.setInt(3, year);
		statement.setString(4, director);

		int count = statement.executeUpdate();
		statement.close();

		if (count > 0)
			return id;

		return null;
	}

	public boolean addStar(String movieID, String name, Integer birthYear) throws SQLException {
		StarDAO starDAO = new StarDAO();

		String id = starDAO.findId(name, birthYear);

		if (id != null)
			return attachStar(movieID, id);
		else
			return attachStar(movieID, starDAO.insert(name, birthYear));
	}

	public boolean attachStar(String movieID, String starID) throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement("INSERT INTO stars_in_movies (starId, movieId) VALUES (?, ?)");

		statement.setString(1, starID);
		statement.setString(2, movieID);

		int count = statement.executeUpdate();
		statement.close();

		return (count > 0);
	}

	public int insertByStoredProcedure(String title, int year, String director, String starName, String genreName) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("CALL add_movie(?, ?, ?, ?, ?)");

		statement.setString(1, title);
		statement.setInt(2, year);
		statement.setString(3, director);
		statement.setString(4, starName);
		statement.setString(5, genreName);

		int count = statement.executeUpdate();
		statement.close();
		
		return count;
	}
	
	public void setAutoCommit(int val) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("set AUTOCOMMIT="+val);
		statement.execute();
		statement.close();
	}
	
	public void commit() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("commit");
		statement.execute();
		statement.close();
	}
}

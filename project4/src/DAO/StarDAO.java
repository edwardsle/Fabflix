package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import Models.Star;

public class StarDAO extends GenericDAO {
	
	public StarDAO() throws SQLException {
		super();
	}
	
	public String getNextID(String maxID)
	{
		// parse max ID
		String prefix = StringUtils.left(maxID, 2);
		int num = Integer.parseInt(StringUtils.mid(maxID, 2, maxID.length()));
		
		// get next ID
		String id = prefix + (++num);
		
		return id;
	}
	
	public String getNextUnsedID() throws SQLException
	{
		PreparedStatement stmGetMaxID = connection.prepareStatement("SELECT max(id) as max from stars");

		ResultSet rs = stmGetMaxID.executeQuery();
		stmGetMaxID.close();
		
		String id = "";
		if (rs.next()) {
			String maxID = rs.getString("max");
			id = getNextID(maxID);
		} 
		
		return id;
	}
	
	public String findId(String name, int birthYear) throws SQLException
	{
		PreparedStatement stmGetCurrentRecord = connection
				.prepareStatement("SELECT * from stars where name = ? AND birthYear = ?");
		stmGetCurrentRecord.setString(1, name);
		stmGetCurrentRecord.setInt(2, birthYear);
		
		ResultSet rsCurrentRecord = stmGetCurrentRecord.executeQuery();
		stmGetCurrentRecord.close();
		
		if (rsCurrentRecord.next())
		{
			return rsCurrentRecord.getString("id");
		}
		
		return null;
	}
	

	public String insert(String name, Integer birthYear) throws SQLException {
		String id = getNextUnsedID();

		PreparedStatement statement = connection
				.prepareStatement("INSERT INTO stars (id, name, birthYear) VALUES (?, ?, ?)");

		statement.setString(1, id);
		statement.setString(2, name);
		if (birthYear != null)
			statement.setInt(3, birthYear);
		else
			statement.setNull(3, Types.NULL);

		int count = statement.executeUpdate();
		statement.close();
		
		if (count > 0)
			return id;
		
		return null;
	}
}

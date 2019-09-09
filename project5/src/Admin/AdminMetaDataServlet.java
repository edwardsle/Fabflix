package Admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Helpers.RequestHelper;
import Helpers.ResponseHelper;
import Models.Star;

/**
 * Servlet implementation class AdminStarServlet
 */
@WebServlet("/admin/api/metadata")
public class AdminMetaDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;     
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminMetaDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Object> result = new ArrayList<Object>();
		
		ArrayList<String> list = getAllTableNames();
		if (list != null)
		{
			for (String tableName : list)
			{
				TreeMap<String, Object> table = new TreeMap<String, Object>();
				table.put("name", tableName);
				
				ArrayList<Object> rows = getMetaData(tableName);
				if (rows != null) {
					TreeMap<String, Object> map = new TreeMap<String, Object>();
					map.put(tableName, rows);
				}
				
				table.put("rows", rows);
				
				result.add(table);
			}
		}
		
		if (result.isEmpty())
		{
			ResponseHelper.sendMessageAsJson(false, "Unable to get data from database", response);
			return;
		}
		ResponseHelper.sendMessageAsJson(true, "success", result, response);
	}
	
	private ArrayList<String> getAllTableNames() {
		try {
			Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");
            Connection dbcon = ds.getConnection();
            
			//Connection connection = dataSource.getConnection();
						
			PreparedStatement statement = dbcon
					.prepareStatement("SHOW TABLES");
			
			ResultSet rs = statement.executeQuery();
			
			ArrayList<String> list = new ArrayList<String>();
			while (rs.next())
			{
				list.add(rs.getString(1));
			}
			
			rs.close();
			statement.close();
			dbcon.close();
			
			return list;
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	private ArrayList<Object> getMetaData(String tableName) {
		if (tableName == null || tableName.isEmpty())
			return null;
		
		try {
			Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");
            Connection dbcon = ds.getConnection();
			//Connection connection = dataSource.getConnection();
						
			PreparedStatement statement = dbcon
					.prepareStatement("DESCRIBE " + tableName);
			
			ResultSet rs = statement.executeQuery();
			
			ArrayList<Object> list = new ArrayList<Object>();
			while (rs.next())
			{
				TreeMap<String, String> map = new TreeMap<String, String>();
				map.put("field", rs.getString("field"));
				map.put("type", rs.getString("type"));
				map.put("null", rs.getString("null"));
				map.put("key", rs.getString("key"));
				map.put("default", rs.getString("default"));
				map.put("extra", rs.getString("extra"));
				list.add(map);
			}
			
			rs.close();
			statement.close();
			dbcon.close();		
			
			return list;
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//ResponseHelper.sendMessageAsJson(false, e.getMessage() , response);	
		}
		
		return null;
}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}

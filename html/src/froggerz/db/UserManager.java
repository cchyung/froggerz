package froggerz.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

// Class for handling database - user interactions
public class UserManager
{
	// List of users in the current game
	private List<User> mUsers;

	/**
	 * Static function to authenticate a user attempting to login to the game
	 * 
	 * @param username
	 * @param password
	 * @return true iff username and password match an entry in the database,
	 *         false otherwise
	 */
	public static boolean authenticate(String username, String password)
	{
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		Boolean valid = false;
		String dbPassword = null;

		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/Froggerz?user=root&password=root&useSSL=false");
			st = conn.createStatement();
			rs = st.executeQuery(
					"SELECT * from Users where username='" + username + "'");

			if (rs.next())
			{
				dbPassword = rs.getString("password");
				valid = (dbPassword.equals(password));
			}

		}
		catch (SQLException sqle)
		{
			System.out.println(sqle.getMessage());
		}
		catch (ClassNotFoundException cnfe)
		{
			System.out.println(cnfe.getMessage());
		}
		finally
		{
			try
			{
				if (rs != null)
				{
					rs.close();
				}
				if (st != null)
				{
					st.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (SQLException sqle)
			{
				System.out.println(sqle.getMessage());
			}
		}
		return valid;
	}

	/**
	 * Static function to insert a newly registered user into the database
	 * @param username
	 * @param password
	 * @return true iff inserted successfully, false otherwise (ex: username
	 *         taken)
	 */
	public static Boolean signup(String username, String password)
	{
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		Boolean success = false;

		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/Froggerz?user=root&password=root&useSSL=false");
			st = conn.createStatement();

			// Find the current highest id
			rs = st.executeQuery("SELECT MAX(id) as lastID FROM Users");
			int lastID = 0;
			if (rs.next())
			{
				lastID = rs.getInt("lastID");
				++lastID;
			}

			// VALUES: (id,username,password,wins)
			String sql = "INSERT INTO Users VALUES " + "(" + lastID + ",'"
					+ username + "','" + password + "', 0)";
			st.executeUpdate(sql);
			success = true;

		}
		catch (Exception e)
		{
			System.out.println("Error inserting user into database: " + e.getMessage());
			success = false;
		}
		finally
		{
			try
			{
				if (rs != null)
				{
					rs.close();
				}
				if (st != null)
				{
					st.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (SQLException sqle)
			{
				System.out.println(sqle.getMessage());
			}
		}
		return success;
	}
	
	/**
	 * Static function to update the wins entry in the databse
	 * at the conclusion of a game
	 * @param username
	 */
	public static void onWin(String username)
	{
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/Froggerz?user=root&password=root&useSSL=false");
			st = conn.createStatement();

			// Find the number of wins the user currently has
			int lastNumWins = 0;
			rs = st.executeQuery("SELECT MAX(wins) as lastNumWins FROM Users");
			if (rs.next())
			{
				lastNumWins = rs.getInt("lastNumWins");
			}
			
			// Update the number of wins
			++lastNumWins;
			st.executeUpdate("UPDATE Users SET wins=" + lastNumWins + " WHERE username='" + username + "'");
		}
		catch (Exception e)
		{
			System.out.println("Error updating wins: " + e.getMessage());
		}
		finally
		{
			try
			{
				if (rs != null)
				{
					rs.close();
				}
				if (st != null)
				{
					st.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			}
			catch (SQLException sqle)
			{
				System.out.println(sqle.getMessage());
			}
		}
	}
}

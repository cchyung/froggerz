package froggerz.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

public class UserManager {
	// List of users in the current game
	private List<User> mUsers;

	/**
	 * Static function to authenticate a user attempting to login to the game
	 * 
	 * @param username
	 * @param password
	 * @return true iff username and password match an entry in the database, false
	 *         otherwise
	 */
	public static User authenticate(String username, String password) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		Boolean valid = false;
		String dbPassword = null;
		User out = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Froggerz?user=root&password=root&useSSL=false");
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * from Users where username='" + username + "'");

			if (rs.next()) {
				dbPassword = rs.getString("password");
				valid = (dbPassword.equals(password));
				if (valid) {
					int numWins = rs.getInt("wins");
					out = new User(username, password, numWins);
				}
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		return out;
	}

	/**
	 * Static function to retrieve the position and wins of every user
	 * 
	 * @return the list of strings of necessary info
	 */
	public static ArrayList<String[]> getLeaderboard() {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<String[]> out = new ArrayList<String[]>();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Froggerz?user=root&password=root&useSSL=false");
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * from Rankings;");

			int count = 1;
			while (rs.next()) {
				Statement st2 = conn.createStatement();

				int id = rs.getInt("user_id");

				ResultSet rs2 = st2.executeQuery("SELECT * from Users where id=" + id + ";");
				
				rs2.next();
				
				String[] temp = new String[3];
				temp[0] = "" + count++;
				temp[1] = rs2.getString("username");
				temp[2] = "" + rs2.getInt("wins");
				out.add(temp);
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		return out;
	}

	/**
	 * Static function to insert a newly registered user into the database
	 * 
	 * @param username
	 * @param password
	 * @return true iff inserted successfully, false otherwise (ex: username taken)
	 */
	public static User signup(String username, String password) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		User out = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Froggerz?user=root&password=root&useSSL=false");
			st = conn.createStatement();

			// Find the current highest id
			rs = st.executeQuery("SELECT MAX(id) as lastID FROM Users");
			int lastID = 0;
			if (rs.next()) {
				lastID = rs.getInt("lastID");
				++lastID;
			}

			// VALUES: (id,username,password,wins)
			String sql = "INSERT INTO Users VALUES " + "(" + lastID + ",'" + username + "','" + password + "', 0)";
			st.executeUpdate(sql);
			out = new User(username, password, 0);

		} catch (Exception e) {
			System.out.println("Error inserting user into database: " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		return out;
	}
}

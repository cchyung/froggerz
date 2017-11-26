package froggerz.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Register")
public class RegisterUser extends HttpServlet {
	public static final long serialVersionUID = 2;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final String inputtedName = request.getParameter("username");
		final String inputtedPassword = request.getParameter("password");
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		boolean empty = true;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost/LoginInformation?user=root&password=root&useSSL=false");
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * from Users where username='" + inputtedName + "'");

			while (rs.next()) {
				empty = false;
			}
			request.setAttribute("username", inputtedName);
			request.setAttribute("loginSuccess", !empty);
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

		if (empty) {
			request.getRequestDispatcher("/userInfo.jsp").forward(request, response);
		}
		else {
			request.setAttribute("message", "Username is already in use");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}

		// RequestDispatcher dispatcher =
		// getServletContext().getRequestDispatcher("/test.jsp");
		// dispatcher.forward(request, response);
	}
}
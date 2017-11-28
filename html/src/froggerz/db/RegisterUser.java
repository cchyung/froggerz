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
public class RegisterUser extends HttpServlet
{
	public static final long serialVersionUID = 2;

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		final String inputtedName = request.getParameter("username");
		final String inputtedPassword = request.getParameter("password");
		User user = UserManager.signup(inputtedName, inputtedPassword);

		if (user != null)
		{
			request.setAttribute("user",user);
			request.getRequestDispatcher("/userInfo.jsp").forward(request,
					response);
		}
		else 
		{
			request.setAttribute("message", "Username is already in use");
			request.getRequestDispatcher("/index.jsp").forward(request,
					response);
		}
	}
}
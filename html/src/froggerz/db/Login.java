package froggerz.db;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Login")
public class Login extends HttpServlet
{
	public static final long serialVersionUID = 1;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		final String username = request.getParameter("username");
		final String password= request.getParameter("password");
		Boolean success = UserManager.authenticate(username, password);
		
		// TODO success will be true iff user was authenticated

		request.getRequestDispatcher("/userInfo.jsp").forward(request, response);
	}
}
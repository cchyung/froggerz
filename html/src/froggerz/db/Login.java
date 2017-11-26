package froggerz.db;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Login")
public class Login extends HttpServlet {
	public static final long serialVersionUID = 1;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final String username = request.getParameter("username");
		final String password = request.getParameter("password");
		User user = UserManager.authenticate(username, password);
		request.setAttribute("user", user);

		if (user == null) {
			request.setAttribute("message", "Invalid Username and password");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/userInfo.jsp").forward(request, response);
		}
	}
}
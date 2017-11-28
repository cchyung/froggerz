package froggerz.db;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@WebServlet("/GetLeaderboard")
public class GetLeaderboard extends HttpServlet {
	public static final long serialVersionUID = 1;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<String[]> out = UserManager.getLeaderboard();
		request.setAttribute("users", out);
		request.getRequestDispatcher("/leaderboard.jsp").forward(request, response);
	}
}
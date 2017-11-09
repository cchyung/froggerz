<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<html>
	<head>
		<title>Login Information</title>
	</head>
	<body>
<%
  		String username = (String)request.getAttribute("username");
		Boolean loginSuccess = (Boolean)request.getAttribute("loginSuccess");

		if (loginSuccess)
		{
%>
			Success! User <%=username%> logged in!
<%
		}
		else /* Login failed */
		{
%>
			Failed! No such username / password combination in database.
<%
		}
%>
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>FROGGERZ Leaderboard</title>
<link rel="stylesheet" type="text/css" href="css/userInfo.css">
<link rel="stylesheet" type="text/css"
	href="bootstrap/bootstrap.min.css">
</head>

<body>
	<div class="container-fluid" id="leaderboard">
		<c:forEach items="${users}" var="entry" varStatus="loop">
			<div class="row">
				<div class="col-xs-4">
					${entry[0]}
				</div>
				<div class="col-xs-4">
					${entry[1]}
				</div>
				<div class="col-xs-4">
					${entry[2]}
				</div>
			</div>
		</c:forEach>
	</div>
	<div class="container-fluid" id="buttons">

		<div id="buttons" class="row">
			<div class="col-xs-2"></div>
			<div class="col-xs-4">
				<button class="saveAndQuit" onclick="changePage(1)">Quit</button>
			</div>
			<div class="col-xs-2"></div>
			<div class="col-xs-4">
				<button class="joinGame" onclick="changePage(2)">Join Game</button>
			</div>
			<div class="col-xs-2"></div>
		</div>
	</div>
</body>
</html>
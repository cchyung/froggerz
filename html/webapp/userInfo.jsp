<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>${username}'sFROGGERZInfo</title>
<link rel="stylesheet" type="text/css" href="css/userInfo.css">
<link rel="stylesheet" type="text/css"
	href="bootstrap/bootstrap.min.css">
</head>

<body>
	<div class="container-fluid" id="user-table">
		<div id="nameAndScore" class="row">
			<div class="col-xs-3">Username:</div>
			<div class="col-xs-3">${user.getmUsername()}</div>
			<div class="col-xs-3">Wins:</div>
			<div class="col-xs-3">${user.getmWins()}</div>
		</div>
		<!-- <div id="chooseSkin" class="row">
			<div class="col-xs-3">Skins:</div>
			<c:choose>
				<c:when test="${user.getmWins()<3}">
					<div class="col-xs-3">
						1 - <img class="selected"
							src="https://static.boredpanda.com/blog/wp-content/uploads/2016/11/princess-leia-frog-snails-photoshop-battle-38.jpg">
					</div>
				</c:when>
				<c:otherwise>
					<div class="col-xs-3">
						1 - <img class="unlocked"
							src="https://static.boredpanda.com/blog/wp-content/uploads/2016/11/princess-leia-frog-snails-photoshop-battle-38.jpg">
					</div>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${user.getmWins()>=3}">
					<div class="col-xs-3">
						2 - <img class="unlocked"
							src="https://www.sciencenews.org/sites/default/files/2017/01/main/articles/013117_SM_frog-tongue_main_free.jpg">
					</div>
				</c:when>
				<c:when test="${user.getmWins()<10}">
					<div class="col-xs-3">
						2 - <img class="selected"
							src="https://www.sciencenews.org/sites/default/files/2017/01/main/articles/013117_SM_frog-tongue_main_free.jpg">
					</div>
				</c:when>
				<c:otherwise>
					<div class="col-xs-3">
						2 - <img class="locked"
							src="https://www.sciencenews.org/sites/default/files/2017/01/main/articles/013117_SM_frog-tongue_main_free.jpg">
					</div>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${user.getmWins()>=10}">
					<div class="col-xs-3">
						3 - <img class="selected"
							src="http://www.pngmart.com/files/1/Frog-Transparent-Background.png">
					</div>
				</c:when>
				<c:otherwise>
					<div class="col-xs-3">
						3 - <img class="locked"
							src="http://www.pngmart.com/files/1/Frog-Transparent-Background.png">
					</div>
				</c:otherwise>
			</c:choose>
		</div>
		 -->
		<div class="buttons" class="row">
			<div class="col-xs-3">
				<button class="saveAndQuit" onclick="changePage(1)">Quit</button>
			</div>

			<div class="col-xs-3">
				<button class="leaderboard" onclick="changePage(3)">Check
					Leaderboard</button>
			</div>
		</div>
	</div>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<script>
		function changePage(x) {
			if (x === 1) {
				document.location.href = "./index.jsp?message=";
			} else if (x === 2) {
				document.location.href = "./game.jsp";
			} else{
				document.location.href = "./leaderboard.jsp";
			}
		}
	</script>
</body>
</html>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>${username}'s FROGGERZ Info</title>
<link rel="stylesheet" type="text/css"  href="css/userInfo.scss">
<link rel="stylesheet" type="text/css"  href="css/userInfo.css">
<link rel="stylesheet" type="text/css"  href="bootstrap/bootstrap.min.css">
</head>

<body>
<div class="container-fluid" id="user-table">
    <div id="nameAndScore" class="row">
        <div class="col-xs-3">Username:</div>
        <div class="col-xs-3">${username}</div>
        <div class="col-xs-3">Wins:</div>
        <div class="col-xs-3">${wins}</div>
    </div>
    <div id="chooseSkin" class="row">
        <div class="col-xs-3">Skins:</div>
        <div class="col-xs-3">1 - <img class="locked"  src="https://static.boredpanda.com/blog/wp-content/uploads/2016/11/princess-leia-frog-snails-photoshop-battle-38.jpg"></div>
        <div class="col-xs-3">2 - <img class="selected"  src="https://www.sciencenews.org/sites/default/files/2017/01/main/articles/013117_SM_frog-tongue_main_free.jpg"></div>
        <div class="col-xs-3">3 - <img class="locked" src="http://www.pngmart.com/files/1/Frog-Transparent-Background.png"></div>
	</div>
    <div id="buttons" class="row">
        <button class="saveAndQuit" onclick="formSwitch()">Quit</button>
        <button class="saveAndPlay" onclick="formSwitch()">Join Game</button>
	</div>
</div>
</body>
</html>

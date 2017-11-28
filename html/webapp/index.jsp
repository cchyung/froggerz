<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>FROGGERZ: Log-In</title>
<link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>
<div class="login-page">
	${message}
    <div class="forms">
        <form action="Register" class="register-form">
            <input type="text" placeholder="name"/>
            <input type="password" placeholder="password"/>
            <input type="password" placeholder="confirm password"/>
            <input type="submit" value="Create Account"/>
        </form>
        <form class="login-form" method="POST" action="Login">
            <input type="text" name="username" placeholder="username"/>
            <input type="password" name="password"  placeholder="password"/>
            <input type="submit" value="Log-in"/>
        </form>
        <button class="signIn" onclick="formSwitch()">Already Registered? <br><b>Sign in</b></button>
        <button class="createAccount" onclick="formSwitch()">New User?<br><b>Create an Account</b></button>
        
    </div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script> 
<script>
		function formSwitch(){
			$('.register-form').toggle();
			$('.login-form').toggle();
			$('.signIn').toggle();
			$('.createAccount').toggle();
		}
	</script>
</body>
</html>

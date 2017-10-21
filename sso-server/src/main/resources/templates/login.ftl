<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Login</title>
</head>

<body>

<form action="login" method="post">
    <div class="form-group">
        <input name="userName" autofocus>
    </div>
    <div class="form-group">
        <input name="password" type="password" value="">
    </div>
    <input name="originUrl" style="display: none" value="${originUrl!''}" />
    <input type="submit" value="登录"/>
</form>

</body>

</html>

<!DOCTYPE html>
<html lang="ru">
<#import "authforms.ftl" as forms/>
<base href="/">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
</head>
<body>
<h2>Вход</h2>
<@forms.loginForm/>
<a href="/register">Регистрация</a>
<script src="js/login.js"></script>
</body>
</html>

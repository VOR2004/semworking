<!DOCTYPE html>
<html lang="ru">
<#import "authforms.ftl" as forms/>
<base href="/">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
</head>
<body>
<h2>Регистрация</h2>
<@forms.registerForm/>
<a href="/login">Вход</a>
<script src="js/register.js"></script>
</body>
</html>

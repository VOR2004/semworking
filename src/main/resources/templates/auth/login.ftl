<!DOCTYPE html>
<html lang="ru">
<#import "authforms.ftl" as forms/>
<base href="/">
<head>
    <link rel="stylesheet" href="css/login-register.css"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Audiowide&family=Coral+Pixels&family=DotGothic16&family=Doto:wght,ROND@100..900,100&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&family=Micro+5&family=Orbitron:wght@400..900&family=Press+Start+2P&family=Rampart+One&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Rubik:ital,wght@0,300..900;1,300..900&family=Silkscreen:wght@400;700&display=swap" rel="stylesheet">
    <meta charset="UTF-8">
    <title>Вход</title>
</head>
<body>
<div class="form-wrapper">
    <h2>Вход</h2>
    <@forms.loginForm/>
    <a href="/register" class="form-link">Регистрация</a>
</div>
<script src="js/login.js"></script>
</body>
</html>

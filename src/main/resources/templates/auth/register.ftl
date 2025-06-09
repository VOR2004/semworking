<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
</head>
<body>
<h2>Регистрация</h2>
<form method="post" action="/register">
    <label>Имя пользователя:
        <input type="text" name="username" value="${form.username!}">
    </label><br>
    <label>Email:
        <input type="email" name="email" value="${form.email!}">
    </label><br>
    <label>Пароль:
        <input type="password" name="password">
    </label><br>
    <label>Подтверждение пароля:
        <input type="password" name="confirmPassword">
    </label><br>
    <button type="submit">Зарегистрироваться</button>
</form>
</body>
</html>

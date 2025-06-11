<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Удаление товара</title>
</head>
<body>
<h2>Удалить товар по ID</h2>
<form method="post" action="/admin/delete-product">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <label>
        <input type="text" name="productId" placeholder="Введите ID товара" required>
    </label>
    <button type="submit">Удалить</button>
</form>
<#if error??>
    <p style="color:red;">${error}</p>
</#if>
</body>
</html>

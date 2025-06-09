<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мои товары</title>
</head>
<body>
<h2>Мои товары</h2>
<#list products as p>
    <div>
        <strong>${p.title}</strong><br>
        ${p.description}<br>
        ${p.price} руб.<br>
        <a href="/product/${p.id}">Открыть</a> |
        <a href="/product/edit/${p.id}">Редактировать</a>
        <p>
            <#if p.imageUrls?? && (p.imageUrls?size > 0)>
                <#list p.imageUrls as url>
                    <img src="${url}" alt="Фото товара" style="max-width:200px; margin:5px;">
                </#list>
            <#else>
                <span>Нет изображений</span>
            </#if>

        </p>
        <form method="post" action="/product/delete/${p.id}" style="display:inline;">
            <button type="submit">Удалить</button>
        </form>
    </div>
</#list>
</body>
</html>

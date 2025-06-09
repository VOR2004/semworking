<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${product.title}</title>
</head>
<body>
<#if product.mainImageUrl?? && (product.mainImageUrl?length > 0)>
    <img src="${product.mainImageUrl}" alt="Главное фото" style="max-width:120px;max-height:120px;margin:5px 0;">
</#if>
<h2>${product.title}</h2>
<p>${product.description}</p>
<p>Цена: ${product.price} руб.</p>
<p>Продавец: ${product.userEntity.username}</p>

<p>
    Теги:
    <#if product.tags?? && (product.tags?size > 0)>
        <#list product.tags as tag>
            <a href="/?tags=${tag.name}">${tag.name}</a><#if tag_has_next>, </#if>
        </#list>
    <#else>
        нет тегов
    </#if>
</p>

<p>
    <#if product.imageUrls?? && (product.imageUrls?size > 0)>
        <#list product.imageUrls as url>
            <img src="${url}" alt="Фото товара" style="max-width:200px; margin:5px;">
        </#list>
    <#else>
        <span>Нет изображений</span>
    </#if>
</p>


<form method="post" action="/chat/start/${product.userEntity.id}">
    <button type="submit">Написать продавцу</button>
</form>

<a href="/">Назад</a>
</body>
</html>

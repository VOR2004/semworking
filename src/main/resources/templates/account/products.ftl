<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мои товары</title>
</head>
<body>
<a href="/">Главная</a>
<h2>Мои товары</h2>
<#list products as p>
    <div>
        <strong>${p.title}</strong><br>
        ${p.description}<br>
        ${p.price} руб.<br>
        <a href="/products/${p.id}">Открыть</a> |
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
<h2>Мои данные</h2>

<#if userProfile.avatarUrl?? && (userProfile.avatarUrl?length > 0)>
    <img src="${userProfile.avatarUrl}" alt="Аватар пользователя" style="max-width:150px; margin-bottom:10px;">
</#if>

<form method="post" action="/account/update/avatar" enctype="multipart/form-data">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <label>Аватар:
        <input type="file" name="avatarFile" accept="image/*" required>
    </label>
    <button type="submit">Загрузить и сохранить</button>
</form>

<form method="post" action="/account/update/phoneNumber">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <label>Телефон:
        <input type="text" name="phoneNumber" value="${userProfile.phoneNumber!}">
    </label>
    <button type="submit">Сохранить</button>
</form>

<form method="post" action="/account/update/bio">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <label>О себе:
        <textarea name="bio">${userProfile.bio!}</textarea>
    </label>
    <button type="submit">Сохранить</button>
</form>
</body>
</html>

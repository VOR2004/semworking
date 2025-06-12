<!DOCTYPE html>
<html lang="ru">
<head>
    <base href="/"/>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/products.css"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Audiowide&family=Coral+Pixels&family=DotGothic16&family=Doto:wght,ROND@100..900,100&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&family=Micro+5&family=Orbitron:wght@400..900&family=Press+Start+2P&family=Rampart+One&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Rubik:ital,wght@0,300..900;1,300..900&family=Silkscreen:wght@400;700&display=swap" rel="stylesheet">
    <title>Мои товары</title>
</head>
<body>
<a href="/">Главная</a>
<h2>Мои данные</h2>
<br>
<#if userProfile.avatarUrl?? && (userProfile.avatarUrl?length > 0)>
    <div class="avatar-wrapper">
        <img src="${userProfile.avatarUrl}" alt="Аватар пользователя">
    </div>
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
<form id="logoutForm" action="/logout" method="post" style="display:none;">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>

<a href="#" class="logout-link" onclick="document.getElementById('logoutForm').submit(); return false;">Выйти</a>
<br><br><br>
<h2>Мои товары</h2>
<br>
<div class="products-grid">
    <#list products as p>
        <div>
            <strong>${p.title}</strong>
            <p>${p.description}</p>
            <p>${p.price} руб.</p>
            <div>
                <a href="/products/${p.id}">Открыть</a>
                <a href="/product/edit/${p.id}">Редактировать</a>
            </div>
            <p>
                <#if p.mainImageUrl?? && (p.mainImageUrl?length > 0)>
                <img src="${p.mainImageUrl}" alt="Фото товара">
                </#if>
            </p>
            <form method="post" action="/product/delete/${p.id}" style="display:inline;">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <button type="submit">Удалить</button>
            </form>
        </div>
    </#list>
</div>
</body>
</html>

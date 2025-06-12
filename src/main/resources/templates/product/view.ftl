<!DOCTYPE html>
<html lang="ru">
<head>
    <base href="/" />
    <meta charset="UTF-8">
    <title>${product.title}</title>
    <link rel="stylesheet" href="css/product-view.css"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Audiowide&family=Coral+Pixels&family=DotGothic16&family=Doto:wght,ROND@100..900,100&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&family=Micro+5&family=Orbitron:wght@400..900&family=Press+Start+2P&family=Rampart+One&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Rubik:ital,wght@0,300..900;1,300..900&family=Silkscreen:wght@400;700&display=swap" rel="stylesheet">
    <script src="https://api-maps.yandex.ru/2.1/?lang=ru_RU" type="text/javascript"></script>
</head>
<body>
<a href="/">Главная</a>
<div class="main-image-container">
    <#if product.mainImageUrl?? && (product.mainImageUrl?length > 0)>
        <img src="${product.mainImageUrl}" alt="Главное фото">
    </#if>
</div>
<h2>${product.title}</h2>
<p>${product.description}</p>
<p>Рейтинг: ${product.rating}</p>
<div class="price-rating-container">
    <p class="price">Цена: ${product.price} руб.</p>

    <div class="rating-container">
        <form method="post" action="/products/vote">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input type="hidden" name="productId" value="${product.id}">
            <input type="hidden" name="voteValue" value="-1">
            <button type="submit" class="vote-btn" <#if userVoted?? && userVoted>disabled</#if>>-</button>
        </form>

        <span class="rating-value">${product.rating}</span>

        <form method="post" action="/products/vote">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input type="hidden" name="productId" value="${product.id}">
            <input type="hidden" name="voteValue" value="1">
            <button type="submit" class="vote-btn" <#if userVoted?? && userVoted>disabled</#if>>+</button>
        </form>
    </div>
</div>
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
<br><br>

<button id="reset-center">Вернуться к месту товара</button>
<br><br>
<div id="map"></div>

<form method="post" action="/chat/start/${product.userEntity.id}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <button type="submit">Написать продавцу</button>
</form>
<script>
    window.productCoords = [${product.lat!0}, ${product.lon!0}];
    window.productTitle = "${product.title?js_string}";
</script>
<script src="js/ymaps-script.js"></script>
</body>
</html>
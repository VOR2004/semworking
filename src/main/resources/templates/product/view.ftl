<!DOCTYPE html>
<html lang="ru">
<head>
    <base href="/" />
    <meta charset="UTF-8">
    <title>${product.title}</title>
    <script src="https://api-maps.yandex.ru/2.1/?lang=ru_RU" type="text/javascript"></script>
    <style>
        #map {
            width: 100%;
            height: 400px;
            margin: 10px 0;
        }
        #reset-center {
            margin-bottom: 10px;
        }
        .rating-container {
            display: flex;
            align-items: center;
            margin: 10px 0;
        }
        .rating-value {
            margin: 0 10px;
            font-size: 18px;
            font-weight: bold;
        }
        .vote-btn {
            padding: 5px 10px;
            cursor: pointer;
        }
        .vote-btn:disabled {
            opacity: 0.5;
            cursor: not-allowed;
        }
    </style>
</head>
<body>
<a href="/">Главная</a>
<#if product.mainImageUrl?? && (product.mainImageUrl?length > 0)>
    <img src="${product.mainImageUrl}" alt="Главное фото" style="max-width:120px;max-height:120px;margin:5px 0;">
</#if>
<h2>${product.title}</h2>
<p>${product.description}</p>
<p>Рейтинг: ${product.rating}</p>
<p>Цена: ${product.price} руб.</p>
<p>Продавец: ${product.userEntity.username}</p>

<div class="rating-container">
    <form method="post" action="/products/vote">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="hidden" name="productId" value="${product.id}">
        <input type="hidden" name="voteValue" value="-1">
        <button type="submit" class="vote-btn"
                <#if userVoted?? && userVoted>disabled</#if>>-</button>
    </form>

    <span class="rating-value">${product.rating}</span>

    <form method="post" action="/products/vote">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="hidden" name="productId" value="${product.id}">
        <input type="hidden" name="voteValue" value="1">
        <button type="submit" class="vote-btn"
                <#if userVoted?? && userVoted>disabled</#if>>+</button>
    </form>
</div>

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

<button id="reset-center">Вернуться к месту товара</button>
<div id="map"></div>

<form method="post" action="/chat/start/${product.userEntity.id}">
    <button type="submit">Написать продавцу</button>
</form>

<a href="/">Назад</a>

<script>
    window.productCoords = [${product.lat!0}, ${product.lon!0}];
    window.productTitle = "${product.title?js_string}";
</script>
<script src="js/ymaps-script.js"></script>
</body>
</html>
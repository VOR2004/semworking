<!DOCTYPE html>
<html lang="ru">
<#import "productforms.ftl" as forms/>
<head>
    <base href="/" />
    <meta charset="UTF-8">
    <title>Все товары</title>
    <link rel="stylesheet" href="css/main.css" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Audiowide&family=Coral+Pixels&family=DotGothic16&family=Doto:wght,ROND@100..900,100&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&family=Micro+5&family=Orbitron:wght@400..900&family=Press+Start+2P&family=Rampart+One&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Rubik:ital,wght@0,300..900;1,300..900&family=Silkscreen:wght@400;700&display=swap" rel="stylesheet">
    <style>
        body {
            background-image: url("images/oboi.png");
        }
    </style>
</head>
<body>
<nav class="navbar">
    <a href="/">Главная</a>
    <a href="/chats">Мои чаты</a>
    <a href="/account">Мой аккаунт</a>
    <button class="add-product-btn" onclick="location.href='/product/add'">Добавить товар</button>
</nav>
<@forms.searchform/>

<div id="products-container">
    <#list products as p>
        <div class="product-card" data-id="${p.id}">
            <#if p.mainImageUrl?? && (p.mainImageUrl?length > 0)>
                <img src="${p.mainImageUrl}" alt="Фото товара">
            </#if>
            <div class="product-title">${p.title}</div>
            <div class="product-rating">Рейтинг: ${p.rating}</div>
            <div class="product-price">Цена: ${p.price} руб.</div>
            <#if p.tags?? && (p.tags?size > 0)>
                <div class="product-tags">
                    <#list p.tags as tag>
                        <span class="tag">${tag}</span>
                    </#list>
                </div>
            </#if>
        </div>
    </#list>
</div>


<#if totalPages?? && (totalPages > 1)>
    <div style="margin-bottom: 100px;margin-left: 100px">
        <#list 0..(totalPages-1) as i>
            <#if i == currentPage>
                <b>${i+1}</b>
            <#else>
                <a href="/?page=${i}&size=10<#if q??>&q=${q}</#if>
                <#if selectedTags??>
                    <#list selectedTags as t>&tags=${t}</#list>
                </#if>
                ">${i+1}</a>
            </#if>
        </#list>
    </div>
</#if>

<script>
    const allTags = [
        <#list allTags as tag>
        "${tag.name}"<#if tag_has_next>,</#if>
        </#list>
    ];
    let selectedTags = [
        <#if selectedTags??>
        <#list selectedTags as tag>
        "${tag}"<#if tag_has_next>,</#if>
        </#list>
        </#if>
    ];
    document.querySelectorAll('.product-card').forEach(card => {
        card.style.cursor = 'pointer';
        card.addEventListener('click', function() {
            const id = this.getAttribute('data-id');
            if (id) {
                window.location.href = '/products/' + id;
            }
        });
    });
</script>
<script src="js/tags-search.js"></script>
</body>
</html>

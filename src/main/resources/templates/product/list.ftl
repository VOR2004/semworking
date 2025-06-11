<!DOCTYPE html>
<html lang="ru">
<#import "productforms.ftl" as forms/>
<head>
    <base href="/" />
    <meta charset="UTF-8">
    <title>Все товары</title>
    <style>
        .tag-search-container { position: relative; display: inline-block; }
        #tag-dropdown {
            display: none;
            position: absolute;
            background: #fff;
            border: 1px solid #ccc;
            max-height: 120px;
            overflow-y: auto;
            z-index: 1000;
            width: 200px;
        }
        .selected-tag {
            display: inline-block;
            background: #e0e0e0;
            border-radius: 12px;
            padding: 2px 8px;
            margin: 2px 4px 2px 0;
            font-size: 14px;
        }
        .remove-tag {
            color: #b00;
            margin-left: 6px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<h2>Все товары</h2>
<nav>
    <a href="/">Главная</a> |
    <a href="/chats">Мои чаты</a> |
    <a href="/account">Мой аккаунт</a>
</nav>
<a href="/product/add">Добавить товар</a><br><br>

<@forms.searchform/>

<#list products as p>
    <div style="margin-bottom: 15px;">
        <#if p.mainImageUrl?? && (p.mainImageUrl?length > 0)>
            <img src="${p.mainImageUrl}" alt="Главное фото" style="max-width:120px;max-height:120px;margin:5px 0;">
        </#if>
        <br>
        <strong>${p.title}</strong><br>
        ${p.description}<br>
        Рейтинг: ${p.rating}<br>
        Цена: ${p.price} руб.<br>
        Размещено: ${p.createdAt}<br>
        Пользователь: ${p.userEntity.username}<br>
        <#if p.tags?? && (p.tags?size > 0)>
            Теги:
            <#list p.tags as tag>
                <a href="/?tags=${tag}<#if q??>&q=${q}</#if>
                    <#if selectedTags??>
                        <#list selectedTags as t>
                            <#if t != tag>&tags=${t}</#if>
                        </#list>
                    </#if>
                ">${tag}</a><#if tag_has_next>, </#if>
            </#list>
            <br>
        </#if>
        <a href="/products/${p.id}">Открыть</a>
    </div>
</#list>

<#if totalPages?? && (totalPages > 1)>
    <div style="margin-top: 20px;">
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
</script>
<script src="js/tags-search.js"></script>
</body>
</html>

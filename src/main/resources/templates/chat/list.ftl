<!DOCTYPE html>
<html lang="ru">
<head><meta charset="UTF-8"><title>Мои чаты</title></head>
<body>
<a href="/">Главная</a>
<h2>Мои чаты</h2>

<#list chats as c>
    <div>
        <#if c.seller.email == currentUserEmail>
            <#assign interlocutor = c.buyer.username>
        <#else>
            <#assign interlocutor = c.seller.username>
        </#if>
        <a href="/chat/${c.id}">Чат с ${interlocutor}</a>
    </div>
</#list>

</body>
</html>


<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мои чаты</title>
    <link rel="stylesheet" href="css/chat-list.css"/>
    <title>Добавить товар</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Audiowide&family=Coral+Pixels&family=DotGothic16&family=Doto:wght,ROND@100..900,100&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&family=Micro+5&family=Orbitron:wght@400..900&family=Press+Start+2P&family=Rampart+One&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Rubik:ital,wght@0,300..900;1,300..900&family=Silkscreen:wght@400;700&display=swap" rel="stylesheet">
</head>
<body>
<a href="/">Главная</a>
<h2>Мои чаты</h2>

<#list chats as c>
    <div class="chat-item">
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


<!DOCTYPE html>
<html lang="ru">
<base href="/">
<head>
    <meta charset="UTF-8">
    <title>Чат</title>
    <link rel="stylesheet" href="css/chat.css"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Audiowide&family=Coral+Pixels&family=DotGothic16&family=Doto:wght,ROND@100..900,100&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&family=Micro+5&family=Orbitron:wght@400..900&family=Press+Start+2P&family=Rampart+One&family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Rubik:ital,wght@0,300..900;1,300..900&family=Silkscreen:wght@400;700&display=swap" rel="stylesheet">
</head>
<body>
<a href="/">Главная</a>
<h2>Чат</h2>
<div id="messages">
    <#list messages as m>
        <#if m.sender?? && m.sender.username?? && m.content??>
            <div style="display: flex; align-items: center; margin-bottom: 10px;">
                <img src="<#if (m.sender.id == chat.seller.id && chat.seller.profile.avatarUrl?has_content)>
              ${chat.seller.profile.avatarUrl}
          <#elseif (m.sender.id == chat.buyer.id && chat.buyer.profile.avatarUrl?has_content)>
              ${chat.buyer.profile.avatarUrl}
          <#else>
              images/avatar-placeholder.png
          </#if>"
                     alt="avatar"
                     style="width: 40px; height: 40px; border-radius: 50%; margin-right: 10px;">
                <div>${m.sender.username}: ${m.content}</div>
                <br>
                <small style="color: #888; font-size: 12px;">
                    ${m.sentAt}
                </small>
            </div>
        </#if>
    </#list>
</div>
<label for="messageInput"></label><input type="text" id="messageInput">
<button onclick="sendMessage()">Send</button>
<div id="messages"></div>
<script>
    window.chatData = {
        chatId: "${chat.id}",
        senderId: "${currentUser.id}",
        senderName: "${currentUser.username}",
        sellerId: "${chat.seller.id}",
        buyerId: "${chat.buyer.id}",
        sellerAvatar: "${sellerAvatar!?html}",
        buyerAvatar: "${buyerAvatar!?html}"
    };
</script>
<script src="js/chat.js"></script>
<a href="/chats">Назад к списку чатов</a>
</body>
</html>
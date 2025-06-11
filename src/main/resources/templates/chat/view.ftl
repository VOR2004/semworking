<!DOCTYPE html>
<html lang="ru">
<base href="/">
<head><meta charset="UTF-8"><title>Чат</title></head>
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
            </div>
        </#if>
    </#list>
</div>
<label for="messageInput"></label><input type="text" id="messageInput">
<button onclick="sendMessage()">Отправить</button>
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
<!DOCTYPE html>
<html lang="ru">
<head><meta charset="UTF-8"><title>Чат</title></head>
<body>
<h2>Чат</h2>
<div id="messages">
    <#list messages as m>
        <div><b>${m.sender.username}</b>: ${m.content}</div>
    </#list>
</div>
<label for="messageInput"></label><input type="text" id="messageInput">
<button onclick="sendMessage()">Отправить</button>
<div id="messages"></div>

<script>
        const chatId = "${chat.id}";
        const senderId = "${currentUser.id}";
        const senderName = "${currentUser.username}";

        const socket = new WebSocket("ws://" + location.host + "/ws/chat?chatId=" + chatId);

        socket.onmessage = function(event) {
                const msg = JSON.parse(event.data);
                const el = document.createElement("div");
                el.textContent = msg.senderName + ": " + msg.content;
                document.getElementById("messages").appendChild(el);
        };

        function sendMessage() {
                const content = document.getElementById("messageInput").value;
                const message = {
                        chatId: chatId,
                        senderId: senderId,
                        senderName: senderName,
                        content: content
                };
                socket.send(JSON.stringify(message));
                document.getElementById("messageInput").value = "";
        }
</script>

<a href="/chats">Назад к списку чатов</a>
</body>
</html>
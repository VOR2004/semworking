const chatId = window.chatData.chatId;
const senderId = window.chatData.senderId;
const senderName = window.chatData.senderName;
const sellerId = window.chatData.sellerId;
const buyerId = window.chatData.buyerId;
const sellerAvatar = window.chatData.sellerAvatar;
const buyerAvatar = window.chatData.buyerAvatar;

const socket = new WebSocket("ws://" + location.host + "/ws/chat?chatId=" + chatId);

function getAvatarUrl(senderId, avatarUrl) {
    if (avatarUrl && avatarUrl.trim() !== "") {
        return avatarUrl;
    }
    if (senderId === sellerId) {
        return sellerAvatar && sellerAvatar.trim() !== "" ? sellerAvatar : "/images/avatar-placeholder.png";
    } else if (senderId === buyerId) {
        return buyerAvatar && buyerAvatar.trim() !== "" ? buyerAvatar : "/images/avatar-placeholder.png";
    } else {
        return "/images/avatar-placeholder.png";
    }
}

socket.onmessage = function(event) {
    const msg = JSON.parse(event.data);

    const messageDiv = document.createElement("div");
    messageDiv.style.display = "flex";
    messageDiv.style.alignItems = "center";
    messageDiv.style.marginBottom = "10px";

    const avatarImg = document.createElement("img");
    avatarImg.style.width = "40px";
    avatarImg.style.height = "40px";
    avatarImg.style.borderRadius = "50%";
    avatarImg.style.marginRight = "10px";

    avatarImg.src = getAvatarUrl(msg.senderId, msg.avatarUrl);

    const textDiv = document.createElement("div");
    textDiv.style.display = "flex";
    textDiv.style.flexDirection = "column";

    const contentDiv = document.createElement("div");
    contentDiv.textContent = msg.senderName + ": " + msg.content;

    const timeDiv = document.createElement("small");
    timeDiv.style.color = "#888";
    timeDiv.style.fontSize = "12px";
    timeDiv.style.marginTop = "2px";

    if (msg.sentAt) {
        const date = new Date(msg.sentAt);
        const hours = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');
        timeDiv.textContent = hours + ":" + minutes;
    } else {
        timeDiv.textContent = "";
    }

    textDiv.appendChild(contentDiv);
    textDiv.appendChild(timeDiv);

    messageDiv.appendChild(avatarImg);
    messageDiv.appendChild(textDiv);

    document.getElementById("messages").appendChild(messageDiv);
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

window.sendMessage = sendMessage;

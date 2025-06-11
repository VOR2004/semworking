const chatId = window.chatData.chatId;
const senderId = window.chatData.senderId;
const senderName = window.chatData.senderName;
const sellerId = window.chatData.sellerId;
const buyerId = window.chatData.buyerId;
const sellerAvatar = window.chatData.sellerAvatar;
const buyerAvatar = window.chatData.buyerAvatar;

const socket = new WebSocket("ws://" + location.host + "/ws/chat?chatId=" + chatId);

function getAvatarUrl(senderId) {
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

    avatarImg.src = getAvatarUrl(msg.senderId);

    const el = document.createElement("div");
    el.textContent = msg.senderName + ": " + msg.content;
    messageDiv.appendChild(avatarImg);
    messageDiv.appendChild(el);
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

class ChatService(
    var chatList: MutableList<Chat> = mutableListOf(),
    var chatIdCount: Int = 1,
    var messageIdCount: Int = 1,

    ) {

    fun getUnreadChatsCount(userId: Int): Int {
        val userUnreadChats: MutableList<Chat> = mutableListOf()
        val userChats = chatList.filter { it.recipientId == userId }
        for (chat in userChats) {
            for (message in chat.messagesList) {
                if (!message.isRead) {
                    userUnreadChats += chat
                }
            }
        }
        return userUnreadChats.size
    }

    fun getChats(userId: Int): MutableList<Chat> {
        val userChats: MutableList<Chat> = mutableListOf()
        var userChat: Chat
        val filteredChats = chatList.filter { (it.senderId == userId) || (it.recipientId == userId) }
        for (chat in filteredChats) {
            val lastMessage = chat.messagesList.lastOrNull() ?: throw NotFoundException("Сообщения отсутствуют")
            userChat = chat
            userChat.messagesList.clear()
            userChat.messagesList.add(lastMessage)
            userChats.add(userChat)
        }
        return userChats
    }

    fun getMessages(chatId: Int, messageId: Int, count: Int, userId: Int): List<Message> {
        val currentChat = chatList
            .find { it.chatId == chatId } ?: throw NotFoundException("Чат отустствует")
        val latestMessages = currentChat.messagesList
            .takeLastWhile { it.messageId == messageId }
            .take(count)
        for (message in latestMessages) {
            if (message.recipientId == userId) {
                message.isRead = true
            }
        }
        return latestMessages
    }

    fun addMessage(recipientId: Int, message: Message): Chat {
        val newMessage = message.copy(messageId = messageIdCount)
        return chatList
            .find { (it.recipientId == recipientId) && (it.chatId == message.chatIdForMessages) }
            ?.apply {
                messageIdCount++
                this.messagesList.add(message)
            }
            ?: Chat(
                messagesList = mutableListOf(newMessage),
                recipientId = recipientId,
                chatId = newMessage.chatIdForMessages
            )
    }

    fun deleteMessage(chatId: Int, messageId: Int) {
        chatList
            .find { it.chatId == chatId }
            .apply {
                this?.messagesList ?: throw NotFoundException("Чат отустствует")
                this.messagesList.removeIf {
                    it.messageId == messageId
                }
                chatList.removeIf { this.messagesList.isEmpty() }
            }
    }

    fun addChat(recipientId: Int, chat: Chat): Chat {
        chatList.apply {
            chat.copy(chatId = chatIdCount, recipientId = recipientId)
            chatIdCount++
            chatList.add(chat)
        }
        return chat
    }

    fun deleteChat(chatId: Int) {
        chatList.removeIf { it ->
            (it.chatId == chatId)
            (it.messagesList.removeIf { it.chatIdForMessages == chatId })
        }
    }

    fun clear() {
        chatList.clear()
        chatIdCount = 1
        messageIdCount = 1
    }
}
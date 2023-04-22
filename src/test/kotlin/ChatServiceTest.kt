import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ChatServiceTest {

    val service = ChatService()

    @Before
    fun clearBeforeTest() {
        service.clear()
    }

    @Test
    fun getUnreadChatsCount() {
        val chat1 = Chat(
            chatId = 1,
            senderId = 1,
            recipientId = 1,
        )
        val message1 = Message(
            messageId = 1,
            chatIdForMessages = 1,
            senderId = 1,
            recipientId = 1,
            text = "mes1",
            isRead = false
        )

        val chat2 = Chat(
            chatId = 2,
            senderId = 1,
            recipientId = 1,
        )
        val message2 = Message(
            messageId = 2,
            chatIdForMessages = 2,
            senderId = 1,
            recipientId = 1,
            text = "mes2",
            isRead = true
        )
        service.addChat(1, chat1)
        service.addMessage(1, message1)
        service.addChat(1, chat2)
        service.addMessage(1, message2)
        val result = service.getUnreadChatsCount(1)
        assertEquals(1, result)
    }

    @Test
    fun getChats() {
        val chat1 = Chat(
            chatId = 1,
            senderId = 1,
            recipientId = 1,
        )
        val message1 = Message(
            messageId = 1,
            chatIdForMessages = 1,
            senderId = 1,
            recipientId = 1,
            text = "mes1",
            isRead = false
        )
        val message2 = Message(
            messageId = 2,
            chatIdForMessages = 1,
            senderId = 1,
            recipientId = 1,
            text = "mes2",
            isRead = false
        )
        val chat2 = Chat(
            chatId = 2,
            senderId = 1,
            recipientId = 1,
        )
        val message3 = Message(
            messageId = 1,
            chatIdForMessages = 2,
            senderId = 1,
            recipientId = 1,
            text = "mes3",
            isRead = true
        )
        val message4 = Message(
            messageId = 2,
            chatIdForMessages = 2,
            senderId = 1,
            recipientId = 1,
            text = "mes4",
            isRead = true
        )
        service.addChat(1, chat1)
        service.addMessage(1, message1)
        service.addMessage(1, message2)
        service.addChat(1, chat2)
        service.addMessage(1, message3)
        service.addMessage(1, message4)
        val result = service.getChats(1)
        assertNotNull(result)
    }

    @Test
    fun getMessages() {
        val chat1 = Chat(
            chatId = 1,
            senderId = 1,
            recipientId = 1,
        )
        val message1 = Message(
            messageId = 1,
            chatIdForMessages = chat1.chatId,
            senderId = 1,
            recipientId = 1,
            text = "mes1",
            isRead = true
        )
        val message2 = Message(
            messageId = 2,
            chatIdForMessages = chat1.chatId,
            senderId = 1,
            recipientId = 1,
            text = "mes2",
            isRead = false
        )
        val message3 = Message(
            messageId = 3,
            chatIdForMessages = chat1.chatId,
            senderId = 1,
            recipientId = 1,
            text = "mes3",
            isRead = false
        )

        service.addChat(1, chat1)
        service.addMessage(1, message1)
        service.addMessage(1, message2)
        service.addMessage(1, message3)
        val result = service.getMessages(1, 3, 2, 1)
        assertNotNull(result)
    }

    @Test
    fun addMessage() {
        val message1 = Message(
            messageId = 1,
            chatIdForMessages = 1,
            senderId = 1,
            recipientId = 1,
            text = "mes1",
            isRead = false
        )
        val result = service.addMessage(1, message1)
        assertNotNull(result)
    }

    @Test
    fun deleteMessage() {
        val chat1 = Chat(
            chatId = 1,
            senderId = 1,
            recipientId = 1,
        )
        val message1 = Message(
            messageId = 1,
            chatIdForMessages = chat1.chatId,
            senderId = 1,
            recipientId = 1,
            text = "mes1",
            isRead = false
        )
        service.addChat(1, chat1)
        service.addMessage(1, message1)
        service.deleteMessage(1, 1)
    }

    @Test
    fun addChat() {
        val add = Chat(
            chatId = 1,
            senderId = 1,
            recipientId = 1,
        )
        val addedChat1 = service.addChat(1, add)
        assertNotNull(addedChat1)
    }

    @Test
    fun deleteChat() {
        val chat1 = Chat(
            chatId = 1,
            senderId = 1,
            recipientId = 1,
        )
        val message1 = Message(
            messageId = 1,
            chatIdForMessages = chat1.chatId,
            senderId = 1,
            recipientId = 1,
            text = "mes1",
            isRead = false
        )
        service.addChat(1, chat1)
        service.addMessage(1, message1)
        service.deleteChat(1)
    }

    @Test (expected = NotFoundException::class)
    fun shouldThrow() {
        val addedChat5 = Chat(
            chatId = 1,
            senderId = 1,
            recipientId = 1,
        )
        val addedChat6 = Chat(
            chatId = 2,
            senderId = 1,
            recipientId = 1,
        )
        service.addChat(1, addedChat5)
        service.addChat(1, addedChat6)
        val result = service.getChats(1)
        assertNull(result)
    }
}
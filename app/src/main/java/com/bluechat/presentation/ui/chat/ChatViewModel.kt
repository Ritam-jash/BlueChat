package com.bluechat.presentation.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluechat.domain.model.Chat
import com.bluechat.domain.model.Message
import com.bluechat.domain.repository.ChatRepository
import com.bluechat.domain.repository.MessageRepository
import com.bluechat.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class ChatUiState(
    val chatId: String = "",
    val chatTitle: String = "",
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSending: Boolean = false
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState(isLoading = true))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    var currentUserId: String = ""
        private set

    init {
        // In a real app, you would get the current user ID from a session manager or auth service
        viewModelScope.launch {
            currentUserId = userRepository.getCurrentUserId()
        }
    }

    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(chatId = chatId, isLoading = true) }
            
            // Load chat details
            chatRepository.getChatById(chatId)?.let { chat ->
                _uiState.update { it.copy(chatTitle = chat.title) }
            }
            
            // Observe messages
            messageRepository.observeMessages(chatId)
                .catch { e ->
                    _uiState.update { it.copy(
                        error = e.message ?: "Failed to load messages",
                        isLoading = false
                    ) }
                }
                .collect { messages ->
                    _uiState.update { it.copy(
                        messages = messages.sortedBy { it.timestamp },
                        isLoading = false,
                        error = null
                    ) }
                }
        }
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return
        
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSending = true) }
                
                val message = Message(
                    id = UUID.randomUUID().toString(),
                    chatId = _uiState.value.chatId,
                    senderId = currentUserId,
                    content = content,
                    timestamp = System.currentTimeMillis(),
                    status = Message.Status.SENDING
                )
                
                // Save message locally first for instant feedback
                messageRepository.saveMessage(message)
                
                // In a real app, you would also send the message to the server/network here
                // and update the status when the server acknowledges it
                
                // For now, just simulate sending
                // In a real app, this would be handled by a service or use case
                kotlinx.coroutines.delay(1000) // Simulate network delay
                
                messageRepository.updateMessageStatus(
                    messageId = message.id,
                    status = Message.Status.SENT
                )
                
                // Update chat's last message
                chatRepository.updateChatLastMessage(
                    chatId = _uiState.value.chatId,
                    lastMessage = content,
                    lastMessageSenderId = currentUserId,
                    timestamp = message.timestamp
                )
                
            } catch (e: Exception) {
                // Update message status to failed
                messageRepository.updateMessageStatus(
                    messageId = UUID.randomUUID().toString(),
                    status = Message.Status.FAILED
                )
                _uiState.update { it.copy(
                    error = e.message ?: "Failed to send message"
                ) }
            } finally {
                _uiState.update { it.copy(isSending = false) }
            }
        }
    }
}

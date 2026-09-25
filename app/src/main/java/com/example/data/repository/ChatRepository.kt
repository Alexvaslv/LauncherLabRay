package com.example.data.repository

import com.example.data.api.ChatApiClient
import com.example.data.local.ChatDao
import com.example.data.local.SettingsEntity
import com.example.data.local.toChatMessage
import com.example.data.local.toEntity
import com.example.data.model.ChatMessage
import com.example.data.model.ChatResponse
import com.example.data.model.HealthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ChatRepository(
    private val chatDao: ChatDao? = null,
    private val apiClient: ChatApiClient = ChatApiClient()
) {
    val messagesFlow: Flow<List<ChatMessage>> = chatDao?.getAllMessages()?.map { list ->
        list.map { it.toChatMessage() }
    } ?: emptyFlow()

    suspend fun saveMessage(message: ChatMessage) {
        chatDao?.insertMessage(message.toEntity())
    }

    suspend fun saveMessages(messages: List<ChatMessage>) {
        chatDao?.insertMessages(messages.map { it.toEntity() })
    }

    suspend fun clearHistory() {
        chatDao?.clearAllMessages()
    }

    suspend fun saveSetting(key: String, value: String) {
        chatDao?.setSetting(SettingsEntity(key = key, value = value))
    }

    suspend fun getSetting(key: String): String? {
        return chatDao?.getSetting(key)?.value
    }

    suspend fun sendMessage(
        serverUrl: String,
        message: String,
        previousResponseId: String?
    ): Result<ChatResponse> {
        val result = apiClient.sendChatMessage(serverUrl, message, previousResponseId)
        return result.recoverCatching { throwable ->
            val friendlyMessage = when (throwable) {
                is ConnectException -> {
                    "Не удалось подключиться к серверу по адресу $serverUrl.\n" +
                    "Убедитесь, что сервер запущен:\n" +
                    "1. cd server && npm start\n" +
                    "2. Для эмулятора используйте адрес: http://10.0.2.2:3000/api/chat"
                }
                is UnknownHostException -> {
                    "Хост не найден: $serverUrl. Проверьте правильность URL в настройках."
                }
                is SocketTimeoutException -> {
                    "Таймаут ответа от сервера (модель gpt-5.6-sol формирует длинный ответ или сеть медленная)."
                }
                else -> throwable.message ?: "Неизвестная ошибка сети"
            }
            throw Exception(friendlyMessage, throwable)
        }
    }

    suspend fun checkHealth(serverUrl: String): Result<HealthResponse> {
        return apiClient.checkHealth(serverUrl)
    }
}

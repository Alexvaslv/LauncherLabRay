package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.HealthResponse
import com.example.data.repository.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

sealed interface HealthCheckState {
    data object Idle : HealthCheckState
    data object Checking : HealthCheckState
    data class Success(val info: HealthResponse) : HealthCheckState
    data class Error(val message: String) : HealthCheckState
}

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = false,
    val currentPreviousResponseId: String? = null,
    val serverUrl: String = "http://10.0.2.2:3000/api/chat",
    val isDemoMode: Boolean = false,
    val showSettingsDialog: Boolean = false,
    val healthCheckState: HealthCheckState = HealthCheckState.Idle,
    val lastFailedMessage: String? = null,
    val toastNotice: String? = null
)

class ChatViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = ChatRepository(chatDao = database.chatDao())

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        // 1. Observe messages from Room SQLite Database (persisted in phone memory)
        viewModelScope.launch {
            repository.messagesFlow.collect { dbMessages ->
                if (dbMessages.isNotEmpty()) {
                    val lastWithResponseId = dbMessages.lastOrNull { it.responseId != null }?.responseId
                    _uiState.update { state ->
                        state.copy(
                            messages = dbMessages,
                            currentPreviousResponseId = lastWithResponseId ?: state.currentPreviousResponseId
                        )
                    }
                } else {
                    // Seed initial welcome message into Room
                    val initialMessage = ChatMessage(
                        text = "Здравствуйте! Я подключен к безопасной модели gpt-5.6-sol через серверный API.\n\n" +
                                "🛡️ Все сообщения и настройки надежно сохраняются в памяти телефона (Room SQLite).\n" +
                                "Вы можете задавать вопросы, генерировать код, структурировать JSON или использовать быстрые промпты ниже.",
                        isFromUser = false,
                        responseId = "resp_init_studio"
                    )
                    repository.saveMessage(initialMessage)
                }
            }
        }

        // 2. Load persisted settings (server URL, demo mode, lastResponseId)
        viewModelScope.launch {
            val savedUrl = repository.getSetting("server_url")
            val savedDemo = repository.getSetting("demo_mode")?.toBooleanStrictOrNull()
            val savedLastId = repository.getSetting("last_response_id")

            _uiState.update { state ->
                state.copy(
                    serverUrl = savedUrl ?: state.serverUrl,
                    isDemoMode = savedDemo ?: state.isDemoMode,
                    currentPreviousResponseId = savedLastId ?: state.currentPreviousResponseId
                )
            }
        }
    }

    fun onInputChanged(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun openSettingsDialog() {
        _uiState.update { it.copy(showSettingsDialog = true) }
        checkServerHealth()
    }

    fun closeSettingsDialog() {
        _uiState.update { it.copy(showSettingsDialog = false) }
    }

    fun updateServerUrl(newUrl: String) {
        val trimmed = newUrl.trim()
        _uiState.update { it.copy(serverUrl = trimmed) }
        viewModelScope.launch {
            repository.saveSetting("server_url", trimmed)
        }
        checkServerHealth()
    }

    fun toggleDemoMode(enabled: Boolean) {
        _uiState.update { it.copy(isDemoMode = enabled) }
        viewModelScope.launch {
            repository.saveSetting("demo_mode", enabled.toString())
        }
    }

    fun clearToastNotice() {
        _uiState.update { it.copy(toastNotice = null) }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearHistory()
            repository.saveSetting("last_response_id", "")
            val resetMessage = ChatMessage(
                text = "История очищена в памяти телефона. Цепочка контекста сброшена (previousResponseId = null).",
                isFromUser = false
            )
            repository.saveMessage(resetMessage)
        }
        _uiState.update {
            it.copy(
                currentPreviousResponseId = null,
                lastFailedMessage = null
            )
        }
    }

    fun sendQuickPrompt(prompt: String) {
        _uiState.update { it.copy(inputText = prompt) }
        sendMessage()
    }

    fun retryLastMessage() {
        val lastMsg = _uiState.value.lastFailedMessage ?: return
        _uiState.update { it.copy(inputText = lastMsg, lastFailedMessage = null) }
        sendMessage()
    }

    fun sendMessage() {
        val currentText = _uiState.value.inputText.trim()
        if (currentText.isBlank() || _uiState.value.isLoading) return

        val userMessage = ChatMessage(
            text = currentText,
            isFromUser = true
        )

        val previousId = _uiState.value.currentPreviousResponseId
        val isDemo = _uiState.value.isDemoMode
        val serverUrl = _uiState.value.serverUrl

        _uiState.update { state ->
            state.copy(
                inputText = "",
                isLoading = true,
                lastFailedMessage = null
            )
        }

        viewModelScope.launch {
            // Persist user message to phone's Room database
            repository.saveMessage(userMessage)

            if (isDemo) {
                // Демо-режим без внешнего сервера (для автономного тестирования интерфейса)
                delay(900)
                val newDemoId = "resp_demo_" + UUID.randomUUID().toString().take(8)
                val demoResponseText = generateDemoReply(currentText, previousId)
                val assistantMessage = ChatMessage(
                    text = demoResponseText,
                    isFromUser = false,
                    responseId = newDemoId
                )
                repository.saveMessage(assistantMessage)
                repository.saveSetting("last_response_id", newDemoId)

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        currentPreviousResponseId = newDemoId
                    )
                }
                return@launch
            }

            // Реальный вызов безопасного серверного endpoint
            val result = repository.sendMessage(
                serverUrl = serverUrl,
                message = currentText,
                previousResponseId = previousId
            )

            result.onSuccess { response ->
                val assistantMessage = ChatMessage(
                    text = response.text.ifBlank { "Модель gpt-5.6-sol вернула пустой ответ." },
                    isFromUser = false,
                    responseId = response.responseId
                )

                // Persist assistant message in phone memory
                repository.saveMessage(assistantMessage)
                if (response.responseId != null) {
                    repository.saveSetting("last_response_id", response.responseId)
                }

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        currentPreviousResponseId = response.responseId ?: state.currentPreviousResponseId,
                        lastFailedMessage = null
                    )
                }
            }.onFailure { error ->
                val errorMessage = ChatMessage(
                    text = "⚠️ Ошибка связи с сервером:\n${error.message}\n\n" +
                            "💡 Совет: если вы тестируете в эмуляторе без запущенного `node server.js`, " +
                            "вы можете включить 'Демо-режим' в настройках (кнопка ⚙️ вверху справа).",
                    isFromUser = false,
                    isError = true
                )
                repository.saveMessage(errorMessage)

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        lastFailedMessage = currentText
                    )
                }
            }
        }
    }

    fun checkServerHealth() {
        val serverUrl = _uiState.value.serverUrl
        _uiState.update { it.copy(healthCheckState = HealthCheckState.Checking) }

        viewModelScope.launch {
            val result = repository.checkHealth(serverUrl)
            result.onSuccess { health ->
                _uiState.update {
                    it.copy(
                        healthCheckState = HealthCheckState.Success(health),
                        toastNotice = "Сервер доступен (${health.model ?: "gpt-5.6-sol"})"
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        healthCheckState = HealthCheckState.Error(err.message ?: "Недоступен")
                    )
                }
            }
        }
    }

    private fun generateDemoReply(query: String, prevId: String?): String {
        return "✨ [Демо-режим эмулятора]\n\n" +
                "Ваш запрос обработан симулятором безопасного бэкенда:\n" +
                "«$query»\n\n" +
                "🔗 Предыдущий контекст: ${prevId ?: "начало диалога (null)"}\n" +
                "🤖 Модель в конфигурации сервера: `gpt-5.6-sol`\n" +
                "🛡️ Данные сохранены в постоянной базе данных Room в памяти телефона!\n\n" +
                "Для перехода в боевой режим запустите бэкенд на хосте:\n" +
                "`cd server && npm start` и отключите демо-режим в настройках."
    }
}

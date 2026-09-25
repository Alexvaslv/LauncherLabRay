package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.ChatMessage

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long,
    val responseId: String?,
    val isError: Boolean
)

fun ChatMessageEntity.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = id,
        text = text,
        isFromUser = isFromUser,
        timestamp = timestamp,
        responseId = responseId,
        isError = isError
    )
}

fun ChatMessage.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        id = id,
        text = text,
        isFromUser = isFromUser,
        timestamp = timestamp,
        responseId = responseId,
        isError = isError
    )
}

@Entity(tableName = "app_settings")
data class SettingsEntity(
    @PrimaryKey val key: String,
    val value: String
)

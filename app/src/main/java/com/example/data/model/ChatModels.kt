package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.UUID

@JsonClass(generateAdapter = true)
data class ChatRequest(
    @Json(name = "message") val message: String,
    @Json(name = "previousResponseId") val previousResponseId: String? = null
)

@JsonClass(generateAdapter = true)
data class ChatResponse(
    @Json(name = "text") val text: String,
    @Json(name = "responseId") val responseId: String? = null
)

@JsonClass(generateAdapter = true)
data class HealthResponse(
    @Json(name = "status") val status: String,
    @Json(name = "service") val service: String? = null,
    @Json(name = "model") val model: String? = null,
    @Json(name = "hasApiKey") val hasApiKey: Boolean? = null
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val responseId: String? = null,
    val isError: Boolean = false
)

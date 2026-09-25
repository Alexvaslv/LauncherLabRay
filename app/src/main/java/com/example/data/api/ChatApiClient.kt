package com.example.data.api

import com.example.data.model.ChatRequest
import com.example.data.model.ChatResponse
import com.example.data.model.HealthResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class ChatApiClient {

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val chatRequestAdapter = moshi.adapter(ChatRequest::class.java)
    private val chatResponseAdapter = moshi.adapter(ChatResponse::class.java)
    private val healthResponseAdapter = moshi.adapter(HealthResponse::class.java)

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Send message to the backend server's POST /api/chat endpoint
     */
    suspend fun sendChatMessage(
        baseUrl: String,
        message: String,
        previousResponseId: String?
    ): Result<ChatResponse> = withContext(Dispatchers.IO) {
        try {
            val formattedBase = normalizeUrl(baseUrl)
            val endpoint = if (formattedBase.endsWith("/api/chat")) {
                formattedBase
            } else {
                "${formattedBase.trimEnd('/')}/api/chat"
            }

            val requestBodyJson = chatRequestAdapter.toJson(
                ChatRequest(message = message, previousResponseId = previousResponseId)
            )

            val body = requestBodyJson.toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(endpoint)
                .post(body)
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                val responseBody = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    val parsed = chatResponseAdapter.fromJson(responseBody)
                    if (parsed != null) {
                        Result.success(parsed)
                    } else {
                        Result.failure(Exception("Не удалось распарсить ответ от сервера: $responseBody"))
                    }
                } else {
                    Result.failure(
                        Exception("Ошибка сервера (HTTP ${response.code}): ${responseBody.ifBlank { response.message }}")
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Check backend health at GET /api/health
     */
    suspend fun checkHealth(baseUrl: String): Result<HealthResponse> = withContext(Dispatchers.IO) {
        try {
            val formattedBase = normalizeUrl(baseUrl)
            val healthUrl = if (formattedBase.endsWith("/api/chat")) {
                formattedBase.replace("/api/chat", "/api/health")
            } else {
                "${formattedBase.trimEnd('/')}/api/health"
            }

            val request = Request.Builder()
                .url(healthUrl)
                .get()
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                val responseBody = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    val parsed = healthResponseAdapter.fromJson(responseBody)
                    if (parsed != null) {
                        Result.success(parsed)
                    } else {
                        Result.failure(Exception("Не удалось распарсить статус"))
                    }
                } else {
                    Result.failure(Exception("HTTP ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun normalizeUrl(url: String): String {
        val trimmed = url.trim()
        return if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            "http://$trimmed"
        } else {
            trimmed
        }
    }
}

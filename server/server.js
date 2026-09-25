const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');
const OpenAI = require('openai');

// Загрузка переменных окружения из .env
dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware для CORS и парсинга JSON
app.use(cors({
  origin: '*', // Разрешает доступ из любых источников (включая Android эмулятор и внешние IP)
  methods: ['GET', 'POST', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization']
}));
app.use(express.json());

// Проверка наличия API ключа OpenAI при старте
const apiKey = process.env.OPENAI_API_KEY;
if (!apiKey) {
  console.warn('⚠️ ВНИМАНИЕ: OPENAI_API_KEY не задан в переменных окружения или файле .env.');
  console.warn('Укажите OPENAI_API_KEY в .env перед отправкой запросов к модели.');
}

// Инициализация официального клиента OpenAI
const openaiClient = new OpenAI({
  apiKey: apiKey || 'dummy-key-placeholder'
});

/**
 * GET /api/health
 * Проверка доступности сервера
 */
app.get('/api/health', (req, res) => {
  res.json({
    status: 'ok',
    service: 'AI Studio Safe Backend',
    model: 'gpt-5.6-sol',
    timestamp: new Date().toISOString(),
    hasApiKey: Boolean(process.env.OPENAI_API_KEY)
  });
});

/**
 * POST /api/chat
 * Безопасный endpoint для Android-приложения.
 * Принимает: { "message": "текст", "previousResponseId": "..." | null }
 * Возвращает: { "text": "ответ модели", "responseId": "..." }
 */
app.post('/api/chat', async (req, res) => {
  try {
    const { message, previousResponseId } = req.body;

    // Валидация входных данных
    if (!message || typeof message !== 'string' || message.trim() === '') {
      return res.status(400).json({
        error: 'Поле "message" обязательно и должно содержать непустую строку.'
      });
    }

    if (!process.env.OPENAI_API_KEY) {
      return res.status(500).json({
        error: 'OPENAI_API_KEY не настроен на сервере. Пожалуйста, добавьте его в .env'
      });
    }

    console.log(`[POST /api/chat] Входящее сообщение: "${message.substring(0, 50)}..."`);
    if (previousResponseId) {
      console.log(`[POST /api/chat] Контекст предыдущего ответа: ${previousResponseId}`);
    }

    // Вызов OpenAI Responses API с моделью gpt-5.6-sol
    const response = await openaiClient.responses.create({
      model: 'gpt-5.6-sol',
      input: message,
      previous_response_id: previousResponseId || undefined
    });

    // Извлечение текстового ответа
    let outputText = response.output_text;
    if (!outputText && response.output && Array.isArray(response.output)) {
      // Защитный fallback для вариаций структуры Responses API
      const textItem = response.output.find(item => item.type === 'message' || item.content);
      if (textItem && textItem.content) {
        outputText = Array.isArray(textItem.content)
          ? textItem.content.map(c => c.text || '').join('\n')
          : textItem.content;
      }
    }

    const responsePayload = {
      text: outputText || '',
      responseId: response.id || null
    };

    console.log(`[POST /api/chat] Успешный ответ. ResponseId: ${responsePayload.responseId}`);
    return res.status(200).json(responsePayload);

  } catch (error) {
    console.error('[POST /api/chat] Ошибка при вызове OpenAI Responses API:', error);

    // Обработка типичных ошибок OpenAI
    const statusCode = error.status || 500;
    const errorMessage = error.message || 'Внутренняя ошибка сервера при обработке запроса к AI';

    return res.status(statusCode).json({
      error: errorMessage,
      code: error.code || 'OPENAI_API_ERROR'
    });
  }
});

// Запуск сервера
app.listen(PORT, '0.0.0.0', () => {
  console.log(`🚀 Сервер запущен на порту ${PORT}`);
  console.log(`👉 Android Emulator URL: http://10.0.2.2:${PORT}/api/chat`);
  console.log(`👉 Локальный URL: http://localhost:${PORT}/api/chat`);
});

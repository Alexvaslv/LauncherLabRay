package com.example.data.playmarket

data class MarketApp(
    val id: String,
    val name: String,
    val category: String,
    val developer: String,
    val rating: Float,
    val reviewsCount: String,
    val sizeMb: String,
    val downloads: String,
    val description: String,
    val iconKey: String,
    val isFeatured: Boolean = false,
    val bannerText: String? = null
)

object PlayMarketCatalog {

    val allMarketApps: List<MarketApp> = listOf(
        MarketApp(
            id = "market_chatgpt",
            name = "ChatGPT & gpt-5.6-sol",
            category = "Инструменты / AI",
            developer = "OpenAI Inc.",
            rating = 4.9f,
            reviewsCount = "4.2M отзывов",
            sizeMb = "48 MB",
            downloads = "100M+",
            description = "Официальное приложение с передовым искусственным интеллектом, генерацией текста, кодингом и мгновенными ответами на любые вопросы.",
            iconKey = "CHAT_GPT",
            isFeatured = true,
            bannerText = "Выбор редакции • Лучший AI 2026"
        ),
        MarketApp(
            id = "market_telegram",
            name = "Telegram",
            category = "Связь",
            developer = "Telegram FZ-LLC",
            rating = 4.8f,
            reviewsCount = "12M отзывов",
            sizeMb = "64 MB",
            downloads = "1B+",
            description = "Быстрый, защищенный и удобный мессенджер с облачным хранением, каналами, группами и ботами.",
            iconKey = "TELEGRAM",
            isFeatured = true,
            bannerText = "Топ чарт #1 в категории Связь"
        ),
        MarketApp(
            id = "market_spotify",
            name = "Spotify: Музыка и подкасты",
            category = "Музыка и аудио",
            developer = "Spotify AB",
            rating = 4.7f,
            reviewsCount = "31M отзывов",
            sizeMb = "52 MB",
            downloads = "1B+",
            description = "Миллионы треков, персональные плейлисты, подкасты и музыка со всего мира в высоком качестве.",
            iconKey = "SPOTIFY"
        ),
        MarketApp(
            id = "market_capcut",
            name = "CapCut - Видеоредактор",
            category = "Видеоплееры и редакторы",
            developer = "Bytedance Pte. Ltd.",
            rating = 4.6f,
            reviewsCount = "8.4M отзывов",
            sizeMb = "95 MB",
            downloads = "500M+",
            description = "Мощный видеоредактор со спецэффектами, интеллектуальным вырезанием фона, музыкой и фильтрами.",
            iconKey = "CAPCUT"
        ),
        MarketApp(
            id = "market_aurawall",
            name = "AuraWall 4K Обои",
            category = "Персонализация",
            developer = "Aura Studio Lab",
            rating = 4.9f,
            reviewsCount = "850K отзывов",
            sizeMb = "28 MB",
            downloads = "10M+",
            description = "Эксклюзивные неоновые, футуристичные и абстрактные обои ультра-высокого разрешения 4K Ultra HD.",
            iconKey = "AURA_WALL",
            isFeatured = true,
            bannerText = "Новые неоновые коллекции"
        ),
        MarketApp(
            id = "market_duolingo",
            name = "Duolingo: Учи языки",
            category = "Образование",
            developer = "Duolingo",
            rating = 4.8f,
            reviewsCount = "18M отзывов",
            sizeMb = "42 MB",
            downloads = "500M+",
            description = "Изучай английский, испанский, немецкий и другие языки в увлекательной игровой форме.",
            iconKey = "TIPS"
        ),
        MarketApp(
            id = "market_shopee",
            name = "Shopee: Маркетплейс",
            category = "Покупки",
            developer = "Shopee International",
            rating = 4.6f,
            reviewsCount = "9.1M отзывов",
            sizeMb = "68 MB",
            downloads = "500M+",
            description = "Шопинг с выгодными скидками, безопасной оплатой и быстрой доставкой по всему миру.",
            iconKey = "SHOPEE"
        ),
        MarketApp(
            id = "market_grab",
            name = "Grab: Такси и доставка",
            category = "Путешествия",
            developer = "Grab Holdings",
            rating = 4.7f,
            reviewsCount = "7.3M отзывов",
            sizeMb = "58 MB",
            downloads = "100M+",
            description = "Супер-приложение для заказа поездок, доставки еды, экспресс-посылок и электронных платежей.",
            iconKey = "GRAB"
        ),
        MarketApp(
            id = "market_chrome",
            name = "Google Chrome",
            category = "Связь / Браузеры",
            developer = "Google LLC",
            rating = 4.4f,
            reviewsCount = "45M отзывов",
            sizeMb = "82 MB",
            downloads = "10B+",
            description = "Быстрый, безопасный и удобный веб-браузер от Google со встроенной защитой и синхронизацией закладок.",
            iconKey = "CHROME"
        ),
        MarketApp(
            id = "market_notion",
            name = "Notion: Заметки и документы",
            category = "Работа",
            developer = "Notion Labs",
            rating = 4.8f,
            reviewsCount = "920K отзывов",
            sizeMb = "36 MB",
            downloads = "50M+",
            description = "Единое рабочее пространство для заметок, задач, баз данных и командных проектов с искусственным интеллектом.",
            iconKey = "NOTES"
        )
    )
}

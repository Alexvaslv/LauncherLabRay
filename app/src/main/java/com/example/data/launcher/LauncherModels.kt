package com.example.data.launcher

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class AppItem(
    val id: String,
    val name: String,
    val iconType: AppIconType,
    val backgroundColor: Color = Color(0xFF2C3E50),
    val badge: String? = null
)

sealed interface AppIconType {
    data class Vector(val imageVector: ImageVector, val tint: Color = Color.White) : AppIconType
    data class Custom(val type: CustomIconType) : AppIconType
}

enum class CustomIconType {
    CHROME,
    SETTINGS,
    PLAY_STORE,
    CALENDAR,
    WEATHER,
    CLOCK,
    CHAT_GPT,
    GRAB,
    XANH_SM,
    HEALTH,
    AURA_WALL,
    WALLET,
    SHOPEE,
    TIPS,
    SPOTIFY,
    YOUTUBE,
    MAPS,
    GMAIL,
    TELEGRAM,
    WHATSAPP,
    DISCORD,
    INSTAGRAM,
    TIKTOK,
    CAPCUT,
    NOTES,
    CAMERA,
    GALLERY,
    PHONE,
    MESSAGES,
    FILES
}

data class FolderItem(
    val id: String,
    val name: String,
    val apps: List<AppItem>
)

sealed interface GridItem {
    data class SingleApp(val app: AppItem) : GridItem
    data class Folder(val folder: FolderItem) : GridItem
}

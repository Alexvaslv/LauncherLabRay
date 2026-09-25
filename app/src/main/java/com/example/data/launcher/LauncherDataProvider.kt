package com.example.data.launcher

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.ui.graphics.Color

object LauncherDataProvider {

    val googleFolder = FolderItem(
        id = "folder_google",
        name = "Google Apps",
        apps = listOf(
            AppItem("g_maps", "Maps", AppIconType.Custom(CustomIconType.MAPS), Color(0xFF34A853)),
            AppItem("g_mail", "Gmail", AppIconType.Custom(CustomIconType.GMAIL), Color(0xFFEA4335)),
            AppItem("g_yt", "YouTube", AppIconType.Custom(CustomIconType.YOUTUBE), Color(0xFFFF0000)),
            AppItem("g_drive", "Drive", AppIconType.Custom(CustomIconType.FILES), Color(0xFF4285F4)),
            AppItem("g_photos", "Photos", AppIconType.Custom(CustomIconType.GALLERY), Color(0xFFFBBC05)),
            AppItem("g_calendar", "Calendar", AppIconType.Custom(CustomIconType.CALENDAR), Color(0xFF4285F4)),
            AppItem("g_chrome", "Chrome", AppIconType.Custom(CustomIconType.CHROME), Color(0xFFFFFFFF)),
            AppItem("g_play", "Play Store", AppIconType.Custom(CustomIconType.PLAY_STORE), Color(0xFF00C853)),
            AppItem("g_meet", "Meet", AppIconType.Custom(CustomIconType.MESSAGES), Color(0xFF00897B))
        )
    )

    val systemFolder = FolderItem(
        id = "folder_system",
        name = "System",
        apps = listOf(
            AppItem("sys_specs", "Характеристики", AppIconType.Vector(Icons.Default.Memory, Color(0xFF00E5FF)), Color(0xFF0D1B2A)),
            AppItem("sys_booster", "Ускорение 144Hz", AppIconType.Vector(Icons.Default.RocketLaunch, Color(0xFFFF6D00)), Color(0xFF261205), badge = "MAX"),
            AppItem("sys_bluetooth", "Bluetooth 5.4", AppIconType.Vector(Icons.Default.Bluetooth, Color(0xFF2979FF)), Color(0xFF0D1B2A)),
            AppItem("sys_apk", "Экспорт APK", AppIconType.Vector(Icons.Default.Android, Color(0xFF00E676)), Color(0xFF0A2614), badge = "APK"),
            AppItem("sys_phone", "Phone", AppIconType.Custom(CustomIconType.PHONE), Color(0xFF4CAF50)),
            AppItem("sys_msg", "Messages", AppIconType.Custom(CustomIconType.MESSAGES), Color(0xFF2196F3)),
            AppItem("sys_cam", "Camera", AppIconType.Custom(CustomIconType.CAMERA), Color(0xFF607D8B)),
            AppItem("sys_gallery", "Gallery", AppIconType.Custom(CustomIconType.GALLERY), Color(0xFFFF9800)),
            AppItem("sys_settings", "Settings", AppIconType.Custom(CustomIconType.SETTINGS), Color(0xFF78909C)),
            AppItem("sys_clock", "Clock", AppIconType.Custom(CustomIconType.CLOCK), Color(0xFF3F51B5)),
            AppItem("sys_files", "Files", AppIconType.Custom(CustomIconType.FILES), Color(0xFF00ACC1))
        )
    )

    val musicFolder = FolderItem(
        id = "folder_music",
        name = "Music & Audio",
        apps = listOf(
            AppItem("m_spotify", "Spotify", AppIconType.Custom(CustomIconType.SPOTIFY), Color(0xFF1DB954)),
            AppItem("m_ytmusic", "YT Music", AppIconType.Custom(CustomIconType.YOUTUBE), Color(0xFFFF0000)),
            AppItem("m_podcasts", "Podcasts", AppIconType.Custom(CustomIconType.SPOTIFY), Color(0xFF9C27B0))
        )
    )

    val moviesFolder = FolderItem(
        id = "folder_movies",
        name = "Movies & Vid...",
        apps = listOf(
            AppItem("vid_capcut", "CapCut", AppIconType.Custom(CustomIconType.CAPCUT), Color(0xFF000000)),
            AppItem("vid_tiktok", "TikTok", AppIconType.Custom(CustomIconType.TIKTOK), Color(0xFF010101)),
            AppItem("vid_player", "Video Player", AppIconType.Custom(CustomIconType.GALLERY), Color(0xFFE91E63))
        )
    )

    val socialFolder = FolderItem(
        id = "folder_social",
        name = "Social & Com...",
        apps = listOf(
            AppItem("soc_tg", "Telegram", AppIconType.Custom(CustomIconType.TELEGRAM), Color(0xFF29B6F6)),
            AppItem("soc_wa", "WhatsApp", AppIconType.Custom(CustomIconType.WHATSAPP), Color(0xFF25D366)),
            AppItem("soc_insta", "Instagram", AppIconType.Custom(CustomIconType.INSTAGRAM), Color(0xFFE1306C)),
            AppItem("soc_discord", "Discord", AppIconType.Custom(CustomIconType.DISCORD), Color(0xFF5865F2)),
            AppItem("soc_fb", "Facebook", AppIconType.Custom(CustomIconType.MESSAGES), Color(0xFF1877F2))
        )
    )

    val productivityFolder = FolderItem(
        id = "folder_prod",
        name = "Productivity",
        apps = listOf(
            AppItem("prod_ai", "AI Studio (gpt-5.6-sol)", AppIconType.Custom(CustomIconType.CHAT_GPT), Color(0xFF10A37F), badge = "AI"),
            AppItem("prod_notes", "Notes", AppIconType.Custom(CustomIconType.NOTES), Color(0xFFFFA000)),
            AppItem("prod_cal", "Calendar", AppIconType.Custom(CustomIconType.CALENDAR), Color(0xFF1E88E5)),
            AppItem("prod_docs", "Docs", AppIconType.Custom(CustomIconType.FILES), Color(0xFF4285F4))
        )
    )

    // Complete 4x4 Grid matching the screenshot
    val gridItems: List<GridItem> = listOf(
        // Row 1
        GridItem.SingleApp(AppItem("app_settings_row", "Settings", AppIconType.Custom(CustomIconType.SETTINGS), Color(0xFF37474F))),
        GridItem.Folder(googleFolder),
        GridItem.Folder(systemFolder),
        GridItem.Folder(musicFolder),

        // Row 2
        GridItem.Folder(moviesFolder),
        GridItem.Folder(socialFolder),
        GridItem.Folder(productivityFolder),
        GridItem.SingleApp(AppItem("app_tips", "Tips", AppIconType.Custom(CustomIconType.TIPS), Color(0xFFFF9100))),

        // Row 3
        GridItem.SingleApp(AppItem("app_health", "Health", AppIconType.Custom(CustomIconType.HEALTH), Color(0xFF00E5FF))),
        GridItem.SingleApp(AppItem("app_grab", "Grab", AppIconType.Custom(CustomIconType.GRAB), Color(0xFFFFFFFF))),
        GridItem.SingleApp(AppItem("app_xanh", "Xanh SM", AppIconType.Custom(CustomIconType.XANH_SM), Color(0xFF00B4D8))),
        GridItem.SingleApp(AppItem("app_aurawall", "AuraWall", AppIconType.Custom(CustomIconType.AURA_WALL), Color(0xFF1A237E))),

        // Row 4
        GridItem.SingleApp(AppItem("app_bluetooth", "Bluetooth 5.4", AppIconType.Vector(Icons.Default.Bluetooth, Color(0xFF2979FF)), Color(0xFF0D1B2A))),
        GridItem.SingleApp(AppItem("app_apk", "Экспорт APK", AppIconType.Vector(Icons.Default.Android, Color(0xFF00E676)), Color(0xFF0A2614), badge = "APK")),
        GridItem.SingleApp(AppItem("app_booster", "Ускорение", AppIconType.Vector(Icons.Default.RocketLaunch, Color(0xFFFF6D00)), Color(0xFF261205), badge = "144 FPS")),
        GridItem.SingleApp(AppItem("app_wallet", "Wallet", AppIconType.Custom(CustomIconType.WALLET), Color(0xFF2979FF)))
    )

    val dockApps: List<AppItem> = listOf(
        AppItem("dock_chrome", "Chrome", AppIconType.Custom(CustomIconType.CHROME), Color.White),
        AppItem("dock_settings", "Settings", AppIconType.Custom(CustomIconType.SETTINGS), Color(0xFF263238)),
        AppItem("dock_play", "Play Store", AppIconType.Custom(CustomIconType.PLAY_STORE), Color.White),
        AppItem("dock_calendar", "Calendar", AppIconType.Custom(CustomIconType.CALENDAR), Color.White)
    )
}

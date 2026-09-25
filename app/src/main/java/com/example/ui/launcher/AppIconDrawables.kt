package com.example.ui.launcher

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.launcher.AppItem
import com.example.data.launcher.CustomIconType
import java.util.Calendar

@Composable
fun AppIconRenderer(
    app: AppItem,
    size: Dp = 60.dp,
    modifier: Modifier = Modifier
) {
    val cornerRadius = size * 0.26f

    Box(
        modifier = modifier
            .size(size)
            .shadow(4.dp, RoundedCornerShape(cornerRadius))
            .clip(RoundedCornerShape(cornerRadius))
            .background(app.backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        when (val iconType = app.iconType) {
            is com.example.data.launcher.AppIconType.Vector -> {
                Icon(
                    imageVector = iconType.imageVector,
                    contentDescription = app.name,
                    tint = iconType.tint,
                    modifier = Modifier.size(size * 0.55f)
                )
            }
            is com.example.data.launcher.AppIconType.Custom -> {
                CustomIconView(iconType = iconType.type, size = size)
            }
        }

        // Optional badge (e.g. AI)
        if (app.badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(3.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFF3D00))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(
                    text = app.badge,
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CustomIconView(
    iconType: CustomIconType,
    size: Dp
) {
    when (iconType) {
        CustomIconType.CHROME -> ChromeIcon(size)
        CustomIconType.SETTINGS -> SettingsIcon(size)
        CustomIconType.PLAY_STORE -> PlayStoreIcon(size)
        CustomIconType.CALENDAR -> CalendarIcon(size)
        CustomIconType.GRAB -> GrabIcon(size)
        CustomIconType.XANH_SM -> XanhSmIcon(size)
        CustomIconType.HEALTH -> HealthIcon(size)
        CustomIconType.AURA_WALL -> AuraWallIcon(size)
        CustomIconType.WALLET -> WalletIcon(size)
        CustomIconType.SHOPEE -> ShopeeIcon(size)
        CustomIconType.TIPS -> TipsIcon(size)
        CustomIconType.CHAT_GPT -> ChatGptIcon(size)
        CustomIconType.SPOTIFY -> SpotifyIcon(size)
        CustomIconType.YOUTUBE -> YouTubeIcon(size)
        CustomIconType.MAPS -> MapsIcon(size)
        CustomIconType.GMAIL -> GmailIcon(size)
        CustomIconType.TELEGRAM -> TelegramIcon(size)
        CustomIconType.WHATSAPP -> WhatsAppIcon(size)
        CustomIconType.INSTAGRAM -> InstagramIcon(size)
        CustomIconType.DISCORD -> DiscordIcon(size)
        CustomIconType.TIKTOK -> TikTokIcon(size)
        CustomIconType.CAPCUT -> CapCutIcon(size)
        CustomIconType.NOTES -> NotesIcon(size)
        CustomIconType.CAMERA -> CameraIcon(size)
        CustomIconType.GALLERY -> GalleryIcon(size)
        CustomIconType.PHONE -> PhoneIcon(size)
        CustomIconType.MESSAGES -> MessagesIcon(size)
        CustomIconType.CLOCK -> ClockMiniIcon(size)
        CustomIconType.FILES -> FilesIcon(size)
        CustomIconType.WEATHER -> WeatherMiniIcon(size)
    }
}

// 1. Chrome Icon (White background with Red, Yellow, Green swirl and Blue center)
@Composable
fun ChromeIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.72f)) {
            val center = Offset(this.size.width / 2, this.size.height / 2)
            val radius = this.size.width / 2

            // Red segment
            drawArc(
                color = Color(0xFFEA4335),
                startAngle = -150f,
                sweepAngle = 120f,
                useCenter = true
            )
            // Yellow segment
            drawArc(
                color = Color(0xFFFBBC05),
                startAngle = -30f,
                sweepAngle = 120f,
                useCenter = true
            )
            // Green segment
            drawArc(
                color = Color(0xFF34A853),
                startAngle = 90f,
                sweepAngle = 120f,
                useCenter = true
            )
            // White circle border around center
            drawCircle(
                color = Color.White,
                radius = radius * 0.46f,
                center = center
            )
            // Blue center
            drawCircle(
                color = Color(0xFF4285F4),
                radius = radius * 0.38f,
                center = center
            )
        }
    }
}

// 2. Settings Icon (Sleek dark gradient with metallic gear)
@Composable
fun SettingsIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF37474F), Color(0xFF212121))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            tint = Color(0xFFCFD8DC),
            modifier = Modifier.size(size * 0.62f)
        )
    }
}

// 3. Google Play Store (White squircle with Google Play triangle)
@Composable
fun PlayStoreIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.62f)) {
            val w = this.size.width
            val h = this.size.height

            // Triangle paths
            val bluePath = Path().apply {
                moveTo(w * 0.1f, h * 0.05f)
                lineTo(w * 0.6f, h * 0.5f)
                lineTo(w * 0.1f, h * 0.95f)
                close()
            }
            drawPath(bluePath, color = Color(0xFF00C853))

            val greenPath = Path().apply {
                moveTo(w * 0.1f, h * 0.05f)
                lineTo(w * 0.75f, h * 0.38f)
                lineTo(w * 0.6f, h * 0.5f)
                close()
            }
            drawPath(greenPath, color = Color(0xFF00E5FF))

            val redPath = Path().apply {
                moveTo(w * 0.1f, h * 0.95f)
                lineTo(w * 0.75f, h * 0.62f)
                lineTo(w * 0.6f, h * 0.5f)
                close()
            }
            drawPath(redPath, color = Color(0xFFFF3D00))

            val yellowPath = Path().apply {
                moveTo(w * 0.6f, h * 0.5f)
                lineTo(w * 0.75f, h * 0.38f)
                lineTo(w * 0.95f, h * 0.5f)
                lineTo(w * 0.75f, h * 0.62f)
                close()
            }
            drawPath(yellowPath, color = Color(0xFFFFD600))
        }
    }
}

// 4. Calendar Icon (Teal top header, white body, dynamic day "31")
@Composable
fun CalendarIcon(size: Dp) {
    val dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Teal top banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(size * 0.30f)
                .background(Color(0xFF00B4D8))
        )
        // White body with day
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(size * 0.70f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayOfMonth,
                fontSize = (size.value * 0.38f).sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E293B)
            )
        }
    }
}

// 5. Grab Icon (White squircle with green Grab cursive logo)
@Composable
fun GrabIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Grab",
            color = Color(0xFF00B14F),
            fontWeight = FontWeight.Black,
            fontSize = (size.value * 0.32f).sp,
            letterSpacing = (-0.5).sp
        )
    }
}

// 6. Xanh SM Icon (Cyan with yellow wing)
@Composable
fun XanhSmIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF00C9A7), Color(0xFF00897B))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.65f)) {
            val w = this.size.width
            val h = this.size.height

            val wingPath = Path().apply {
                moveTo(w * 0.15f, h * 0.45f)
                quadraticTo(w * 0.4f, h * 0.85f, w * 0.85f, h * 0.25f)
                quadraticTo(w * 0.55f, h * 0.6f, w * 0.25f, h * 0.45f)
                close()
            }
            drawPath(wingPath, color = Color(0xFFFFD54F))

            val whiteWing = Path().apply {
                moveTo(w * 0.25f, h * 0.55f)
                quadraticTo(w * 0.5f, h * 0.9f, w * 0.9f, h * 0.3f)
                quadraticTo(w * 0.6f, h * 0.7f, w * 0.35f, h * 0.55f)
                close()
            }
            drawPath(whiteWing, color = Color.White)
        }
    }
}

// 7. Health Icon (Teal squircle with white yoga silhouette)
@Composable
fun HealthIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF00E5FF), Color(0xFF00B0FF))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.SelfImprovement,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.65f)
        )
    }
}

// 8. AuraWall Icon (Dark blue with glowing neon wallpaper vibe)
@Composable
fun AuraWallIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    listOf(Color(0xFF00E5FF), Color(0xFF1A237E), Color(0xFF0A0E27))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size * 0.6f)
                .border(1.5.dp, Color(0xFF00E5FF), RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "4K",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = (size.value * 0.22f).sp
            )
        }
    }
}

// 9. Wallet Icon
@Composable
fun WalletIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF2979FF), Color(0xFF1565C0))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Wallet,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

// 10. Shopee Icon (Orange with shopping bag and S)
@Composable
fun ShopeeIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFFF5722), Color(0xFFE64A19))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(size * 0.65f)
            )
            Text(
                text = "S",
                color = Color(0xFFE64A19),
                fontWeight = FontWeight.Black,
                fontSize = (size.value * 0.26f).sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// 11. Tips Icon (Orange with white glowing lightbulb)
@Composable
fun TipsIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFF9800), Color(0xFFF57C00))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lightbulb,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.65f)
        )
    }
}

// 12. ChatGPT / AI Studio
@Composable
fun ChatGptIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF10A37F)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

// 13. Spotify Icon
@Composable
fun SpotifyIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1DB954)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Headphones,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

// 14. YouTube Icon
@Composable
fun YouTubeIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF0000)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.65f)
        )
    }
}

// 15. Maps Icon
@Composable
fun MapsIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Navigation,
            contentDescription = null,
            tint = Color(0xFF34A853),
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

// 16. Gmail Icon
@Composable
fun GmailIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "M",
            color = Color(0xFFEA4335),
            fontWeight = FontWeight.Black,
            fontSize = (size.value * 0.44f).sp
        )
    }
}

// 17. Telegram Icon
@Composable
fun TelegramIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF29B6F6)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Send,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.55f)
        )
    }
}

// 18. WhatsApp Icon
@Composable
fun WhatsAppIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF25D366)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.58f)
        )
    }
}

// 19. Instagram Icon
@Composable
fun InstagramIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFCB045))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

// 20. Discord Icon
@Composable
fun DiscordIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5865F2)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.SportsEsports,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

// 21. TikTok Icon
@Composable
fun TikTokIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "♪",
            color = Color(0xFF00F2FE),
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.44f).sp
        )
    }
}

// 22. CapCut Icon
@Composable
fun CapCutIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "✂",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.38f).sp
        )
    }
}

// Fallbacks for remaining
@Composable
fun NotesIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFA000)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Description, null, tint = Color.White, modifier = Modifier.size(size * 0.55f))
    }
}

@Composable
fun CameraIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF455A64)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(size * 0.55f))
    }
}

@Composable
fun GalleryIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF9800)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Photo, null, tint = Color.White, modifier = Modifier.size(size * 0.55f))
    }
}

@Composable
fun PhoneIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4CAF50)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Phone, null, tint = Color.White, modifier = Modifier.size(size * 0.55f))
    }
}

@Composable
fun MessagesIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2196F3)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Chat, null, tint = Color.White, modifier = Modifier.size(size * 0.55f))
    }
}

@Composable
fun FilesIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00ACC1)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Folder, null, tint = Color.White, modifier = Modifier.size(size * 0.55f))
    }
}

@Composable
fun ClockMiniIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F51B5)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.VideoLibrary, null, tint = Color.White, modifier = Modifier.size(size * 0.55f))
    }
}

@Composable
fun WeatherMiniIcon(size: Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0288D1)),
        contentAlignment = Alignment.Center
    ) {
        Text("27°", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

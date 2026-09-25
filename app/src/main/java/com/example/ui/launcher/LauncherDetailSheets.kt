package com.example.ui.launcher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.launcher.AppItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailSheet(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF131D31),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Long Island City",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "27°",
                fontSize = 64.sp,
                fontWeight = FontWeight.ExtraLight,
                lineHeight = 68.sp
            )
            Text(
                text = "Mostly Cloudy • H:35° L:25°",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Hourly forecast row
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2D4A).copy(alpha = 0.7f)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "ПОЧАСОВОЙ ПРОГНОЗ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(listOf(
                            Triple("Сейчас", "27°", Icons.Default.Cloud),
                            Triple("09:00", "28°", Icons.Default.Cloud),
                            Triple("10:00", "29°", Icons.Default.WbSunny),
                            Triple("11:00", "31°", Icons.Default.WbSunny),
                            Triple("12:00", "33°", Icons.Default.WbSunny),
                            Triple("13:00", "35°", Icons.Default.WbSunny),
                            Triple("14:00", "35°", Icons.Default.Cloud),
                            Triple("15:00", "34°", Icons.Default.Cloud),
                        )) { (time, t, icon) ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = time, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(6.dp))
                                Icon(icon, null, tint = Color(0xFFFFD54F), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = t, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Weather details grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WeatherMetricCard(
                    icon = Icons.Default.WaterDrop,
                    title = "Влажность",
                    value = "62%",
                    modifier = Modifier.weight(1f)
                )
                WeatherMetricCard(
                    icon = Icons.Default.Air,
                    title = "Ветер",
                    value = "14 км/ч",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun WeatherMetricCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2D4A).copy(alpha = 0.7f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(title, fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockDetailSheet(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var alarmActive by remember { mutableStateOf(true) }
    var alarmTime by remember { mutableStateOf("07:00") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF191C24),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Часы и Будильники", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Icon(Icons.Default.Alarm, null, tint = Color(0xFFFF9100))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Alarm Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF262A36)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = alarmTime, fontSize = 36.sp, fontWeight = FontWeight.Light)
                        Text(text = "Будильник • Каждый день", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                    }
                    Switch(checked = alarmActive, onCheckedChange = { alarmActive = it })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // World clock items
            Text(text = "МИРОВОЕ ВРЕМЯ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
            val nowTime = remember { timeFormat.format(Date()) }

            listOf(
                Pair("Long Island City (Нью-Йорк)", nowTime),
                Pair("Лондон (GMT+1)", "13:47"),
                Pair("Токио (JST)", "21:47")
            ).forEach { (city, time) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(city, fontSize = 15.sp, color = Color.White.copy(alpha = 0.9f))
                    Text(time, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF00E5FF))
                }
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailSheet(
    app: AppItem,
    onLaunchAiStudio: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF181B26),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppIconRenderer(app = app, size = 72.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = app.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = when (app.id) {
                    "prod_ai" -> "Безопасный клиент AI Studio на модели gpt-5.6-sol через серверный Node.js API."
                    "app_tips" -> "Советы по использованию смартфона, полезные шорткаты и возможности персонализации."
                    "app_health" -> "Мониторинг активности, шагов, тренировок и показателей здоровья."
                    "app_grab" -> "Заказ поездок, доставка еды и сервисы курьерской доставки."
                    "app_xanh" -> "Сервис электротакси и электромобильности Xanh SM."
                    "app_aurawall" -> "Коллекция премиальных 4K обоев и неоновых тем оформления."
                    "app_wallet" -> "Цифровой кошелек, банковские карты и бесконтактная оплата."
                    "app_shopee" -> "Маркетплейс для онлайн-покупок с быстрой доставкой."
                    else -> "Быстрый запуск приложения «${app.name}» из кастомного лаунчера."
                },
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.75f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (app.id == "prod_ai" || app.name.contains("AI", ignoreCase = true)) {
                Button(
                    onClick = {
                        onLaunchAiStudio()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("launch_ai_studio_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10A37F)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Открыть AI Studio Chat", fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E50)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Закрыть")
                }
            }
        }
    }
}

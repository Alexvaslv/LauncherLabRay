package com.example.ui.launcher

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneSpecsAndBoosterSheet(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isOptimizing by remember { mutableStateOf(false) }
    var optimizationDone by remember { mutableStateOf(false) }
    var freedRamMb by remember { mutableStateOf("0") }

    var ultraTouchEnabled by remember { mutableStateOf(true) }
    var zeroLagMemcEnabled by remember { mutableStateOf(true) }
    var extremeCpuClock by remember { mutableStateOf(true) }
    var liquidCoolingTurbo by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF0F1218),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with Device Name & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Aura Phone 16 Ultra Pro",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Флагманские характеристики & Система 0 Лагов",
                        fontSize = 13.sp,
                        color = Color(0xFF00E5FF)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFFF3D00), Color(0xFFFF9100))
                            )
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "144 FPS EXTREME",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. One-Tap Anti-Lag Turbo Booster Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("turbo_booster_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF161C26)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1E2838), Color(0xFF101722))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF3D00).copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RocketLaunch,
                                    contentDescription = null,
                                    tint = Color(0xFFFF6D00),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Оптимизация и снятие лагов",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = if (optimizationDone) "Система на 100% ускорена • 0 мс задержка" else "Очистка ОЗУ, кэша и разгон GPU",
                                    fontSize = 12.sp,
                                    color = if (optimizationDone) Color(0xFF00E676) else Color.White.copy(alpha = 0.65f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live FPS & Latency stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MetricPill(title = "Частота", value = "144.0 FPS", color = Color(0xFF00E676))
                        MetricPill(title = "Задержка ввода", value = "0.8 мс", color = Color(0xFF00E5FF))
                        MetricPill(title = "Пропуск кадров", value = "0.0%", color = Color(0xFFFFD54F))
                        MetricPill(title = "Температура", value = "27.2°C", color = Color(0xFF40C4FF))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isOptimizing) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp),
                                color = Color(0xFFFF6D00)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Сброс мусора в памяти, компиляция байткода, активация 144Hz...",
                                fontSize = 11.sp,
                                color = Color(0xFFFFAB40)
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                scope.launch {
                                    isOptimizing = true
                                    delay(800)
                                    freedRamMb = "7 420"
                                    isOptimizing = false
                                    optimizationDone = true
                                    Toast.makeText(
                                        context,
                                        "🚀 Лаги устранены! Освобождено 7.4 GB ОЗУ, включен 144 FPS режим",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (optimizationDone) Color(0xFF00897B) else Color(0xFFFF3D00)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("boost_phone_button")
                        ) {
                            Icon(
                                imageVector = if (optimizationDone) Icons.Default.CheckCircle else Icons.Default.ElectricBolt,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (optimizationDone) "Оптимизировано (7.4 GB очищено • 144 FPS)" else "УСКОРИТЬ ТЕЛЕФОН И УБРАТЬ ЛАГИ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Hardware Specs Sections (Мощные характеристики)
            Text(
                text = "МОЩНЫЕ АППАРАТНЫЕ ХАРАКТЕРИСТИКИ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Spec 1: Processor & GPU & NPU
            SpecDetailCard(
                icon = Icons.Default.Memory,
                iconColor = Color(0xFF00E5FF),
                title = "Процессор & Видеочип",
                mainSpec = "Snapdragon 8 Gen 4 Extreme (3nm N3E)",
                details = listOf(
                    "Тактовая частота: 4.32 GHz (2x Oryon Phoenix L + 6x Oryon M)",
                    "Графика (GPU): Adreno 830 Ultra @ 1250 MHz (Аппаратный Ray Tracing)",
                    "Нейрочип (NPU): Qualcomm Hexagon 50 TOPS для локального ИИ"
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Spec 2: RAM & ROM
            SpecDetailCard(
                icon = Icons.Default.Storage,
                iconColor = Color(0xFFFFD54F),
                title = "Оперативная & Встроенная память",
                mainSpec = "24 GB LPDDR5X + 1024 GB (1 TB) UFS 4.1 Pro",
                details = listOf(
                    "ОЗУ: 24 GB со скоростью 10,700 MT/s + 12 GB AI RAM Turbo = 36 GB",
                    "Накопитель: 1 TB сверхбыстрого UFS 4.1 (Чтение: 4800 МБ/с, Запись: 4200 МБ/с)",
                    "База данных: Room SQLite с прямой памятью (без фрагментации)"
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Spec 3: Display
            SpecDetailCard(
                icon = Icons.Default.PhoneAndroid,
                iconColor = Color(0xFF00E676),
                title = "Дисплей и плавность анимаций",
                mainSpec = "6.82\" 2K Super AMOLED LTPO 4.0 (1-144 Hz)",
                details = listOf(
                    "Частота обновления: 144 Hz Ultra ProMotion (отклик 0.1 мс)",
                    "Разрешение: 3168 x 1440 QHD+ (510 PPI)",
                    "Пиковая яркость: 4500 нит, HDR10+, Dolby Vision",
                    "Частота сенсора (Touch Sampling): 2160 Hz (мгновенная реакция)"
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Spec 4: Battery & Cooling
            SpecDetailCard(
                icon = Icons.Default.BatteryChargingFull,
                iconColor = Color(0xFFFF6D00),
                title = "Батарея и жидкостное охлаждение",
                mainSpec = "6500 mAh Кремний-углерод + 165W HyperCharge",
                details = listOf(
                    "Зарядка: от 0% до 100% за 11 минут",
                    "Охлаждение: 3D IceLoop Vapor Chamber (площадь 12 000 мм²)",
                    "Защита от нагрева: интеллектуальный троттлинг-контроль"
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Toggles for anti-lag game & system features
            Text(
                text = "НАСТРОЙКИ ПЛАВНОСТИ (0 ЛАГОВ)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF171A22)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    AntiLagSwitchRow(
                        title = "Мгновенный сенсор UltraTouch (0.8 мс)",
                        subtitle = "Отключает программные задержки буфера касаний",
                        checked = ultraTouchEnabled,
                        onCheckedChange = { ultraTouchEnabled = it }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.White.copy(alpha = 0.1f))
                    AntiLagSwitchRow(
                        title = "Аппаратное ускорение (Hardware Acceleration)",
                        subtitle = "Рендеринг интерфейса на GPU без загрузки CPU",
                        checked = zeroLagMemcEnabled,
                        onCheckedChange = { zeroLagMemcEnabled = it }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.White.copy(alpha = 0.1f))
                    AntiLagSwitchRow(
                        title = "Максимальная частота ядер (Extreme Boost)",
                        subtitle = "Фиксация пиковых частот процессора для избежания просадок",
                        checked = extremeCpuClock,
                        onCheckedChange = { extremeCpuClock = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Close button
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222836)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Закрыть параметры системы", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MetricPill(title: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = color)
    }
}

@Composable
fun SpecDetailCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    mainSpec: String,
    details: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161A22))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = iconColor, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(title, fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                    Text(mainSpec, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            details.forEach { detail ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("•", color = iconColor, fontSize = 14.sp, modifier = Modifier.padding(end = 6.dp))
                    Text(detail, fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }
    }
}

@Composable
fun AntiLagSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
            Text(subtitle, fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF00E676)
            )
        )
    }
}

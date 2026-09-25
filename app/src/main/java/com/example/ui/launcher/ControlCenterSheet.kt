package com.example.ui.launcher

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlCenterSheet(
    onOpenSpecsAndBooster: () -> Unit = {},
    onOpenBluetoothManager: () -> Unit = {},
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var wifiActive by remember { mutableStateOf(true) }
    var bluetoothActive by remember { mutableStateOf(true) }
    var cellularActive by remember { mutableStateOf(true) }
    var turboBoostActive by remember { mutableStateOf(true) }
    var flashlightActive by remember { mutableStateOf(false) }
    var darkModeActive by remember { mutableStateOf(true) }
    var airplaneActive by remember { mutableStateOf(false) }

    var brightness by remember { mutableFloatStateOf(0.78f) }
    var volume by remember { mutableFloatStateOf(0.65f) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF14171F).copy(alpha = 0.98f),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(bottom = 30.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("AuraOS 16 Pro Extreme", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Центр управления мощной ОС • Ядро Android 16", fontSize = 12.sp, color = Color(0xFF00E5FF))
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFF3D00).copy(alpha = 0.25f))
                        .clickable {
                            onDismiss()
                            onOpenSpecsAndBooster()
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("144 FPS BOOST", color = Color(0xFFFF6D00), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Toggles Grid (2 Rows of Big Buttons)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickToggleTile(
                    title = "Wi-Fi 7",
                    subtitle = if (wifiActive) "Ultra-6G (5.8 Gbps)" else "Выкл",
                    icon = Icons.Default.Wifi,
                    isActive = wifiActive,
                    onClick = { wifiActive = !wifiActive },
                    modifier = Modifier.weight(1f)
                )
                QuickToggleTile(
                    title = "Bluetooth 5.4",
                    subtitle = if (bluetoothActive) "AuraPods (Нажмите для меню)" else "Выкл",
                    icon = Icons.Default.Bluetooth,
                    isActive = bluetoothActive,
                    onClick = {
                        onDismiss()
                        onOpenBluetoothManager()
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickToggleTile(
                    title = "5G Ultra",
                    subtitle = if (cellularActive) "Гигабит (10 Gbps)" else "Выкл",
                    icon = Icons.Default.NetworkCell,
                    isActive = cellularActive,
                    onClick = { cellularActive = !cellularActive },
                    modifier = Modifier.weight(1f)
                )
                QuickToggleTile(
                    title = "Anti-Lag Turbo",
                    subtitle = if (turboBoostActive) "4.3 GHz • 144 FPS" else "Стандарт",
                    icon = Icons.Default.Speed,
                    isActive = turboBoostActive,
                    activeColor = Color(0xFFFF3D00),
                    onClick = {
                        onDismiss()
                        onOpenSpecsAndBooster()
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Mini toggles row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MiniToggleIcon(Icons.Default.FlashlightOn, "Фонарик", flashlightActive) { flashlightActive = !flashlightActive }
                MiniToggleIcon(Icons.Default.DarkMode, "Тёмная тема", darkModeActive) { darkModeActive = !darkModeActive }
                MiniToggleIcon(Icons.Default.AirplanemodeActive, "Авиарежим", airplaneActive) { airplaneActive = !airplaneActive }
                MiniToggleIcon(Icons.Default.AutoAwesome, "AI Движок", true) {}
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sliders (Brightness & Volume)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E232F)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.BrightnessMedium, null, tint = Color(0xFFFFD54F), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Яркость экрана 120Hz LTPO", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                    Slider(
                        value = brightness,
                        onValueChange = { brightness = it },
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFFFD54F),
                            activeTrackColor = Color(0xFFFFD54F)
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VolumeUp, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Громкость динамиков Dolby Atmos", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                    Slider(
                        value = volume,
                        onValueChange = { volume = it },
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF00E5FF),
                            activeTrackColor = Color(0xFF00E5FF)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Powerful Hardware & OS Specs
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B202B)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        onDismiss()
                        onOpenSpecsAndBooster()
                    }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Memory, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Snapdragon 8 Gen 4 • 24 GB RAM", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("36 GB Turbo", fontSize = 12.sp, color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { 0.22f },
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        color = Color(0xFF00E5FF)
                    )
                    Text("5.2 GB используется • 30.8 GB свободно (10.7 Gbps)", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Storage, null, tint = Color(0xFFFF9100), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Хранилище UFS 4.1 Pro (4800 MB/s)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("1024 GB (1 TB)", fontSize = 12.sp, color = Color(0xFFFF9100), fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { 0.12f },
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        color = Color(0xFFFF9100)
                    )
                    Text("124 GB используется • 900 GB свободно (Room SQLite)", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Нажмите для снятия лагов и полного отчета →",
                            fontSize = 11.sp,
                            color = Color(0xFFFF9100),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickToggleTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit,
    activeColor: Color = Color(0xFF1E88E5),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(68.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) activeColor.copy(alpha = 0.85f) else Color(0xFF222733)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (isActive) Color.White.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                Text(subtitle, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun MiniToggleIcon(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (isActive) Color(0xFF00A86B) else Color(0xFF222733)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
    }
}

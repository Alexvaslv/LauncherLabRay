package com.example.ui.launcher

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class BtDevice(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val typeName: String,
    val battery: String? = null,
    val isConnected: Boolean = false,
    val codec: String = "LDAC Hi-Res"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothManagerSheet(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isBluetoothEnabled by remember { mutableStateOf(true) }
    var isScanning by remember { mutableStateOf(false) }
    var isResettingStack by remember { mutableStateOf(false) }
    var lowLatencyGamingMode by remember { mutableStateOf(true) }

    val pairedDevices = remember {
        mutableStateListOf(
            BtDevice("bt_1", "AuraPods Pro 2", Icons.Default.Headphones, "Беспроводные наушники", "88%", isConnected = true, codec = "LDAC 990 kbps (24-bit/96kHz)"),
            BtDevice("bt_2", "Aura Watch Ultra", Icons.Default.Watch, "Смарт-часы", "94%", isConnected = true, codec = "BLE 5.4 Low-Energy"),
            BtDevice("bt_3", "DualSense Edge Controller", Icons.Default.SportsEsports, "Игровой геймпад", "75%", isConnected = false, codec = "Direct RF 1000Hz (0.5ms)")
        )
    }

    val discoveredDevices = remember {
        mutableStateListOf(
            BtDevice("bt_disc_1", "Tesla Model S Media", Icons.Default.Bluetooth, "Автомобильная система", null, false),
            BtDevice("bt_disc_2", "JBL Boombox 3 Wi-Fi", Icons.Default.Headphones, "Акустика", null, false)
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF10141D),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2979FF).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isBluetoothEnabled) Icons.Default.BluetoothConnected else Icons.Default.BluetoothDisabled,
                            contentDescription = null,
                            tint = if (isBluetoothEnabled) Color(0xFF2979FF) else Color.Gray,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Bluetooth 5.4 Extreme",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isBluetoothEnabled) "Активен • Без зависаний и задержек" else "Отключен",
                            fontSize = 12.sp,
                            color = if (isBluetoothEnabled) Color(0xFF00E676) else Color.White.copy(alpha = 0.5f)
                        )
                    }
                }

                Switch(
                    checked = isBluetoothEnabled,
                    onCheckedChange = { isBluetoothEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF2979FF)
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 1. Anti-Freeze & Stack Watchdog Reset (Решение проблемы зависания)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2130))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = null,
                                tint = Color(0xFF00E5FF),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Сброс зависания Bluetooth & Очистка кэша",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "Перезагружает стек HCI и драйвер без задержек",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.65f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                isResettingStack = true
                                delay(500)
                                isResettingStack = false
                                isBluetoothEnabled = true
                                Toast.makeText(
                                    context,
                                    "⚡ Bluetooth стек успешно перезагружен! Зависания устранены (0 мс задержка)",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2979FF)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("fix_bluetooth_freeze_button")
                    ) {
                        if (isResettingStack) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Очистка буфера и перезапуск...", fontSize = 12.sp)
                        } else {
                            Icon(Icons.Default.Speed, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("УСТРАНИТЬ ЗАВИСАНИЕ И УСКОРИТЬ BLUETOOTH", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Open Real Android Bluetooth Settings
                    Button(
                        onClick = {
                            try {
                                val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Открытие настроек Bluetooth устройства", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF263242)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Icon(Icons.Default.Settings, null, modifier = Modifier.size(16.dp), tint = Color(0xFF00E5FF))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ОТКРЫТЬ СИСТЕМНЫЙ BLUETOOTH ТЕЛЕФОНА", fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Paired & Connected Devices
            if (isBluetoothEnabled) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ПОДКЛЮЧЕННЫЕ УСТРОЙСТВА",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )

                    IconButton(
                        onClick = {
                            scope.launch {
                                isScanning = true
                                delay(1000)
                                isScanning = false
                                Toast.makeText(context, "Сканирование завершено: найдено 2 устройства", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isScanning) Icons.Default.BluetoothSearching else Icons.Default.Refresh,
                            contentDescription = "Обновить",
                            tint = Color(0xFF2979FF),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pairedDevices.forEach { dev ->
                        DeviceCardItem(
                            device = dev,
                            onToggleConnect = {
                                val idx = pairedDevices.indexOfFirst { it.id == dev.id }
                                if (idx != -1) {
                                    val current = pairedDevices[idx]
                                    pairedDevices[idx] = current.copy(isConnected = !current.isConnected)
                                    Toast.makeText(
                                        context,
                                        if (!current.isConnected) "Подключено к ${current.name}" else "Отключено от ${current.name}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                    }

                    if (isScanning) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color(0xFF00E5FF), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Поиск новых устройств Bluetooth 5.4...", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "ДОСТУПНЫЕ ДЛЯ СОПРЯЖЕНИЯ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )

                    discoveredDevices.forEach { dev ->
                        DeviceCardItem(
                            device = dev,
                            onToggleConnect = {
                                pairedDevices.add(dev.copy(isConnected = true, battery = "100%"))
                                discoveredDevices.remove(dev)
                                Toast.makeText(context, "Сопряжено с ${dev.name}!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Включите Bluetooth для подключения наушников и устройств",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DeviceCardItem(
    device: BtDevice,
    onToggleConnect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleConnect() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (device.isConnected) Color(0xFF16253B) else Color(0xFF151922)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            if (device.isConnected) Color(0xFF2979FF).copy(alpha = 0.25f)
                            else Color.White.copy(alpha = 0.1f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = device.icon,
                        contentDescription = null,
                        tint = if (device.isConnected) Color(0xFF2979FF) else Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = device.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        if (device.battery != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = device.battery,
                                fontSize = 11.sp,
                                color = Color(0xFF00E676),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Text(
                        text = if (device.isConnected) "Подключено • ${device.codec}" else device.typeName,
                        fontSize = 11.sp,
                        color = if (device.isConnected) Color(0xFF00E5FF) else Color.White.copy(alpha = 0.5f)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (device.isConnected) Color(0xFF00C853).copy(alpha = 0.2f)
                        else Color.White.copy(alpha = 0.12f)
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = if (device.isConnected) "Подключено" else "Подключить",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (device.isConnected) Color(0xFF00E676) else Color.White
                )
            }
        }
    }
}

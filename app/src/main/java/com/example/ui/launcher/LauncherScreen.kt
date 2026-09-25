package com.example.ui.launcher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.launcher.AppItem
import com.example.data.launcher.FolderItem
import com.example.data.launcher.GridItem
import com.example.data.launcher.LauncherDataProvider
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LauncherScreen(
    onOpenAiChat: (initialPrompt: String?) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenPlayMarket: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedFolder by remember { mutableStateOf<FolderItem?>(null) }
    var selectedAppDetail by remember { mutableStateOf<AppItem?>(null) }
    var showWeatherSheet by remember { mutableStateOf(false) }
    var showClockSheet by remember { mutableStateOf(false) }
    var showSearchSheet by remember { mutableStateOf(false) }
    var showControlCenter by remember { mutableStateOf(false) }
    var showSpecsAndBooster by remember { mutableStateOf(false) }
    var showBluetoothManager by remember { mutableStateOf(false) }
    var showApkExportSheet by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 1. Wallpaper Background (Fluid purple/violet silk waves)
        Image(
            painter = painterResource(id = R.drawable.img_launcher_wallpaper),
            contentDescription = "Launcher Wallpaper",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Subtle gradient overlay for extra legibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.45f)
                        )
                    )
                )
        )

        // 2. Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section (Status Bar + Widgets)
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top Status Bar Replica (Clickable to open OS Control Center)
                LauncherTopStatusBar(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showControlCenter = true }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Widgets Row: Weather (Left) + Analog Clock (Right)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    WeatherWidget(
                        city = "Long Island City",
                        temp = "27°",
                        condition = "Mostly cloudy",
                        highLow = "H:35° L:25°",
                        onClick = { showWeatherSheet = true },
                        modifier = Modifier.weight(1f)
                    )

                    AnalogClockWidget(
                        onClick = { showClockSheet = true },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Middle App Grid (4 Columns)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(LauncherDataProvider.gridItems) { item ->
                        when (item) {
                            is GridItem.SingleApp -> {
                                LauncherAppItemView(
                                    app = item.app,
                                    onClick = {
                                        if (item.app.id == "app_settings_row" || item.app.name.contains("Settings", true)) {
                                            onOpenSettings()
                                        } else if (item.app.id == "sys_specs" || item.app.id == "sys_booster" || item.app.id == "app_booster" || item.app.id == "app_specs" || item.app.name.contains("Ускорение", true) || item.app.name.contains("Характеристики", true)) {
                                            showSpecsAndBooster = true
                                        } else if (item.app.id == "sys_bluetooth" || item.app.id == "app_bluetooth" || item.app.name.contains("Bluetooth", true)) {
                                            showBluetoothManager = true
                                        } else if (item.app.id == "sys_apk" || item.app.id == "app_apk" || item.app.name.contains("APK", true)) {
                                            showApkExportSheet = true
                                        } else {
                                            selectedAppDetail = item.app
                                        }
                                    }
                                )
                            }
                            is GridItem.Folder -> {
                                FolderIconPreview(
                                    folder = item.folder,
                                    onClick = { expandedFolder = item.folder },
                                    size = 58.dp
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Section (Search pill + Dock)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Frosted Glass Search Capsule
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.22f))
                        .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                        .clickable { showSearchSheet = true }
                        .padding(horizontal = 20.dp, vertical = 7.dp)
                        .testTag("launcher_search_capsule"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Search",
                            color = Color.White.copy(alpha = 0.95f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.7f),
                                    offset = Offset(0f, 1f),
                                    blurRadius = 2f
                                )
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Dock (Chrome, Settings, Play Store, Calendar)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LauncherDataProvider.dockApps.forEach { dockApp ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    if (dockApp.name == "Settings") {
                                        onOpenSettings()
                                    } else if (dockApp.name == "Play Store" || dockApp.id == "dock_play") {
                                        onOpenPlayMarket()
                                    } else {
                                        selectedAppDetail = dockApp
                                    }
                                }
                                .padding(4.dp)
                                .testTag("dock_${dockApp.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            AppIconRenderer(app = dockApp, size = 58.dp)
                        }
                    }
                }
            }
        }

        // 3. Modals and Dialogs
        if (expandedFolder != null) {
            FolderExpandedDialog(
                folder = expandedFolder!!,
                onAppClick = { app ->
                    expandedFolder = null
                    if (app.id == "prod_ai" || app.name.contains("AI Studio", true)) {
                        onOpenAiChat(null)
                    } else if (app.name == "Settings") {
                        onOpenSettings()
                    } else if (app.name == "Play Store" || app.id == "g_play") {
                        onOpenPlayMarket()
                    } else if (app.id == "sys_specs" || app.id == "sys_booster" || app.id == "app_booster" || app.id == "app_specs" || app.name.contains("Ускорение", true) || app.name.contains("Характеристики", true)) {
                        showSpecsAndBooster = true
                    } else if (app.id == "sys_bluetooth" || app.id == "app_bluetooth" || app.name.contains("Bluetooth", true)) {
                        showBluetoothManager = true
                    } else if (app.id == "sys_apk" || app.id == "app_apk" || app.name.contains("APK", true)) {
                        showApkExportSheet = true
                    } else {
                        selectedAppDetail = app
                    }
                },
                onDismiss = { expandedFolder = null }
            )
        }

        if (selectedAppDetail != null) {
            AppDetailSheet(
                app = selectedAppDetail!!,
                onLaunchAiStudio = {
                    val app = selectedAppDetail
                    selectedAppDetail = null
                    onOpenAiChat(null)
                },
                onDismiss = { selectedAppDetail = null }
            )
        }

        if (showWeatherSheet) {
            WeatherDetailSheet(onDismiss = { showWeatherSheet = false })
        }

        if (showClockSheet) {
            ClockDetailSheet(onDismiss = { showClockSheet = false })
        }

        if (showControlCenter) {
            ControlCenterSheet(
                onOpenSpecsAndBooster = { showSpecsAndBooster = true },
                onOpenBluetoothManager = { showBluetoothManager = true },
                onDismiss = { showControlCenter = false }
            )
        }

        if (showSpecsAndBooster) {
            PhoneSpecsAndBoosterSheet(onDismiss = { showSpecsAndBooster = false })
        }

        if (showBluetoothManager) {
            BluetoothManagerSheet(onDismiss = { showBluetoothManager = false })
        }

        if (showApkExportSheet) {
            ApkExportSheet(onDismiss = { showApkExportSheet = false })
        }

        if (showSearchSheet) {
            SearchAppSheet(
                onAppSelect = { app ->
                    if (app.id == "prod_ai") {
                        onOpenAiChat(null)
                    } else if (app.name == "Settings") {
                        onOpenSettings()
                    } else if (app.name == "Play Store" || app.id == "g_play") {
                        onOpenPlayMarket()
                    } else if (app.id == "sys_specs" || app.id == "sys_booster" || app.id == "app_booster" || app.id == "app_specs" || app.name.contains("Ускорение", true) || app.name.contains("Характеристики", true)) {
                        showSpecsAndBooster = true
                    } else if (app.id == "sys_bluetooth" || app.id == "app_bluetooth" || app.name.contains("Bluetooth", true)) {
                        showBluetoothManager = true
                    } else if (app.id == "sys_apk" || app.id == "app_apk" || app.name.contains("APK", true)) {
                        showApkExportSheet = true
                    } else {
                        selectedAppDetail = app
                    }
                },
                onAiPrompt = { prompt ->
                    onOpenAiChat(prompt)
                },
                onDismiss = { showSearchSheet = false }
            )
        }
    }
}

@Composable
fun LauncherAppItemView(
    app: AppItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 2.dp)
            .testTag("app_${app.id}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppIconRenderer(app = app, size = 58.dp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = app.name,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.85f),
                    offset = Offset(0f, 1.5f),
                    blurRadius = 3f
                )
            )
        )
    }
}

@Composable
fun LauncherTopStatusBar(modifier: Modifier = Modifier) {
    var currentTime by remember {
        mutableStateOf(SimpleDateFormat("08:47", Locale.getDefault()).format(Date()))
    }

    LaunchedEffect(Unit) {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        while (true) {
            currentTime = format.format(Date())
            delay(10000)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Time & notification icons (YouTube, weather, Chrome, AI)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = currentTime,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                style = TextStyle(
                    shadow = Shadow(color = Color.Black.copy(alpha = 0.7f), offset = Offset(0f, 1f), blurRadius = 2f)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color(0xFF00E5FF),
                modifier = Modifier.size(13.dp)
            )
        }

        // Right: VoLTE, 4G, Network Signal, Battery 71%
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "VoLTE",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "4G",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.NetworkCell,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "71%",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Default.BatteryChargingFull,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

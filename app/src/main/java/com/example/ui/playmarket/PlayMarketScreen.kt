package com.example.ui.playmarket

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.launcher.AppIconType
import com.example.data.launcher.AppItem
import com.example.data.launcher.CustomIconType
import com.example.data.playmarket.MarketApp
import com.example.data.playmarket.PlayMarketCatalog
import com.example.ui.launcher.AppIconRenderer
import com.example.ui.launcher.PlayStoreIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayMarketScreen(
    viewModel: PlayMarketViewModel = viewModel(),
    onBackToLauncher: () -> Unit,
    onLaunchAiChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    BackHandler {
        onBackToLauncher()
    }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    val filteredApps = remember(uiState.searchQuery, uiState.selectedCategoryTab) {
        var list = PlayMarketCatalog.allMarketApps
        if (uiState.searchQuery.isNotBlank()) {
            list = list.filter {
                it.name.contains(uiState.searchQuery, ignoreCase = true) ||
                it.category.contains(uiState.searchQuery, ignoreCase = true)
            }
        } else if (uiState.selectedCategoryTab == "Топ чарты") {
            list = list.sortedByDescending { it.rating }
        } else if (uiState.selectedCategoryTab == "Категории") {
            list = list.sortedBy { it.category }
        }
        list
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        containerColor = Color(0xFF121316),
        topBar = {
            PlayMarketTopBar(
                searchQuery = uiState.searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                onBack = onBackToLauncher
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Category Tabs
            PlayMarketCategoryTabs(
                selectedTab = uiState.selectedCategoryTab,
                onSelectTab = { viewModel.selectCategoryTab(it) }
            )

            // App list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Featured Hero Banner
                if (uiState.searchQuery.isBlank()) {
                    item {
                        FeaturedAppBanner(
                            onInstallOrOpen = {
                                val chatGptApp = PlayMarketCatalog.allMarketApps.first { it.id == "market_chatgpt" }
                                if (uiState.installedAppIds.contains(chatGptApp.id)) {
                                    onLaunchAiChat()
                                } else {
                                    viewModel.installApp(chatGptApp)
                                }
                            },
                            isInstalled = uiState.installedAppIds.contains("market_chatgpt"),
                            isInstalling = uiState.installingAppId == "market_chatgpt",
                            progress = uiState.installProgress
                        )
                    }

                    item {
                        Text(
                            text = "Рекомендуемые приложения",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                // App Cards
                items(filteredApps, key = { it.id }) { app ->
                    val isInstalled = uiState.installedAppIds.contains(app.id)
                    val isInstalling = uiState.installingAppId == app.id

                    PlayMarketAppRow(
                        app = app,
                        isInstalled = isInstalled,
                        isInstalling = isInstalling,
                        installProgress = if (isInstalling) uiState.installProgress else 0,
                        onAppClick = { viewModel.openAppDetail(app) },
                        onInstallClick = {
                            if (isInstalled) {
                                if (app.id == "market_chatgpt") {
                                    onLaunchAiChat()
                                } else {
                                    viewModel.openAppDetail(app)
                                }
                            } else {
                                viewModel.installApp(app)
                            }
                        }
                    )
                }
            }
        }
    }

    // App Detail Bottom Sheet
    if (uiState.selectedAppForDetail != null) {
        val app = uiState.selectedAppForDetail!!
        val isInstalled = uiState.installedAppIds.contains(app.id)
        val isInstalling = uiState.installingAppId == app.id

        PlayMarketDetailSheet(
            app = app,
            isInstalled = isInstalled,
            isInstalling = isInstalling,
            installProgress = uiState.installProgress,
            onInstall = { viewModel.installApp(app) },
            onUninstall = { viewModel.uninstallApp(app.id) },
            onLaunch = {
                if (app.id == "market_chatgpt") {
                    viewModel.closeAppDetail()
                    onLaunchAiChat()
                } else {
                    Toast.makeText(context, "Запуск «${app.name}»", Toast.LENGTH_SHORT).show()
                }
            },
            onDismiss = { viewModel.closeAppDetail() }
        )
    }
}

@Composable
fun PlayMarketTopBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF1E2024),
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Search pill resembling Google Play
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFF2C2F36))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onQueryChange,
                        placeholder = {
                            Text(
                                "Поиск в Google Play...",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Clear, null, tint = Color.White.copy(alpha = 0.6f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // User Avatar circle
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00897B)),
                contentAlignment = Alignment.Center
            ) {
                Text("A", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun PlayMarketCategoryTabs(
    selectedTab: String,
    onSelectTab: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("Для вас", "Топ чарты", "Категории", "Дети", "Премиум")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tabs.forEach { tab ->
            FilterChip(
                selected = selectedTab == tab,
                onClick = { onSelectTab(tab) },
                label = { Text(tab, fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF00A86B),
                    selectedLabelColor = Color.White,
                    containerColor = Color(0xFF22252C),
                    labelColor = Color.White.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
fun FeaturedAppBanner(
    onInstallOrOpen: () -> Unit,
    isInstalled: Boolean,
    isInstalling: Boolean,
    progress: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B2A32)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0D3236), Color(0xFF142127))
                    )
                )
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF00A86B))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        "ВЫБОР РЕДАКЦИИ AURAOS",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // ChatGPT icon
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF10A37F)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ChatGPT (gpt-5.6-sol)",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Самый продвинутый AI ассистент",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("4.9 ★", color = Color(0xFFFFD54F), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("100M+ скачиваний", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                    }
                }
            }

            if (isInstalling) {
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = Color(0xFF00C853)
                )
                Text(
                    "Установка в память телефона: $progress%",
                    color = Color(0xFF00C853),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = onInstallOrOpen,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isInstalled) Color(0xFF00897B) else Color(0xFF00A86B)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(42.dp)
                ) {
                    Text(
                        if (isInstalled) "Открыть приложение" else "Установить бесплатно",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun PlayMarketAppRow(
    app: MarketApp,
    isInstalled: Boolean,
    isInstalling: Boolean,
    installProgress: Int,
    onAppClick: () -> Unit,
    onInstallClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onAppClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2128)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                AppIconRenderer(
                    app = AppItem(
                        id = app.id,
                        name = app.name,
                        iconType = AppIconType.Custom(
                            try {
                                CustomIconType.valueOf(app.iconKey)
                            } catch (e: Exception) {
                                CustomIconType.SETTINGS
                            }
                        )
                    ),
                    size = 54.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = app.name,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = app.category,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${app.rating} ★",
                            color = Color(0xFFFFD54F),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${app.sizeMb}",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Action Button
                if (isInstalling) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            progress = { installProgress / 100f },
                            modifier = Modifier.size(36.dp),
                            color = Color(0xFF00C853),
                            strokeWidth = 3.dp
                        )
                        Text("$installProgress%", fontSize = 10.sp, color = Color(0xFF00C853))
                    }
                } else {
                    Button(
                        onClick = onInstallClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isInstalled) Color(0xFF2E3842) else Color(0xFF00A86B)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text(
                            text = if (isInstalled) "Открыть" else "Установить",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isInstalled) Color(0xFF00E5FF) else Color.White
                        )
                    }
                }
            }

            if (isInstalling) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { installProgress / 100f },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = Color(0xFF00C853)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayMarketDetailSheet(
    app: MarketApp,
    isInstalled: Boolean,
    isInstalling: Boolean,
    installProgress: Int,
    onInstall: () -> Unit,
    onUninstall: () -> Unit,
    onLaunch: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF181B22),
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppIconRenderer(
                    app = AppItem(
                        id = app.id,
                        name = app.name,
                        iconType = AppIconType.Custom(
                            try {
                                CustomIconType.valueOf(app.iconKey)
                            } catch (e: Exception) {
                                CustomIconType.SETTINGS
                            }
                        )
                    ),
                    size = 72.dp
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(text = app.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = app.developer, fontSize = 13.sp, color = Color(0xFF00E5FF))
                    Text(text = app.category, fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Rating / Downloads stats row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF222630), RoundedCornerShape(14.dp))
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${app.rating} ★", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFFFFD54F))
                    Text(app.reviewsCount, fontSize = 10.sp, color = Color.White.copy(alpha = 0.5f))
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.White.copy(alpha = 0.2f)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(app.downloads, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Скачиваний", fontSize = 10.sp, color = Color.White.copy(alpha = 0.5f))
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.White.copy(alpha = 0.2f)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(app.sizeMb, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Размер файла", fontSize = 10.sp, color = Color.White.copy(alpha = 0.5f))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons
            if (isInstalling) {
                LinearProgressIndicator(
                    progress = { installProgress / 100f },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color(0xFF00C853)
                )
                Text(
                    "Установка в постоянную память: $installProgress%",
                    color = Color(0xFF00C853),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            } else if (isInstalled) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onUninstall,
                        modifier = Modifier.weight(1f).height(46.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Удалить", color = Color(0xFFFF5252))
                    }
                    Button(
                        onClick = onLaunch,
                        modifier = Modifier.weight(1.5f).height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A86B))
                    ) {
                        Text("Открыть", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Button(
                    onClick = onInstall,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A86B))
                ) {
                    Icon(Icons.Default.Download, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Установить на телефон", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Play Protect badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Security, null, tint = Color(0xFF00C853), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Проверено Google Play Protect • Безопасно",
                    color = Color(0xFF00C853),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Description
            Text("О приложении", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = app.description,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

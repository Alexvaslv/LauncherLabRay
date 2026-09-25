package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.ChatScreen
import com.example.ui.ChatViewModel
import com.example.ui.ServerSettingsDialog
import com.example.ui.launcher.LauncherScreen
import com.example.ui.playmarket.PlayMarketScreen
import com.example.ui.theme.MyApplicationTheme

enum class MainDestination {
    LAUNCHER,
    AI_CHAT,
    PLAY_MARKET
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppNavigation()
            }
        }
    }
}

@Composable
fun MainAppNavigation(
    chatViewModel: ChatViewModel = viewModel()
) {
    var destination by remember { mutableStateOf(MainDestination.LAUNCHER) }
    var initialChatPrompt by remember { mutableStateOf<String?>(null) }
    val chatUiState by chatViewModel.uiState.collectAsState()

    Crossfade(
        targetState = destination,
        animationSpec = tween(durationMillis = 280),
        label = "screen_transition"
    ) { currentScreen ->
        when (currentScreen) {
            MainDestination.LAUNCHER -> {
                LauncherScreen(
                    onOpenAiChat = { prompt ->
                        initialChatPrompt = prompt
                        destination = MainDestination.AI_CHAT
                    },
                    onOpenSettings = {
                        chatViewModel.openSettingsDialog()
                    },
                    onOpenPlayMarket = {
                        destination = MainDestination.PLAY_MARKET
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // If settings was opened from launcher
                if (chatUiState.showSettingsDialog) {
                    ServerSettingsDialog(
                        currentUrl = chatUiState.serverUrl,
                        isDemoMode = chatUiState.isDemoMode,
                        healthState = chatUiState.healthCheckState,
                        onSave = { newUrl ->
                            chatViewModel.updateServerUrl(newUrl)
                            chatViewModel.closeSettingsDialog()
                        },
                        onCheckHealth = { chatViewModel.checkServerHealth() },
                        onToggleDemo = { chatViewModel.toggleDemoMode(it) },
                        onDismiss = { chatViewModel.closeSettingsDialog() }
                    )
                }
            }
            MainDestination.AI_CHAT -> {
                ChatScreen(
                    viewModel = chatViewModel,
                    initialPrompt = initialChatPrompt,
                    onBackToLauncher = {
                        initialChatPrompt = null
                        destination = MainDestination.LAUNCHER
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            MainDestination.PLAY_MARKET -> {
                PlayMarketScreen(
                    onBackToLauncher = {
                        destination = MainDestination.LAUNCHER
                    },
                    onLaunchAiChat = {
                        destination = MainDestination.AI_CHAT
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

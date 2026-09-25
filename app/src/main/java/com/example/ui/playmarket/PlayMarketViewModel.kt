package com.example.ui.playmarket

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.InstalledAppEntity
import com.example.data.playmarket.MarketApp
import com.example.data.playmarket.PlayMarketCatalog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlayMarketUiState(
    val selectedCategoryTab: String = "Для вас",
    val searchQuery: String = "",
    val installingAppId: String? = null,
    val installProgress: Int = 0,
    val installedAppIds: Set<String> = emptySet(),
    val selectedAppForDetail: MarketApp? = null,
    val toastMessage: String? = null
)

class PlayMarketViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val chatDao = database.chatDao()

    private val _uiState = MutableStateFlow(PlayMarketUiState())
    val uiState: StateFlow<PlayMarketUiState> = _uiState.asStateFlow()

    init {
        // Observe installed apps from Room SQLite database
        viewModelScope.launch {
            chatDao.getInstalledApps().collect { installedList ->
                val installedIds = installedList.map { it.id }.toSet()
                _uiState.update { it.copy(installedAppIds = installedIds) }
            }
        }
    }

    fun selectCategoryTab(tab: String) {
        _uiState.update { it.copy(selectedCategoryTab = tab) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun openAppDetail(app: MarketApp) {
        _uiState.update { it.copy(selectedAppForDetail = app) }
    }

    fun closeAppDetail() {
        _uiState.update { it.copy(selectedAppForDetail = null) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun installApp(app: MarketApp) {
        if (_uiState.value.installingAppId != null) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    installingAppId = app.id,
                    installProgress = 0,
                    toastMessage = "Загрузка ${app.name}..."
                )
            }

            // Simulate realistic fast download and verification
            val steps = listOf(15, 38, 62, 85, 95, 100)
            for (p in steps) {
                delay(220)
                _uiState.update { it.copy(installProgress = p) }
            }

            delay(200)

            // Save to Room persistent database
            chatDao.insertInstalledApp(
                InstalledAppEntity(
                    id = app.id,
                    name = app.name,
                    category = app.category,
                    developer = app.developer,
                    rating = app.rating,
                    reviewsCount = app.reviewsCount,
                    sizeMb = app.sizeMb,
                    downloads = app.downloads,
                    description = app.description,
                    iconKey = app.iconKey
                )
            )

            _uiState.update {
                it.copy(
                    installingAppId = null,
                    installProgress = 0,
                    toastMessage = "«${app.name}» успешно установлено в память телефона!"
                )
            }
        }
    }

    fun uninstallApp(appId: String) {
        viewModelScope.launch {
            chatDao.uninstallApp(appId)
            _uiState.update { it.copy(toastMessage = "Приложение удалено") }
        }
    }
}

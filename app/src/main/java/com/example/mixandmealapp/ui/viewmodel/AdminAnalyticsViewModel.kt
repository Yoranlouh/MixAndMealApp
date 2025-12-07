package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.AnalyticsRepository
import com.example.mixandmealapp.data.ServiceLocator
import kotlinx.coroutines.launch

data class AdminAnalyticsUiState(
    val period: String = "7d",
    val metrics: Map<String, Number> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class AdminAnalyticsViewModel(
    private val repo: AnalyticsRepository = ServiceLocator.analyticsRepository
) : ViewModel() {
    var uiState by mutableStateOf(AdminAnalyticsUiState())
        private set

    fun setPeriod(p: String) { uiState = uiState.copy(period = p) }

    fun refresh() {
        uiState = uiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val metrics = repo.getMetrics(uiState.period)
                uiState = uiState.copy(metrics = metrics, isLoading = false)
            } catch (t: Throwable) {
                uiState = uiState.copy(isLoading = false, error = t.message)
            }
        }
    }
}

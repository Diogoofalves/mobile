package com.tpbcontrol.mobile.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tpbcontrol.mobile.data.TPBRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: TPBRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun load(userType: String?) {
        if (userType.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(
                loading = false,
                error = "Tipo de usuario nao encontrado"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)

            try {
                val dashboardDeferred = async { repository.getDashboard() }
                val activityDeferred = async { repository.getRecentActivity() }
                val pendingDeferred = if (userType == "veterinario") {
                    async { repository.getPendingVerifications() }
                } else null

                val dashboard = dashboardDeferred.await()
                val activity = activityDeferred.await()
                val pending = pendingDeferred?.await().orEmpty()

                _uiState.value = DashboardUiState(
                    loading = false,
                    data = dashboard,
                    pendingReviews = pending,
                    recentActivity = activity,
                    error = null
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = error.message ?: "Nao foi possivel carregar o dashboard"
                )
            }
        }
    }

    companion object {
        fun provideFactory(repository: TPBRepository) = viewModelFactory {
            initializer {
                DashboardViewModel(repository)
            }
        }
    }
}

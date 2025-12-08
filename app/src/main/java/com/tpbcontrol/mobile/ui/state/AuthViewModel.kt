package com.tpbcontrol.mobile.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tpbcontrol.mobile.data.TPBRepository
import com.tpbcontrol.mobile.data.remote.dto.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: TPBRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.tokenFlow.collectLatest { token ->
                if (!token.isNullOrBlank()) {
                    fetchProfile()
                } else {
                    _uiState.value = AuthUiState()
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                val user = repository.login(email, password)
                _uiState.value = AuthUiState(
                    isAuthenticated = true,
                    user = user,
                    loading = false,
                    error = null
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = error.message ?: "Nao foi possivel entrar"
                )
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                val user = repository.register(request)
                _uiState.value = AuthUiState(
                    isAuthenticated = true,
                    user = user,
                    loading = false,
                    error = null
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = error.message ?: "Nao foi possivel registrar"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _uiState.value = AuthUiState(isAuthenticated = false, user = null)
        }
    }

    fun refreshProfile() {
        viewModelScope.launch {
            fetchProfile()
        }
    }

    private suspend fun fetchProfile() {
        try {
            val user = repository.getProfile()
            _uiState.value = AuthUiState(
                isAuthenticated = true,
                user = user,
                loading = false,
                error = null
            )
        } catch (error: Exception) {
            _uiState.value = _uiState.value.copy(
                loading = false,
                isAuthenticated = false,
                error = "Sessao expirada. faca login novamente."
            )
            repository.logout()
        }
    }

    companion object {
        fun provideFactory(repository: TPBRepository) = viewModelFactory {
            initializer {
                AuthViewModel(repository)
            }
        }
    }
}

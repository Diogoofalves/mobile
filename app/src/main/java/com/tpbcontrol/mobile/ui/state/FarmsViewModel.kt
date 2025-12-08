package com.tpbcontrol.mobile.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tpbcontrol.mobile.data.TPBRepository
import com.tpbcontrol.mobile.data.remote.dto.FarmDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FarmsViewModel(
    private val repository: TPBRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState<FarmDto>(loading = true))
    val uiState: StateFlow<ListUiState<FarmDto>> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                val farms = repository.getMyFarms()
                _uiState.value = ListUiState(
                    loading = false,
                    items = farms,
                    error = null
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = error.message ?: "Erro ao buscar fazendas"
                )
            }
        }
    }

    companion object {
        fun provideFactory(repository: TPBRepository) = viewModelFactory {
            initializer {
                FarmsViewModel(repository)
            }
        }
    }
}

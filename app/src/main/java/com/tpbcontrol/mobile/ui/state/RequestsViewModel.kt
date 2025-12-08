package com.tpbcontrol.mobile.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tpbcontrol.mobile.data.TPBRepository
import com.tpbcontrol.mobile.data.remote.dto.MedicationRequestDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RequestsViewModel(
    private val repository: TPBRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState<MedicationRequestDto>(loading = true))
    val uiState: StateFlow<ListUiState<MedicationRequestDto>> = _uiState.asStateFlow()

    fun load(farmId: Int? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                val requests = repository.listRequests(farmId = farmId)
                _uiState.value = ListUiState(
                    loading = false,
                    items = requests,
                    error = null
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = error.message ?: "Erro ao buscar solicitacoes"
                )
            }
        }
    }

    companion object {
        fun provideFactory(repository: TPBRepository) = viewModelFactory {
            initializer {
                RequestsViewModel(repository)
            }
        }
    }
}

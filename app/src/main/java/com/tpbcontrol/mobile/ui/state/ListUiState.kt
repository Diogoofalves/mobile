package com.tpbcontrol.mobile.ui.state

data class ListUiState<T>(
    val loading: Boolean = false,
    val items: List<T> = emptyList(),
    val error: String? = null
)

package com.tpbcontrol.mobile.ui.state

import com.tpbcontrol.mobile.data.remote.dto.UserDto

data class AuthUiState(
    val isAuthenticated: Boolean = false,
    val user: UserDto? = null,
    val loading: Boolean = false,
    val error: String? = null
)

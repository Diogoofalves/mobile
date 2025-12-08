package com.tpbcontrol.mobile.ui.state

import com.tpbcontrol.mobile.data.remote.dto.ActivityItemDto
import com.tpbcontrol.mobile.data.remote.dto.DashboardResponse
import com.tpbcontrol.mobile.data.remote.dto.MedicationRequestDto

data class DashboardUiState(
    val loading: Boolean = true,
    val data: DashboardResponse? = null,
    val pendingReviews: List<MedicationRequestDto> = emptyList(),
    val recentActivity: List<ActivityItemDto> = emptyList(),
    val error: String? = null
)

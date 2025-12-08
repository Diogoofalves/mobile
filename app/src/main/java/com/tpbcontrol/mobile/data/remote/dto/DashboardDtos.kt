package com.tpbcontrol.mobile.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    val userType: String,
    val stats: DashboardStats? = null,
    val lastUpdated: String? = null
)

@Serializable
data class DashboardStats(
    val farms: FarmStats? = null,
    val team: TeamStats? = null,
    val requests: RequestStats? = null,
    val verifications: VerificationStats? = null,
    val tasks: TaskStats? = null
)

@Serializable
data class FarmStats(
    val total: Int = 0,
    val active: Int = 0
)

@Serializable
data class TeamStats(
    val veterinarians: Int = 0,
    val workers: Int = 0,
    val total: Int = 0
)

@Serializable
data class RequestStats(
    val total: Int? = null,
    val pending: Int? = null,
    val completed: Int? = null,
    val overdue: Int? = null,
    val todayCompleted: Int? = null,
    val weekCompleted: Int? = null,
    val monthCompleted: Int? = null
)

@Serializable
data class VerificationStats(
    val pending: Int? = null,
    val todayVerified: Int? = null,
    val weekVerified: Int? = null
)

@Serializable
data class TaskStats(
    val total: Int? = null,
    val pending: Int? = null,
    val completed: Int? = null,
    val overdue: Int? = null,
    val todayCompleted: Int? = null,
    val weekCompleted: Int? = null,
    val monthCompleted: Int? = null,
    @SerialName("awaitingVerification") val awaitingVerification: Int? = null
)

@Serializable
data class ActivityItemDto(
    val id: Int? = null,
    val type: String? = null,
    val title: String? = null,
    val description: String? = null,
    val status: String? = null,
    val timestamp: String? = null,
    val assignedTo: String? = null,
    val veterinarian: String? = null
)

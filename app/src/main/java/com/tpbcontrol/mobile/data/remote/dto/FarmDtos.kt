package com.tpbcontrol.mobile.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FarmDto(
    val id: Int,
    val name: String? = null,
    val address: String? = null,
    val contactPhone: String? = null,
    val areaHectares: Double? = null,
    val cattleCount: Int? = null,
    val propertyType: String? = null,
    @SerialName("ownerId") val ownerId: Int? = null,
    val requestsStats: RequestSummaryStats? = null,
    val workersCount: Int? = null,
    val vetsCount: Int? = null
)

@Serializable
data class RequestSummaryStats(
    val pending: Int = 0,
    val completed: Int = 0
)

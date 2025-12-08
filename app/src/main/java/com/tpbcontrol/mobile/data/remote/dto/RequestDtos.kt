package com.tpbcontrol.mobile.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicationRequestDto(
    val id: Int,
    val farmId: Int? = null,
    val veterinarianId: Int? = null,
    val assignedWorkerId: Int? = null,
    val animalIdentification: String? = null,
    val medicationName: String? = null,
    val dosage: String? = null,
    val applicationMethod: String? = null,
    val scheduledDatetime: String? = null,
    val appliedDatetime: String? = null,
    val verifiedDatetime: String? = null,
    val priority: String? = null,
    val observations: String? = null,
    val status: String? = null,
    val applicationPhotoUrl: String? = null,
    val rejectionReason: String? = null,
    val farm: FarmSummaryDto? = null,
    val veterinarian: PersonDto? = null,
    val assignedWorker: PersonDto? = null
)

@Serializable
data class PersonDto(
    val id: Int? = null,
    val fullName: String? = null,
    val email: String? = null,
    val phone: String? = null
)

@Serializable
data class FarmSummaryDto(
    val id: Int? = null,
    val name: String? = null
)

@Serializable
data class VerifyApplicationRequest(
    val approved: Boolean,
    val rejectionReason: String? = null
)

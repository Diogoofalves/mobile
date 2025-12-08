package com.tpbcontrol.mobile.data

import com.tpbcontrol.mobile.data.local.TokenStore
import com.tpbcontrol.mobile.data.remote.TPBApi
import com.tpbcontrol.mobile.data.remote.dto.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class TPBRepository(
    private val api: TPBApi,
    private val tokenStore: TokenStore
) {
    val tokenFlow: Flow<String?> = tokenStore.tokenFlow

    suspend fun login(email: String, password: String): UserDto {
        val response = api.login(LoginRequest(email = email, password = password))
        tokenStore.saveToken(response.token)
        return response.user
    }

    suspend fun register(request: RegisterRequest): UserDto {
        val response = api.register(request)
        tokenStore.saveToken(response.token)
        return response.user
    }

    suspend fun logout() {
        tokenStore.saveToken(null)
    }

    suspend fun getProfile(): UserDto = api.getProfile()

    suspend fun getDashboard(): DashboardResponse = api.getDashboard()

    suspend fun getRecentActivity(): List<ActivityItemDto> = api.getRecentActivity()

    suspend fun getPendingVerifications(): List<MedicationRequestDto> = api.getPendingVerifications()

    suspend fun listRequests(farmId: Int? = null): List<MedicationRequestDto> =
        api.listRequests(farmId = farmId)

    suspend fun getMyTasks(): List<MedicationRequestDto> = api.getMyTasks()

    suspend fun verifyApplication(requestId: Int, approved: Boolean, reason: String?): MedicationRequestDto =
        api.verifyApplication(requestId, VerifyApplicationRequest(approved = approved, rejectionReason = reason))

    suspend fun applyMedication(requestId: Int, photoFile: File): MedicationRequestDto {
        val requestBody: RequestBody = photoFile.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("photo", photoFile.name, requestBody)
        return api.applyMedication(requestId = requestId, photo = part)
    }

    suspend fun getMyFarms(): List<FarmDto> = api.getMyFarms()
}

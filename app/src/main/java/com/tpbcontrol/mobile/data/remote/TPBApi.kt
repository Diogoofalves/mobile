package com.tpbcontrol.mobile.data.remote

import com.tpbcontrol.mobile.data.remote.dto.ActivityItemDto
import com.tpbcontrol.mobile.data.remote.dto.AuthResponse
import com.tpbcontrol.mobile.data.remote.dto.DashboardResponse
import com.tpbcontrol.mobile.data.remote.dto.FarmDto
import com.tpbcontrol.mobile.data.remote.dto.LoginRequest
import com.tpbcontrol.mobile.data.remote.dto.MedicationRequestDto
import com.tpbcontrol.mobile.data.remote.dto.RegisterRequest
import com.tpbcontrol.mobile.data.remote.dto.UserDto
import com.tpbcontrol.mobile.data.remote.dto.VerifyApplicationRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface TPBApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("profile")
    suspend fun getProfile(): UserDto

    @GET("dashboard")
    suspend fun getDashboard(): DashboardResponse

    @GET("dashboard/recent-activity")
    suspend fun getRecentActivity(): List<ActivityItemDto>

    @GET("requests/pending-verification")
    suspend fun getPendingVerifications(): List<MedicationRequestDto>

    @GET("requests")
    suspend fun listRequests(
        @Query("farmId") farmId: Int? = null,
        @Query("farmIds") farmIds: String? = null
    ): List<MedicationRequestDto>

    @GET("requests/mytasks")
    suspend fun getMyTasks(): List<MedicationRequestDto>

    @Multipart
    @PATCH("requests/{requestId}/apply")
    suspend fun applyMedication(
        @Path("requestId") requestId: Int,
        @Part photo: MultipartBody.Part
    ): MedicationRequestDto

    @PATCH("requests/{requestId}/verify")
    suspend fun verifyApplication(
        @Path("requestId") requestId: Int,
        @Body body: VerifyApplicationRequest
    ): MedicationRequestDto

    @GET("farms/my-farms")
    suspend fun getMyFarms(): List<FarmDto>
}

package com.tpbcontrol.mobile.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val userType: String,
    val phone: String? = null,
    val specialization: String? = null,
    val crmvNumber: String? = null,
    val farmExperience: String? = null,
    val companyName: String? = null,
    val cnpj: String? = null,
    val mainAddress: String? = null
)

@Serializable
data class AuthResponse(
    val message: String? = null,
    val user: UserDto,
    val token: String
)

@Serializable
data class UserDto(
    val id: Int,
    val email: String? = null,
    val fullName: String? = null,
    val userType: String? = null,
    val phone: String? = null,
    val crmvNumber: String? = null,
    val companyName: String? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

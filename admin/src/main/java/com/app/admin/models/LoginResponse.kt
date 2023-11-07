package com.app.admin.models

import com.google.gson.annotations.SerializedName


data class LoginRequest(
    val type: String,
    val username: String,
    val password: String
)

data class TokenResponse(
    @SerializedName("token")
    val token: String
)
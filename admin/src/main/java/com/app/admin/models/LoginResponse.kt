package com.app.admin.models

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token")
    val token: String
)
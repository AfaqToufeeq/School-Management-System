package com.attech.sms.models


data class LogoutResponse(
    val msg: String
)

data class LogoutRequest(
    val type: String,
    val token: String
)

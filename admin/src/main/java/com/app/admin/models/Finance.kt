package com.app.admin.models

data class Finance(
    val type: String,
    val token: String,
    val name: String,
    val email: String,
    val role: String,
    val username: String,
    val password: String,
    val image: String
)


data class FinanceResponse(
    val msg: String
)
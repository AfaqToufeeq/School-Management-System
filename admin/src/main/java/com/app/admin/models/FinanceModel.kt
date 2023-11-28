package com.app.admin.models

data class FinanceModel(
    val id: Int,
    val name: String,
    val email: String,
    val role: String?,
    val username: String,
    val password: String,
    val authToken: String?,
    val isLoggedIn: String,
    val status: String,
    val image: String? = null
)

package com.app.admin.repository

import com.app.admin.interfaces.ApiService
import com.app.admin.models.LoginRequest
import com.app.admin.models.TokenResponse


class RetrofitRepository (private val apiService: ApiService) {
    suspend fun login(type: String, username: String, password: String): TokenResponse {
        return apiService.login(type, username, password)
    }
}
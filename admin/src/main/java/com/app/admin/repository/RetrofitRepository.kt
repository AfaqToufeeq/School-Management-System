package com.app.admin.repository

import com.app.admin.interfaces.ApiService
import com.app.admin.models.StudentDetails
import com.app.admin.models.StudentListResponse
import com.app.admin.models.TokenResponse
import retrofit2.Response

class RetrofitRepository (private val apiService: ApiService) {
    suspend fun login(type: String, username: String, password: String): TokenResponse {
        return apiService.login(type, username, password)
    }

    suspend fun getStudents(type: String, token: String): Response<List<StudentDetails>> {
        return apiService.getStudents(type, token)
    }
}
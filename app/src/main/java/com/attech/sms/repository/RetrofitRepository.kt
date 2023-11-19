package com.attech.sms.repository

import com.attech.sms.interfaces.ApiService
import com.attech.sms.models.LoginResponse
import com.attech.sms.models.LogoutResponse
import com.attech.sms.models.StudentDetailsResponse
import retrofit2.Response

class RetrofitRepository(private val apiService: ApiService) {

    suspend fun login(type: String, username: String, password: String): LoginResponse {
        return apiService.login(type, username, password)
    }

    suspend fun logout(type: String, token: String): LogoutResponse {
        return apiService.logout(type, token)
    }

    suspend fun getStudents(type: String, token: String): Response<List<StudentDetailsResponse>> {
        return apiService.getStudents(type, token)
    }
}
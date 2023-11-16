package com.app.admin.repository

import android.util.Log
import com.app.admin.interfaces.ApiService
import com.app.admin.models.Student
import com.app.admin.models.StudentDetails
import com.app.admin.models.StudentResponse
import com.app.admin.models.TokenResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitRepository (private val apiService: ApiService) {
    suspend fun login(type: String, username: String, password: String): TokenResponse {
        return apiService.login(type, username, password)
    }

    suspend fun getStudents(type: String, token: String): Response<List<StudentDetails>> {
        return apiService.getStudents(type, token)
    }

    suspend fun addStudent(type: String, token: String, student: Student) {
        val response = apiService.addStudent( type = type,
            token = token,
            firstname = student.firstname,
            lastname = student.lastname,
            rollno = student.rollno,
            contact = student.contact,
            nic = student.nic,
            address = student.address,
            username = student.username,
            password = student.password
        ).enqueue(object : Callback<StudentResponse> {
            override fun onResponse(call: Call<StudentResponse>, response: Response<StudentResponse>) {
                if (response.isSuccessful) {
                    val student = response.body()

                    Log.d("API_CALL", "Response: ${student.toString()}")
                } else {
                    Log.e("API_CALL", "Error code: ${response.code()}, Error body: ${response.errorBody()}")

                }
            }
            override fun onFailure(call: Call<StudentResponse>, t: Throwable) {
                // Handle failure
                Log.e("API_CALL", "Error adding student: ${t.message}", t)
            }
        })
    }
}
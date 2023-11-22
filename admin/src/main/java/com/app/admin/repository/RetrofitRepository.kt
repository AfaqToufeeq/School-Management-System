package com.app.admin.repository

import android.util.Log
import com.app.admin.interfaces.ApiService
import com.app.admin.models.Student
import com.app.admin.models.StudentDetailsResponse
import com.app.admin.models.LoginResponse
import com.app.admin.models.LogoutResponse
import com.app.admin.models.Teacher
import com.app.admin.models.TeacherDetailsResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

class RetrofitRepository(private val apiService: ApiService) {

    suspend fun login(type: String, username: String, password: String): LoginResponse {
        return apiService.login(type, username, password)
    }

    suspend fun getStudents(type: String, token: String): Response<List<StudentDetailsResponse>> {
        return apiService.getStudents(type, token)
    }

    suspend fun getTeachers(type: String, token: String): Response<List<TeacherDetailsResponse>> {
        return apiService.getTeachers(type, token)
    }

    suspend fun logout(type: String, token: String): LogoutResponse {
        return apiService.logout(type, token)
    }

    suspend fun addStudent(type: String, token: String, student: Student): Boolean {
        return suspendCancellableCoroutine { continuation ->
            try {
                val addStudentCall = apiService.addStudent(
                    type = type,
                    token = token,
                    firstname = student.firstname,
                    lastname = student.lastname,
                    rollno = student.rollno,
                    contact = student.contact,
                    nic = student.nic,
                    address = student.address,
                    username = student.username,
                    password = student.password,
                    image = student.image!!
                )

                addStudentCall.enqueue(object : Callback<StudentDetailsResponse> {
                    override fun onResponse(
                        call: Call<StudentDetailsResponse>,
                        response: Response<StudentDetailsResponse>
                    ) {
                        if (response.isSuccessful) {
                            continuation.resume(true)
                        } else {
                            Log.e(
                                "API_CALL",
                                "Error code: ${response.code()}, Error body: ${response.errorBody()}"
                            )
                            continuation.resume(false)
                        }
                    }

                    override fun onFailure(call: Call<StudentDetailsResponse>, t: Throwable) {
                        handleCallFailure(t)
                        continuation.resume(false)
                    }
                })
            } catch (e: Exception) {
                Log.e("API_CALL", "Exception: ${e.message}")
                continuation.resume(false)
            }
        }
    }


    suspend fun addTeacher(type: String, token: String, teacher: Teacher): Boolean {
        return suspendCancellableCoroutine { continuation ->
            try {
                val addTeacherCall = apiService.addTeacher(
                    type = type,
                    token = token,
                    firstname = teacher.firstname,
                    lastname = teacher.lastname,
                    contact = teacher.contact,
                    nic = teacher.nic,
                    address = teacher.address,
                    username = teacher.username,
                    password = teacher.password,
                    image = teacher.image!!
                )

                addTeacherCall.enqueue(object : Callback<TeacherDetailsResponse> {
                    override fun onResponse(
                        call: Call<TeacherDetailsResponse>,
                        response: Response<TeacherDetailsResponse>
                    ) {
                        if (response.isSuccessful) {
                            continuation.resume(true)
                        } else {
                            Log.e(
                                "API_CALL",
                                "Error code: ${response.code()}, Error body: ${response.errorBody()}"
                            )
                            continuation.resume(false)
                        }
                    }

                    override fun onFailure(call: Call<TeacherDetailsResponse>, t: Throwable) {
                        handleCallFailure(t)
                        continuation.resume(false)
                    }
                })
            } catch (e: Exception) {
                Log.e("API_CALL", "Exception: ${e.message}")
                continuation.resume(false)
            }
        }
    }


    private fun handleCallFailure(t: Throwable, message: String = ":") {
        Log.e("API_CALL", "Error $message ${t.message}", t)
    }
}

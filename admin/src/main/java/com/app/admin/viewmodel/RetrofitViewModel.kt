package com.app.admin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.admin.models.Student
import com.app.admin.models.StudentDetails
import com.app.admin.models.StudentResponse
import com.app.admin.models.TokenResponse
import com.app.admin.repository.RetrofitRepository
import kotlinx.coroutines.launch


class RetrofitViewModel(private val repository: RetrofitRepository) : ViewModel() {

    private val _students = MutableLiveData<List<StudentDetails>>()
    val students: LiveData<List<StudentDetails>> get() = _students

    private val _addStudentResult = MutableLiveData<StudentResponse?>()
    val addStudentResult: MutableLiveData<StudentResponse?> get() = _addStudentResult

    suspend fun login(type: String, username: String, password: String): TokenResponse {
        return repository.login(type, username, password)
    }

    fun fetchStudents(type: String, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getStudents(type, token)
                if (response.isSuccessful) {
                    _students.value = response.body() ?: emptyList()
                    Log.d("Token","Success ${response.body()}")
                } else {
                    Log.d("Token","Error")
                }
            } catch (e: Exception) {
                Log.d("Token","Error: ${e.message}")
            }
        }
    }

//    fun addStudent(
//        type: String,
//        token: String,
//        student: Student
//    ) {
//        viewModelScope.launch {
//            try {
//                val response = repository.addStudent(type, token, student)
//                if (response.isSuccessful) {
//                    _studentAdded.value = true
//                } else {
//                    Log.e("API_ERROR", "Error: ${response.code()}. Message: ${response.message()}")
//                    _studentAdded.value = false
//                }
//            } catch (e: Exception) {
//                Log.e("API_ERROR", "Exception: ${e.message}")
//                _studentAdded.value = false
//            }
//        }
//    }

    fun addStudent(type: String, token: String, student: Student) {
        viewModelScope.launch {
            try {
                val response = repository.addStudent(type, token, student)
//                _addStudentResult.value = response
                Log.d("API_ERROR","Success $response")
            } catch (e: Exception) {
                // Handle error
                Log.d("API_ERROR","Failed ${e.message.toString()}")
                _addStudentResult.value = null
            }
        }
    }

}
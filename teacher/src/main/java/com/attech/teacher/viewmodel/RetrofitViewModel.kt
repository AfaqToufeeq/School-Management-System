package com.attech.teacher.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attech.teacher.models.BatchesModel
import com.attech.teacher.models.LoginResponse
import com.attech.teacher.models.LogoutResponse
import com.attech.teacher.models.Student
import com.attech.teacher.models.StudentDetailsResponse
import com.attech.teacher.models.Teacher
import com.attech.teacher.models.TeacherDetailsResponse
import com.attech.teacher.repository.RetrofitRepository
import kotlinx.coroutines.launch


class RetrofitViewModel(private val repository: RetrofitRepository) : ViewModel() {

    private val _students = MutableLiveData<List<StudentDetailsResponse>>()
    val students: LiveData<List<StudentDetailsResponse>> get() = _students

    private val _addStudentResult = MutableLiveData<Boolean?>()
    val addStudentResult: LiveData<Boolean?> get() = _addStudentResult

    private val _teachers = MutableLiveData<List<TeacherDetailsResponse>>()
    val teachers: LiveData<List<TeacherDetailsResponse>> get() = _teachers

    private val _allBatches = MutableLiveData<List<BatchesModel>>()
    val allBatches: LiveData<List<BatchesModel>> get() = _allBatches



    suspend fun login(type: String, username: String, password: String): LoginResponse {
        return repository.login(type, username, password)
    }

    suspend fun logout(type: String, token: String): LogoutResponse {
        return repository.logout(type, token)
    }


    fun fetchStudents(type: String, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getStudents(type, token)
                if (response.isSuccessful) {
                    _students.value = response.body() ?: emptyList()
                    Log.d("Token", "Success ${response.body()}")
                } else {
                    Log.d("Token", "Error")
                }
            } catch (e: Exception) {
                Log.d("Token", "Error: ${e.message}")
            }
        }
    }


    fun fetchTeachers(type: String, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTeachers(type, token)
                if (response.isSuccessful) {
                    _teachers.value = response.body() ?: emptyList()
                    Log.d("Token", "Success ${response.body()}")
                } else {
                    Log.d("Token", "Error")
                }
            } catch (e: Exception) {
                Log.d("Token", "Error: ${e.message}")
            }
        }
    }


    fun fetchBatches(type: String, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getBatches(type, token)
                if (response.isSuccessful) {
                    _allBatches.value = response.body() ?: emptyList()
                    Log.d("Token", "Success ${response.body()}")
                } else {
                    Log.d("Token", "Error")
                }
            } catch (e: Exception) {
                Log.d("Token", "Error: ${e.message}")
            }
        }
    }


    fun addStudent(type: String, token: String, student: Student) {
        viewModelScope.launch {
            try {
                val response = repository.addStudent(type, token, student)
                _addStudentResult.value = response
            } catch (e: Exception) {
                Log.d("API_ERROR", "View Model Failed ${e.message.toString()}")
                _addStudentResult.value = false
            }
        }
    }

}

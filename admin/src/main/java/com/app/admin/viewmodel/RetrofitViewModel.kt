package com.app.admin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.admin.models.FResponce
import com.app.admin.models.Finance
import com.app.admin.models.Student
import com.app.admin.models.StudentDetailsResponse
import com.app.admin.models.LoginResponse
import com.app.admin.models.LogoutResponse
import com.app.admin.models.Teacher
import com.app.admin.models.TeacherDetailsResponse
import com.app.admin.repository.RetrofitRepository
import kotlinx.coroutines.launch
import retrofit2.Response


class RetrofitViewModel(private val repository: RetrofitRepository) : ViewModel() {

    private val _students = MutableLiveData<List<StudentDetailsResponse>>()
    val students: LiveData<List<StudentDetailsResponse>> get() = _students

    private val _addStudentResult = MutableLiveData<Boolean?>()
    val addStudentResult: LiveData<Boolean?> get() = _addStudentResult

    private val _teachers = MutableLiveData<List<TeacherDetailsResponse>>()
    val teachers: LiveData<List<TeacherDetailsResponse>> get() = _teachers

    private val _addTeacherResult = MutableLiveData<Boolean?>()
    val addTeacherResult: LiveData<Boolean?> get() = _addTeacherResult


    suspend fun login(type: String, username: String, password: String): LoginResponse {
        return repository.login(type, username, password)
    }

    suspend fun logout(type: String, token: String): LogoutResponse {
        return repository.logout(type, token)
    }


    suspend fun addFinancePerson(finance: Finance): Response<FResponce> {
        return repository.addFinancePerson(finance)
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


    fun addTeacher(type: String, token: String, teacher: Teacher) {
        viewModelScope.launch {
            try {
                val response = repository.addTeacher(type, token, teacher)
                _addTeacherResult.value = response
            } catch (e: Exception) {
                Log.d("API_ERROR", "View Model Failed ${e.message.toString()}")
                _addTeacherResult.value = false
            }
        }
    }
}

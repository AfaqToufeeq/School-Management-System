package com.attech.sms.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attech.sms.models.LoginResponse
import com.attech.sms.models.Student
import com.attech.sms.models.StudentDetailsResponse
import com.attech.sms.repository.RetrofitRepository

import kotlinx.coroutines.launch

class RetrofitViewModel(private val repository: RetrofitRepository) : ViewModel() {

    private val _students = MutableLiveData<List<StudentDetailsResponse>>()
    val students: LiveData<List<StudentDetailsResponse>> get() = _students


    suspend fun login(type: String, username: String, password: String): LoginResponse {
        return repository.login(type, username, password)
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

}

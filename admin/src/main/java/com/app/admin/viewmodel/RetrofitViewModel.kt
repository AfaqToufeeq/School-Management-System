package com.app.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.admin.models.LoginRequest
import com.app.admin.models.TokenResponse
import com.app.admin.repository.RetrofitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RetrofitViewModel(private val repository: RetrofitRepository) : ViewModel() {

    suspend fun login(type: String, username: String, password: String): TokenResponse {
        return repository.login(type, username, password)
    }
}
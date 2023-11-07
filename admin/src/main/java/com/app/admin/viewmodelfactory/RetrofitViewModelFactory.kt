package com.app.admin.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.admin.interfaces.ApiService
import com.app.admin.repository.RetrofitRepository
import com.app.admin.viewmodel.RetrofitViewModel

class RetrofitViewModelFactory(private val repository: RetrofitRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RetrofitViewModel::class.java)) {
            return RetrofitViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
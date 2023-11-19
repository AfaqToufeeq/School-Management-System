package com.attech.sms.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.attech.sms.repository.RetrofitRepository
import com.attech.sms.viewmodel.RetrofitViewModel

class RetrofitViewModelFactory(private val repository: RetrofitRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RetrofitViewModel::class.java)) {
            return RetrofitViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.attech.sms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.attech.sms.models.DashboardItem
import com.attech.sms.repository.StudentRepository

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {
    val dashboardItemsLiveData: LiveData<List<DashboardItem>> = repository.dashboardItemsLiveData

    private val _webPageUrl = MutableLiveData<String>()
    val webPageUrl: LiveData<String> get() = _webPageUrl

    init {
        loadWebPage()
    }

    private fun loadWebPage() {
        _webPageUrl.value = repository.getWebPageUrl()
    }

}
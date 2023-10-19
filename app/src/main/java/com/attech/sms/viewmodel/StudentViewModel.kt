package com.attech.sms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.attech.sms.models.DashboardItem
import com.attech.sms.models.NewsItem
import com.attech.sms.repository.StudentRepository

class StudentViewModel(repository: StudentRepository) : ViewModel() {
    val dashboardItemsLiveData: LiveData<List<DashboardItem>> = repository.getDashboardItems()
    val newsItemsLiveData: LiveData<List<NewsItem>> = repository.getNewsItems()

}
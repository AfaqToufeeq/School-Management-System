package com.app.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.admin.models.DashboardItem
import com.app.admin.models.NewsItem
import com.app.admin.models.Teacher
import com.app.admin.repository.AdminRepository

class AdminViewModel(private val repository: AdminRepository) : ViewModel() {
    val dashboardItemsLiveData: LiveData<List<DashboardItem>> = repository.getDashboardItems()
    val newsItemsLiveData: LiveData<List<NewsItem>> = repository.getNewsItems()
    val teachersLiveData: LiveData<List<Teacher>> = repository.getTeachers()

    fun addTeacher(teacher: Teacher) {
        repository.addTeacher(teacher)
    }
}
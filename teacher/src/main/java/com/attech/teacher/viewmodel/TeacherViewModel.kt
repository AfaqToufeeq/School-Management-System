package com.attech.teacher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.attech.teacher.models.DashboardItem
import com.attech.teacher.repository.TeacherRepository


class TeacherViewModel(repository: TeacherRepository) : ViewModel() {
    val dashboardItemsLiveData: LiveData<List<DashboardItem>> = repository.dashboardItemsLiveData
}
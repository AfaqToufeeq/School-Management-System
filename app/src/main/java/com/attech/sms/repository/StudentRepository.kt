package com.attech.sms.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.attech.sms.R
import com.attech.sms.models.DashboardItem

class StudentRepository {
    private val _dashboardItemsData = MutableLiveData<List<DashboardItem>>()
    val dashboardItemsLiveData: LiveData<List<DashboardItem>> get() = _dashboardItemsData

    init {
        _dashboardItemsData.value = getDashBoardItemsData()
    }

    private fun getDashBoardItemsData(): List<DashboardItem> {
        return mutableListOf(
            DashboardItem(R.drawable.attendance, "Attendance"),
            DashboardItem(R.drawable.attendance, "Courses"),
            DashboardItem(R.drawable.exam, "Marks"),
            DashboardItem(R.drawable.baseline_attach_money_24, "Fee Status"),
            DashboardItem(R.drawable.homework, "Past Papers")
        )
    }

    fun getWebPageUrl(): String {
        return "https://teachingbd24.com/hsc-board-question-archives/"
    }

}

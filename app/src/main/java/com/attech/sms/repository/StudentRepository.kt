package com.attech.sms.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.attech.sms.R
import com.attech.sms.models.DashboardItem
import com.attech.sms.models.NewsItem

class StudentRepository {
    private val _dashboardItemsData = MutableLiveData<List<DashboardItem>>()
    val dashboardItemsLiveData: LiveData<List<DashboardItem>> get() = _dashboardItemsData

    private val _newsItemsData = MutableLiveData<List<NewsItem>>()
    val newsItemsLiveData: LiveData<List<NewsItem>> get() = _newsItemsData

    init {
        _dashboardItemsData.value = getDashBoardItemsData()
        _newsItemsData.value = getNewsItemsData()
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

    private fun getNewsItemsData(): List<NewsItem> {
        return mutableListOf(
            NewsItem(R.drawable.logo, "School Science Fair", "Join us at the annual School Science Fair on October 10th. Students will showcase their exciting science projects.", "Oct 10, 2023"),
            NewsItem(R.drawable.logo, "Parent-Teacher Conferences", "Parent-Teacher conferences are scheduled for November 15th and 16th. It's a great opportunity to discuss your child's progress.", "Nov 15, 2023"),
        )
    }

    fun getWebPageUrl(): String {
        return "https://teachingbd24.com/hsc-board-question-archives/"
    }

}

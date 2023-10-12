package com.attech.sms.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.attech.sms.R
import com.attech.sms.models.DashboardItem
import com.attech.sms.models.NewsItem

class StudentRepository {
    private val dashboardItemsData = MutableLiveData<List<DashboardItem>>()
    private val newsItemsData = MutableLiveData<List<NewsItem>>()

    init {
        dashboardItemsData.value = getDashBoardItemsData()
        newsItemsData.value = getNewsItemsData()
    }

    fun getDashboardItems(): LiveData<List<DashboardItem>> {
        return dashboardItemsData
    }

    fun getNewsItems(): LiveData<List<NewsItem>> {
        return newsItemsData
    }

    private fun getDashBoardItemsData(): List<DashboardItem> {
        return mutableListOf(
            DashboardItem(R.drawable.attendance, "Attendance"),
            DashboardItem(R.drawable.attendance, "My Class"),
            DashboardItem(R.drawable.exam, "Performance"),
            DashboardItem(R.drawable.exam, "Class Test Marks"),
            DashboardItem(R.drawable.homework, "Assignment"),
            DashboardItem(R.drawable.homework, "Notice")
        )
    }

    private fun getNewsItemsData(): List<NewsItem> {
        return mutableListOf(
            NewsItem(R.drawable.placeholder, "Title 1", "Description 1", "Sep 29, 2023"),
            NewsItem(R.drawable.placeholder, "Title 2", "Description 2", "Sep 30, 2023")
        )
    }
}

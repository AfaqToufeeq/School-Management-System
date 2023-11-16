package com.attech.sms.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.attech.sms.R
import com.attech.sms.models.AttendanceData
import com.attech.sms.models.DashboardItem
import com.attech.sms.models.NewsItem

class StudentRepository {
    private val dashboardItemsData = MutableLiveData<List<DashboardItem>>()
    private val newsItemsData = MutableLiveData<List<NewsItem>>()
    private val attendanceData = MutableLiveData<List<AttendanceData>>()

    init {
        dashboardItemsData.value = getDashBoardItemsData()
        newsItemsData.value = getNewsItemsData()
        attendanceData.value = getAttendanceData()
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

    fun getAttendanceLiveData(): LiveData<List<AttendanceData>> {
        return attendanceData
    }

    fun filterAttendance(selectedDate: String) {
        val filteredData = getFilteredData(selectedDate)
        attendanceData.value = filteredData
    }

    private fun getFilteredData(selectedDate: String): List<AttendanceData> {
        return getAttendanceData().filter { it.date == selectedDate }
    }

    private fun getAttendanceData(): List<AttendanceData> {
        val dummyData = mutableListOf<AttendanceData>()
        for (i in 1..30) {
            if (i % 2 == 0)
                dummyData.add(AttendanceData("2023-9-$i", "Present"))
            else
                dummyData.add(AttendanceData("2023-9-$i", "Absent"))
        }
        return dummyData
    }
}

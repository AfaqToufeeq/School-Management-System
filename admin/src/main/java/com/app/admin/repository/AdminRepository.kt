package com.app.admin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.admin.R
import com.app.admin.models.DashboardItem
import com.app.admin.models.NewsItem
import com.app.admin.models.Teacher

class AdminRepository {
    private val dashboardItemsData = MutableLiveData<List<DashboardItem>>()
    private val newsItemsData = MutableLiveData<List<NewsItem>>()
    private val teachersData = MutableLiveData<List<Teacher>>()

    init {
        dashboardItemsData.value = getDashBoardItemsData()
        newsItemsData.value = getNewsItemsData()
        teachersData.value = getTeachersData()
    }

    fun getDashboardItems(): LiveData<List<DashboardItem>> {
        return dashboardItemsData
    }

    fun getNewsItems(): LiveData<List<NewsItem>> {
        return newsItemsData
    }

    fun getTeachers(): LiveData<List<Teacher>> {
        return teachersData
    }

    fun addTeacher(teacher: Teacher) {
        val teacherList = teachersData.value?.toMutableList() ?: mutableListOf()
        teacherList.add(teacher)
        teachersData.value = teacherList
    }

    private fun getTeachersData(): List<Teacher> {
        return mutableListOf()  // Initially empty; load from your data source
    }

    private fun getDashBoardItemsData(): List<DashboardItem> {
        return mutableListOf(
            DashboardItem(R.drawable.teacher_icon, "Add Teachers"),
            DashboardItem(R.drawable.student_icon, "Add Students"),
            DashboardItem(R.drawable.teacher_icon, "Add Finance"),
            DashboardItem(R.drawable.news_icon, "Add Events"),
        )
    }

    private fun getNewsItemsData(): List<NewsItem> {
        return mutableListOf(
            NewsItem(R.drawable.logo, "School Science Fair", "Join us at the annual School Science Fair on October 10th. Students will showcase their exciting science projects.", "Oct 10, 2023"),
            NewsItem(R.drawable.logo, "Parent-Teacher Conferences", "Parent-Teacher conferences are scheduled for November 15th and 16th. It's a great opportunity to discuss your child's progress.", "Nov 15, 2023"),
        )
    }

}

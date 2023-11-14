package com.attech.teacher.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.attech.teacher.R
import com.attech.teacher.models.Class
import com.attech.teacher.models.DashboardItem
import com.attech.teacher.models.NewsItem
import com.attech.teacher.models.Student

class TeacherRepository {

    private val _dashboardItemsData = MutableLiveData<List<DashboardItem>>()
    val dashboardItemsLiveData: LiveData<List<DashboardItem>> get() = _dashboardItemsData

    private val _newsItemsData = MutableLiveData<List<NewsItem>>()
    val newsItemsLiveData: LiveData<List<NewsItem>> get() = _newsItemsData

    init {
        _dashboardItemsData.value = getDashBoardItemsData()
        _newsItemsData.value = getNewsItemsData()
    }

    fun getClasses(): List<Class> {
        return classes
    }

    fun getClassStudents(className: String): List<Student> {
        return classes.find { it.className == className }?.students.orEmpty()
    }

    private val classes = listOf(
        Class(1, "Class A", listOf(Student(1, "Student 1", "Class A", "Roll 1"), Student(2, "Student 2", "Class A", "Roll 2"))),
        Class(2, "Class B", listOf(Student(3, "Student 3", "Class B", "Roll 3"), Student(4, "Student 4", "Class B", "Roll 4")))
    )

    private fun getDashBoardItemsData(): List<DashboardItem> {
        return mutableListOf(
            DashboardItem(R.drawable.student_male, "View Students"),
            DashboardItem(R.drawable.attendance, "Mark Attendance"),
            DashboardItem(R.drawable.exam, "Upload Marks"),
        )
    }

    private fun getNewsItemsData(): List<NewsItem> {
        return mutableListOf(
            NewsItem(R.drawable.logo, "School Science Fair", "Join us at the annual School Science Fair on October 10th. Students will showcase their exciting science projects.", "Oct 10, 2023"),
            NewsItem(R.drawable.logo, "Parent-Teacher Conferences", "Parent-Teacher conferences are scheduled for November 15th and 16th. It's a great opportunity to discuss your child's progress.", "Nov 15, 2023"),
        )
    }

}

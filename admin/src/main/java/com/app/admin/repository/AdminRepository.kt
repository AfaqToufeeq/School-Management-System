package com.app.admin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.admin.R
import com.app.admin.models.DashboardItem


class AdminRepository {
    private val dashboardItemsData = MutableLiveData<List<DashboardItem>>()

    init {
        dashboardItemsData.value = getDashBoardItemsData()
    }

    fun getDashboardItems(): LiveData<List<DashboardItem>> {
        return dashboardItemsData
    }


    private fun getDashBoardItemsData(): List<DashboardItem> {
        return mutableListOf(
            DashboardItem(R.drawable.teacher_icon, "Add Teachers"),
            DashboardItem(R.drawable.student_icon, "Add Students"),
            DashboardItem(R.drawable.finance_icon, "Add Finance"),
            DashboardItem(R.drawable.events_icon, "Add Events"),
            DashboardItem(R.drawable.class_icon, "Add Batch"),
            DashboardItem(R.drawable.courses_icon, "Add Courses"),
            DashboardItem(R.drawable.teacher_icon, "View Teachers"),
            DashboardItem(R.drawable.student_icon, "View Students"),
            DashboardItem(R.drawable.finance_icon, "View Finance"),
            DashboardItem(R.drawable.class_icon, "View Batch"),
            DashboardItem(R.drawable.courses_icon, "View Courses"),
        )
    }

}

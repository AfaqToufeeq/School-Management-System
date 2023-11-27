package com.attech.teacher.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.attech.teacher.R
import com.attech.teacher.models.DashboardItem


class TeacherRepository {

    private val _dashboardItemsData = MutableLiveData<List<DashboardItem>>()
    val dashboardItemsLiveData: LiveData<List<DashboardItem>> get() = _dashboardItemsData

    init {
        _dashboardItemsData.value = getDashBoardItemsData()
    }

    private fun getDashBoardItemsData(): List<DashboardItem> {
        return mutableListOf(
            DashboardItem(R.drawable.attendance, "Mark Attendance"),
            DashboardItem(R.drawable.exam, "Upload Marks"),
        )
    }

}

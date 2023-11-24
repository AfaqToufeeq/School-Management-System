package com.attech.teacher.utils

import androidx.lifecycle.MutableLiveData
import com.attech.teacher.models.BatchesModel
import com.attech.teacher.models.CourseTeacherResponse
import com.attech.teacher.models.TeacherDetailsResponse

object PickerManager {
    var token: String? = null
    var userName: String? = null
    var allBatchesList : List<BatchesModel>? = null
    var getTeacherCourses : List<CourseTeacherResponse>? = null

    //Live Data Objects
    val liveTeacherData = MutableLiveData<TeacherDetailsResponse?>()
    val userIdData = MutableLiveData<Int>()
    fun setTeacherData(teacher: TeacherDetailsResponse?) {
        liveTeacherData.postValue(teacher)
        teacher?.let { userIdData.postValue(it.id) }
    }
}
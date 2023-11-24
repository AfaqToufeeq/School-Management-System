package com.attech.sms.utils

import androidx.lifecycle.MutableLiveData
import com.attech.sms.models.BatchesModel
import com.attech.sms.models.CourseTeacherResponse
import com.attech.sms.models.StudentDetailsResponse
import com.attech.sms.models.TeacherDetailsResponse

object PickerManager {

    var token: String? = null
    var userName: String? = null

    //Live Data Objects
    val liveStudentData = MutableLiveData<StudentDetailsResponse?>()
    val userIdData = MutableLiveData<Int>()
    fun setStudentData(student: StudentDetailsResponse?) {
        liveStudentData.postValue(student)
        student?.let { userIdData.postValue(it.id) }
    }

}
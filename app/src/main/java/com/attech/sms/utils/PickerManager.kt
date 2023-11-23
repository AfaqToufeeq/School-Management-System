package com.attech.sms.utils

import com.attech.sms.models.BatchesModel
import com.attech.sms.models.CourseTeacherResponse
import com.attech.sms.models.StudentDetailsResponse
import com.attech.sms.models.TeacherDetailsResponse

object PickerManager {

    var token: String? = null
    var userName: String? = null
    var studentData: StudentDetailsResponse? = null
    var userId: Int = 0
    var teacherData: TeacherDetailsResponse? = null
    var batchCodes: List<String>? = null
    var batchClass: String? = null
    var allBatchesList : List<BatchesModel>? = null
    var getBatchStudents : List<StudentDetailsResponse>? = null
    var getTeacherCourses : List<CourseTeacherResponse>? = null
}
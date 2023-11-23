package com.attech.teacher.utils

import com.attech.teacher.models.BatchesModel
import com.attech.teacher.models.CourseTeacherResponse
import com.attech.teacher.models.StudentDetailsResponse
import com.attech.teacher.models.TeacherDetailsResponse

object PickerManager {

    var token: String? = null
    var userName: String? = null
    var userId: Int = 0
    var teacherData: TeacherDetailsResponse? = null
    var batchCodes: List<String>? = null
    var allBatchesList : List<BatchesModel>? = null
    var getBatchStudents : List<StudentDetailsResponse>? = null
    var getTeacherCourses : List<CourseTeacherResponse>? = null
}
package com.attech.teacher.utils

import com.attech.teacher.models.BatchesModel
import com.attech.teacher.models.TeacherDetailsResponse

object PickerManager {

    var token: String? = null
    var userName: String? = null
    var teacherData: TeacherDetailsResponse? = null

    var allBatchesList : List<BatchesModel>? = null
}
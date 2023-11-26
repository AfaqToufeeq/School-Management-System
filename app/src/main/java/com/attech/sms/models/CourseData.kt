package com.attech.sms.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CourseData(
    val courseId: Int,
    val courseCode: String,
    val courseName: String,
    val teacherName: String,
    val subjectTotalMarks: Int,
    val batchCode: String
) : Parcelable

package com.attech.sms.models

data class CourseDetails(
    val courseNo: String,
    val courseName: String,
    val teacher: String,
    val className: String,
    val isMarks: Boolean = false
)

package com.attech.teacher.models

data class CourseDetails(
    val courseNo: String,
    val courseName: String,
    val teacher: String,
    val className: String,
    val isMarks: Boolean = false
)

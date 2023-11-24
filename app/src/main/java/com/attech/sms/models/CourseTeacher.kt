package com.attech.sms.models

data class CourseTeacher(
    val type: String,
    val token: String,
    val teacher: String
)


data class CourseTeacherResponse(
    val id: String,
    val name: String,
    val code: String,
    val status: String,
    val max_marks: Int,
)
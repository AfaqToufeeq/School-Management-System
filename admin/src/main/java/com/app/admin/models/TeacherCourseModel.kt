package com.app.admin.models

data class TeacherCourseModel(
    val type: String,
    val token: String,
    val course: Int,
    val teacher: Int,
)

data class TeacherCourseResponse(
    val msg: String
)

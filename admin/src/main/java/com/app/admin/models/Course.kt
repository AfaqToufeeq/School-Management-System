package com.app.admin.models

data class Course(
    val type: String,
    val token: String,
    val name: String,
    val code: String,
    val maxMarks: Int
)

data class CoursesResponse(
    val msg: String
)
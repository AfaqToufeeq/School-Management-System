package com.app.admin.models

data class GetCourse(
    val type: String,
    val toke: String,
    val id: Int
)

data class GetCourseResponse(
    val id: Int,
    val name: String,
    val code: String,
    val status: String,
    val marks: Int
)

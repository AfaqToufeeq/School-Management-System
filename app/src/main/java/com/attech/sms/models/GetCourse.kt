package com.attech.sms.models

data class GetCourse(
    val type: String,
    val token: String
)

data class GetCourseResponse(
    val id: Int,
    val name: String,
    val code: String,
    val status: String,
    val marks: Int
)
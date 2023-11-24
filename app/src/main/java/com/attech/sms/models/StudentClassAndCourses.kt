package com.attech.sms.models

data class StudentClassAndCourses(
    val type: String,
    val token: String,
    val id: Int
)

data class StudentClassAndCoursesResponse(
    val batchcode: String,
    val courses: List<String>
)

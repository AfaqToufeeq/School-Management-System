package com.attech.teacher.models

data class TeacherClasses(
    val type: String,
    val token: String,
    val id: Int
)

data class TeacherClassesResponse(
    val batches: List<String>,
    val courses: List<String>
)

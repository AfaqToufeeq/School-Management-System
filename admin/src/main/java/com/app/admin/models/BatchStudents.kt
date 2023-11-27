package com.app.admin.models

data class BatchStudents(
    val type: String,
    val token: String,
    val bcode: String,
    val students: List<Int>,
    val teachers: List<Int>
)

data class BatchStudentsResponse(
    val msg: String
)

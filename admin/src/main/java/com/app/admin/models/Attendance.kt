package com.app.admin.models

data class Attendance (
    val type: String,
    val token: String,
    val batchCode: String,
    val course: String,
    val student: String,
    val date: String
)

data class MarkAttendanceResponse (
    val msg: String
)
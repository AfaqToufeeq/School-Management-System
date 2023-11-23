package com.attech.teacher.models

data class AttendanceModel(
    val type: String,
    val token: String,
    val bcode: String,
    val course: String,
    val student: Int,
    val date: String
)


data class AttendanceResponse(
    val msg: String
)

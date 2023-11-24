package com.attech.sms.models

data class GetAttendanceModel(
    val type: String,
    val token: String,
    val bcode: String,
    val student: Int,
)

data class GetAttendanceModelResponse(
    val id: Int,
    val student: Int,
    val course: Int,
    val date: String,
    val marked_by: Int
)

package com.attech.teacher.models

data class MarksData(
    val type: String,
    val token: String,
    val course: Int,
    val student: Int,
    val bcode: String,
    val score: String
)

data class UploadMarksResponse (
    val msg: String
)

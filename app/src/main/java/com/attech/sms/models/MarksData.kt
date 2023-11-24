package com.attech.sms.models

data class MarksData(
    val type: String,
    val token: String,
    val course: Int,
    val student: Int,
    val bcode: String,
    val score: Int
)

data class UploadMarksResponse (
    val msg: String
)

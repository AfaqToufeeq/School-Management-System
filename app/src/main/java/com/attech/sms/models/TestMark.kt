package com.attech.sms.models// TestMark.kt

data class TestMark(
    val subject: String,
    val testName: String,
    val teacher: String,
    val totalMarks: Int,
    val percentage: Double,
    val date: String
)


data class TestMarksResponse(
    val id :Int,
    val student: Int,
    val course: Int,
    val score: String,
    val marked_by: Int
)


data class TestMarksRequest(
    val type: String,
    val token: String,
    val course: Int,
    val student: Int,
    val bcode: String,
)

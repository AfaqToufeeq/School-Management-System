package com.attech.sms.models// TestMark.kt

data class TestMark(
    val subject: String,
    val testName: String,
    val teacher: String,
    val totalMarks: Int,
    val percentage: Double,
    val date: String
)

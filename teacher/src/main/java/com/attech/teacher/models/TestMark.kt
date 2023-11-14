package com.attech.teacher.models

data class TestMark(
    val subject: String,
    val testName: String,
    val teacher: String,
    val totalMarks: Int,
    val percentage: Double,
    val date: String
)

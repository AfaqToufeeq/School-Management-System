package com.attech.teacher.models

data class TestMarksRequest(
    val type: String,
    val token: String,
    val course: Int,
    val student: Int,
    val bcode: String,
)


data class TestMarksResponse(
    val id :Int,
    val student: Int,
    val course: Int,
    val score: String,
    val marked_by: Int
)
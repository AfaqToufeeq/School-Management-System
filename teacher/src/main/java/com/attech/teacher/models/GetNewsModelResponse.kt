package com.attech.teacher.models

data class GetNewsModelResponse(
    val id: String,
    val date: String,
    val title: String,
    val description: String,
    val image: String
)
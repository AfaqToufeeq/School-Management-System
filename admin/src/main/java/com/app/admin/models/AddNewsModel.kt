package com.app.admin.models

data class AddNewsModel(
    val type: String,
    val token: String,
    val date: String,
    val title: String,
    val description: String,
    val image: String
)


data class NewsModelResponse(
   val msg: String
)


data class GetNewsModelResponse(
    val id: String,
    val date: String,
    val title: String,
    val description: String,
    val image: String
)
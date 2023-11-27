package com.app.admin.models

data class FeeDetailModel (
    val type: String,
    val token: String,
    val student: Int
)


data class FeeDetailResponse (
    val id: Int,
    val student: Int,
    val date: String
)
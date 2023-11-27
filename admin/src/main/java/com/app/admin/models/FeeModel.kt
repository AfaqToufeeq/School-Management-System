package com.app.admin.models

data class FeeModel (
    val type: String,
    val token: String,
    val student: Int,
    val date: String
)

data class FeeResponse(
    val msg: String
)
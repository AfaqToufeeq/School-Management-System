package com.app.admin.models

data class AdminRemoveAction (
    val type: String,
    val token: String,
    val id: Int,
    val del: String
)

data class AdminRemoveResponse (
    val msg: String
)

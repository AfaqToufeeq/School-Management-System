package com.app.admin.models

data class TeacherDetailsResponse(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val contact: String,
    val nic: String,
    val address: String,
    val username: String,
    val password: String,
    val auth_token: String?,
    val is_logged_in: String,
    val status: String,
    val image: String? = null
)

data class Teacher(
    val type: String,
    val token: String,
    val firstname: String,
    val lastname: String,
    val contact: String,
    val nic: String,
    val address: String,
    val username: String,
    val password: String,
    val image: String? = null
)
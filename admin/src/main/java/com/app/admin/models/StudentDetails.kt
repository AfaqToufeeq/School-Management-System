package com.app.admin.models


data class StudentListResponse(
    val students: List<StudentDetails>
)


data class StudentDetails(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val rollno: String,
    val contact: String,
    val nic: String,
    val address: String,
    val username: String,
    val password: String,
    val auth_token: String?,
    val is_logged_in: String,
    val status: String
)
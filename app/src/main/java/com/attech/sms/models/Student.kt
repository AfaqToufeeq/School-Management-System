package com.attech.sms.models

data class StudentDetailsResponse(
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

data class Student(
    val type: String,
    val token: String,
    val firstname: String,
    val lastname: String,
    val rollno: String,
    val contact: String,
    val nic: String,
    val address: String,
    val username: String,
    val password: String
)

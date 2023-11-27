package com.app.admin.models

import java.io.Serializable

data class FeeStudent(
    val studentID:Int,
    val firstname: String,
    val lastname: String,
    val rollno: String,
    val contact: String,
    val nic: String,
    val address: String,
    val image: String?
): Serializable

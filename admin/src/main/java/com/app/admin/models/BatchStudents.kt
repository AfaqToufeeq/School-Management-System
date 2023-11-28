package com.app.admin.models

import org.json.JSONArray

data class BatchStudents(
    val type: String,
    val token: String,
    val bcode: String,
    val students: JSONArray,
    val teachers: JSONArray
)

data class BatchStudentsResponse(
    val msg: String
)

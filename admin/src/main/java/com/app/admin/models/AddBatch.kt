package com.app.admin.models

data class AddBatch(
    val type: String,
    val token: String,
    val bcode: String
)

data class BatchResponse (
    val msg: String
)

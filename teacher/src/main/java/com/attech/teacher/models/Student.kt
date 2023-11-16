package com.attech.teacher.models

data class Class(
    val id: Int,
    val className: String,
    val students: List<Student>
)

data class Student(
    val id: Int,
    val name: String,
    val className: String,
    var rollNumber: String,
    var isPresent: Boolean = false,
    val marksList: MutableList<MarksData> = mutableListOf()
)
package com.attech.teacher.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.attech.teacher.models.DashboardItem
import com.attech.teacher.models.MarksData
import com.attech.teacher.models.NewsItem
import com.attech.teacher.models.Student
import com.attech.teacher.repository.TeacherRepository
import java.util.Calendar


class TeacherViewModel(private val repository: TeacherRepository) : ViewModel() {
    val dashboardItemsLiveData: LiveData<List<DashboardItem>> = repository.dashboardItemsLiveData
    val newsItemsLiveData: LiveData<List<NewsItem>> = repository.newsItemsLiveData

    private val _classes = MutableLiveData<List<String>>()
    val classes: LiveData<List<String>> get() = _classes

    private val _selectedClass = MutableLiveData<String>()
    val selectedClass: LiveData<String> get() = _selectedClass

    private val _selectedDate = MutableLiveData<Long>()
    val selectedDate: LiveData<Long> get() = _selectedDate

    private val _attendance = MutableLiveData<List<Student>>()
    val attendance: LiveData<List<Student>> get() = _attendance

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSuccess: LiveData<Boolean> get() = _uploadSuccess


    init {
        loadClasses()
    }

    private fun loadClasses() {
        _classes.value = repository.getClasses().map { it.className }
    }

    fun onClassSelected(className: String) {
        _selectedClass.value = className
        _attendance.value = repository.getClassStudents(className)
    }

    fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        _selectedDate.value = calendar.timeInMillis
    }

    fun submitAttendance() {
        // Handle attendance submission logic here
        // You can access selectedClass.value, selectedDate.value, and attendance.value
    }

    fun handleAttendanceUpdate(updatedStudent: Student) {
        // Handle the updated student's attendance here
        // For example, you can log the attendance status or perform any necessary actions
        Log.d("Attendance", "Student ${updatedStudent.name} attendance updated: ${updatedStudent.isPresent}")
    }

    fun uploadMarks(marksData: MarksData) {
        val className = selectedClass.value
        if (!className.isNullOrEmpty()) {
            val isSuccess = repository.uploadMarks(className, marksData)
            _uploadSuccess.value = isSuccess
        } else {
            _uploadSuccess.value = false
        }
    }

}
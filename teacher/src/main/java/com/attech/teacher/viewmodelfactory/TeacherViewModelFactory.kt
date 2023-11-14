package com.attech.teacher.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.attech.teacher.repository.TeacherRepository
import com.attech.teacher.viewmodel.TeacherViewModel

class TeacherViewModelFactory(private val repository: TeacherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeacherViewModel::class.java)) {
            return TeacherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.attech.teacher.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attech.teacher.databinding.ItemStudentBinding
import com.attech.teacher.models.Student

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    private var students: List<Student> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount(): Int = students.size

    fun setStudents(students: List<Student>) {
        this.students = students
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student) {
            binding.tvStudentName.text = student.name
            binding.tvStudentClass.text = "Class: ${student.className}"
            binding.tvStudentRollNum.text = "Roll Number: ${student.rollNumber}"
        }
    }
}

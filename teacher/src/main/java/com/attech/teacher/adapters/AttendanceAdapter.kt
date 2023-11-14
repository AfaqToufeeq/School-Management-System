package com.attech.teacher.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attech.teacher.databinding.ItemAttendanceBinding
import com.attech.teacher.models.Student

class AttendanceAdapter(private val onAttendanceClickListener: (Student) -> Unit) :
    RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {

    private var students: List<Student> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ViewHolder(private val binding: ItemAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) {
            binding.tvStudentName.text = student.name
            binding.checkboxAttendance.isChecked = student.isPresent

            binding.checkboxAttendance.setOnCheckedChangeListener { _, isChecked ->
                student.isPresent = isChecked
                onAttendanceClickListener.invoke(student)
            }
        }
    }
}

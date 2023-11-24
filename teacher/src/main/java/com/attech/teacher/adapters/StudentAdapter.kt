package com.attech.teacher.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attech.teacher.databinding.ItemStudentBinding
import com.attech.teacher.models.Student
import com.attech.teacher.models.StudentDetailsResponse

class StudentAdapter(private val onStudentListener: (StudentDetailsResponse, Int) -> Unit) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    private var students: List<StudentDetailsResponse> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount(): Int = students.size

    fun setStudents(students: List<StudentDetailsResponse>) {
        this.students = students
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: StudentDetailsResponse) {
            binding.textViewStudentName.text = "${student.firstname} ${student.lastname}"
            binding.textViewRollNumber.text = "Roll Number: ${student.rollno}"
            binding.root.setOnClickListener {
                onStudentListener.invoke(student,adapterPosition)
            }
        }
    }
}

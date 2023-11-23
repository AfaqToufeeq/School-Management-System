package com.attech.teacher.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attech.teacher.databinding.ItemAttendanceBinding
import com.attech.teacher.models.Student
import com.attech.teacher.models.StudentDetailsResponse

class AttendanceAdapter(private val onAttendanceClickListener: (StudentDetailsResponse, Int, Boolean) -> Unit) :
    RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {

    private var students: List<StudentDetailsResponse> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ViewHolder(private val binding: ItemAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(student: StudentDetailsResponse) {
            binding.tvStudentName.text = "${student.firstname} ${student.lastname}"
            binding.tvStudentRollNo.text = "Roll No. ${student.rollno}"
            binding.checkboxAttendance.isChecked = false

            binding.checkboxAttendance.setOnCheckedChangeListener { _, isChecked ->
//                student.isPresent = isChecked
                    onAttendanceClickListener.invoke(student, adapterPosition, isChecked)
            }
        }
    }
}

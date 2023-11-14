package com.app.admin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.admin.databinding.ListItemStudentBinding
import com.app.admin.models.StudentDetails

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    private var students = listOf<StudentDetails>()
    
    inner class StudentViewHolder(private val binding: ListItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(student: StudentDetails) {
            binding.apply {
                textViewStudentName.text = "${student.firstname} ${student.lastname}"
                textViewRollNumber.text = student.rollno

                // Set other details
                textViewFirstName.text = "First Name: ${student.firstname}"
                textViewLastName.text = "Last Name: ${student.lastname}"
                textViewRollNoDetails.text = "Roll Number: ${student.rollno}"
                textViewContact.text = "Contact: ${student.contact}"
                textViewNIC.text = "NIC: ${student.nic}"
                textViewAddress.text = "Address: ${student.address}"
                textViewUsername.text = "Username: ${student.username}"
                textViewPassword.text = "Password: ${student.password}"

                root.setOnClickListener {
                    toggleDetails(expandableLayout)
                }
            }
        }

        private fun toggleDetails(layout: LinearLayout) {
            layout.visibility = if (layout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ListItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount(): Int = students.size

    fun submitList(newStudents: List<StudentDetails>) {
        students = newStudents
        notifyDataSetChanged()
    }
}

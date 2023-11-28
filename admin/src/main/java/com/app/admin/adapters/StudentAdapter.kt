package com.app.admin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.admin.R
import com.app.admin.databinding.ListItemStudentBinding
import com.app.admin.models.StudentDetailsResponse
import com.app.admin.utils.ImageUtil

class StudentAdapter(
    private val callback:(StudentDetailsResponse) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    private var students = mutableListOf<StudentDetailsResponse>()
    
    inner class StudentViewHolder(private val binding: ListItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(student: StudentDetailsResponse) {
            binding.apply {
                textViewStudentName.text = "${student.firstname} ${student.lastname}"
                textViewRollNumber.text = student.rollno
                if (student.image!=null)
                    studentImageView.setImageBitmap(ImageUtil.decodeBase64ToBitmap(student.image))
                else
                    studentImageView.setImageResource(R.drawable.student_icon)

                // Set other details
                textViewFirstName.text = "First Name: ${student.firstname}"
                textViewLastName.text = "Last Name: ${student.lastname}"
                textViewRollNoDetails.text = "Roll Number: ${student.rollno}"
                textViewContact.text = "Contact: ${student.contact}"
                textViewNIC.text = "NIC: ${student.nic}"
                textViewAddress.text = "Address: ${student.address}"
                textViewUsername.text = "Username: ${student.username}"
                textViewPassword.text = "Password: ${student.password}"

                root.setOnClickListener { toggleDetails(expandableLayout, arrowIcon) }
                arrowIcon.setOnClickListener { toggleDetails(expandableLayout, arrowIcon) }
                binding.removeIV.setOnClickListener { callback.invoke(student) }
            }
        }

        private fun toggleDetails(layout: LinearLayout, arrowIcon: ImageView) {
            val newVisibility = if (layout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            layout.visibility = newVisibility
            arrowIcon.rotation = if (newVisibility == View.VISIBLE) -90f else 0f
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

    fun submitList(newStudents: List<StudentDetailsResponse>) {
        students.clear()
        students = newStudents.toMutableList()
        notifyDataSetChanged()
    }
}

package com.app.admin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.admin.R
import com.app.admin.databinding.ItemTeachersBinding
import com.app.admin.models.TeacherDetailsResponse
import com.app.admin.utils.ImageUtil.decodeBase64ToBitmap

class ViewTeachersAdapter : RecyclerView.Adapter<ViewTeachersAdapter.TeacherViewHolder>() {
    private var teachers = listOf<TeacherDetailsResponse>()

    inner class TeacherViewHolder(private val binding: ItemTeachersBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(teacher: TeacherDetailsResponse) {
            binding.apply {
                textViewTeacherName.text = "${teacher.firstname} ${teacher.lastname}"
                if (teacher.image!=null)
                    teacherImageView.setImageBitmap(decodeBase64ToBitmap(teacher.image))
                else
                    teacherImageView.setImageResource(R.drawable.teacher_icon)

                // Set other details
                textViewFirstName.text = "First Name: ${teacher.firstname}"
                textViewLastName.text = "Last Name: ${teacher.lastname}"
                textViewContact.text = "Contact: ${teacher.contact}"
                textViewNIC.text = "NIC: ${teacher.nic}"
                textViewAddress.text = "Address: ${teacher.address}"
                textViewUsername.text = "Username: ${teacher.username}"
                textViewPassword.text = "Password: ${teacher.password}"

                root.setOnClickListener { toggleDetails(expandableLayout, arrowIcon) }
                arrowIcon.setOnClickListener { toggleDetails(expandableLayout, arrowIcon) }
            }
        }

        private fun toggleDetails(layout: LinearLayout, arrowIcon: ImageView) {
            val newVisibility = if (layout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            layout.visibility = newVisibility
            arrowIcon.rotation = if (newVisibility == View.VISIBLE) -90f else 0f
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val binding = ItemTeachersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeacherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        holder.bind(teachers[position])
    }

    override fun getItemCount(): Int = teachers.size

    fun submitList(newTeachers: List<TeacherDetailsResponse>) {
        teachers = newTeachers
        notifyDataSetChanged()
    }
}

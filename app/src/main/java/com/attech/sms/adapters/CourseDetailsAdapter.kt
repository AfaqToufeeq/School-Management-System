package com.attech.sms.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attech.sms.callbacks.OnItemClick
import com.attech.sms.databinding.ItemCourseDetailsBinding
import com.attech.sms.models.CourseDetails

class CourseDetailsAdapter(
    private val courseDetailsList: List<CourseDetails>,
    private val isMarks: Boolean,
    private val onItemClick: OnItemClick
) : RecyclerView.Adapter<CourseDetailsAdapter.CourseDetailsViewHolder>() {

    inner class CourseDetailsViewHolder(private val binding: ItemCourseDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(courseDetails: CourseDetails) = with(binding) {
            btnShowMarks.visibility = if (isMarks) View.VISIBLE else View.GONE
            textCourseNo.text = "Course No: ${courseDetails.courseNo}"
            textCourseName.text = "Course Name: ${courseDetails.courseName}"
            textTeacher.text = "Teacher: ${courseDetails.teacher}"
            textClass.text = "Class: ${courseDetails.className}"
            btnShowMarks.setOnClickListener { onItemClick.clickListener(adapterPosition, courseDetails.courseName)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseDetailsViewHolder {
        val binding = ItemCourseDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CourseDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseDetailsViewHolder, position: Int) {
        holder.bind(courseDetailsList[position])
    }

    override fun getItemCount() = courseDetailsList.size
}

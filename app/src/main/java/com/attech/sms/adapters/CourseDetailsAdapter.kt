package com.attech.sms.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attech.sms.databinding.ItemCourseDetailsBinding

class CourseDetailsAdapter : RecyclerView.Adapter<CourseDetailsAdapter.CourseDetailsViewHolder>() {

    private val courseDetailsList: MutableList<String> = mutableListOf()

    inner class CourseDetailsViewHolder(private val binding: ItemCourseDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(courseDetails: String) = with(binding) {
            textCourseNo.text = "${adapterPosition+1}"
            textCourseName.text = "${courseDetails}"
//            textTeacher.text = "Teacher: ${courseDetails.teacher}"
//            textClass.text = "Class: ${courseDetails.className}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseDetailsViewHolder {
        val binding = ItemCourseDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseDetailsViewHolder, position: Int) {
        holder.bind(courseDetailsList[position])
    }

    override fun getItemCount() = courseDetailsList.size

    fun setCourseDetailsList(list: List<String>) {
        courseDetailsList.clear()
        courseDetailsList.addAll(list)
        notifyDataSetChanged()
        Log.d("Adapter", "List size: ${courseDetailsList.size}")

    }

}

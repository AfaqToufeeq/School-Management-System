package com.app.admin.adapters;


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.admin.databinding.ItemCourseBinding;
import com.app.admin.models.GetCourseResponse

class CourseAdapter : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {
    private var courseList: List<GetCourseResponse> = emptyList()

    inner class CourseViewHolder(private val binding:ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(courseItem: GetCourseResponse) {
            binding.apply {
                tvCourseName.text = "Course Name: ${courseItem.name}"
                tvCourseCode.text = "Course Code: ${courseItem.code}"
                tvMaxMarks.text = "Max Marks: ${courseItem.max_marks}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courseList[position])
    }

    override fun getItemCount(): Int = courseList.size

    fun setCourses(courses: List<GetCourseResponse>) {
        courseList = emptyList()
        courseList = courses
        notifyDataSetChanged()
    }
}

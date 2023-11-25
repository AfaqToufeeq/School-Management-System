package com.attech.sms.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attech.sms.databinding.ItemCourseDetailsBinding
import com.attech.sms.viewmodel.RetrofitViewModel

class CourseDetailsAdapter(
    private val viewModel: RetrofitViewModel
) : RecyclerView.Adapter<CourseDetailsAdapter.CourseDetailsViewHolder>() {

    private val courseDetailsList: MutableList<String> = mutableListOf()

    inner class CourseDetailsViewHolder(private val binding: ItemCourseDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(courseName: String) = with(binding) {
            val courseID = getCourseIDAgainstName(courseName)
            val courseTotalMarks = getCourseMarksAgainstName(courseName)
            val courseTeacher = getCourseTeacherAgainstName(courseName)

            textCourseNo.text = courseID
            textCourseName.text = courseName
            textMarks.text = courseTotalMarks
            textTeacher.text = courseTeacher
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
    }

    private fun getCourseIDAgainstName(course: String): String {
        return viewModel.getCourses.value!!.firstOrNull { it.name == course }!!.code
    }

    private fun getCourseMarksAgainstName(course: String): String {
        return viewModel.getCourses.value!!.firstOrNull { it.name == course }!!.marks.toString()
    }

    private fun getCourseTeacherAgainstName(course: String): String {
        return viewModel.getCourses.value?.firstOrNull { it.name == course }?.id
            ?.let { courseId ->
                viewModel.getAttendance.value?.firstOrNull { it.course == courseId }?.marked_by
                    ?.let { teacherId ->
                        viewModel.courseTeachers.value?.firstOrNull { it.id == teacherId }?.let {
                           "${it.firstname} ${it.lastname}"
                        }
                    }?:""
            } ?: ""
    }
}

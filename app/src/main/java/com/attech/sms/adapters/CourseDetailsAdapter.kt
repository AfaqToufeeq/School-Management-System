package com.attech.sms.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attech.sms.databinding.ItemCourseDetailsBinding
import com.attech.sms.viewmodel.RetrofitViewModel

class CourseDetailsAdapter(
    private val viewModel: RetrofitViewModel,
    private val callback: (Int, String, String, String, Int)->Unit
) : RecyclerView.Adapter<CourseDetailsAdapter.CourseDetailsViewHolder>() {

    private val courseDetailsList: MutableList<String> = mutableListOf()

    inner class CourseDetailsViewHolder(private val binding: ItemCourseDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(courseName: String) = with(binding) {
            val courseCode = getCourseCodeAgainstName(courseName)
            val courseTotalMarks = getCourseTotalMarksAgainstName(courseName)
            val courseTeacher = getCourseTeacherAgainstName(courseName)

            textCourseNo.text = courseCode
            textCourseName.text = courseName
            textSubjectMarks.text = courseTotalMarks.toString()
            textTeacher.text = courseTeacher

            root.setOnClickListener {
                callback.invoke( getCourseIDAgainstName(courseName), courseCode, courseName, courseTeacher, courseTotalMarks )
            }
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

    private fun getCourseIDAgainstName(course: String): Int {
        return viewModel.getCourses.value!!.firstOrNull { it.name == course }!!.id
    }

    private fun getCourseCodeAgainstName(course: String): String {
        return viewModel.getCourses.value!!.firstOrNull { it.name == course }!!.code
    }

    private fun getCourseTotalMarksAgainstName(course: String): Int {
        return viewModel.getCourses.value!!.firstOrNull { it.name == course }!!.max_marks
    }

    private fun getCourseTeacherAgainstName(course: String): String {
        val courses = viewModel.getCourses.value
        val attendance = viewModel.getAttendance.value
        val teachers = viewModel.courseTeachers.value

        return courses?.firstOrNull { it.name == course }?.id
            ?.let { courseId ->
                attendance?.firstOrNull { it.course == courseId }?.marked_by
                    ?.let { teacherId ->
                        teachers?.firstOrNull { it.id == teacherId }?.let {
                            "${it.firstname} ${it.lastname}"
                        }
                    }
            } ?: ""
    }
}

package com.attech.sms.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attech.sms.databinding.ItemTestMarksBinding
import com.attech.sms.models.CourseData
import com.attech.sms.models.TestMarksResponse

class TestMarksAdapter : RecyclerView.Adapter<TestMarksAdapter.TestMarkViewHolder>() {

    private var testMarksList: MutableList<TestMarksResponse> = mutableListOf()
    private var courseData: CourseData? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestMarkViewHolder {
        val binding = ItemTestMarksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestMarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TestMarkViewHolder, position: Int) {
        val testMark = testMarksList[position]
        holder.bind(testMark)
    }

    override fun getItemCount(): Int {
        return testMarksList.size
    }

    inner class TestMarkViewHolder(private val binding: ItemTestMarksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(testMark: TestMarksResponse) {
            binding.apply {
                courseData?.let { courseData->
                    textSubjectCodeValue.text = courseData.courseCode
                    textSubjectValue.text = courseData.courseName
                    textTeacherValue.text = courseData.teacherName
                    textTotalMarksValue.text = courseData.subjectTotalMarks.toString()
                    textObtainedMarksValue.text = testMark.score.toString()
                    val percentage = (testMark.score.toDouble() / courseData.subjectTotalMarks) * 100
                    textPercentageValue.text = "%.2f%%".format(percentage)
                }
            }
        }
    }

    fun setTestMarksList(testMarksList: MutableList<TestMarksResponse>, courseData: CourseData) {
        this.courseData = null
        this.testMarksList.clear()
        this.courseData = courseData
        this.testMarksList.addAll(testMarksList)
        notifyDataSetChanged()
    }
}

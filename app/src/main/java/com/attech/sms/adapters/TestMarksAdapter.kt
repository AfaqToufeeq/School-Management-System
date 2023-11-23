package com.attech.sms.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attech.sms.databinding.ItemTestMarksBinding
import com.attech.sms.models.TestMark

class TestMarksAdapter : RecyclerView.Adapter<TestMarksAdapter.TestMarkViewHolder>() {

    private var testMarks: List<TestMark> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestMarkViewHolder {
        val binding = ItemTestMarksBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TestMarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TestMarkViewHolder, position: Int) {
        val testMark = testMarks[position]
        holder.bind(testMark)
    }

    override fun getItemCount(): Int {
        return testMarks.size
    }

    inner class TestMarkViewHolder(private val binding: ItemTestMarksBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(testMark: TestMark) {
            binding.apply {
                textSubject.text = "Subject: ${testMark.subject}"
                textTestName.text = "Test Name: ${testMark.testName}"
                textTeacher.text = "Teacher: ${testMark.teacher}"
                textTotalMarks.text = "Total Marks: ${testMark.totalMarks}"
                textPercentage.text = "Percentage: ${testMark.percentage}%"
                textDate.text = "Date: ${testMark.date}"
            }
        }
    }

    fun setTestMarksList(testMarksList: List<TestMark>) {
        testMarks = testMarksList
        notifyDataSetChanged()
    }
}

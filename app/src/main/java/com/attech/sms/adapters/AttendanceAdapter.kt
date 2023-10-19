package com.attech.sms.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attech.sms.databinding.ItemAttendanceBinding
import com.attech.sms.models.AttendanceData
import java.text.SimpleDateFormat
import java.util.*

class AttendanceAdapter(private val attendanceList: List<AttendanceData>) :
    RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    private var filteredList: List<AttendanceData> = attendanceList


    inner class AttendanceViewHolder(private val binding: ItemAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(attendanceData: AttendanceData) {
            val dayOfWeek = getDayOfWeek(attendanceData.date)

            binding.apply {
                dayTextView.text = dayOfWeek
                dateTextView.text = attendanceData.date
                statusTextView.text = attendanceData.status
            }
            checkStatus(attendanceData, binding)
        }
    }

    fun filterByDate(selectedDate: String) {
        filteredList = attendanceList.filter { it.date == selectedDate }
        notifyDataSetChanged()
    }

    private fun checkStatus(attendanceData: AttendanceData, binding: ItemAttendanceBinding) {
        binding.statusTextView.setBackgroundColor(
            if (attendanceData.status.equals("Absent", ignoreCase = true)) {
                Color.RED
            } else {
                Color.TRANSPARENT
            }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        return AttendanceViewHolder(ItemAttendanceBinding.
        inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val currentAttendance = filteredList[position]
        holder.bind(currentAttendance)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    private fun getDayOfWeek(date: String): String {
        // Get the day of the week from the date string
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: Date = inputFormat.parse(date)!!
        val calendar = Calendar.getInstance()
        calendar.time = formattedDate
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())!!
    }
}

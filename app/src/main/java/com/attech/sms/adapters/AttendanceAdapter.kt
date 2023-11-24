package com.attech.sms.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attech.sms.databinding.ItemAttendanceBinding
import com.attech.sms.models.AttendanceData
import com.attech.sms.models.GetAttendanceModel
import com.attech.sms.models.GetAttendanceModelResponse
import com.attech.sms.models.GetCourse
import com.attech.sms.utils.PickerManager.token
import com.attech.sms.utils.USER_TYPE
import com.attech.sms.viewmodel.RetrofitViewModel
import java.text.SimpleDateFormat
import java.util.*

class AttendanceAdapter(
    private val viewModel: RetrofitViewModel
) :
    RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {
    private var attendanceList: List<GetAttendanceModelResponse> = emptyList()
    private var filteredList: List<GetAttendanceModelResponse> = emptyList()


    inner class AttendanceViewHolder(private val binding: ItemAttendanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(attendanceData: GetAttendanceModelResponse) {
            val dayOfWeek = getDayOfWeek(attendanceData.date)

            binding.apply {
                dayTextView.text = dayOfWeek
                dateTextView.text = attendanceData.date
                statusTextView.text = "Present"
                textCourseName.text = ""
            }
        }
    }

    fun filterByDate(selectedDate: String) {
        filteredList = attendanceList.filter { it.date == selectedDate }
        notifyDataSetChanged()
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

    fun setAttendanceList(list: List<GetAttendanceModelResponse>) {
        attendanceList = list
        filteredList = attendanceList
        notifyDataSetChanged()
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

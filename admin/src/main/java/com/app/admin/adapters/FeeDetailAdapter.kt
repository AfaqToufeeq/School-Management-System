package com.app.admin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.admin.databinding.ItemFeeStatusBinding
import com.app.admin.models.FeeDetailResponse
import com.app.admin.viewmodel.RetrofitViewModel
import java.text.SimpleDateFormat
import java.util.*

class FeeDetailAdapter : RecyclerView.Adapter<FeeDetailAdapter.FeeDetailViewHolder>() {
    private var feeDetailList: List<FeeDetailResponse> = emptyList()
    private var filteredList: List<FeeDetailResponse> = emptyList()


    inner class FeeDetailViewHolder(private val binding: ItemFeeStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(feeDetailResponse: FeeDetailResponse) {
            val dayOfWeek = feeDetailResponse.date?.let {getDayOfWeek(it) }
            binding.apply {
                dayTextView.text = dayOfWeek
                dateTextView.text = feeDetailResponse.date
            }
        }
    }

    fun filterByDate(selectedDate: String) {
        filteredList = feeDetailList.filter { it.date == selectedDate }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeeDetailViewHolder {
        return FeeDetailViewHolder(ItemFeeStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FeeDetailViewHolder, position: Int) {
        val currentAttendance = filteredList[position]
        holder.bind(currentAttendance)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun setFeeDetailList(list: List<FeeDetailResponse>) {
        feeDetailList = list
        filteredList = feeDetailList
        notifyDataSetChanged()
    }

    private fun getDayOfWeek(date: String): String {
        //Get day of the week
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: Date = inputFormat.parse(date)!!
        val calendar = Calendar.getInstance()
        calendar.time = formattedDate
        return SimpleDateFormat("EEEE", Locale.getDefault()).format(formattedDate)
    }
}

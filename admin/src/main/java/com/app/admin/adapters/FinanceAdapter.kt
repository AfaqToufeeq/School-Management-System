package com.app.admin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.admin.R
import com.app.admin.databinding.ListItemFinanceBinding
import com.app.admin.models.FinanceModel
import com.app.admin.utils.ImageUtil

class FinanceAdapter(
    private val callback:(FinanceModel) -> Unit
) : RecyclerView.Adapter<FinanceAdapter.FinanceViewHolder>() {
    private var financeList = mutableListOf<FinanceModel>()

    inner class FinanceViewHolder(private val binding: ListItemFinanceBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(finance: FinanceModel) {
            binding.apply {
                textViewStudentName.text = finance.name
                textViewEmail.text = finance.email
                if (finance.image!=null)
                    studentImageView.setImageBitmap(ImageUtil.decodeBase64ToBitmap(finance.image))
                else
                    studentImageView.setImageResource(R.drawable.finance_icon)

                // Set other details
                textViewUsername.text = "Username: ${finance.username}"
                textViewPassword.text = "Password: ${finance.password}"

                root.setOnClickListener { toggleDetails(expandableLayout, arrowIcon) }
                arrowIcon.setOnClickListener { toggleDetails(expandableLayout, arrowIcon) }
                binding.removeIV.setOnClickListener { callback.invoke(finance) }
            }
        }

        private fun toggleDetails(layout: LinearLayout, arrowIcon: ImageView) {
            val newVisibility = if (layout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            layout.visibility = newVisibility
            arrowIcon.rotation = if (newVisibility == View.VISIBLE) -90f else 0f
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinanceViewHolder {
        val binding = ListItemFinanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FinanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FinanceViewHolder, position: Int) {
        holder.bind(financeList[position])
    }

    override fun getItemCount(): Int = financeList.size

    fun submitList(newFinance: List<FinanceModel>) {
        financeList.clear()
        financeList = newFinance.toMutableList()
        notifyDataSetChanged()
    }
}

package com.attech.sms.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attech.sms.callbacks.OnItemClick
import com.attech.sms.databinding.ItemDashboardBinding
import com.attech.sms.models.DashboardItem

class DashboardAdapter(
    private val clickListener: OnItemClick
    ) :
    RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {
    private var dashBoardItemsList: List<DashboardItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val binding = ItemDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(dashBoardItemsList[position])
    }

    fun submitList(newItems: List<DashboardItem>) {
        dashBoardItemsList = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dashBoardItemsList.size
    }

    inner class DashboardViewHolder(private val binding: ItemDashboardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DashboardItem) {
            binding.iconImageView.setImageResource(item.iconRes)
            binding.itemTextView.text = item.title

            binding.root.setOnClickListener { clickListener.clickListener(adapterPosition,item.title) }
        }
    }
}

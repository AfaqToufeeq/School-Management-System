package com.app.admin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.admin.databinding.ItemBatchBinding
import com.app.admin.models.BatchesModel

class BatchesAdapter : RecyclerView.Adapter<BatchesAdapter.ViewHolder>() {
    private var batches: List<BatchesModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(batches[position])
    }

    override fun getItemCount(): Int {
        return batches.size
    }

    inner class ViewHolder(private val binding: ItemBatchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(batch: BatchesModel) {
            binding.textViewBatchCode.text = batch.batchcode
            binding.textViewStatus.text = batch.status
        }
    }

    fun submitList(list: List<BatchesModel>) {
        batches = emptyList()
        batches = list
        notifyDataSetChanged()
    }
}

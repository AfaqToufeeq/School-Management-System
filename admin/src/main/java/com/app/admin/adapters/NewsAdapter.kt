package com.app.admin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.admin.databinding.ItemsNewsBinding
import com.app.admin.models.GetNewsModelResponse
import com.app.admin.utils.ImageUtil

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>()
{
    private var newsItems: List<GetNewsModelResponse> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = newsItems[position]
        holder.bind(newsItem)
    }

    fun submitList(newItems: List<GetNewsModelResponse>) {
        newsItems = emptyList()
        newsItems = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return newsItems.size
    }

    inner class ViewHolder(private val binding: ItemsNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(newsItem: GetNewsModelResponse) {
            binding.imageView.setImageBitmap(ImageUtil.decodeBase64ToBitmap(newsItem.image))
            binding.titleTextView.text = newsItem.title
            binding.descriptionTextView.text = newsItem.description
            binding.dateTextView.text = newsItem.date
        }
    }
}

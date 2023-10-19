package com.app.admin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.admin.databinding.ItemsNewsBinding
import com.app.admin.models.NewsItem

class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.ViewHolder>()
{
    private var newsItems: List<NewsItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = newsItems[position]
        holder.bind(newsItem)
    }

    fun submitList(newItems: List<NewsItem>) {
        newsItems = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return newsItems.size
    }

    inner class ViewHolder(private val binding: ItemsNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(newsItem: NewsItem) {
            binding.imageView.setImageResource(newsItem.imageResource)
            binding.titleTextView.text = newsItem.title
            binding.descriptionTextView.text = newsItem.description
            binding.dateTextView.text = newsItem.date
        }
    }
}

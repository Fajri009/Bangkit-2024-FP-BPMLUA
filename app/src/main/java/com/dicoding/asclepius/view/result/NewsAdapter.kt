package com.dicoding.asclepius.view.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemArticleBinding

class NewsAdapter(private val listArticle: List<ArticlesItem>): ListAdapter<ArticlesItem, NewsAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallBack: OnItemClickCallBack

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    class ViewHolder(val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(items: ArticlesItem) {
            Glide.with(itemView.context)
            binding.tvTitleNews.text = items.title
            binding.tvDesc.text = items.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items)

        holder.itemView.setOnClickListener {
            onItemClickCallBack.onItemClicked(listArticle[holder.adapterPosition])
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: ArticlesItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
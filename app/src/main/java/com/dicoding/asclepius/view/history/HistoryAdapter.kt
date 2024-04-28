package com.dicoding.asclepius.view.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.database.HistoryEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding

class HistoryAdapter(private val listHistory: List<HistoryEntity>): ListAdapter<HistoryEntity, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallBack: OnItemClickCallBack

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    class ViewHolder(val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(items: HistoryEntity) {
            binding.tvDate.text = items.date
            binding.tvResultThreshold.text = items.resultThreshold
            binding.tvResultCategory.apply {
                text = items.resultCategory
                setTextColor(
                    if (text == "Cancer") {
                        ContextCompat.getColor(itemView.context, R.color.red)
                    } else {
                        ContextCompat.getColor(itemView.context, R.color.blue)
                    }
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items)

        holder.itemView.setOnClickListener {
            onItemClickCallBack.onItemClicked(listHistory[holder.adapterPosition])
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: HistoryEntity)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
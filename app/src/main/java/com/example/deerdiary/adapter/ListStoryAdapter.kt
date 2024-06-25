package com.example.deerdiary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.deerdiary.data.datasource.Story
import com.example.deerdiary.databinding.CardviewDiaryBinding
import com.example.deerdiary.utils.GlideBindingUtil

class ListStoryAdapter : PagingDataAdapter<Story, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    class MyViewHolder(private val binding: CardviewDiaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(
            item: Story,
            onItemClickListener: OnItemClickListener,
        ) {
            binding.apply {
                tvItemName.text = item.name
                tvDeskripsi.text = item.description
                GlideBindingUtil.setImageUrl(ivItemPhoto, item.photoUrl ?: "")
                root.setOnClickListener {
                    onItemClickListener.onItemClick(item)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Story>() {
                override fun areItemsTheSame(
                    oldItem: Story,
                    newItem: Story,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Story,
                    newItem: Story,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val binding =
            CardviewDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        if (item != null) {
            holder.binding(item, onItemClickListener)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Story)
    }
}

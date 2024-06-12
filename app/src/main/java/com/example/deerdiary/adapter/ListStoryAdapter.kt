package com.example.deerdiary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.deerdiary.databinding.CardviewDiaryBinding
import com.example.deerdiary.ui.homeScreen.model.StoryModel
import com.example.deerdiary.utils.GlideBindingUtil

class ListStoryAdapter : ListAdapter<StoryModel, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    class MyViewHolder(private val binding: CardviewDiaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(
            item: StoryModel,
            onItemClickListener: OnItemClickListener,
        ) {
            binding.apply {
                tvItemName.text = item.name
                tvDeskripsi.text = item.description
                GlideBindingUtil.setImageUrl(ivItemPhoto, item.photoUrl)
                root.setOnClickListener {
                    onItemClickListener.onItemClick(item)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<StoryModel>() {
                override fun areItemsTheSame(
                    oldItem: StoryModel,
                    newItem: StoryModel,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: StoryModel,
                    newItem: StoryModel,
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
        holder.binding(item, onItemClickListener)
    }

    interface OnItemClickListener {
        fun onItemClick(item: StoryModel)
    }
}

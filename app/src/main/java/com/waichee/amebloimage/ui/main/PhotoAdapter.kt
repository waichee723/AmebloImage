package com.waichee.amebloimage.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waichee.amebloimage.databinding.PhotoItemBinding

class PhotoAdapter: ListAdapter<String, PhotoAdapter.PhotoViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoAdapter.PhotoViewHolder {
        return PhotoViewHolder(PhotoItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PhotoAdapter.PhotoViewHolder, position: Int) {
        val url = getItem(position)
        holder.bind(url)
    }

    class PhotoViewHolder(private var binding: PhotoItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            binding.url = url
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
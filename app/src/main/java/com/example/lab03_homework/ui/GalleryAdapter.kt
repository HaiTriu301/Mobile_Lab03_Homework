package com.example.lab03_homework.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.lab03_homework.R
import com.example.lab03_homework.model.ProcessedImage

class GalleryAdapter : PagingDataAdapter<ProcessedImage, GalleryAdapter.GalleryViewHolder>(IMAGE_COMPARATOR) {

    class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val tagsTextView: TextView = view.findViewById(R.id.tvAiTags)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.imageView.load(item.image.webformatURL) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background) // Thêm ảnh placeholder nếu có
            }
            holder.tagsTextView.text = "AI: ${item.aiTags.joinToString(", ")}"
        }
    }

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<ProcessedImage>() {
            override fun areItemsTheSame(oldItem: ProcessedImage, newItem: ProcessedImage): Boolean =
                oldItem.image.id == newItem.image.id

            override fun areContentsTheSame(oldItem: ProcessedImage, newItem: ProcessedImage): Boolean =
                oldItem == newItem
        }
    }
}
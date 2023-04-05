package com.atulya.photogallery.features.photogallery.recyclerView

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.atulya.photogallery.R
import com.atulya.photogallery.core.api.models.GalleryItem
import com.atulya.photogallery.databinding.ItemPhotoGalleryBinding

class PhotoViewHolder(val binding: ItemPhotoGalleryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val TAG = "# Holder"
    fun bind(photo: GalleryItem) {
        binding.imageView.load(photo.url) {
            placeholder(R.drawable.sweet)
        }
    }
}
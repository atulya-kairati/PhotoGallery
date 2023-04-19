package com.atulya.photogallery.features.photogallery.recyclerView

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.atulya.photogallery.R
import com.atulya.photogallery.core.api.models.GalleryItem
import com.atulya.photogallery.databinding.ItemPhotoGalleryBinding

class PhotoViewHolder(private val binding: ItemPhotoGalleryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val TAG = "# Holder"
    fun bind(
        photo: GalleryItem,
        onItemClicked: (Uri) -> Unit
    ) {
        binding.imageView.load(photo.url) {
            placeholder(R.drawable.sweet)
        }

        binding.root.setOnClickListener {
            onItemClicked(photo.photoPageUri)
        }
    }
}
package com.atulya.photogallery.features.photogallery.recyclerView

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.atulya.photogallery.core.api.models.GalleryItem
import com.atulya.photogallery.databinding.ItemPhotoGalleryBinding

class PhotoListAdapter(
    private val photoList: List<GalleryItem>,
    private val onItemClicked: (Uri) -> Unit
    ) :
    RecyclerView.Adapter<PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPhotoGalleryBinding.inflate(inflater, parent, false)

        return PhotoViewHolder(binding)
    }

    override fun getItemCount() = photoList.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photoList[position]

        holder.bind(photo, onItemClicked)
    }
}
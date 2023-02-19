package com.atulya.photogallery.features.photogallery.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atulya.photogallery.core.models.GalleryItem
import com.atulya.photogallery.core.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhotoGalleryViewModel : ViewModel() {

    private val photoRepository = PhotoRepository()

    private val _gallery = MutableStateFlow<List<GalleryItem>>(emptyList())
    val gallery: StateFlow<List<GalleryItem>>
        get() = _gallery.asStateFlow()


    init {

        viewModelScope.launch {
            try {
                val flickerPhotos = photoRepository.searchPhotos("monkey")
                _gallery.value = flickerPhotos.response.photoList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}
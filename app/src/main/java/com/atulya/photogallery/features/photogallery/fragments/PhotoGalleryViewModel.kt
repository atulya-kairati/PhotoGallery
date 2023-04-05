package com.atulya.photogallery.features.photogallery.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atulya.photogallery.core.datastore.PreferenceRepository
import com.atulya.photogallery.core.models.GalleryItem
import com.atulya.photogallery.core.photorepository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PhotoGalleryViewModel : ViewModel() {

    private val photoRepository = PhotoRepository.get()
    private val preferenceRepository = PreferenceRepository.get()

    private val _gallery = MutableStateFlow<List<GalleryItem>>(emptyList())
    val gallery: StateFlow<List<GalleryItem>>
        get() = _gallery.asStateFlow()


    init {

        viewModelScope.launch {
            try {

                /**
                 * Since the user can submit many queries in the time that
                 * it takes to perform a single network request, we will
                 * use collectLatest() instead of collect(). If our lambda
                 * expression is in the middle of processing the last emission
                 * from a Flow and a new emission arrives, the current work will
                 * be canceled and your lambda expression will restart, executing
                 * on the new emission.
                 */
                preferenceRepository.storedQuery.collectLatest{ query ->
                    val flickerPhotos = fetchPhotos(query)
                    _gallery.value = flickerPhotos
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun setQuery(query: String) {
        viewModelScope.launch {
            preferenceRepository.setStoredQuery(query)
        }
    }

    private suspend fun fetchPhotos(query: String = "") = if (query.isEmpty()) {
        photoRepository.fetchPhotos()
    } else {
        photoRepository.searchPhotos(query)
    }
}
package com.atulya.photogallery.features.photogallery.fragments

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atulya.photogallery.core.api.models.GalleryItem
import com.atulya.photogallery.core.datastore.PreferenceRepository
import com.atulya.photogallery.core.photorepository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhotoGalleryViewModel : ViewModel() {

    private val photoRepository = PhotoRepository.get()
    private val preferenceRepository = PreferenceRepository.get()

    private val _uiState = MutableStateFlow(PhotoGalleryUiState())
    val uiState: StateFlow<PhotoGalleryUiState>
        get() = _uiState.asStateFlow()

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
                preferenceRepository.storedQuery.collectLatest { storedQuery ->
                    _uiState.update { old ->
                        old.copy(
                            images = fetchPhotos(storedQuery),
                            query = storedQuery
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * collect method is a blocking function so
         * if two collects are placed in that same
         * coroutine then only the first one will work.
         *
         * Because the second collect will be unreachable
         */

        viewModelScope.launch {
            preferenceRepository.isPolling.collect { isPolling ->
                _uiState.update { old ->
                    old.copy(
                        isPolling = isPolling
                    )
                }
            }
        }
    }

    fun setQuery(query: String) {
        viewModelScope.launch {
            preferenceRepository.setStoredQuery(query)
        }
    }

    fun toggleIsPolling() {
        Log.d("#> ${this::class.simpleName}", "toggleIsPolling")
        viewModelScope.launch {
            preferenceRepository.setIsPolling(! uiState.value.isPolling)
        }
    }

    private suspend fun fetchPhotos(query: String = "") = if (query.isEmpty()) {
        photoRepository.fetchPhotos()
    } else {
        val photos = photoRepository.searchPhotos(query)
        preferenceRepository.setLastResultId(photos.first().id)
        photos
    }
}

data class PhotoGalleryUiState(
    val images: List<GalleryItem> = listOf(),
    val query: String = "",
    val isPolling: Boolean = false
)
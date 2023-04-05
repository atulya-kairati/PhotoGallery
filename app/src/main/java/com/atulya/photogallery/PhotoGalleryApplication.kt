package com.atulya.photogallery

import android.app.Application
import com.atulya.photogallery.core.datastore.PreferenceRepository
import com.atulya.photogallery.core.photorepository.PhotoRepository

class PhotoGalleryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        PhotoRepository.init()
        PreferenceRepository.init(applicationContext)
    }
}
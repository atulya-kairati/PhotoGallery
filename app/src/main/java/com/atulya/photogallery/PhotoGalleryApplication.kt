package com.atulya.photogallery

import android.app.Application
import com.atulya.photogallery.core.singletons.FlickerApiSingleton

class PhotoGalleryApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        FlickerApiSingleton.init()
    }
}
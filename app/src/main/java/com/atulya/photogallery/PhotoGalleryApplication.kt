package com.atulya.photogallery

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.atulya.photogallery.core.datastore.PreferenceRepository
import com.atulya.photogallery.core.photorepository.PhotoRepository
import com.atulya.photogallery.core.utils.NOTIFICATION_CHANNEL_ID

class PhotoGalleryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        PhotoRepository.init()
        PreferenceRepository.init(applicationContext)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                name,
                importance
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
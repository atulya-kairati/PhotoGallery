package com.atulya.photogallery.core.worker

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.atulya.photogallery.MainActivity
import com.atulya.photogallery.R
import com.atulya.photogallery.core.datastore.PreferenceRepository
import com.atulya.photogallery.core.photorepository.PhotoRepository
import com.atulya.photogallery.core.utils.NOTIFICATION_CHANNEL_ID
import com.atulya.photogallery.core.utils.NOTIFICATION_REQUEST_CODE
import kotlinx.coroutines.flow.first

private const val TAG = "PollWorker"

class PollWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {

        Log.d("#> ${this::class.simpleName}", "doWork: Doing lot of work!")

        val photoRepository = PhotoRepository.get()
        val preferences = PreferenceRepository.get()

        val query = preferences.storedQuery.first()
        val lastResultId = preferences.lastResultId.first()

        if (query.isEmpty()) {
            Log.d("#> ${this::class.simpleName}", "No saved queries, quitting!")
            return Result.success()
        }

        try {
            val photos = photoRepository.searchPhotos(query)
            val resultId = photos[0].id

            if (resultId == lastResultId) {
                Log.d(
                    "#> ${this::class.simpleName}",
                    "No updates: ID($resultId)"
                )
            } else {
                preferences.setLastResultId(resultId)
                Log.d(
                    "#> ${this::class.simpleName}",
                    "New photos!! ID($resultId)"
                )
                notifyUser()
            }
        } catch (ex: Exception) {
            Log.d("#> ${this::class.simpleName}", "Error during fetching photos")
            return Result.failure()
        }

        return Result.success()
    }

    private fun notifyUser() {
        val intent = MainActivity.newIntent(applicationContext)

        // pendingIntent will be passes to notification and will
        // fire when the user presses the notification
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val resources = applicationContext.resources

        // NotificationCompat takes care of displaying
        // notification uniformly across all versions of
        // android. And takes cares of notification channels too.
        val notification = NotificationCompat
            .Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setTicker(resources.getString(R.string.new_pictures_text))
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentTitle(resources.getString(R.string.new_pictures_title))
            .setContentText(resources.getString(R.string.new_pictures_text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // notification will be deleted when user presses it
            .build()


        // if notification permission not granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    applicationContext,
                    "Not permitted notify you.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

        NotificationManagerCompat.from(applicationContext)
            .notify(NOTIFICATION_REQUEST_CODE, notification)
    }
}


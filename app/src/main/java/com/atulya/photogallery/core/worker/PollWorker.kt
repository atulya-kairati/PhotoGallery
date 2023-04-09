package com.atulya.photogallery.core.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

private const val TAG = "PollWorker"

class PollWorker(
    context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {

        Log.d(TAG, "doWork: Doing lot of work! ${this::class.simpleName}")

        return Result.success()
    }
}
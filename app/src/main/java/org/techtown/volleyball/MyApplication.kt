package org.techtown.volleyball

import android.app.Application
import android.os.Looper
import androidx.core.os.HandlerCompat
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.Executors

@HiltAndroidApp
class MyApplication : Application() {
    private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()
    var executorService = Executors.newFixedThreadPool(NUMBER_OF_CORES)
    var mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())
}
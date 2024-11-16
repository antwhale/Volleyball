package org.techtown.volleyball.activities

import android.app.Application
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import org.techtown.volleyball.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application
) : BaseViewModel(application) {
    init {
        Log.d(TAG, "init")
    }
}
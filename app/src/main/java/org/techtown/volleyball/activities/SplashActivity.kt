package org.techtown.volleyball.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.techtown.volleyball.R
import org.techtown.volleyball.viewmodels.SplashViewModel

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


    }
}
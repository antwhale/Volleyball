package org.techtown.volleyball.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.techtown.volleyball.R
import org.techtown.volleyball.viewmodels.SplashViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}
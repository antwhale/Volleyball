package org.techtown.volleyball.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.boo.sample.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.techtown.volleyball.R
import org.techtown.volleyball.databinding.ActivitySplashBinding
import org.techtown.volleyball.viewmodels.SplashViewModel

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private val splashViewModel: SplashViewModel by viewModels()
    override val layoutResId: Int
        get() = R.layout.activity_splash
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        observeLiveData()
        splashViewModel.versionCheck()
    }

    private fun initData() {
        Log.d(TAG, "initData")

//        splashViewModel.initTeamInfo()
    }
    private fun observeLiveData() {
        Log.d(TAG, "observeLiveData")

        splashViewModel.finishedInitTeamInfo.observe(this) { result ->
            if(result) {
                //배구경기 스케줄 가져오기
                splashViewModel.fetchMatchSchedule()
            }
        }

        splashViewModel.needToUpdate.observe(this) { needToUpdate ->
            Log.d(TAG, "needToUpdate: " + needToUpdate)
            if(needToUpdate) {
                displayUpdateMessage()
            } else {
                //업데이트 필요없다 -> 팀정보 초기화
                splashViewModel.initTeamInfo()
            }
        }

        splashViewModel.finishedFetchMatchSchedule.observe(this) { result ->
            if(result) {
                goMainActivity()
            }
        }
    }

    private fun goMainActivity() {
        lifecycleScope.launch {
            delay(2000)

            Log.d(TAG, "goMainActivity")

            val intent = Intent(baseContext, org.techtown.volleyball.activities.MainActivity::class.java)
            startActivity(intent)

            finish()
        }
    }

    private fun displayUpdateMessage() {
        Log.d(TAG, "displayUpdateMessage")

        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("업데이트 안내")
            setMessage("앱 버젼이 다릅니다. 보다 좋은 서비스를 위해 업데이트 해주세요.")
            setPositiveButton("업데이트") { dialog, which ->
                val url = "https://play.google.com/store/apps/details?id=org.techtown.volleyball"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            setNegativeButton("취소") { _, _ -> }
        }
        val dialog = builder.create()
        dialog.show()
    }
}
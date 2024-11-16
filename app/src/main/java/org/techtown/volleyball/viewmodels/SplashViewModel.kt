package org.techtown.volleyball.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.techtown.volleyball.BuildConfig
import org.techtown.volleyball.R
import org.techtown.volleyball.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    application: Application
) : BaseViewModel(application) {
    val needToUpdate = MutableLiveData(false)

    init {
        Log.d(TAG, "init")
        initTeamInfo()
    }

    private fun initTeamInfo() {
        Log.d(TAG, "initTeamInfo")


    }

    //어플 버전체크하기!
    //버전 바뀌면은 gradle(app) 가서 version code와 version name을 수정한다.
    //그리고 firebase가서 버전 수정한다.
    private fun versionCheck() {
        viewModelScope.launch(dispatcherIO) {
            Log.d(TAG, "versionCheck")

            val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5000)
                .build()
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)

            //인앱 매개변수 기본값 설정
            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default)
            //값 가져오기 및 활성화
            mFirebaseRemoteConfig.fetch()
                .addOnCompleteListener { task ->
                    var installedVersion = ""
                    var firebaseVersion = "0@0@0"

                    if(task.isSuccessful) {
                        mFirebaseRemoteConfig.activate()
                        installedVersion = BuildConfig.VERSION_NAME
                        Log.d(TAG, "installed version: " + installedVersion)

                        if(mFirebaseRemoteConfig.getString("android_version").length > 0) {
                            firebaseVersion = mFirebaseRemoteConfig.getString("android_version")
                            Log.d(TAG, "firebase version: " + firebaseVersion)
                        }

                        needToUpdate.postValue(compareVersion(installedVersion, firebaseVersion))
                    } else {
                        Log.d(TAG, "versionCheck task failed")
                        needToUpdate.postValue(false)
                    }
                }
        }
    }

    private fun compareVersion(installedVersion: String, firebaseVersion: String) : Boolean {
        Log.d(TAG, "compareVersion, installedVersion: " + installedVersion + ", firebaseVersion: " + firebaseVersion)

        var isNeedUpdate = false
        val arrX: List<String> = installedVersion.split("[.]")
        val arrY: List<String> = firebaseVersion.split("[.]")

        val length = Math.max(arrX.size, arrY.size)

        for (i in 0 until length) {
            var x: Int
            var y: Int
            x = try {
                arrX[i].toInt()
            } catch (e: ArrayIndexOutOfBoundsException) {
                0
            }
            y = try {
                arrY[i].toInt()
            } catch (e: ArrayIndexOutOfBoundsException) {
                0
            }
            if (x > y) {
                // 앱 버전이 큼
                isNeedUpdate = false
                break
            } else if (x < y) {
                // 비교 버전이 큼
                isNeedUpdate = true
                break
            } else {
                // 버전 동일
                isNeedUpdate = false
            }
        }
        return isNeedUpdate
    }
}
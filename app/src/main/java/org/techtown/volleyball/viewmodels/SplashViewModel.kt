package org.techtown.volleyball.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.techtown.volleyball.BuildConfig
import org.techtown.volleyball.MyApplication
import org.techtown.volleyball.R
import org.techtown.volleyball.base.BaseViewModel
import org.techtown.volleyball.constant.MAN_SCHEDULE_FILE_NAME
import org.techtown.volleyball.constant.MAN_SCHEDULE_FILE_URL
import org.techtown.volleyball.constant.SCHEDULE_DIRECTORY_NAME
import org.techtown.volleyball.constant.WOMAN_SCHEDULE_FILE_NAME
import org.techtown.volleyball.constant.WOMAN_SCHEDULE_FILE_URL
import org.techtown.volleyball.data.entity.TeamInfo
import org.techtown.volleyball.data.dao.TeamInfoDao
import org.techtown.volleyball.data.dao.UserInfoDao
import org.techtown.volleyball.data.entity.UserInfo
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    application: Application,
    val teamInfoDao: TeamInfoDao,
    val userInfoDao: UserInfoDao
) : BaseViewModel(application) {
    val needToUpdate = MutableLiveData<Boolean>()
    val finishedInitTeamInfo = MutableLiveData(false)
    val finishedFetchMatchSchedule = MutableLiveData(false)

    init {
        Log.d(TAG, "init")
    }

    fun initTeamInfo() {
        viewModelScope.launch(dispatcherIO) {
            Log.d(TAG, "initTeamInfo")

            teamInfoDao.apply {
                //여자배구
                insert(TeamInfo(1, "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411607&qvt=0&query=GS%EC%B9%BC%ED%85%8D%EC%8A%A4%20%EC%84%9C%EC%9A%B8KIXX%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","gscaltexkixx", "gs%EC%B9%BC%ED%85%8D%EC%8A%A4"))
                insert(TeamInfo(2, "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411611&qvt=0&query=IBK%EA%B8%B0%EC%97%85%EC%9D%80%ED%96%89%20%EC%95%8C%ED%86%A0%EC%8A%A4%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","ibk__altos", "%EA%B8%B0%EC%97%85%EC%9D%80%ED%96%89"))
                insert(TeamInfo(3, "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411608&qvt=0&query=%ED%95%9C%EA%B5%AD%EB%8F%84%EB%A1%9C%EA%B3%B5%EC%82%AC%20%ED%95%98%EC%9D%B4%ED%8C%A8%EC%8A%A4%20%EB%B0%B0%EA%B5%AC%EB%8B%A8%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","hipassvolleyclub", "%EB%8F%84%EB%A1%9C%EA%B3%B5%EC%82%AC"))
                insert(TeamInfo(4, "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=%EC%A0%95%EA%B4%80%EC%9E%A5+%EB%A0%88%EB%93%9C%EC%8A%A4%ED%8C%8C%ED%81%AC%EC%8A%A4+%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81&oquery=%EC%A0%95%EA%B4%80%EC%9E%A5+%EB%A0%88%EB%93%9C%EC%8A%A4%ED%8C%8C%ED%81%AC%EC%8A%A4+%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81&tqi=iPeHVwqVOsCssSDf%2F0Vssssss4h-239438","red_sparks", "%EC%9D%B8%EC%82%BC%EA%B3%B5%EC%82%AC"))
                insert(TeamInfo(5, "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411604&qvt=0&query=%ED%8E%98%ED%8D%BC%EC%A0%80%EC%B6%95%EC%9D%80%ED%96%89%20AI%20PEPPERS%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","", "%ED%8E%98%ED%8D%BC%EC%A0%80%EC%B6%95%EC%9D%80%ED%96%89"))
                insert(TeamInfo(6, "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411609&qvt=0&query=%ED%98%84%EB%8C%80%EA%B1%B4%EC%84%A4%20%ED%9E%90%EC%8A%A4%ED%85%8C%EC%9D%B4%ED%8A%B8%20%EB%B0%B0%EA%B5%AC%EB%8B%A8%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","hdecvolleyball", "%ED%98%84%EB%8C%80%EA%B1%B4%EC%84%A4"))
                insert(TeamInfo(7, "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411605&qvt=0&query=%ED%9D%A5%EA%B5%AD%EC%83%9D%EB%AA%85%20%ED%95%91%ED%81%AC%EC%8A%A4%ED%8C%8C%EC%9D%B4%EB%8D%94%EC%8A%A4%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","hkpinkspiders", "%ED%9D%A5%EA%B5%AD%EC%83%9D%EB%AA%85"))
                //남자배구
                insert(TeamInfo(8, "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411598&qvt=0&query=KB%EC%86%90%ED%95%B4%EB%B3%B4%ED%97%98%20%EC%8A%A4%ED%83%80%EC%A6%88%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","kbstarsvc", "kb%EC%86%90%ED%95%B4%EB%B3%B4%ED%97%98"))
                insert(TeamInfo(9, "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411600&qvt=0&query=OK%EA%B8%88%EC%9C%B5%EA%B7%B8%EB%A3%B9%20%EC%9D%8F%EB%A7%A8%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","okman_volleyballclub", "OK%EA%B8%88%EC%9C%B5%EA%B7%B8%EB%A3%B9"))
                insert(TeamInfo(10, "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411599&qvt=0&query=%EB%8C%80%ED%95%9C%ED%95%AD%EA%B3%B5%20%EC%A0%90%EB%B3%B4%EC%8A%A4%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","kal_jbos", "%EB%8C%80%ED%95%9C%ED%95%AD%EA%B3%B5"))
                insert(TeamInfo(11, "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411603&qvt=0&query=%EC%82%BC%EC%84%B1%ED%99%94%EC%9E%AC%20%EB%B8%94%EB%A3%A8%ED%8C%A1%EC%8A%A4%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","bluefangsvc", "%EC%82%BC%EC%84%B1%ED%99%94%EC%9E%AC"))
                insert(TeamInfo(12, "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411601&qvt=0&query=%EC%9A%B0%EB%A6%AC%EC%B9%B4%EB%93%9C%20%EC%9A%B0%EB%A6%ACWON%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","wooricardwibee", "%EC%9A%B0%EB%A6%AC%EC%B9%B4%EB%93%9C"))
                insert(TeamInfo(13, "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411602&qvt=0&query=%ED%95%9C%EA%B5%AD%EC%A0%84%EB%A0%A5%20%EB%B9%85%EC%8A%A4%ED%86%B0%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","vixtorm_vbc", "%ED%95%9C%EA%B5%AD%EC%A0%84%EB%A0%A5"))
                insert(TeamInfo(14, "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bjRJ&x_csa=%7B%22pkid%22%3A694%7D&pkid=694&os=26411597&qvt=0&query=%ED%98%84%EB%8C%80%EC%BA%90%ED%94%BC%ED%83%88%20%EC%8A%A4%EC%B9%B4%EC%9D%B4%EC%9B%8C%EC%BB%A4%EC%8A%A4%20%EA%B2%BD%EA%B8%B0%EC%98%81%EC%83%81","skywalkers_vbc", "%ED%98%84%EB%8C%80%EC%BA%90%ED%94%BC%ED%83%88"))
                insert(TeamInfo(15, "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ssc=tab.nx.all&query=%ED%94%84%EB%A1%9C%EB%B0%B0%EA%B5%AC+%EC%A0%95%EA%B7%9C%EB%A6%AC%EA%B7%B8+%EB%A6%AC%EA%B7%B8%EC%98%81%EC%83%81&oquery=2023-2024+%ED%94%84%EB%A1%9C%EB%B0%B0%EA%B5%AC+%EC%A0%95%EA%B7%9C%EB%A6%AC%EA%B7%B8+%EB%A6%AC%EA%B7%B8%EC%98%81%EC%83%81&tqi=iPeJJlqo15wsshr2Z10ssssssdw-255768","kovopr_official", "%EB%B0%B0%EA%B5%AC"))
            }

            finishedInitTeamInfo.postValue(true)
        }
    }

    fun fetchMatchSchedule() {
        viewModelScope.launch(dispatcherIO) {
            Log.d(TAG, "fetchMatchSchedule")

            val currentUserInfo = userInfoDao.getUserInfo()
            val currentLastDateFetchSchedule = currentUserInfo?.lastDateFetchSchedule
            val today = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.now().toString()
            } else {
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dateFormat.format(calendar.time)
            }

            if(currentUserInfo == null || currentLastDateFetchSchedule.isNullOrEmpty()) {
                Log.d(TAG, "경기일정 데이터 한 번도 가져온 적 없어서 가져와야 함")
                downloadSchedule()
                userInfoDao.insertUserInfo(UserInfo("1", today, 15))
            } else if(currentLastDateFetchSchedule != today) {
                Log.d(TAG, "경기일정 데이터 가져온지 하루 지나서 다시 가져와야함")
                downloadSchedule()
                userInfoDao.updateUserInfo(UserInfo("1", today, currentUserInfo.favoriteTeam))
            } else {
                Log.d(TAG, "경기일정 데이터 안 가져와도 됨")
            }

            finishedFetchMatchSchedule.postValue(true)
        }
    }

    private suspend fun downloadSchedule() {
        withContext(dispatcherIO) {
            Log.d(TAG, "downloadSchedule")
            //남자 스케줄 다운로드
            try {
                // 스케줄 디렉토리 생성
                val scheduleDir = File(getApplication<MyApplication>().filesDir, SCHEDULE_DIRECTORY_NAME)
                if (!scheduleDir.exists()) {
                    scheduleDir.mkdir()
                }

                // 파일 저장 경로
                val outputFile = File(scheduleDir, MAN_SCHEDULE_FILE_NAME)

                // URL 연결 및 데이터 읽기
                val url = URL(MAN_SCHEDULE_FILE_URL)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    // 스트림을 통해 파일 저장
                    connection.inputStream.use { input ->
                        outputFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    Log.d(TAG, "ManSchedule File downloaded and saved at: ${outputFile.absolutePath}")
                } else {
                    Log.d(TAG, "ManSchedule File Failed to download file. HTTP Response: ${connection.responseCode}")
                }
                connection.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                // 스케줄 디렉토리 생성
                val scheduleDir = File(getApplication<MyApplication>().filesDir, SCHEDULE_DIRECTORY_NAME)
                if (!scheduleDir.exists()) {
                    scheduleDir.mkdir()
                }

                // 파일 저장 경로
                val outputFile = File(scheduleDir, WOMAN_SCHEDULE_FILE_NAME)

                // URL 연결 및 데이터 읽기
                val url = URL(WOMAN_SCHEDULE_FILE_URL)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    // 스트림을 통해 파일 저장
                    connection.inputStream.use { input ->
                        outputFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    Log.d(TAG, "WomanSchedule File downloaded and saved at: ${outputFile.absolutePath}")
                } else {
                    Log.d(TAG, "WomanSchedule Failed to download file. HTTP Response: ${connection.responseCode}")
                }
                connection.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    //어플 버전체크하기!
    //버전 바뀌면은 gradle(app) 가서 version code와 version name을 수정한다.
    //그리고 firebase가서 버전 수정한다.
    fun versionCheck() {
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
        val arrX: List<String> = installedVersion.split(".")
        val arrY: List<String> = firebaseVersion.split(".")

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
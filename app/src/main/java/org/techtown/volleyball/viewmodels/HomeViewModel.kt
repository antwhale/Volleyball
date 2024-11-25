package org.techtown.volleyball.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.techtown.volleyball.MyApplication
import org.techtown.volleyball.base.BaseViewModel
import org.techtown.volleyball.base.utils.UiState
import org.techtown.volleyball.constant.AWAY_TEAM_KEY
import org.techtown.volleyball.constant.BASE_TEAM_NEWS_URL
import org.techtown.volleyball.constant.HOME_TEAM_KEY
import org.techtown.volleyball.constant.MAN_SCHEDULE_FILE_NAME
import org.techtown.volleyball.constant.PLACE_KEY
import org.techtown.volleyball.constant.ROUND_KEY
import org.techtown.volleyball.constant.SCHEDULE_DATE_FORMAT
import org.techtown.volleyball.constant.SCHEDULE_DIRECTORY_NAME
import org.techtown.volleyball.constant.TIME_KEY
import org.techtown.volleyball.constant.WOMAN_SCHEDULE_FILE_NAME
import org.techtown.volleyball.data.dao.TeamInfoDao
import org.techtown.volleyball.data.dao.UserInfoDao
import org.techtown.volleyball.data.entity.NaverTVItem
import org.techtown.volleyball.data.entity.NewsItem
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    val teamInfoDao: TeamInfoDao,
    val userInfoDao: UserInfoDao
) : BaseViewModel(application) {
    //match schedule
    val womanMatchScheduleUiState : MutableStateFlow<UiState<List<String>>> = MutableStateFlow(UiState.Loading)
    val manMatchScheduleUiState : MutableStateFlow<UiState<List<String>>> = MutableStateFlow(UiState.Loading)

    //news
    val teamNewsUiState : MutableStateFlow<UiState<List<NewsItem>>> = MutableStateFlow(UiState.Loading)

    //naverTV
    val naverTVUiState : MutableStateFlow<UiState<List<NaverTVItem>>> = MutableStateFlow(UiState.Loading)
    init {
        Log.d(TAG, "init ")
    }

    fun readManSchedule() {
        viewModelScope.launch(dispatcherIO) {
            val nowDate = Date()
            val sdf = SimpleDateFormat(SCHEDULE_DATE_FORMAT, Locale.KOREA)
            val todayString = sdf.format(nowDate)

            Log.d(TAG, "readManSchedule, todayString: " + todayString + ", locale: " + Locale.getDefault().toString())

            val manScheduleJsonObject = readFileAsJson(MAN_SCHEDULE_FILE_NAME)
            val todayManScheduleJsonObject = manScheduleJsonObject?.get(todayString)?.asJsonObject ?: kotlin.run {
                manMatchScheduleUiState.value = UiState.Success(emptyList())
                return@launch
            }
            val homeTeam = todayManScheduleJsonObject.get(HOME_TEAM_KEY).toString()
            val awayTeam = todayManScheduleJsonObject.get(AWAY_TEAM_KEY).toString()
            val place = todayManScheduleJsonObject.get(PLACE_KEY).toString()
            val round = todayManScheduleJsonObject.get(ROUND_KEY).toString()
            val time = todayManScheduleJsonObject.get(TIME_KEY).toString()

            Log.d(TAG, "readManSchedule, awayTeam: " + awayTeam + ", homeTeam: " + homeTeam + ", place: " + place + ", round: " + round + ", time: " + time)

            val scheduleData : List<String> = listOf(homeTeam, awayTeam, place, round, time)
            manMatchScheduleUiState.value = UiState.Success(scheduleData)
        }
    }

    fun readWomanSchedule() {
        viewModelScope.launch(dispatcherIO) {
            val nowDate = Date()
            val sdf = SimpleDateFormat(SCHEDULE_DATE_FORMAT, Locale.KOREA)
            val todayString = sdf.format(nowDate)

            Log.d(TAG, "readWomanSchedule, todayString: " + todayString + ", locale: " + Locale.getDefault().toString())

            val womanScheduleJsonObject = readFileAsJson(WOMAN_SCHEDULE_FILE_NAME)
            val todayWomanScheduleJsonObject = womanScheduleJsonObject?.get(todayString)?.asJsonObject ?: kotlin.run {
                womanMatchScheduleUiState.value = UiState.Success(emptyList())
                return@launch
            }
            val homeTeam = todayWomanScheduleJsonObject.get(HOME_TEAM_KEY).toString()
            val awayTeam = todayWomanScheduleJsonObject.get(AWAY_TEAM_KEY).toString()
            val place = todayWomanScheduleJsonObject.get(PLACE_KEY).toString()
            val round = todayWomanScheduleJsonObject.get(ROUND_KEY).toString()
            val time = todayWomanScheduleJsonObject.get(TIME_KEY).toString()

            Log.d(TAG, "readWomanSchedule, awayTeam: " + awayTeam + ", homeTeam: " + homeTeam + ", place: " + place + ", round: " + round + ", time: " + time)

            val scheduleData = listOf(homeTeam, awayTeam, place, round, time)
            womanMatchScheduleUiState.value = UiState.Success(scheduleData)
        }
    }

    private suspend fun readFileAsJson(fileName : String) : JsonObject? {
        return withContext(dispatcherIO) {
            try {
                val scheduleDir = File(getApplication<MyApplication>().filesDir, SCHEDULE_DIRECTORY_NAME)
                val file = File(scheduleDir, fileName)
                if(file.exists()) {
                    val content = file.readText()
                    JsonParser.parseString(content).asJsonObject
                } else {
                    //File not found
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if(MAN_SCHEDULE_FILE_NAME == fileName) {
                    manMatchScheduleUiState.value = UiState.Error(e)
                } else {
                    womanMatchScheduleUiState.value = UiState.Error(e)
                }
                null
            }
        }
    }

    fun fetchFavoriteTeamNews() {
        viewModelScope.launch(dispatcherIO) {
            Log.d(TAG, "fetchFavoriteTeamNews")

            val favoriteTeamId = userInfoDao.getFavoriteTeamId() ?: 15
            val favoriteTeamNewsUrl = teamInfoDao.getTeamInfo(favoriteTeamId)?.newsUrl ?: ""
            val crawlingUrl = BASE_TEAM_NEWS_URL + favoriteTeamNewsUrl

            val crawlingResult = mutableListOf<NewsItem>()
            val doc = Jsoup.connect(crawlingUrl).get()
            val elements = doc.select("div#listWrap").select("div.listPhoto")

            var count = 0
            for (element in elements) {
                if (count == 4) break
                val newsUrl = element.select("p.img a").attr("href")
                val imgUrl = element.select("img").attr("src")
                val title = element.select("dt a").text()
                crawlingResult.add(NewsItem(imgUrl, newsUrl, title))
                Log.d(TAG, "newsUrl: $newsUrl\n imgUrl: $imgUrl\n title: $title")
                count++
            }

            teamNewsUiState.value = UiState.Success(crawlingResult)
        }
    }

    fun fetchNaverTVVideo() {
        viewModelScope.launch(dispatcherIO) {
            Log.d(TAG, "fetchNaverTVVideo")

            val favoriteTeamId = userInfoDao.getFavoriteTeamId() ?: 15
            val favoriteTeamNaverTVUrl = teamInfoDao.getTeamInfo(favoriteTeamId)?.naverTVUrl ?: ""

            val crawlingResult = mutableListOf<NaverTVItem>()
            val doc = Jsoup.connect(favoriteTeamNaverTVUrl).get()
            val elements = doc.select("div.video_thumbnail")

            var count = 0
            for (element in elements) {
                if(count == 5) break;
                val naverTVUrl = element.select("a.thumb_link").attr("href")
                val imgUrl = element.select("img").attr("src")
                crawlingResult.add(NaverTVItem(imgUrl, naverTVUrl))

                count++
            }

            naverTVUiState.value = UiState.Success(crawlingResult)
        }
    }


}


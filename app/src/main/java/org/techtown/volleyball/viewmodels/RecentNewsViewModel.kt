package org.techtown.volleyball.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.techtown.volleyball.MyApplication
import org.techtown.volleyball.base.BaseViewModel
import org.techtown.volleyball.base.utils.UiState
import org.techtown.volleyball.constant.THE_SPIKE_RSS_URL
import org.techtown.volleyball.data.dao.TeamInfoDao
import org.techtown.volleyball.news3.RSSNewsItem
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.lang.Exception
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class RecentNewsViewModel @Inject constructor(
    application: Application
) : BaseViewModel(application) {
    val recentNewsUiState = MutableStateFlow<UiState<List<RSSNewsItem>>>(UiState.Loading)

    init {
        Log.d(TAG, "init")
    }

    fun fetchRecentNews() {
        viewModelScope.launch(dispatcherIO) {
            Log.d(TAG, "fetchRecentNews")

            val result = mutableListOf<RSSNewsItem>()

            try {
                val rssURL = URL(THE_SPIKE_RSS_URL)

                val inputStream = rssURL.openStream()

                //읽어온 xml를 파싱(분석)해주는 객체 생성
                val factory = XmlPullParserFactory.newInstance()
                val xpp = factory.newPullParser()

                //utf-8은 한글도 읽어오기 위한 인코딩 방식
                xpp.setInput(inputStream, "utf-8")
                var eventType = xpp.eventType

                var item: RSSNewsItem? = null
                var tagName = ""

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when(eventType) {
                        XmlPullParser.START_DOCUMENT ->
                            break
                        XmlPullParser.START_TAG -> {
                            tagName = xpp.name

                            if(tagName == "item") {
                                item = RSSNewsItem()
                            } else if(tagName == "title") {
                                xpp.next()
                                item?.title = xpp.text
                            } else if(tagName == "link") {
                                xpp.next()
                                item?.link = xpp.text
                            } else if(tagName == "description") {
                                xpp.next()
                                item?.description = xpp.text
                            }
                            break
                        }
                        XmlPullParser.TEXT -> break
                        XmlPullParser.END_TAG -> {
                            tagName = xpp.name
                            if(tagName == "item") {

                                //기사 하나 파싱했으니 읽어온 기사 한개를 대량의 데이터에 추가
                                if (item != null) {
                                    result.add(item)

                                    //img 주소 가져오기
                                    val description = item.description
                                    if(description != null && description.indexOf("img") != -1){
                                        //이미지 있다면
                                        val imgSrc = item.description.split("<img")[1].split("src=\"")[1].split("thum")[0].plus("thum")
                                        item.imgUrl = imgSrc
                                    }
                                    item = null
                                }
                            }
                            break
                        }
                    }
                    eventType = xpp.next()
                } //while

                //파싱 작업이 완료되었다!!
                recentNewsUiState.value = UiState.Success(result)

            } catch (e: IOException) {
                e.printStackTrace()
                recentNewsUiState.value = UiState.Error(e)
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
                recentNewsUiState.value = UiState.Error(e)
            }
        }
    }
}
package org.techtown.volleyball.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.techtown.volleyball.NewsActivity
import org.techtown.volleyball.R
import org.techtown.volleyball.base.BaseFragment
import org.techtown.volleyball.base.utils.UiState
import org.techtown.volleyball.base.utils.setOneClickListener
import org.techtown.volleyball.constant.SCHEDULE_DATE_FORMAT
import org.techtown.volleyball.constant.THE_SPIKE_URL
import org.techtown.volleyball.data.entity.NewsItem
import org.techtown.volleyball.databinding.FragmentHomeBinding
import org.techtown.volleyball.viewmodels.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val homeViewModel: HomeViewModel by viewModels()

    override val layoutResID: Int
        get() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated")

        binding.homeViewModel = homeViewModel

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.manMatchScheduleUiState
                    .onStart {
                        homeViewModel.readManSchedule()
                    }.collect { uiState ->
                        when(uiState) {
                            is UiState.Success -> {
                                showProgress(false)
                                Log.d(TAG, "man's schedule, homeTeam: " + uiState.data[0] + ", awayTeam: " + uiState.data[1] + ", place: " + uiState.data[2] + ", round: " + uiState.data[3] + ", time: " + uiState.data[4])
                                showManSchedule(uiState.data)
                            }
                            is UiState.Error -> {
                                Log.d(TAG, "error occurred when reading man's schedule")
                                showProgress(false)
                            }
                            is UiState.Loading -> {
                                Log.d(TAG, "MatchScheduleUiState Loading")
//                                showProgress(true)
                            }
                        }
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.womanMatchScheduleUiState
                    .onStart {
                        homeViewModel.readWomanSchedule()
                    }.collect { uiState ->
                        when(uiState) {
                            is UiState.Success -> {
                                showScheduleProgress(false)
                                Log.d(TAG, "woman's schedule, homeTeam: " + uiState.data[0] + ", awayTeam: " + uiState.data[1] + ", place: " + uiState.data[2] + ", round: " + uiState.data[3] + ", time: " + uiState.data[4])
                                showWomanSchedule(uiState.data)
                            }
                            is UiState.Error -> {
                                Log.d(TAG, "error occurred when reading woman's schedule")
                                showScheduleProgress(false)
                            }
                            is UiState.Loading -> {
                                Log.d(TAG, "MatchScheduleUiState Loading")
                                showScheduleProgress(true)
                            }
                        }
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.teamNewsUiState
                    .onStart {
                        homeViewModel.fetchFavoriteTeamNews()
                    }.collect {uiState ->
                        when(uiState) {
                            is UiState.Success -> {
                                Log.d(TAG, "fetchFavoriteTeamNews success")
                                showFavoriteTeamNews(uiState.data)
                            }
                            is UiState.Error -> {
                                Log.d(TAG, "error occurred when fetching team news")
                            }
                            is UiState.Loading -> {
                                Log.d(TAG, "TeamNewsUiState Loading")
                            }
                        }
                    }
            }
        }
    }

    private fun showManSchedule(scheduleData: List<String>) {

        val nowDate = Date()
        val sdf = SimpleDateFormat(SCHEDULE_DATE_FORMAT, Locale.KOREA)
        val todayString = sdf.format(nowDate)

        binding.apply {
            if(scheduleData.isEmpty()) {
                manTextView.text = "남자부 경기가 없습니다."
                matchTimePlaceTextView.visibility = View.GONE
                matchRoundTextView.visibility = View.GONE
                matchContentTextView.visibility = View.GONE
            } else {
                manTextView.text = todayString + " " + scheduleData[4].replace("\"", "")
                matchTimePlaceTextView.text = scheduleData[2].replace("\"", "")
                matchRoundTextView.text = scheduleData[3].replace("\"", "") + "ound"
                matchContentTextView.text = scheduleData[0].replace("\"", "") + " vs " + scheduleData[1].replace("\"", "")

                matchTimePlaceTextView.visibility = View.VISIBLE
                matchRoundTextView.visibility = View.VISIBLE
                matchContentTextView.visibility = View.VISIBLE
            }
        }
    }

    private fun showWomanSchedule(scheduleData: List<String>) {
        val nowDate = Date()
        val sdf = SimpleDateFormat(SCHEDULE_DATE_FORMAT, Locale.KOREA)
        val todayString = sdf.format(nowDate)

        binding.apply {
            if(scheduleData.isEmpty()) {
                womanTextView.text = "여자부 경기가 없습니다."
                matchTimePlaceTextView2.visibility = View.GONE
                matchRoundTextView2.visibility = View.GONE
                matchContentTextView2.visibility = View.GONE
            } else {
                womanTextView.text = todayString + " " + scheduleData[4].replace("\"", "")
                matchTimePlaceTextView2.text = scheduleData[2].replace("\"", "")
                matchRoundTextView2.text = scheduleData[3].replace("\"", "") + "ound"
                matchContentTextView2.text = scheduleData[0].replace("\"", "") + " vs " + scheduleData[1].replace("\"", "")

                matchTimePlaceTextView2.visibility = View.VISIBLE
                matchRoundTextView2.visibility = View.VISIBLE
                matchContentTextView2.visibility = View.VISIBLE
            }
        }
    }

    private fun showScheduleProgress(isShow: Boolean) {
        Log.d(TAG, "showScheduleProgress, isShow: " + isShow)

        binding.progressBar.visibility = if(isShow) View.VISIBLE else View.GONE
    }

    private fun showFavoriteTeamNews(newsItems: List<NewsItem>) {
        Log.d(TAG, "showFavoriteTeamNews")

        if(newsItems.isEmpty()) {

        } else {
            with(binding) {
                Glide.with(newsImage.context)
                    .load(THE_SPIKE_URL + newsItems[0].thumbUrl)
                    .into(newsImage)

                newsTitle1.text = newsItems[0].title
                newsTitle2.text = newsItems[1].title
                newsTitle3.text = newsItems[2].title
                newsTitle4.text = newsItems[3].title

                newsImage.setOneClickListener {
                    showLinkPage(THE_SPIKE_URL + newsItems[0].newsUrl)
                }

                newsTitle1.setOneClickListener {
                    showLinkPage(THE_SPIKE_URL + newsItems[0].newsUrl)
                }

                newsTitle2.setOneClickListener {
                    showLinkPage(THE_SPIKE_URL + newsItems[1].newsUrl)
                }

                newsTitle3.setOneClickListener {
                    showLinkPage(THE_SPIKE_URL + newsItems[2].newsUrl)
                }

                newsTitle4.setOneClickListener {
                    showLinkPage(THE_SPIKE_URL + newsItems[3].newsUrl)
                }
            }
        }
    }

    private fun showLinkPage(link: String) {
        Log.d(TAG, "showLinkPage, link: $link")
//        val intent = Intent(context, NewsActivity::class.java)
//        intent.putExtra("Link", link)
//        startActivity(intent)
    }
}
package org.techtown.volleyball.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.techtown.volleyball.R
import org.techtown.volleyball.adapters.RecentNewsAdapter
import org.techtown.volleyball.base.BaseFragment
import org.techtown.volleyball.base.utils.UiState
import org.techtown.volleyball.databinding.FragmentRecentNewsBinding
import org.techtown.volleyball.news3.RSSNewsItem
import org.techtown.volleyball.viewmodels.RecentNewsViewModel

@AndroidEntryPoint
class RecentNewsFragment : BaseFragment<FragmentRecentNewsBinding>() {
    private val recentNewsViewModel : RecentNewsViewModel by viewModels()
    override val layoutResID: Int
        get() = R.layout.fragment_recent_news

    private lateinit var rssNewsAdapter : RecentNewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated")

        initViews()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                recentNewsViewModel.recentNewsUiState
                    .onStart {
                        recentNewsViewModel.fetchRecentNews()
                    }.collect { uiState ->
                        when(uiState) {
                            is UiState.Success -> {
                                Log.d(TAG, "fetchRecentNews success")

                            }
                            is UiState.Error -> {
                                Log.d(TAG, "error occurred when fetching recent news")
                            }
                            is UiState.Loading -> {
                                Log.d(TAG, "recentNewsUiState Loading")
                            }
                        }
                    }
            }
        }

    }

    private fun initViews() {
        Log.d(TAG, "initViews")

        with(binding.recyclerView) {
            val layoutManager = GridLayoutManager(context, 1)
            setLayoutManager(layoutManager)

            rssNewsAdapter = RecentNewsAdapter(emptyList<RSSNewsItem>(), context)
        }
    }
}
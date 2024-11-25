package org.techtown.volleyball.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.techtown.volleyball.R
import org.techtown.volleyball.base.BaseFragment
import org.techtown.volleyball.databinding.FragmentTeamNewsBinding

class TeamNewsFragment : BaseFragment<FragmentTeamNewsBinding>(){
    override val layoutResID: Int
        get() = R.layout.fragment_team_news
    private val args : TeamNewsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        initWebView()

        binding.webView.loadUrl(args.newsUrl)
    }

    private fun initWebView() {
        Log.d(TAG, "initWebView")

        binding.webView.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            allowFileAccess = true
            allowUniversalAccessFromFileURLs = true
            setSupportMultipleWindows(true)
        }
        binding.webView.webViewClient = MyWebViewClient()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback {
            Log.d(TAG, "Click backPressButton")
            findNavController().navigateUp()
        }
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return args.newsUrl == Uri.parse(url).host
        }
    }
}
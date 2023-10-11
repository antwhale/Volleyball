package org.techtown.volleyball.playerranking5;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.techtown.volleyball.R;

public class PlayerFragment extends Fragment {
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.web_screen, container, false);

        webView = rootView.findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new PlayerFragment.MyWebViewClient());

        webView.loadUrl("https://kovo.co.kr/KOVO/game/v-league?first=%EC%84%A0%EC%88%98+%EC%88%9C%EC%9C%84");

        return rootView;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if ("https://m.kovo.co.kr/game/v-league/11300_player_ranking.asp?season=017".equals(Uri.parse(url).getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return true;
            }
            return false;
            /*
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
             */
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }
}
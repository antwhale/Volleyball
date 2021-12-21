package org.techtown.volleyball;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsActivity extends AppCompatActivity {
    WebView webView;
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 타이틀 사용 안함
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); // 커스텀 사용
        getSupportActionBar().setCustomView(R.layout.title_v9v9); // 커스텀 사용할 파일 위치

        Intent intent = getIntent();
        link = intent.getStringExtra("Link");

        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webSettings.setAllowFileAccess(true);
// 파일 기반 XSS 취약성 문제 해결 (구글지적)
// true -> false 변경
//1. WebView에 위험한 설정이 포함되지 않도록 설정
        webSettings.setAllowFileAccessFromFileURLs(true);
// 다른 도메인의경우에도 허용하는가
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setSupportMultipleWindows(true);

        webView.setWebViewClient(new MyWebViewClient());

        webView.loadUrl(link);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (link.equals(Uri.parse(url).getHost())) {
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
}
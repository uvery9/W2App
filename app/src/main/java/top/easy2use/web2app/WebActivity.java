/*
https://github.com/limpoxe/Android-Plugin-Framework
package top.easy2use.web2app;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class WebActivity extends AppCompatActivity {

    WebView web;
    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);

        web = findViewById(R.id.webview);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web.loadUrl("https://www.baidu.com/");
            }
        });

        setUpWebViewSetting();
        setClient();

        web.loadUrl("file:///android_asset/host_localweb_test.html");
    }

    private void setUpWebViewSetting() {
        WebSettings webSettings = web.getSettings();

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 根据cache-control决定是否从网络上取数据
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);// 显示放大缩小
        webSettings.setJavaScriptEnabled(true);
        // webSettings.setPluginsEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setUserAgentString(webSettings.getUserAgentString());
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getCacheDir().getPath());
        webSettings.setUseWideViewPort(true);// 影响默认满屏和双击缩放
        webSettings.setLoadWithOverviewMode(true);// 影响默认满屏和手势缩放

    }

    private void setClient() {

        web.setWebChromeClient(new WebChromeClient() {
        });

        // 如果要自动唤起自定义的scheme，不能设置WebViewClient，
        // 否则，需要在shouldOverrideUrlLoading中自行处理自定义scheme
        // webView.setWebViewClient();
        web.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Debug.trackHuaweiReceivers();
    }
}*/

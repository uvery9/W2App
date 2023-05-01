package top.easy2use.web2app

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    private lateinit var btnForward: ImageButton
    private lateinit var btnFresh: ImageButton
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWebView()
        initButtonsAndClicks()

        lifecycleScope.launch {
            val website =
                WebUtils.getWebSite("https://www.google.com/", "https://www.baidu.com/", 460)
            Log.d("WebUtils", "Website: $website")
            webView.loadUrl(website)
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView = findViewById<WebView>(R.id.webView).apply {
            canGoBack()
            canGoForward()
        }
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                btnFresh.isEnabled = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                //Make Enable or Disable buttons
                view?.canGoBack()?.let {
                    btnBack.isEnabled = it
                }
                view?.canGoForward()?.let {
                    btnForward.isEnabled = it
                }
                btnFresh.isEnabled = true
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                Toast.makeText(view?.context, description, Toast.LENGTH_LONG).show()
            }

            /*override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url == null) return false
                return try {
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        view?.loadUrl(url)
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                    true
                } catch (ex: Exception) {
                    false
                }

            }*/

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return shouldOverrideUrlLoading(view, request?.url.toString())
            }

            /**
             * 跳转到其他链接
             */
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                Log.d("", "WebView shouldOverrideUrlLoading：$url")
                val scheme = Uri.parse(url).scheme ?: return false
                when (scheme) {
                    // 如果这是跳链接操作
                    "http", "https" -> view?.loadUrl(url!!)
                    // 如果这是打电话操作
                    "tel" -> ""
                    else -> ""
                }
                return true
            }


            /*override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
                request?.url
            }*/
        }

        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.settings.domStorageEnabled = true
//        webView.settings.setAppCacheEnabled(true);
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
    }

    private fun initButtonsAndClicks() {
        btnBack = findViewById<ImageButton?>(R.id.backButton).apply {
            setOnClickListener {
                if (webView.canGoBack())
                    webView.goBack()
            }
        }
        btnForward = findViewById<ImageButton?>(R.id.forwardButton).apply {
            setOnClickListener {
                if (webView.canGoForward())
                    webView.goForward()
            }
        }
        btnFresh = findViewById<ImageButton?>(R.id.btnFresh).apply {
            setOnClickListener {
                webView.reload()
            }
        }


    }


}
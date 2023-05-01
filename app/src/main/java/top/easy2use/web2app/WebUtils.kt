package top.easy2use.web2app

import android.util.Log
import android.webkit.URLUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL

object WebUtils {

    /*
    *
    *
    *
    *
    * class CrossAppWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String urlString) {
            if (urlString.startsWith("weixin://wap/pay?")) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urlString));
                    CrossAppActivity.getContext().startActivity(intent);
                } catch (Exception exception) {
                    Toast.makeText(CrossAppActivity.getContext(), "支付失败,请重试", Toast.LENGTH_SHORT).show();
                }
                CrossAppActivity.getContext().runOnGLThread(new Runnable() {
                    @Override
                    public void run() {
                        CrossAppWebViewHelper._onJsCallback(viewTag, urlString);
                    }
                });
                return true;
            } else if (urlString.contains("platformapi/startapp")) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urlString));
                    intent.addCategory("android.intent.category.BROWSABLE");
                    intent.setComponent(null);
                    CrossAppActivity.getContext().startActivity(intent);
                } catch (Exception exception) {
                    Toast.makeText(CrossAppActivity.getContext(), "支付失败,请重试", Toast.LENGTH_SHORT).show();
                }
                CrossAppActivity.getContext().runOnGLThread(new Runnable() {
                    @Override
                    public void run() {
                        CrossAppWebViewHelper._onJsCallback(viewTag, urlString);
                    }
                });
                return true;
            }

            URI uri = URI.create(urlString);
            if (uri != null && uri.getScheme().equals(jsScheme)) {
                CrossAppActivity.getContext().runOnGLThread(new Runnable() {
                    @Override
                    public void run() {
                        CrossAppWebViewHelper._onJsCallback(viewTag, urlString);
                    }
                });
                return true;
            }
            return CrossAppWebViewHelper._shouldStartLoading(viewTag, urlString);
        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            super.onPageFinished(view, url);
            CrossAppActivity.getContext().runOnGLThread(new Runnable() {
                @Override
                public void run() {
                    CrossAppWebViewHelper._didFinishLoading(viewTag, url);
                }
            });
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, final String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            CrossAppActivity.getContext().runOnGLThread(new Runnable() {
                @Override
                public void run() {
                    CrossAppWebViewHelper._didFailLoading(viewTag, failingUrl);
                }
            });

        }

        //        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }
    *
    *
    *
    *
    *
    *
    *
    * /**
         * 同名 API 兼容
         */
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl().toString());
        }

        /**
         * 跳转到其他链接
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Timber.i("WebView shouldOverrideUrlLoading：%s", url);
            String scheme = Uri.parse(url).getScheme();
            if (scheme == null) {
                return false;
            }
            switch (scheme) {
                // 如果这是跳链接操作
                case "http":
                case "https":
                    view.loadUrl(url);
                    break;
                // 如果这是打电话操作
                case "tel":
                    dialing(view, url);
                    break;
                default:
                    break;
            }
            return true;
        }
    *
    *
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (onByWebClientCallback != null) {
            return onByWebClientCallback.isOpenThirdApp(url);
        } else {
            Activity mActivity = this.mActivityWeakReference.get();
            if (mActivity != null && !mActivity.isFinishing()) {
                return ByWebTools.handleThirdApp(mActivity, url);
            } else {
                return !url.startsWith("http:") && !url.startsWith("https:");
            }
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (onByWebClientCallback != null) {
            return onByWebClientCallback.isOpenThirdApp(url);
        } else {
            Activity mActivity = this.mActivityWeakReference.get();
            if (mActivity != null && !mActivity.isFinishing()) {
                return ByWebTools.handleThirdApp(mActivity, url);
            } else {
                return !url.startsWith("http:") && !url.startsWith("https:");
            }
        }
    }
     */
    suspend fun getWebSite(primary: String, backup: String, timeout: Int = 460): String {
        return if (isInternetAvailableSuspend(primary, timeout)) return primary
        else backup
    }

    private fun isInternetReachable(urlStr: String?, timeout: Int = 460): Boolean {
        return try {
            /*val urlConnection = URL(urlStr).openConnection() as HttpURLConnection
                urlConnection.instanceFollowRedirects = true
                val openWebsite = urlConnection.content */
            if (!URLUtil.isValidUrl(urlStr)) return false
            val connection = URL(urlStr).openConnection() as HttpURLConnection
            connection.connectTimeout = timeout
            val code: Int = connection.responseCode
            code == 200
        } catch (e: Exception) {
            Log.e("WebUtils", "isInternetReachable exception occurs: $e")
            false
        }
    }

    private suspend fun isInternetAvailableSuspend(urlStr: String?, timeout: Int): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext isInternetReachable(urlStr, timeout)
        }
    }
}
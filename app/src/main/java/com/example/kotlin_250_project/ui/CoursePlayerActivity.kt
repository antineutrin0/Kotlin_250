package com.example.kotlin_250_project.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.R

class CoursePlayerActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_player)

        val videoUrl = intent.getStringExtra("videoUrl")

        webView = findViewById(R.id.videoWebView)

        if (!videoUrl.isNullOrEmpty()) {
            val embedUrl = convertToEmbeddedUrl(videoUrl)

            webView.settings.javaScriptEnabled = true
            webView.settings.pluginState = WebSettings.PluginState.ON
            webView.settings.loadWithOverviewMode = true
            webView.settings.useWideViewPort = true
            webView.webChromeClient = WebChromeClient()
            webView.webViewClient = WebViewClient()

            val htmlData = """
                <html>
                <body style="margin:0;padding:0;">
                    <iframe width="100%" height="100%" 
                        src="$embedUrl" 
                        frameborder="0" allowfullscreen></iframe>
                </body>
                </html>
            """
            webView.loadData(htmlData, "text/html", "utf-8")
        }
    }

    private fun convertToEmbeddedUrl(youtubeUrl: String): String {
        return if (youtubeUrl.contains("watch?v=")) {
            youtubeUrl.replace("watch?v=", "embed/")
        } else {
            youtubeUrl
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}

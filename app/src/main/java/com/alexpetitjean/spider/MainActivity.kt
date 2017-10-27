package com.alexpetitjean.spider

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.*
import android.support.v4.util.PatternsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebViewClient
import com.alexpetitjean.spider.extensions.closeKeyboard
import com.alexpetitjean.spider.extensions.onTextChanged
import com.alexpetitjean.spider.extensions.requestFocusAndOpenKeyboard
import com.alexpetitjean.spider.extensions.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "Spider"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomSheet = BottomSheetBehavior.from(settingsBottomSheet)
        bottomSheet.state = STATE_COLLAPSED

        settingsBar.setOnClickListener {
            if (bottomSheet.state == STATE_COLLAPSED) {
                bottomSheet.state = STATE_EXPANDED
            } else if (bottomSheet.state == STATE_EXPANDED){
                bottomSheet.state = STATE_COLLAPSED
            }
        }

        bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
            override fun onStateChanged(bottomSheet: View, newState: Int) = rotateChevron(newState)
        })

        webView.webViewClient = WebViewClient()

        swipeRefreshLayout.setOnRefreshListener {
            reloadWebView()
            swipeRefreshLayout.isRefreshing = false
        }

        urlInput.onTextChanged { text ->
            insertHttpButton.isEnabled = text.isEmpty()
            insertNgrokButton.isEnabled = text.isEmpty()
        }

        insertHttpButton.setOnClickListener {
            urlInput.setText("https://")
            urlInput.setSelection(urlInput.text.length)
        }

        insertNgrokButton.setOnClickListener {
            urlInput.setText("https://.ngrok.io/")
            urlInput.setSelection(8)
        }

        clearButton.setOnClickListener { urlInput.text = null }

        goButton.setOnClickListener {
            reloadWebView()
            urlInput.closeKeyboard()
            urlInput.clearFocus()
            urlInput.postDelayed({
                bottomSheet.state = STATE_COLLAPSED
            }, 200)
        }
    }

    private fun reloadWebView() {
        val url = urlInput.text.toString()

        val validUrl = PatternsCompat.WEB_URL.matcher(url).matches()
        if (validUrl) {
            // TODO: Get headers from whatever input
            val extraHeaders = mapOf<String, String>()
            webView.loadUrl(url, extraHeaders)
        } else {
            toast("Invalid URL")
        }
    }

    private fun rotateChevron(bottomSheetState: Int) {
        val rotationAngle: Float = when(bottomSheetState) {
            STATE_COLLAPSED -> 0f
            STATE_EXPANDED -> 180f
            else -> settingsChevron.rotation
        }
        settingsChevron.animate().rotation(rotationAngle).setDuration(200).start()
    }
}

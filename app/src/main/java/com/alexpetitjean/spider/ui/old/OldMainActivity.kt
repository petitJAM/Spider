package com.alexpetitjean.spider.ui.old

import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED
import android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alexpetitjean.spider.R
import com.alexpetitjean.spider.data.HttpHeader
import com.alexpetitjean.spider.data.WebViewConfig
import com.alexpetitjean.spider.extensions.children
import com.alexpetitjean.spider.extensions.closeKeyboard
import com.alexpetitjean.spider.extensions.onTextChanged
import com.alexpetitjean.spider.isValidUrl
import com.alexpetitjean.spider.tintAllIcons
import kotlinx.android.synthetic.main.activity_old_main.*
import kotlinx.android.synthetic.main.content_bottom_sheet_toolbar.*
import kotlinx.android.synthetic.main.content_web_view_config.*
import kotlinx.android.synthetic.main.row_http_header_inputs.view.*

class OldMainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "Spider"
    }

    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(settingsBottomSheet)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_main)

        toolbar.inflateMenu(R.menu.webview_controls)
        tintAllIcons(toolbar.menu, ContextCompat.getColor(this, R.color.white))

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.webViewControlsBack -> {
                    webView.goBack()
                    true
                }
                R.id.webViewControlsForward -> {
                    webView.goForward()
                    true
                }
                else -> false
            }
        }

        bottomSheetBehavior.state = STATE_COLLAPSED

        settingsBar.setOnClickListener {
            if (bottomSheetBehavior.state == STATE_COLLAPSED) {
                bottomSheetBehavior.state = STATE_EXPANDED
            } else if (bottomSheetBehavior.state == STATE_EXPANDED) {
                bottomSheetBehavior.state = STATE_COLLAPSED
            }
        }

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                settingsChevron.rotation = -slideOffset * 180
                overlay.alpha = 0.6f * slideOffset
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) = Unit
        })

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                swipeRefreshLayout.isRefreshing = true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                swipeRefreshLayout.isRefreshing = false
                toolbar.title = view.title
            }
        }

        reloadCurrentOrLoadUrl()
        swipeRefreshLayout.setOnRefreshListener {
            reloadCurrentOrLoadUrl()
        }

        urlInput.onTextChanged { text ->
            val isUrlEmpty = text.trim().isEmpty()
            insertNgrokButton.isEnabled = isUrlEmpty
        }

        urlInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                reloadWebView()
                true
            } else {
                false
            }
        }

        insertHeadersDisplayWebsiteButton.setOnClickListener {
            urlInput.setText("www.whatismybrowser.com/detect/what-http-headers-is-my-browser-sending")
            urlInput.error = null
            urlInput.requestFocus()
        }

        insertNgrokButton.setOnClickListener {
            urlInput.setText(".ngrok.io/")
            urlInput.setSelection(0)
            urlInput.error = null
            urlInput.requestFocus()
        }

        clearButton.setOnClickListener {
            urlInput.text = null
            urlInput.error = null
        }

        goButton.setOnClickListener {
            reloadWebView()
        }

        httpsCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                urlInput.setPrefix("https://")
            } else {
                urlInput.setPrefix("http://")
            }
        }

        addHttpHeaderButton.setOnClickListener {
            val httpHeaderRow = LayoutInflater.from(this)
                    .inflate(R.layout.row_http_header_inputs, httpHeadersList, false)

            httpHeaderRow.removeHttpHeaderButton.setOnClickListener {
                httpHeadersList.removeView(httpHeaderRow)
            }
            httpHeadersList.addView(httpHeaderRow)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (bottomSheetBehavior.state == STATE_EXPANDED) {
                val outRect = Rect()
                settingsBottomSheet.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    bottomSheetBehavior.state = STATE_COLLAPSED
                    return true
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun reloadCurrentOrLoadUrl() {
        val currentUrl = webView.url
        if (currentUrl.isNullOrEmpty()) {
            reloadWebView()
        } else {
            webView.reload()
        }
    }

    private fun reloadWebView() {
        val webViewConfig = getWebViewConfig()

        val prefix = if (webViewConfig.https) {
            "https://"
        } else {
            "http://"
        }
        val url = "$prefix${webViewConfig.url}"
        if (isValidUrl(url)) {
            // TODO: Get headers from whatever input
            val extraHeaders = webViewConfig.httpHeadersMap
            webView.loadUrl(url, extraHeaders)
            closeKeyboardAndBottomSheet()
            urlInput.error = null
        } else {
            urlInput.error = "Invalid URL"
        }
    }

    private fun closeKeyboardAndBottomSheet() {
        urlInput.closeKeyboard()
        urlInput.clearFocus()
        urlInput.postDelayed({
            bottomSheetBehavior.state = STATE_COLLAPSED
        }, 200)
    }

    private fun getWebViewConfig(): WebViewConfig {
        val url = urlInput.text.toString().trim()
        val https = httpsCheckbox.isChecked

        val httpHeaders = httpHeadersList.children.map { view ->
            val name = view.httpHeaderNameInput.text.toString().trim()
            val value = view.httpHeaderValueInput.text.toString().trim()
            HttpHeader(name, value)
        }

        return WebViewConfig(url, https, httpHeaders)
    }
}

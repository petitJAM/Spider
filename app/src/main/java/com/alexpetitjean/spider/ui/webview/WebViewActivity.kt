package com.alexpetitjean.spider.ui.webview

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED
import android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.alexpetitjean.spider.R
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.content_bottom_sheet_toolbar.*

class WebViewActivity
    : AppCompatActivity() {

    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(settingsBottomSheet)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView.loadUrl("www.whatismybrowser.com/detect/what-http-headers-is-my-browser-sending")

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

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

        //        val spiderDb = Room.databaseBuilder(applicationContext, SpiderDatabase::class.java, SpiderDatabase.NAME).build()
    }
}

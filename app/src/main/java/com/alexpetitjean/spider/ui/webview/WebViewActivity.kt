package com.alexpetitjean.spider.ui.webview

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED
import android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import com.alexpetitjean.spider.R
import com.alexpetitjean.spider.database.SpiderDatabase
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.content_bottom_sheet_toolbar.*
import kotlinx.android.synthetic.main.content_web_view_config.*

class WebViewActivity
    : AppCompatActivity(),
      WebViewContract.View {


    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(settingsBottomSheet)
    }

    override lateinit var presenter: WebViewContract.Presenter

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

        val projectAdapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf("Project A", "Project B", "Project C"))
        projectSpinner.adapter = projectAdapter

        val pageAdapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf("Page A", "Page B", "Page C"))
        pageSpinner.adapter = pageAdapter

        val spiderDb = Room.databaseBuilder(applicationContext, SpiderDatabase::class.java, SpiderDatabase.NAME).build()

        WebViewPresenter(this, spiderDb)
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribe()
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    override fun render(viewState: WebViewViewState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goIntent(): Observable<Any> = TODO()
}

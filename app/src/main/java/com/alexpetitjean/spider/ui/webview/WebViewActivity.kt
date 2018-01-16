package com.alexpetitjean.spider.ui.webview

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED
import android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.alexpetitjean.spider.R
import com.alexpetitjean.spider.data.Page
import com.alexpetitjean.spider.data.Project
import com.alexpetitjean.spider.database.SpiderDatabase
import com.jakewharton.rxbinding2.widget.itemSelections
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.content_bottom_sheet_toolbar.*
import kotlinx.android.synthetic.main.content_web_view_config.*

class WebViewActivity
    : AppCompatActivity(),
      WebViewContract.View {

    companion object {
        private const val TAG = "WebView"
    }

    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(settingsBottomSheet)
    }

    private var projects = emptyList<Project>()
    private var pages = emptyList<Page>()

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
        Log.d(TAG, "Rendering: $viewState")

        projects = viewState.projects

        val projectsAdapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                viewState.projects.map(Project::name))
        projectsSpinner.adapter = projectsAdapter
        if (viewState.selectedProject != null) {
            projectsSpinner.setSelection(viewState.projects.indexOf(viewState.selectedProject))
        }

        pages = viewState.pages
        val pagesAdapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                viewState.pages.map(Page::name))
        pagesSpinner.adapter = pagesAdapter
    }

    override fun selectProjectIntent(): Observable<Project> = projectsSpinner
            .itemSelections()
            .map { projects[it] }

    override fun goIntent(): Observable<Any> = TODO()
}

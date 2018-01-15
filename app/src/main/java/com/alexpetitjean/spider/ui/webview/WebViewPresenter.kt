package com.alexpetitjean.spider.ui.webview

import com.alexpetitjean.spider.database.SpiderDatabase

class WebViewPresenter(private val view: WebViewContract.View,
                       private val spiderDb: SpiderDatabase)
    : WebViewContract.Presenter {

    private val projectDao = spiderDb.projectDao()
    private val pageDao = spiderDb.pageDao()

    init {
        view.presenter = this
    }

    override fun subscribe() {
        projectDao.getAllProjects()
                .
    }

    override fun unsubscribe() {

    }
}

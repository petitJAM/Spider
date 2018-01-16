package com.alexpetitjean.spider.ui.webview

import com.alexpetitjean.spider.data.Project
import com.alexpetitjean.spider.ui.BaseContract
import io.reactivex.Observable

interface WebViewContract {

    interface View : BaseContract.BaseView<Presenter, WebViewViewState> {
        fun goIntent(): Observable<Any>

        fun selectProjectIntent(): Observable<Project>
    }

    interface Presenter : BaseContract.BasePresenter
}

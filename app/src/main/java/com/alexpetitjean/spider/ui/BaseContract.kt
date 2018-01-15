package com.alexpetitjean.spider.ui

interface BaseContract {

    interface BaseView<T, in ViewState> {
        var presenter: T
        fun render(viewState: ViewState)
    }

    interface BasePresenter {
        fun subscribe()
        fun unsubscribe()
    }
}

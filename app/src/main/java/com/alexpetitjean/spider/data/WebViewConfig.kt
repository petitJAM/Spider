package com.alexpetitjean.spider.data

data class WebViewConfig(val url: String, val https: Boolean, val headers: List<HttpHeader>) {

    val httpHeadersMap: Map<String, String>
        get() = headers.map { it.name to it.value }.toMap()
}

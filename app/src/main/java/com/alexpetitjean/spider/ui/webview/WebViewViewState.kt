package com.alexpetitjean.spider.ui.webview

import com.alexpetitjean.spider.data.Page
import com.alexpetitjean.spider.data.Project

data class WebViewViewState(val url: String = "",
                            val projects: List<Project> = emptyList(),
                            val selectedProject: Project? = null,
                            val projectsLoadError: Boolean = false,
                            val pages: List<Page> = emptyList())

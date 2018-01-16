package com.alexpetitjean.spider.ui.webview

import com.alexpetitjean.spider.data.Page
import com.alexpetitjean.spider.data.Project

sealed class PartialState {

    data class SelectedProject(val project: Project, val pages: List<Page>) : PartialState()

    data class LoadedProjects(val projects: List<Project>) : PartialState() {
        companion object {
            val EMPTY = LoadedProjects(emptyList())
        }
    }

    data class LoadedProjectsError(val error: Throwable) : PartialState()
}

package com.alexpetitjean.spider.ui.webview

import android.util.Log
import com.alexpetitjean.spider.data.Page
import com.alexpetitjean.spider.data.Project
import com.alexpetitjean.spider.database.SpiderDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class WebViewPresenter(private val view: WebViewContract.View,
                       spiderDb: SpiderDatabase)
    : WebViewContract.Presenter {

    companion object {
        private const val TAG = "WebViewPresenter"
    }

    private val disposables = CompositeDisposable()

    private val projectDao = spiderDb.projectDao()
    private val pageDao = spiderDb.pageDao()

    init {
        view.presenter = this
    }

    override fun subscribe() {
        Completable.fromRunnable {
            val habit = Project("Habit")
            projectDao.insert(habit)
            val today = Page("Today", 1)
            pageDao.insert(today)
        }.subscribeOn(Schedulers.io()).subscribe()

        val loadedProjects: Observable<PartialState> = projectDao
                .getAllProjects().toObservable()
                .map { PartialState.LoadedProjects(it) as PartialState } // this is dumb
                .startWith(PartialState.LoadedProjects.EMPTY)
                .onErrorReturn { PartialState.LoadedProjectsError(it) }
                .doOnNext { Log.d(TAG, "loadedProjects: $it") }

        val selectedProject = view.selectProjectIntent()
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap { project ->
                    pageDao.getPagesForProject(project.id).toObservable()
                            .map { pages ->
                                Pair(project, pages)
                            }
                }
                .map { (project, pages) ->
                    PartialState.SelectedProject(project, pages)
                }
                .doOnNext { Log.d(TAG, "selectedProject: $it") }

        val allIntents = Observable.merge(loadedProjects, selectedProject)

        val initialState = WebViewViewState("")

        allIntents.scan(initialState, this::viewStateReducer)
                .doOnSubscribe { disposables += it }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { webViewViewState ->
                            view.render(webViewViewState)
                        },
                        onError = {
                            it.printStackTrace()
                            throw it
                        })
    }

    override fun unsubscribe() {
        //        disposables.clear()
    }

    private fun viewStateReducer(previousState: WebViewViewState, changes: PartialState): WebViewViewState {
        return when (changes) {
            is PartialState.LoadedProjects -> {
                previousState.copy(projects = changes.projects)
            }
            is PartialState.LoadedProjectsError -> {
                previousState.copy(projects = emptyList(), projectsLoadError = true)
            }
            is PartialState.SelectedProject -> {
                previousState.copy(selectedProject = changes.project)
            }
        }
    }
}

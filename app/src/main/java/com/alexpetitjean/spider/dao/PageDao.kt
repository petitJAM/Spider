package com.alexpetitjean.spider.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.alexpetitjean.spider.data.Page
import io.reactivex.Flowable

@Dao
interface PageDao {

    @Query("SELECT * FROM pages")
    fun getAllPages(): Flowable<List<Page>>

    @Query("SELECT * FROM pages WHERE projectId = :projectId")
    fun getPagesForProject(projectId: Long): Flowable<List<Page>>

    @Insert
    fun insert(page: Page)
}

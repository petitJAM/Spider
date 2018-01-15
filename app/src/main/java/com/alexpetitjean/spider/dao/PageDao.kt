package com.alexpetitjean.spider.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.alexpetitjean.spider.data.Page
import io.reactivex.Observable

@Dao
interface PageDao {

    @Query("SELECT * FROM pages")
    fun getAllPages(): Observable<List<Page>>

    @Insert
    fun insertPage(page: Page)
}

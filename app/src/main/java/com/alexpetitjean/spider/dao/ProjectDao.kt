package com.alexpetitjean.spider.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.alexpetitjean.spider.data.Project
import io.reactivex.Flowable

@Dao
interface ProjectDao {

    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flowable<List<Project>>

    @Insert
    fun insert(project: Project)
}

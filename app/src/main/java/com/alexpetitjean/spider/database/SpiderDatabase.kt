package com.alexpetitjean.spider.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.alexpetitjean.spider.dao.PageDao
import com.alexpetitjean.spider.dao.ProjectDao
import com.alexpetitjean.spider.data.Page
import com.alexpetitjean.spider.data.Project

@Database(entities = [Project::class, Page::class], version = 1, exportSchema = false)
abstract class SpiderDatabase : RoomDatabase() {

    companion object {
        const val NAME = "spider_db"
    }

    abstract fun projectDao(): ProjectDao

    abstract fun pageDao(): PageDao
}

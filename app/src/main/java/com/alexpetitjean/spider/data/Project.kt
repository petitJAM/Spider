package com.alexpetitjean.spider.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        @ColumnInfo(name = "name")
        val name: String) {

    @Ignore
    constructor(name: String) : this(0, name)
}

package com.alexpetitjean.spider.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "pages",
        foreignKeys = [
            ForeignKey(entity = Project::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf("projectId"),
                    onDelete = ForeignKey.CASCADE)])
data class Page(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val name: String,
        val projectId: Long)

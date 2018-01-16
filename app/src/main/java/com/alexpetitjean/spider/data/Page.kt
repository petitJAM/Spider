package com.alexpetitjean.spider.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
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
        val projectId: Long) {

    @Ignore
    constructor(name: String, project: Project) : this(0, name, project.id)

    @Ignore
    constructor(name: String, projectId: Long) : this(0, name, projectId)
}

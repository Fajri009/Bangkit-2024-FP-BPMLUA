package com.dicoding.asclepius.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "uri")
    var uri: String,

    @ColumnInfo(name = "resultThreshold")
    var resultThreshold: String,

    @ColumnInfo(name = "resultCategory")
    var resultCategory: String
)
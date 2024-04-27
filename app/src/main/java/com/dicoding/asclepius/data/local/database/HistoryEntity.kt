package com.dicoding.asclepius.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "uri")
    var uri: String,

    @ColumnInfo(name = "result")
    var result: String
)
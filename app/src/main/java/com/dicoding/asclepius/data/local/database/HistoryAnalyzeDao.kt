package com.dicoding.asclepius.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryAnalyzeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addHistory(history: HistoryEntity)

    @Query("SELECT * FROM HistoryEntity ORDER BY date DESC")
    fun getAllHistory(): LiveData<List<HistoryEntity>>
}
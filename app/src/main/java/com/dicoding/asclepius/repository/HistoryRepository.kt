package com.dicoding.asclepius.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.database.AsclepiusDatabase
import com.dicoding.asclepius.data.local.database.HistoryEntity
import com.dicoding.asclepius.data.local.database.HistoryAnalyzeDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository(application: Application) {
    private val mHistoryAnalyzeDao: HistoryAnalyzeDao
    private val executoreService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = AsclepiusDatabase.getDatabase(application)
        mHistoryAnalyzeDao = db.historyAnalyzeDao()
    }

    fun addHistory(history: HistoryEntity) {
        executoreService.execute {
            mHistoryAnalyzeDao.addHistory(history)
        }
    }

    fun getAllHistory(): LiveData<List<HistoryEntity>> = mHistoryAnalyzeDao.getAllHistory()
}
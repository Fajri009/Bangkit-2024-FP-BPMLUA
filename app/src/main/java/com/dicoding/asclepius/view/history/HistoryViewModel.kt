package com.dicoding.asclepius.view.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.database.HistoryEntity
import com.dicoding.asclepius.repository.HistoryRepository

class HistoryViewModel(application: Application): ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    fun getAllHistory(): LiveData<List<HistoryEntity>> = mHistoryRepository.getAllHistory()
}
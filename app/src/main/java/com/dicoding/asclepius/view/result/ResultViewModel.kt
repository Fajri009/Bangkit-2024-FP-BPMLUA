package com.dicoding.asclepius.view.result

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.database.HistoryEntity
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig
import com.dicoding.asclepius.repository.HistoryRepository

class ResultViewModel(application: Application): ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    fun addHistory(history: HistoryEntity) {
        mHistoryRepository.addHistory(history)
    }

//    private fun getNews() {
//        val response = ApiConfig.getApiService().getAllNews()
//    }
}
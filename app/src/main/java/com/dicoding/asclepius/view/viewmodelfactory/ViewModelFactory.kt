package com.dicoding.asclepius.view.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.dicoding.asclepius.view.history.HistoryViewModel
import com.dicoding.asclepius.view.result.ResultViewModel

class ViewModelFactory(private val application: Application): NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(application) as T
        } else if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown viewModel class: " + modelClass.name)
    }
}
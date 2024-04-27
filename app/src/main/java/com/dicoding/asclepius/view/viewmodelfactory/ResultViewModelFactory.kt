package com.dicoding.asclepius.view.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.dicoding.asclepius.view.result.ResultViewModel
import javax.xml.transform.Result

class ResultViewModelFactory(private val application: Application): NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ResultViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ResultViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ResultViewModelFactory::class.java) {
                    INSTANCE = ResultViewModelFactory(application)
                }
            }
            return INSTANCE as ResultViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown viewModel class: " + modelClass.name)
    }
}
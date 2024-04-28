package com.dicoding.asclepius.view.history

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.local.database.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.view.result.ResultActivity
import com.dicoding.asclepius.view.viewmodelfactory.ViewModelFactory

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        back()

        val application = ViewModelFactory.getInstance(this@HistoryActivity.application)
        viewModel = ViewModelProvider(this@HistoryActivity, application).get(HistoryViewModel::class.java)

        viewModel.getAllHistory().observe(this) { listHistory ->
            if (listHistory != null) {
                setAdapter(listHistory)
            }
        }
    }

    private fun setAdapter(listHistory: List<HistoryEntity>) {
        val adapter = HistoryAdapter(listHistory)
        adapter.submitList(listHistory)
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            this.adapter = adapter
        }

        adapter.setOnItemClickCallBack(object: HistoryAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: HistoryEntity) {
                showDetailAnalyze(data)
            }
        })
    }

    private fun showDetailAnalyze(data: HistoryEntity) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, data.uri)
        intent.putExtra(ResultActivity.EXTRA_RESULT_THRESHOLD, data.resultThreshold)
        intent.putExtra(ResultActivity.EXTRA_RESULT_CATEGORY, data.resultCategory)
        startActivity(intent)
    }

    private fun back() {
        binding.ivBack.setOnClickListener { finish() }
    }
}
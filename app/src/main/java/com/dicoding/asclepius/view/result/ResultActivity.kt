package com.dicoding.asclepius.view.result

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.database.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.utils.convertMillisToDateString
import com.dicoding.asclepius.view.viewmodelfactory.ResultViewModelFactory

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultViewModel
    private lateinit var imageUri: Uri
    private var resultThreshold: String? = null
    private var resultCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val application = ResultViewModelFactory.getInstance(this@ResultActivity.application)
        viewModel = ViewModelProvider(this@ResultActivity, application).get(ResultViewModel::class.java)

        back()

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        getImageUri()
        getResult()
        setHistory()
    }

    private fun back() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun getImageUri() {
        imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }
    }

    private fun getResult() {
        resultThreshold = intent.getStringExtra(EXTRA_RESULT_THRESHOLD)
        resultCategory = intent.getStringExtra(EXTRA_RESULT_CATEGORY)

        binding.resultThreshold.text = resultThreshold
        binding.resultCategory.apply {
            setTextColor(
                if (resultCategory == "Cancer") ContextCompat.getColor(this@ResultActivity, R.color.red)
                else ContextCompat.getColor(this@ResultActivity, R.color.blue)
            )
            text = resultCategory
        }
    }

    private fun setHistory() {
        val date = convertMillisToDateString(System.currentTimeMillis())
        val data = HistoryEntity(0, date, imageUri.toString(), resultThreshold!!)
        viewModel.addHistory(data)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT_THRESHOLD = "extra_result_threshold"
        const val EXTRA_RESULT_CATEGORY = "extra_result_category"
        const val EXTRA_RESULT = "extra_result"
    }
}
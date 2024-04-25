package com.dicoding.asclepius.view

import android.Manifest
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage(it)
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
//            UCrop
            currentImageUri = uri
            showImage()
        } else {
            showToast(getString(R.string.no_media_selected))
        }
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage(uri: Uri) {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        binding.progressIndicator.visibility = View.VISIBLE

        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object: ImageClassifierHelper.ClassifierListener {
                override fun onResults(results: List<Classifications>?) {
                    binding.progressIndicator.visibility = View.INVISIBLE

                    results?.joinToString {
                        "${it.categories[0].label} : ${it.categories}%"
                    }
                    results?.let { it ->
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            val threshold = (it[0].categories[0].score * 100).toInt()
                            val displayResult = "${it[0].categories[0].label}: $threshold%"
                            moveToResult(uri, displayResult)
                        }
                    }
                }

                override fun error(error: String) {
                    showToast(error)
                }
            }
        )

        imageClassifierHelper.classifyStaticImage(uri)
    }

    private fun moveToResult(uri: Uri, result: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
        intent.putExtra(ResultActivity.EXTRA_RESULT, result)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
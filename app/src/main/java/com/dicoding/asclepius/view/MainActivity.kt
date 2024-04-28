package com.dicoding.asclepius.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.history.HistoryActivity
import com.dicoding.asclepius.view.result.ResultActivity
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.analyzeButton.visibility = View.INVISIBLE

        appBar()

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

    private fun appBar() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            UCrop.of(uri, Uri.fromFile(cacheDir.resolve("${System.currentTimeMillis()}.jpg")))
                .withAspectRatio(16F, 9F)
                .withMaxResultSize(2000, 2000)
                .start(this)
        } else {
            showToast(getString(R.string.no_media_selected))
        }
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            currentImageUri = resultUri
            showImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            showToast(cropError.toString())
        }
    }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
            binding.analyzeButton.visibility = View.VISIBLE
            binding.galleryButton.apply {
                text = "Replace Image"
                setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.color_secondary))
            }
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

                    results?.let { it ->
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            val threshold = (it[0].categories[0].score * 100).toInt()
                            val thresholdText = "$threshold %"
                            val category = it[0].categories[0].label
                            moveToResult(uri, thresholdText, category)
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

    private fun moveToResult(uri: Uri, resultThreshold: String, resultCategory: String) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_RESOURCE, "MAIN_ACTIVITY")
            putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
            putExtra(ResultActivity.EXTRA_RESULT_THRESHOLD, resultThreshold)
            putExtra(ResultActivity.EXTRA_RESULT_CATEGORY, resultCategory)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
package com.example.deerdiary.ui.addStoryScreen

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.deerdiary.ViewModelFactory
import com.example.deerdiary.data.repository.Resource
import com.example.deerdiary.databinding.ActivityAddStoryBinding
import com.example.deerdiary.ui.addStoryScreen.CameraActivity.Companion.CAMERAX_RESULT
import com.example.deerdiary.ui.homeScreen.HomeActivity
import com.example.deerdiary.utils.reduceFileImage
import com.example.deerdiary.utils.uriToFile
import kotlinx.coroutines.launch

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding

    private var currentImageUri: Uri? = null

    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }

    private val addStoryViewModel: AddStoryViewModel by viewModels { factory }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION,
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        setUpListeners()
        setupAppBar()
    }

    private fun setUpListeners() {
        binding.btnGaleri.setOnClickListener {
            startGallery()
        }
        binding.btnUpload.setOnClickListener {
            currentImageUri?.let {
                val imageFile = uriToFile(it, this).reduceFileImage()
                val description = binding.edAddDescription.text.toString()
                lifecycleScope.launch {
                    addStoryViewModel.uploadStory(imageFile, description)
                        .observe(this@AddStoryActivity) { resource ->
                            if (resource != null) {
                                when (resource) {
                                    is Resource.Loading -> {
                                        Log.d("AddStoryActivity", "Loading")
                                        binding.pbAddStory.visibility = android.view.View.VISIBLE
                                    }

                                    is Resource.Success -> {
                                        Log.d("AddStoryActivity", "Success")
                                        binding.pbAddStory.visibility = android.view.View.GONE
                                        Toast.makeText(
                                            this@AddStoryActivity,
                                            "Story uploaded",
                                            Toast.LENGTH_LONG,
                                        ).show()
                                        val result = Intent()
                                        setResult(RESULT_STORY_CODE, result)
                                        val intent =
                                            Intent(this@AddStoryActivity, HomeActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                    }

                                    is Resource.Error -> {
                                        Log.d("AddStoryActivity", resource.error)
                                        Toast.makeText(
                                            this@AddStoryActivity,
                                            resource.error,
                                            Toast.LENGTH_LONG,
                                        ).show()
                                    }
                                }
                            }
                        }
                }
            }
        }

        binding.btnKamera.setOnClickListener {
            startCameraX()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia(),
        ) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivAddStory.setImageURI(it)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (it.resultCode == CAMERAX_RESULT) {
                currentImageUri =
                    it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
                showImage()
            }
        }

    private fun setupAppBar() {
        binding.apply {
            toolbar.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
        const val RESULT_STORY_CODE = 100
    }
}

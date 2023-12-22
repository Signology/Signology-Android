    package com.adem.signology.view.ui.camera

    import android.annotation.SuppressLint
    import android.content.Intent
    import android.os.Build
    import android.os.Bundle
    import android.util.Log
    import android.view.OrientationEventListener
    import android.view.Surface
    import android.view.View
    import android.view.WindowInsets
    import android.view.WindowManager
    import android.widget.Toast
    import androidx.activity.viewModels
    import androidx.appcompat.app.AppCompatActivity
    import androidx.camera.core.CameraSelector
    import androidx.camera.core.ImageCapture
    import androidx.camera.core.ImageCaptureException
    import androidx.camera.core.Preview
    import androidx.camera.lifecycle.ProcessCameraProvider
    import androidx.core.content.ContextCompat
    import android.Manifest
    import com.adem.signology.R
    import com.adem.signology.data.Result
    import com.adem.signology.databinding.ActivityCameraBinding
    import com.adem.signology.util.createCustomTempFile
    import com.adem.signology.util.reduceFileImage
    import com.adem.signology.util.uriToFile
    import com.adem.signology.view.ViewModelFactory
    import okhttp3.MediaType.Companion.toMediaTypeOrNull
    import okhttp3.MultipartBody
    import okhttp3.RequestBody.Companion.asRequestBody
    import java.io.File
    import java.lang.Exception

    class CameraActivity : AppCompatActivity() {
        private lateinit var binding: ActivityCameraBinding
        private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        private var imageCapture: ImageCapture? = null
        private val viewModel by viewModels<CameraActivityViewModel> {
            ViewModelFactory.getInstance(this)
        }

        private var uId: Int = 0
        private var photoCounter = 0
        private var shouldDeleteHistory = true
        private var accType:String = ""
        private var point:Int = 0


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityCameraBinding.inflate(layoutInflater)
            setContentView(binding.root)

            viewModel.history("-").observe(this) { result ->
                println("GETDATA = $result")

                when (result) {
                    is Result.Success -> {
                        uId = result.data.newHistoryId!!
                    }
                    is Result.Error -> {
                        val errorMessage = result.error
                        showToast(errorMessage)
                    }

                    else -> {}
                }
            }


            viewModel.getSession().observe(this) { user ->
                point = user.point
                println("Point: $point")
                accType = user.accType
                println("Account type: $accType")
                if (accType.trim() == "Free" || accType.trim() == "Student")  {
                    binding.tvTotalTakePhoto.text = "$photoCounter/15"
                } else {
                    binding.tvTotalTakePhoto.text = "$photoCounter/30"
                }
            }

            binding.switchCamera.setOnClickListener {
                finish()
            }



            binding.captureImage.setOnClickListener { takePhoto() }
            binding.uploadButton.setOnClickListener {
                shouldDeleteHistory = false
                println("POINT NOW: $point")
                if(accType.trim() != "Free"){
                    predictImage()
                } else if(accType.trim() == "Free" && point >= 75) {
                    predictImage()
                } else {
                    showToast(getString(R.string.limit_point))
                }
            }
        }

        public override fun onResume() {
            super.onResume()
            hideSystemUI()
            startCamera()
        }

        private fun startCamera() {
            // showCamera
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    }

                imageCapture = ImageCapture.Builder().build()

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (exc: Exception) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Faild to show camera",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "startCamera: ${exc.message}")
                }
            }, ContextCompat.getMainExecutor(this))
        }

        private fun takePhoto() {
            val imageCapture = imageCapture ?: return
            val photoFile = createCustomTempFile(application)
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {
                    @SuppressLint("SetTextI18n")
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                        val myFile = output.savedUri?.let { uriToFile(it, this@CameraActivity) }
                        val file = reduceFileImage(myFile as File)
                        val imageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val imageMultipart = MultipartBody.Part.createFormData("image", file.name, imageFile)

                        photoCounter++

                        println("AKUN INI: $accType")

                        if((accType.trim() == "Free" || accType.trim() == "Student") && point > 75) {
                            if(photoCounter==15) {predictImage()}
                            binding.tvTotalTakePhoto.text = "$photoCounter/15"

                        } else if (accType.trim() == "Free" && point < 75) {
                            showToast(getString(R.string.limit_point))
                            binding.tvTotalTakePhoto.text = "$photoCounter/15"

                        }

                        if(accType.trim() != "Free" || accType.trim() != "Student") {
                            if(photoCounter==30) {predictImage()}
                            binding.tvTotalTakePhoto.text = "$photoCounter/30"

                        }


                        viewModel.historyAdd(uId, imageMultipart).observe(this@CameraActivity) { result ->

                            when (result) {
                                is Result.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                }

                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }

                                is Result.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    val errorMessage = result.error
                                    showToast(errorMessage)
                                }

                                else -> {}
                            }
                        }

                    }
                    override fun onError(exc: ImageCaptureException) {
                        Toast.makeText(
                            this@CameraActivity,
                            "Error Camera.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(TAG, "onError: ${exc.message}")
                    }
                }
            )
        }

        private fun predictImage() {
            val intent = Intent(this, PredictActivity::class.java)
            intent.putExtra(EXTRA_CAMERAX_IMAGE, uId)
            startActivity(intent)
            finish()
        }

        private fun hideSystemUI() {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            supportActionBar?.hide()
        }

        private val orientationEventListener by lazy {
            object : OrientationEventListener(this) {
                override fun onOrientationChanged(orientation: Int) {
                    if (orientation == ORIENTATION_UNKNOWN) {
                        return
                    }
                    val rotation = when (orientation) {
                        in 45 until 135 -> Surface.ROTATION_270
                        in 135 until 225 -> Surface.ROTATION_180
                        in 225 until 315 -> Surface.ROTATION_90
                        else -> Surface.ROTATION_0
                    }
                    imageCapture?.targetRotation = rotation
                }
            }
        }

        private fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        override fun onStart() {
            super.onStart()
            orientationEventListener.enable()
        }

        override fun onPause() {
            super.onPause()
            if (shouldDeleteHistory) {
                viewModel.deleteHistory(uId).observe(this) { result ->
                    when (result) {
                        is Result.Success -> {
                            println("History id deleted successfully")
                        }

                        is Result.Error -> {
                            val errorMessage = result.error
                            println(errorMessage)

                        }

                        else -> {}
                    }
                }
            }
        }

        override fun onStop() {
            super.onStop()
            orientationEventListener.disable()
        }

        companion object {
            private const val TAG = "CameraActivity"
            const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
            const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        }
    }
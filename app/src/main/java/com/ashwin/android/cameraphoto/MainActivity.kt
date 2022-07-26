package com.ashwin.android.cameraphoto

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File

private const val TAG = "camera-photo"

private const val FILE_NAME = "my_photo.png"

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView

    private lateinit var imageUri: Uri

    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture(), { success ->
        if (success) {
            imageView.setImageURI(null)
            imageView.setImageURI(imageUri)
        } else {
            Log.e(TAG, "Camera take photo failed")
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.image_view)

        val changeButton = findViewById<Button>(R.id.change_button)
        changeButton.setOnClickListener {
            takePicture()
        }

        val deleteButton = findViewById<Button>(R.id.delete_button)
        deleteButton.setOnClickListener {
            deletePicture()
        }
    }

    private fun createImageUri(): Uri? {
        val file = File(applicationContext.filesDir, FILE_NAME)
        if (file.exists()) {
            file.delete()
        }
        return FileProvider.getUriForFile(applicationContext, "com.ashwin.android.cameraphoto.fileProvider", file)
    }

    private fun takePicture() {
        imageUri = createImageUri()!!
        cameraResultLauncher.launch(imageUri)
    }

    private fun deletePicture() {
        val file = File(applicationContext.filesDir, FILE_NAME)
        var isDeleted = true
        if (file.exists()) {
            isDeleted = file.delete()
        }
        Log.d(TAG, "isDeleted = $isDeleted")
        if (isDeleted) {
            imageView.setImageURI(null)
        }
    }
}

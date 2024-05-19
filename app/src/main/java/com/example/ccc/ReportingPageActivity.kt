package com.example.ccc

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ReportingPageActivity : AppCompatActivity() {
    private lateinit var imageUri: Uri
    private lateinit var imageView: ImageView

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null && data.data != null) {
                imageUri = data.data!!
                imageView.setImageURI(imageUri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporting_page)

        imageView = findViewById(R.id.image_view)

        val uploadButton: Button = findViewById(R.id.upload_button)
        val dateInput: EditText = findViewById(R.id.date_input)
        val locationInput: EditText = findViewById(R.id.location_input)
        val contentInput: EditText = findViewById(R.id.content_input)
        val cancelButton: Button = findViewById(R.id.cancel_button)
        val submitButton: Button = findViewById(R.id.submit_button)

        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
        }

        submitButton.setOnClickListener {
            val date = dateInput.text.toString()
            val location = locationInput.text.toString()
            val content = contentInput.text.toString()
        }

        cancelButton.setOnClickListener {
            navigateToHomeScreen()
        }
    }
    private fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun clearInputFields() {
        val dateInput: EditText = findViewById(R.id.date_input)
        val locationInput: EditText = findViewById(R.id.location_input)
        val contentInput: EditText = findViewById(R.id.content_input)
        imageView.setImageDrawable(null)

        dateInput.text.clear()
        locationInput.text.clear()
        contentInput.text.clear()
    }
}
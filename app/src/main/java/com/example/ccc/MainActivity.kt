package com.example.ccc

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val reportButton: ImageButton = findViewById(R.id.report_btn)
        reportButton.setOnClickListener {
            val intent = Intent(this, ReportingPageActivity::class.java)
            startActivity(intent)
        }

        val ListButton: ImageButton = findViewById(R.id.reported_list_btn)
        ListButton.setOnClickListener {
            val intent = Intent(this, ReportedListActivity::class.java)
            startActivity(intent)
        }
    }
}
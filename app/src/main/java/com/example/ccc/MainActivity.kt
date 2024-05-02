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
            // 새로운 액티비티로 이동
            val intent = Intent(this, ReportingPage::class.java)
            startActivity(intent)
        }

        val ListButton: ImageButton = findViewById(R.id.reported_list_btn)
        ListButton.setOnClickListener {
            // 새로운 액티비티로 이동
            val intent = Intent(this, ListOfReported::class.java)
            startActivity(intent)
        }


    }
}
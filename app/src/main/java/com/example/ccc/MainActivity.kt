package com.example.ccc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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
        reportButton.setOnClickListener(View.OnClickListener {
            val url = "https://jincoo.github.io/#/reporting" // 원하는 웹 페이지 URL로 변경
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        })

        val ListButton: ImageButton = findViewById(R.id.reported_list_btn)
        ListButton.setOnClickListener(View.OnClickListener {
            val url = "https://jincoo.github.io/#/list" // 원하는 웹 페이지 URL로 변경
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        })


    }
}
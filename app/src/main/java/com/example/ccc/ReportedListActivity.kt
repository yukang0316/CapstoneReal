package com.example.ccc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ccc.adapter.ReportAdapter
import com.example.ccc.data.Report

class ReportedListActivity : AppCompatActivity() {
    private lateinit var reportList: RecyclerView
    private lateinit var reportAdapter: ReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reported_list)

        reportList = findViewById(R.id.report_list)
        reportList.layoutManager = LinearLayoutManager(this)

        val reports = mutableListOf<Report>()

        val date = intent.getStringExtra("date")
        val location = intent.getStringExtra("location")
        val content = intent.getStringExtra("content")

        if (!date.isNullOrEmpty() && !location.isNullOrEmpty() && !content.isNullOrEmpty()) {
            reports.add(Report(date, location, content))
        }

        reportAdapter = ReportAdapter(reports)
        reportList.adapter = reportAdapter
    }
}
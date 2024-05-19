package com.example.ccc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ccc.R
import com.example.ccc.data.Report

class ReportAdapter(private val reports: List<Report>) :
    RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.date_text_view)
        val locationTextView: TextView = itemView.findViewById(R.id.location_text_view)
        val contentTextView: TextView = itemView.findViewById(R.id.content_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = reports[position]
        holder.dateTextView.text = report.date
        holder.locationTextView.text = report.location
        holder.contentTextView.text = report.content
    }

    override fun getItemCount(): Int {
        return reports.size
    }
}
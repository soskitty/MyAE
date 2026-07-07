package com.example.lowiapworkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lowiapworkout.data.TrainingRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private val records: MutableList<TrainingRecord>,
    private val onDelete: (TrainingRecord) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvHistoryDate)
        val tvDetail: TextView = view.findViewById(R.id.tvHistoryDetail)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        holder.tvDate.text = sdf.format(Date(record.date))
        holder.tvDetail.text = "${record.exerciseName} · ${record.sets}组×${record.reps}次 · ${record.durationSeconds}秒"
        holder.btnDelete.setOnClickListener { onDelete(record) }
    }

    override fun getItemCount() = records.size
}

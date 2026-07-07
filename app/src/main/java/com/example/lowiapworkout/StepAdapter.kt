package com.example.lowiapworkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StepAdapter(private val steps: List<String>) :
    RecyclerView.Adapter<StepAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStepNum: TextView = view.findViewById(R.id.tvStepNum)
        val tvStepText: TextView = view.findViewById(R.id.tvStepText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_step, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvStepNum.text = "${position + 1}"
        holder.tvStepText.text = steps[position]
    }

    override fun getItemCount() = steps.size
}

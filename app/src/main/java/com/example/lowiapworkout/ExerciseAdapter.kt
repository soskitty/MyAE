package com.example.lowiapworkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lowiapworkout.data.Exercise

class ExerciseAdapter(
    private val exercises: List<Exercise>,
    private val onClick: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcon: TextView = view.findViewById(R.id.tvIcon)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvTarget: TextView = view.findViewById(R.id.tvTarget)
        val tvEquipment: TextView = view.findViewById(R.id.tvEquipment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.tvIcon.text = exercise.icon
        holder.tvName.text = exercise.name
        holder.tvTarget.text = exercise.target
        holder.tvEquipment.text = exercise.equipment
        holder.itemView.setOnClickListener { onClick(exercise) }
    }

    override fun getItemCount() = exercises.size
}

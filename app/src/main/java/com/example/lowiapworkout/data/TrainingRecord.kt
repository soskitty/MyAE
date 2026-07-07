package com.example.lowiapworkout.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training_records")
data class TrainingRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long,
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val durationSeconds: Long
)

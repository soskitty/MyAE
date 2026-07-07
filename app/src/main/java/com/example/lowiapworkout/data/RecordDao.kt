package com.example.lowiapworkout.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordDao {
    @Query("SELECT * FROM training_records ORDER BY date DESC")
    suspend fun getAllRecords(): List<TrainingRecord>

    @Insert
    suspend fun insertRecord(record: TrainingRecord)

    @Delete
    suspend fun deleteRecord(record: TrainingRecord)

    @Query("DELETE FROM training_records")
    suspend fun deleteAllRecords()
}

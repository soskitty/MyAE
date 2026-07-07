package com.example.lowiapworkout.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class TrainingRecord(
    val id: Long = System.currentTimeMillis(),
    val date: Long = System.currentTimeMillis(),
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val durationSeconds: Long
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("date", date)
        put("exerciseName", exerciseName)
        put("sets", sets)
        put("reps", reps)
        put("durationSeconds", durationSeconds)
    }

    companion object {
        fun fromJson(json: JSONObject): TrainingRecord = TrainingRecord(
            id = json.optLong("id", System.currentTimeMillis()),
            date = json.optLong("date", System.currentTimeMillis()),
            exerciseName = json.optString("exerciseName", ""),
            sets = json.optInt("sets", 0),
            reps = json.optInt("reps", 0),
            durationSeconds = json.optLong("durationSeconds", 0)
        )
    }
}

class RecordStorage(context: Context) {
    private val prefs = context.getSharedPreferences("training_records", Context.MODE_PRIVATE)

    fun getAllRecords(): List<TrainingRecord> {
        val json = prefs.getString("records", "[]") ?: "[]"
        val arr = JSONArray(json)
        val list = mutableListOf<TrainingRecord>()
        for (i in 0 until arr.length()) {
            list.add(TrainingRecord.fromJson(arr.getJSONObject(i)))
        }
        return list.sortedByDescending { it.date }
    }

    fun insertRecord(record: TrainingRecord) {
        val records = getAllRecords().toMutableList()
        records.add(0, record)
        saveRecords(records)
    }

    fun deleteRecord(record: TrainingRecord) {
        val records = getAllRecords().toMutableList()
        records.removeAll { it.id == record.id }
        saveRecords(records)
    }

    fun deleteAllRecords() {
        prefs.edit().remove("records").apply()
    }

    private fun saveRecords(records: List<TrainingRecord>) {
        val arr = JSONArray()
        records.forEach { arr.put(it.toJson()) }
        prefs.edit().putString("records", arr.toString()).apply()
    }
}

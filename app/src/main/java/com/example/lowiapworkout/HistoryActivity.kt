package com.example.lowiapworkout

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lowiapworkout.data.AppDatabase
import com.example.lowiapworkout.data.TrainingRecord
import com.example.lowiapworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val records = mutableListOf<TrainingRecord>()
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        adapter = HistoryAdapter(records) { record ->
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete))
                .setMessage("确定删除这条记录？")
                .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        AppDatabase.getInstance(this@HistoryActivity).recordDao().deleteRecord(record)
                        loadRecords()
                    }
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }

        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = adapter

        loadRecords()
    }

    private fun loadRecords() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AppDatabase.getInstance(this@HistoryActivity).recordDao().getAllRecords()
            withContext(Dispatchers.Main) {
                records.clear()
                records.addAll(result)
                adapter.notifyDataSetChanged()
                binding.tvEmpty.visibility = if (records.isEmpty()) TextView.VISIBLE else TextView.GONE
            }
        }
    }
}

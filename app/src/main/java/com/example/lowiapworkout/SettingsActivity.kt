package com.example.lowiapworkout

import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.lowiapworkout.data.AppDatabase
import com.example.lowiapworkout.databinding.ActivitySettingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        loadCurrentSettings()
        setupListeners()
    }

    private fun loadCurrentSettings() {
        val prefs = getSharedPreferences("myae_prefs", MODE_PRIVATE)
        val rest = prefs.getInt("default_rest", 60)
        val sets = prefs.getInt("default_sets", 3)
        val reps = prefs.getInt("default_reps", 12)

        when (rest) {
            30 -> binding.radioRest30.isChecked = true
            60 -> binding.radioRest60.isChecked = true
            90 -> binding.radioRest90.isChecked = true
        }
        when (sets) {
            2 -> binding.radioSets2.isChecked = true
            3 -> binding.radioSets3.isChecked = true
            4 -> binding.radioSets4.isChecked = true
        }
        when (reps) {
            8 -> binding.radioReps8.isChecked = true
            10 -> binding.radioReps10.isChecked = true
            12 -> binding.radioReps12.isChecked = true
            15 -> binding.radioReps15.isChecked = true
        }
    }

    private fun setupListeners() {
        val prefs = getSharedPreferences("myae_prefs", MODE_PRIVATE)

        binding.radioRest.setOnCheckedChangeListener { _, checkedId ->
            val value = when (checkedId) {
                R.id.radioRest30 -> 30
                R.id.radioRest90 -> 90
                else -> 60
            }
            prefs.edit().putInt("default_rest", value).apply()
        }

        binding.radioSets.setOnCheckedChangeListener { _, checkedId ->
            val value = when (checkedId) {
                R.id.radioSets2 -> 2
                R.id.radioSets4 -> 4
                else -> 3
            }
            prefs.edit().putInt("default_sets", value).apply()
        }

        binding.radioReps.setOnCheckedChangeListener { _, checkedId ->
            val value = when (checkedId) {
                R.id.radioReps8 -> 8
                R.id.radioReps10 -> 10
                R.id.radioReps15 -> 15
                else -> 12
            }
            prefs.edit().putInt("default_reps", value).apply()
        }

        binding.btnResetRecords.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_reset))
                .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        AppDatabase.getInstance(this@SettingsActivity).recordDao().deleteAllRecords()
                    }
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }
    }
}

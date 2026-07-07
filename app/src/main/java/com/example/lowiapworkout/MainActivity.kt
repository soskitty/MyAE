package com.example.lowiapworkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lowiapworkout.data.ExerciseData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.lowiapworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupBottomNav()

        showSafetyDialog()
    }

    private fun setupRecyclerView() {
        val adapter = ExerciseAdapter(ExerciseData.exercises) { exercise ->
            val intent = Intent(this, ExerciseDetailActivity::class.java)
            intent.putExtra("exercise_id", exercise.id)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
    }

    private fun setupBottomNav() {
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun showSafetyDialog() {
        val prefs = getSharedPreferences("myae_prefs", MODE_PRIVATE)
        if (!prefs.getBoolean("safety_shown", false)) {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.safety_title))
                .setMessage(getString(R.string.safety_content))
                .setPositiveButton(getString(R.string.got_it)) { _, _ ->
                    prefs.edit().putBoolean("safety_shown", true).apply()
                }
                .setCancelable(false)
                .show()
        }
    }
}

package com.example.lowiapworkout

import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lowiapworkout.data.AppDatabase
import com.example.lowiapworkout.data.Exercise
import com.example.lowiapworkout.data.ExerciseData
import com.example.lowiapworkout.data.TrainingRecord
import com.example.lowiapworkout.databinding.ActivityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var exercise: Exercise? = null
    private var sets = 3
    private var reps = 12
    private var restSeconds = 60
    private var currentSet = 1
    private var isResting = false
    private var timerRunning = false
    private var elapsedSeconds = 0L
    private var timer: CountDownTimer? = null
    private var elapsedTimer: kotlinx.coroutines.Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exerciseId = intent.getStringExtra("exercise_id")
        exercise = ExerciseData.getExerciseById(exerciseId ?: "")

        loadSettings()
        setupViews()
    }

    private fun loadSettings() {
        val prefs = getSharedPreferences("myae_prefs", MODE_PRIVATE)
        sets = prefs.getInt("default_sets", 3)
        reps = prefs.getInt("default_reps", 12)
        restSeconds = prefs.getInt("default_rest", 60)
    }

    private fun setupViews() {
        val ex = exercise ?: return

        binding.tvTitle.text = ex.name
        binding.tvTarget.text = "目标肌群：${ex.target} · 协同肌：${ex.muscleGroup}"
        binding.tvEquipment.text = "器材：${ex.equipment} · 难度：${ex.difficulty}"

        binding.rvSteps.layoutManager = LinearLayoutManager(this)
        binding.rvSteps.adapter = StepAdapter(ex.stepsZh)

        updateSetRepDisplay()

        binding.btnSetsMinus.setOnClickListener {
            if (sets > 1) { sets--; updateSetRepDisplay() }
        }
        binding.btnSetsPlus.setOnClickListener {
            if (sets < 10) { sets++; updateSetRepDisplay() }
        }
        binding.btnRepsMinus.setOnClickListener {
            if (reps > 1) { reps--; updateSetRepDisplay() }
        }
        binding.btnRepsPlus.setOnClickListener {
            if (reps < 50) { reps++; updateSetRepDisplay() }
        }

        binding.btnTimer.setOnClickListener {
            if (!timerRunning) {
                startWorkoutSet()
            } else {
                stopTimer()
            }
        }
        binding.btnSaveRecord.setOnClickListener {
            saveRecord()
        }
    }

    private fun updateSetRepDisplay() {
        binding.tvSets.text = "$sets"
        binding.tvReps.text = "$reps"
    }

    private fun startWorkoutSet() {
        timerRunning = true
        binding.btnTimer.text = getString(R.string.stop_timer)
        binding.btnSaveRecord.visibility = View.GONE
        binding.tvTimerStatus.text = "第 ${currentSet}/${sets} 组 · 进行中"
        elapsedSeconds = 0L

        elapsedTimer = CoroutineScope(Dispatchers.Main).launch {
            while (timerRunning) {
                updateTimerDisplay(elapsedSeconds)
                elapsedSeconds++
                    delay(1000)
            }
        }
    }

    private fun completeSet() {
        timerRunning = false
        elapsedTimer?.cancel()

        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        }

        if (currentSet < sets) {
            isResting = true
            binding.tvTimerStatus.text = "第 ${currentSet} 组完成！休息 ${restSeconds} 秒"
            binding.btnTimer.text = getString(R.string.stop_timer)
            binding.btnTimer.isEnabled = false

            var restCount = restSeconds
            timer = object : CountDownTimer((restSeconds * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    restCount--
                    binding.tvTimerDisplay.text = "休息 ${restCount}s"
                }

                override fun onFinish() {
                    isResting = false
                    currentSet++
                    binding.btnTimer.isEnabled = true
                    binding.btnTimer.text = getString(R.string.start_timer)
                    binding.tvTimerStatus.text = "休息结束，开始第 ${currentSet} 组"
                    binding.tvTimerDisplay.text = "00:00"
                }
            }.start()
        } else {
            binding.tvTimerStatus.text = getString(R.string.all_complete)
            binding.btnTimer.text = getString(R.string.completed)
            binding.btnTimer.isEnabled = false
            binding.btnSaveRecord.visibility = View.VISIBLE
            binding.tvTimerDisplay.text = formatTime(elapsedSeconds)
        }
    }

    private fun stopTimer() {
        timerRunning = false
        elapsedTimer?.cancel()
        timer?.cancel()

        completeSet()
    }

    private fun updateTimerDisplay(seconds: Long) {
        binding.tvTimerDisplay.text = formatTime(seconds)
    }

    private fun formatTime(seconds: Long): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }

    private fun saveRecord() {
        val ex = exercise ?: return
        val record = TrainingRecord(
            date = System.currentTimeMillis(),
            exerciseName = ex.name,
            sets = sets,
            reps = reps,
            durationSeconds = elapsedSeconds
        )

        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getInstance(this@ExerciseDetailActivity)
            db.recordDao().insertRecord(record)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@ExerciseDetailActivity, "记录已保存", Toast.LENGTH_SHORT).show()
                binding.btnSaveRecord.visibility = View.GONE
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        elapsedTimer?.cancel()
    }
}

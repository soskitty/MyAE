package com.example.lowiapworkout.data

data class Exercise(
    val id: String,
    val name: String,
    val nameEn: String,
    val category: String,
    val equipment: String,
    val target: String,
    val muscleGroup: String,
    val difficulty: String,
    val stepsZh: List<String>,
    val stepsEn: List<String>
) {
    val isBandExercise: Boolean get() = equipment.contains("弹力带")
    val icon: String get() = if (isBandExercise) "🏋️" else "💪"
}

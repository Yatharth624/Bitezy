package com.bitezy.app.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bitezy.app.data.health.HealthConnectManager
import com.bitezy.app.data.local.AppDatabase
import com.bitezy.app.data.local.FoodLogEntity
import com.bitezy.app.data.local.UserGoalEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardState(
    val goal: UserGoalEntity? = null,
    val logs: List<FoodLogEntity> = emptyList(),
    val totalCalories: Int = 0,
    val totalProtein: Int = 0,
    val totalCarbs: Int = 0,
    val totalFat: Int = 0,
    val avgFoodScore: Float = 0f,
    val steps: Long = 0L
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    val healthConnectManager = HealthConnectManager(application)

    private val _steps = MutableStateFlow(0L)

    val uiState: StateFlow<DashboardState> = combine(
        db.appDao().getUserGoal(),
        db.appDao().getAllFoodLogs(),
        _steps
    ) { goal, logs, steps ->
        
        // Sum today's macros (assuming all logs are today for now in this MVP)
        val todayLogs = logs 
        
        val totalCal = todayLogs.sumOf { it.calories }
        val totalPro = todayLogs.sumOf { it.protein }
        val totalCarbs = todayLogs.sumOf { it.carbs }
        val totalFat = todayLogs.sumOf { it.fat }
        
        val validScores = todayLogs.map { it.foodScore }.filter { it > 0f }
        val avgScore = if (validScores.isNotEmpty()) validScores.average().toFloat() else 0f

        // Provide default goal if none set yet
        val safeGoal = goal ?: UserGoalEntity(
            name = "User", 
            goal = "Custom", 
            targetCalories = 2000, 
            targetProtein = 120, 
            targetWater = 3, 
            targetSteps = 8000
        )

        DashboardState(
            goal = safeGoal,
            logs = todayLogs,
            totalCalories = totalCal,
            totalProtein = totalPro,
            totalCarbs = totalCarbs,
            totalFat = totalFat,
            avgFoodScore = avgScore,
            steps = steps
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardState())

    init {
        refreshSteps()
    }

    fun refreshSteps() {
        viewModelScope.launch {
            try {
                if (healthConnectManager.isAvailable() && healthConnectManager.hasAllPermissions()) {
                    _steps.value = healthConnectManager.readTodaySteps()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

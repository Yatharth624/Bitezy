package com.bitezy.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_goals")
data class UserGoalEntity(
    @PrimaryKey val id: Int = 1, // Singleton row for the current user
    val name: String,
    val goal: String,
    val targetCalories: Int,
    val targetProtein: Int,
    val targetWater: Int,
    val targetSteps: Int
)

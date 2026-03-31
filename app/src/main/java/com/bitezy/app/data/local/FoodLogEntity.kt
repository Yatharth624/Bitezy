package com.bitezy.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_logs")
data class FoodLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mealName: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val foodScore: Float, // Score out of 10.0
    val tip: String,
    val timestamp: Long = System.currentTimeMillis()
)

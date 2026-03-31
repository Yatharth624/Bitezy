package com.bitezy.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserGoal(goal: UserGoalEntity)

    @Query("SELECT * FROM user_goals WHERE id = 1 LIMIT 1")
    fun getUserGoal(): Flow<UserGoalEntity?>

    @Insert
    suspend fun insertFoodLog(log: FoodLogEntity)

    @Query("SELECT * FROM food_logs ORDER BY timestamp DESC")
    fun getAllFoodLogs(): Flow<List<FoodLogEntity>>
}

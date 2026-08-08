package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PredictionHistoryDao {
  @Query("SELECT * FROM prediction_history WHERE userId = :userId AND bettingSite = :site ORDER BY timestamp DESC LIMIT 20")
  fun getHistoryForUser(userId: String, site: String): Flow<List<PredictionHistoryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPrediction(prediction: PredictionHistoryEntity)

  @Query("DELETE FROM prediction_history WHERE userId = :userId AND bettingSite = :site")
  suspend fun clearHistoryForUser(userId: String, site: String)
}

package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prediction_history")
data class PredictionHistoryEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val bettingSite: String,
  val userId: String,
  val predictedMultiplier: Double,
  val accuracy: Int = 98,
  val timestamp: Long = System.currentTimeMillis(),
  val roundCode: String = "#${(1000..9999).random()}",
  val safeCashout: Double = (predictedMultiplier * 0.75),
  val isVerifiedWin: Boolean = true
)

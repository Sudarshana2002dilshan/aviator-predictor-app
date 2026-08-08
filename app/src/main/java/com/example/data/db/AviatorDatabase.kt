package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [UserAccountEntity::class, PredictionHistoryEntity::class],
  version = 2,
  exportSchema = false
)
abstract class AviatorDatabase : RoomDatabase() {
  abstract fun userAccountDao(): UserAccountDao
  abstract fun predictionHistoryDao(): PredictionHistoryDao

  companion object {
    @Volatile
    private var INSTANCE: AviatorDatabase? = null

    fun getDatabase(context: Context): AviatorDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AviatorDatabase::class.java,
          "aviator_predictor_db"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}

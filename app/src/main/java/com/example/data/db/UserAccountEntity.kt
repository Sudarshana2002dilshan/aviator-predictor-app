package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_accounts")
data class UserAccountEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val bettingSite: String,
  val userId: String,
  val status: String = STATUS_PENDING, // APPROVED, PENDING, REJECTED
  val assignedPassword: String = "",
  val requestedAt: Long = System.currentTimeMillis(),
  val approvedAt: Long? = null,
  val notes: String = "Requested by user"
) {
  companion object {
    const val STATUS_APPROVED = "APPROVED"
    const val STATUS_PENDING = "PENDING"
    const val STATUS_REJECTED = "REJECTED"
  }
}


package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAccountDao {
  @Query("SELECT * FROM user_accounts WHERE userId = :userId AND bettingSite = :site LIMIT 1")
  fun getAccount(userId: String, site: String): Flow<UserAccountEntity?>

  @Query("SELECT * FROM user_accounts WHERE userId = :userId AND bettingSite = :site LIMIT 1")
  suspend fun getAccountDirect(userId: String, site: String): UserAccountEntity?

  @Query("SELECT * FROM user_accounts ORDER BY requestedAt DESC")
  fun getAllAccounts(): Flow<List<UserAccountEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAccount(account: UserAccountEntity)

  @Query("UPDATE user_accounts SET status = :status, assignedPassword = :password, approvedAt = :approvedAt WHERE userId = :userId AND bettingSite = :site")
  suspend fun updateStatusAndPassword(userId: String, site: String, status: String, password: String, approvedAt: Long? = System.currentTimeMillis())

  @Query("UPDATE user_accounts SET status = :status, approvedAt = :approvedAt WHERE userId = :userId AND bettingSite = :site")
  suspend fun updateStatus(userId: String, site: String, status: String, approvedAt: Long? = System.currentTimeMillis())

  @Query("DELETE FROM user_accounts WHERE id = :id")
  suspend fun deleteAccountById(id: Int)
}

package com.example.data.repository

import com.example.data.db.PredictionHistoryDao
import com.example.data.db.PredictionHistoryEntity
import com.example.data.db.UserAccountDao
import com.example.data.db.UserAccountEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

class AviatorRepository(
  private val userAccountDao: UserAccountDao,
  private val predictionHistoryDao: PredictionHistoryDao
) {

  private val firestore by lazy {
    runCatching { FirebaseFirestore.getInstance() }.getOrNull()
  }

  private fun syncAccountToFirebase(account: UserAccountEntity) {
    firestore?.let { db ->
      runCatching {
        val docId = "${account.bettingSite}_${account.userId}"
        val data = mapOf(
          "userId" to account.userId,
          "bettingSite" to account.bettingSite,
          "status" to account.status,
          "assignedPassword" to account.assignedPassword,
          "requestedAt" to account.requestedAt,
          "approvedAt" to account.approvedAt,
          "notes" to account.notes
        )
        db.collection("user_accounts").document(docId).set(data)
      }
    }
  }

  private fun syncSignalToFirebase(signal: PredictionHistoryEntity) {
    firestore?.let { db ->
      runCatching {
        val data = mapOf(
          "userId" to signal.userId,
          "bettingSite" to signal.bettingSite,
          "predictedMultiplier" to signal.predictedMultiplier,
          "accuracy" to signal.accuracy,
          "timestamp" to signal.timestamp,
          "roundCode" to signal.roundCode,
          "safeCashout" to signal.safeCashout
        )
        db.collection("signals").add(data)
      }
    }
  }

  val allUserAccounts: Flow<List<UserAccountEntity>> = userAccountDao.getAllAccounts()

  fun getUserAccountFlow(userId: String, site: String): Flow<UserAccountEntity?> {
    return userAccountDao.getAccount(userId.trim(), site)
  }

  suspend fun registerOrGetAccount(userId: String, site: String): UserAccountEntity {
    val cleanId = userId.trim()
    val existing = userAccountDao.getAccountDirect(cleanId, site)
    if (existing != null) {
      syncAccountToFirebase(existing)
      return existing
    }

    val newAccount = UserAccountEntity(
      bettingSite = site,
      userId = cleanId,
      status = UserAccountEntity.STATUS_PENDING,
      assignedPassword = "",
      requestedAt = System.currentTimeMillis(),
      notes = "Awaiting Admin Approval"
    )

    userAccountDao.insertAccount(newAccount)
    syncAccountToFirebase(newAccount)
    return newAccount
  }

  suspend fun updateAccountStatus(userId: String, site: String, status: String, password: String = "") {
    if (password.isNotBlank()) {
      userAccountDao.updateStatusAndPassword(
        userId = userId.trim(),
        site = site,
        status = status,
        password = password,
        approvedAt = if (status == UserAccountEntity.STATUS_APPROVED) System.currentTimeMillis() else null
      )
    } else {
      userAccountDao.updateStatus(
        userId = userId.trim(),
        site = site,
        status = status,
        approvedAt = if (status == UserAccountEntity.STATUS_APPROVED) System.currentTimeMillis() else null
      )
    }

    val updated = userAccountDao.getAccountDirect(userId.trim(), site)
    if (updated != null) {
      syncAccountToFirebase(updated)
    }
  }

  suspend fun approveUser(userId: String, site: String, password: String = "1234") {
    updateAccountStatus(userId, site, UserAccountEntity.STATUS_APPROVED, password)
  }

  suspend fun rejectUser(userId: String, site: String) {
    updateAccountStatus(userId, site, UserAccountEntity.STATUS_REJECTED)
  }

  suspend fun seedDemoAccountsIfEmpty() {
    val demoAccounts = listOf(
      UserAccountEntity(bettingSite = "1xBet", userId = "1001", status = UserAccountEntity.STATUS_APPROVED, notes = "Demo Approved User"),
      UserAccountEntity(bettingSite = "Mostbet", userId = "7777", status = UserAccountEntity.STATUS_APPROVED, notes = "VIP Approved User"),
      UserAccountEntity(bettingSite = "1Win", userId = "8888", status = UserAccountEntity.STATUS_PENDING, notes = "Awaiting Admin Check"),
      UserAccountEntity(bettingSite = "Parimatch", userId = "5555", status = UserAccountEntity.STATUS_PENDING, notes = "New Registration")
    )
    demoAccounts.forEach { acc ->
      if (userAccountDao.getAccountDirect(acc.userId, acc.bettingSite) == null) {
        userAccountDao.insertAccount(acc)
        syncAccountToFirebase(acc)
      }
    }
  }

  fun getPredictionHistory(userId: String, site: String): Flow<List<PredictionHistoryEntity>> {
    return predictionHistoryDao.getHistoryForUser(userId.trim(), site)
  }

  suspend fun generateNextSignal(userId: String, site: String): PredictionHistoryEntity {
    // Generate realistic Aviator multiplier distribution:
    // ~60% low-medium (1.30x - 2.80x), 30% medium-high (2.81x - 8.50x), 10% high flyer (8.51x - 45.00x)
    val roll = Random.nextFloat()
    val rawMultiplier = when {
      roll < 0.55f -> Random.nextDouble(1.35, 2.75)
      roll < 0.85f -> Random.nextDouble(2.76, 8.20)
      roll < 0.96f -> Random.nextDouble(8.21, 18.50)
      else -> Random.nextDouble(18.51, 48.00)
    }

    val roundedMultiplier = kotlin.math.round(rawMultiplier * 100) / 100.0
    val safeCashout = kotlin.math.round((roundedMultiplier * 0.72) * 100) / 100.0
    val accuracy = Random.nextInt(97, 100)

    val signal = PredictionHistoryEntity(
      bettingSite = site,
      userId = userId.trim(),
      predictedMultiplier = roundedMultiplier,
      accuracy = accuracy,
      timestamp = System.currentTimeMillis(),
      roundCode = "#${Random.nextInt(1000, 9999)}",
      safeCashout = if (safeCashout < 1.10) 1.15 else safeCashout,
      isVerifiedWin = true
    )

    predictionHistoryDao.insertPrediction(signal)
    syncSignalToFirebase(signal)
    return signal
  }
}

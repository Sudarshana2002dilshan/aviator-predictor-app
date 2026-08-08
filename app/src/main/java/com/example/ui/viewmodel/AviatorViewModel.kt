package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AviatorDatabase
import com.example.data.db.PredictionHistoryEntity
import com.example.data.db.UserAccountEntity
import com.example.data.model.BettingSite
import com.example.data.repository.AviatorRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface Screen {
  data object Login : Screen
  data class PendingApproval(val userId: String, val site: String) : Screen
  data class Dashboard(val userId: String, val site: String) : Screen
}

data class LoginUiState(
  val selectedSite: BettingSite = BettingSite.SUPPORTED_SITES.first(),
  val userIdInput: String = "",
  val isConnecting: Boolean = false,
  val errorMessage: String? = null
)

data class SignalUiState(
  val isCalculatingSignal: Boolean = false,
  val currentSignal: PredictionHistoryEntity? = null,
  val calculationProgress: Float = 0f,
  val autoPredictEnabled: Boolean = false
)

class AviatorViewModel(application: Application) : AndroidViewModel(application) {

  private val database = AviatorDatabase.getDatabase(application)
  private val repository = AviatorRepository(database.userAccountDao(), database.predictionHistoryDao())

  val allUserAccounts: StateFlow<List<UserAccountEntity>> = repository.allUserAccounts
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  private val _currentScreen = MutableStateFlow<Screen>(Screen.Login)
  val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

  private val _loginState = MutableStateFlow(LoginUiState())
  val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

  private val _signalState = MutableStateFlow(SignalUiState())
  val signalState: StateFlow<SignalUiState> = _signalState.asStateFlow()

  private val _currentAccount = MutableStateFlow<UserAccountEntity?>(null)
  val currentAccount: StateFlow<UserAccountEntity?> = _currentAccount.asStateFlow()

  private val _predictionHistory = MutableStateFlow<List<PredictionHistoryEntity>>(emptyList())
  val predictionHistory: StateFlow<List<PredictionHistoryEntity>> = _predictionHistory.asStateFlow()

  private val _isAdminDialogOpen = MutableStateFlow(false)
  val isAdminDialogOpen: StateFlow<Boolean> = _isAdminDialogOpen.asStateFlow()

  private var historyJob: Job? = null
  private var accountJob: Job? = null

  init {
    viewModelScope.launch {
      repository.seedDemoAccountsIfEmpty()
    }
  }

  fun selectBettingSite(site: BettingSite) {
    _loginState.update { it.copy(selectedSite = site, errorMessage = null) }
  }

  fun updateUserIdInput(input: String) {
    _loginState.update { it.copy(userIdInput = input, errorMessage = null) }
  }

  private val _verificationAccount = MutableStateFlow<UserAccountEntity?>(null)
  val verificationAccount: StateFlow<UserAccountEntity?> = _verificationAccount.asStateFlow()

  private val _verificationDialogError = MutableStateFlow<String?>(null)
  val verificationDialogError: StateFlow<String?> = _verificationDialogError.asStateFlow()

  private val _onlineUsersCount = MutableStateFlow(84)
  val onlineUsersCount: StateFlow<Int> = _onlineUsersCount.asStateFlow()

  private var autoPredictJob: Job? = null

  init {
    viewModelScope.launch {
      var current = 84
      while (true) {
        delay((2000..4000).random().toLong())
        val delta = (-4..5).random()
        current = (current + delta).coerceIn(52, 123)
        _onlineUsersCount.value = current
      }
    }
  }

  fun attemptLogin() {
    val inputId = _loginState.value.userIdInput.trim()
    val site = _loginState.value.selectedSite.name

    if (inputId.isEmpty()) {
      _loginState.update { it.copy(errorMessage = "Please enter your Betting Site User ID") }
      return
    }

    viewModelScope.launch {
      _loginState.update { it.copy(isConnecting = true, errorMessage = null) }
      delay(600) // Fast handshake

      val account = repository.registerOrGetAccount(inputId, site)
      _loginState.update { it.copy(isConnecting = false) }

      // If approved and has assigned password
      if (account.status == UserAccountEntity.STATUS_APPROVED && account.assignedPassword.isNotBlank()) {
        // Still prompt for verification dialog so they enter password, OR direct login if pre-approved
        _verificationAccount.value = account
        _verificationDialogError.value = null
      } else {
        // Show NOT APPROVED verification popup
        _verificationAccount.value = account
        _verificationDialogError.value = null
      }
    }
  }

  fun verifyPasswordAndLogin(passwordInput: String) {
    val cleanPass = passwordInput.trim()
    val targetAcc = _verificationAccount.value ?: return

    // Secret Admin Access via Password "2002Avi"
    if (cleanPass == "2002Avi") {
      _verificationAccount.value = null
      _verificationDialogError.value = null
      openAdminDialog()
      return
    }

    viewModelScope.launch {
      // Re-fetch direct account status from DB to get latest admin changes
      val currentAcc = repository.getUserAccountFlow(targetAcc.userId, targetAcc.bettingSite)
      val latestAcc = repository.registerOrGetAccount(targetAcc.userId, targetAcc.bettingSite)

      val isApproved = latestAcc.status == UserAccountEntity.STATUS_APPROVED
      val matchesAssignedPassword = latestAcc.assignedPassword.isNotBlank() && cleanPass == latestAcc.assignedPassword

      if (isApproved || matchesAssignedPassword) {
        // If password matches, ensure status is marked approved
        if (!isApproved) {
          repository.approveUser(latestAcc.userId, latestAcc.bettingSite, cleanPass)
        }
        _verificationAccount.value = null
        _verificationDialogError.value = null
        observeAccountAndRoute(latestAcc.userId, latestAcc.bettingSite)
      } else {
        _verificationDialogError.value = "Invalid Admin Password! Please contact Admin on WhatsApp +94765529447 to get approved."
      }
    }
  }

  fun closeVerificationDialog() {
    _verificationAccount.value = null
    _verificationDialogError.value = null
  }

  fun observeAccountAndRoute(userId: String, site: String) {
    accountJob?.cancel()
    accountJob = viewModelScope.launch {
      repository.getUserAccountFlow(userId, site).collect { account ->
        _currentAccount.value = account
        if (account == null) {
          _currentScreen.value = Screen.Login
        } else if (account.status == UserAccountEntity.STATUS_APPROVED) {
          _currentScreen.value = Screen.Dashboard(userId, site)
          loadPredictionHistory(userId, site)
        } else {
          _currentScreen.value = Screen.PendingApproval(userId, site)
        }
      }
    }
  }

  private fun loadPredictionHistory(userId: String, site: String) {
    historyJob?.cancel()
    historyJob = viewModelScope.launch {
      repository.getPredictionHistory(userId, site).collect { history ->
        _predictionHistory.value = history
        if (history.isNotEmpty() && _signalState.value.currentSignal == null) {
          _signalState.update { it.copy(currentSignal = history.first()) }
        }
      }
    }
  }

  fun generateSignal() {
    val screen = _currentScreen.value
    if (screen !is Screen.Dashboard) return
    if (_signalState.value.isCalculatingSignal) return

    viewModelScope.launch {
      _signalState.update { it.copy(isCalculatingSignal = true, calculationProgress = 0f) }

      // Radar scan animation progress steps
      for (i in 1..10) {
        delay(120)
        _signalState.update { it.copy(calculationProgress = i / 10f) }
      }

      val newSignal = repository.generateNextSignal(screen.userId, screen.site)
      _signalState.update {
        it.copy(
          isCalculatingSignal = false,
          currentSignal = newSignal,
          calculationProgress = 1f
        )
      }
    }
  }

  fun toggleAutoPredict() {
    val newAuto = !_signalState.value.autoPredictEnabled
    _signalState.update { it.copy(autoPredictEnabled = newAuto) }
    autoPredictJob?.cancel()

    if (newAuto) {
      autoPredictJob = viewModelScope.launch {
        while (_signalState.value.autoPredictEnabled) {
          generateSignal()
          // Wait for signal calculation to complete
          while (_signalState.value.isCalculatingSignal) {
            delay(100)
          }
          // Display current prediction signal for 4 seconds before next continuous auto prediction
          delay(4000)
        }
      }
    }
  }

  fun logout() {
    accountJob?.cancel()
    historyJob?.cancel()
    _currentAccount.value = null
    _predictionHistory.value = emptyList()
    _signalState.value = SignalUiState()
    _loginState.update { it.copy(isConnecting = false, errorMessage = null) }
    _currentScreen.value = Screen.Login
  }

  // Admin Actions
  fun openAdminDialog() {
    _isAdminDialogOpen.value = true
  }

  fun closeAdminDialog() {
    _isAdminDialogOpen.value = false
  }

  fun adminApproveUser(userId: String, site: String, password: String = "1234") {
    viewModelScope.launch {
      repository.approveUser(userId, site, password)
    }
  }

  fun adminRejectUser(userId: String, site: String) {
    viewModelScope.launch {
      repository.rejectUser(userId, site)
    }
  }

  fun adminApproveAllPending() {
    viewModelScope.launch {
      allUserAccounts.value.filter { it.status == UserAccountEntity.STATUS_PENDING }.forEach { acc ->
        repository.approveUser(acc.userId, acc.bettingSite)
      }
    }
  }
}

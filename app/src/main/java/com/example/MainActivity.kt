package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AdminPanelDialog
import com.example.ui.components.UserVerificationDialog
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.PendingApprovalScreen
import com.example.ui.screens.PredictorDashboardScreen
import com.example.ui.theme.AviatorTheme
import com.example.ui.viewmodel.AviatorViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {

  private val viewModel: AviatorViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AviatorTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          AviatorApp(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }
}

@Composable
fun AviatorApp(
  viewModel: AviatorViewModel,
  modifier: Modifier = Modifier
) {
  val screen by viewModel.currentScreen.collectAsStateWithLifecycle()
  val loginState by viewModel.loginState.collectAsStateWithLifecycle()
  val signalState by viewModel.signalState.collectAsStateWithLifecycle()
  val predictionHistory by viewModel.predictionHistory.collectAsStateWithLifecycle()
  val allUserAccounts by viewModel.allUserAccounts.collectAsStateWithLifecycle()
  val isAdminDialogOpen by viewModel.isAdminDialogOpen.collectAsStateWithLifecycle()
  val verificationAccount by viewModel.verificationAccount.collectAsStateWithLifecycle()
  val verificationDialogError by viewModel.verificationDialogError.collectAsStateWithLifecycle()
  val onlineUsers by viewModel.onlineUsersCount.collectAsStateWithLifecycle()

  // User Verification & NOT APPROVED WhatsApp Popup Dialog
  verificationAccount?.let { acc ->
    UserVerificationDialog(
      userId = acc.userId,
      site = acc.bettingSite,
      dialogErrorMessage = verificationDialogError,
      onVerifyAndLogin = { password -> viewModel.verifyPasswordAndLogin(password) },
      onDismiss = { viewModel.closeVerificationDialog() }
    )
  }

  if (isAdminDialogOpen) {
    AdminPanelDialog(
      accounts = allUserAccounts,
      onApprove = { userId, site, pass -> viewModel.adminApproveUser(userId, site, pass) },
      onReject = { userId, site -> viewModel.adminRejectUser(userId, site) },
      onApproveAll = { viewModel.adminApproveAllPending() },
      onDismiss = { viewModel.closeAdminDialog() }
    )
  }

  when (val current = screen) {
    is Screen.Login -> {
      LoginScreen(
        selectedSite = loginState.selectedSite,
        userIdInput = loginState.userIdInput,
        isConnecting = loginState.isConnecting,
        errorMessage = loginState.errorMessage,
        onlineUsers = onlineUsers,
        onSiteSelect = { site -> viewModel.selectBettingSite(site) },
        onUserIdChange = { input -> viewModel.updateUserIdInput(input) },
        onLoginClick = { viewModel.attemptLogin() }
      )
    }

    is Screen.PendingApproval -> {
      PendingApprovalScreen(
        userId = current.userId,
        site = current.site,
        onlineUsers = onlineUsers,
        onRefreshStatus = { viewModel.observeAccountAndRoute(current.userId, current.site) }
      )
    }

    is Screen.Dashboard -> {
      PredictorDashboardScreen(
        userId = current.userId,
        site = current.site,
        isCalculating = signalState.isCalculatingSignal,
        calculationProgress = signalState.calculationProgress,
        currentSignal = signalState.currentSignal,
        predictionHistory = predictionHistory,
        autoPredictEnabled = signalState.autoPredictEnabled,
        onlineUsers = onlineUsers,
        onGetSignalClick = { viewModel.generateSignal() },
        onToggleAutoPredict = { viewModel.toggleAutoPredict() }
      )
    }
  }
}


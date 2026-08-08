package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.db.UserAccountEntity
import com.example.ui.theme.AviatorCardBg
import com.example.ui.theme.AviatorCardBorder
import com.example.ui.theme.AviatorDarkBg
import com.example.ui.theme.AviatorGold
import com.example.ui.theme.AviatorGreen
import com.example.ui.theme.AviatorOrange
import com.example.ui.theme.AviatorRedPrimary
import com.example.ui.theme.AviatorSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AdminPanelDialog(
  accounts: List<UserAccountEntity>,
  onApprove: (userId: String, site: String, password: String) -> Unit,
  onReject: (userId: String, site: String) -> Unit,
  onApproveAll: () -> Unit,
  onDismiss: () -> Unit
) {
  var newUserId by remember { mutableStateOf("") }
  var newSiteName by remember { mutableStateOf("1xBet") }
  var newPassword by remember { mutableStateOf("1234") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .testTag("admin_panel_dialog"),
      shape = RoundedCornerShape(20.dp),
      color = AviatorSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, AviatorCardBorder)
    ) {
      Column(
        modifier = Modifier
          .padding(18.dp)
          .fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = "Admin Shield",
              tint = AviatorGold,
              modifier = Modifier.padding(end = 8.dp)
            )
            Text(
              text = "ADMIN CONTROL PANEL",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                color = TextPrimary
              )
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextSecondary
            )
          }
        }

        Text(
          text = "Approve user ID requests and assign login passwords.",
          style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
          modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Button(
            onClick = onApproveAll,
            colors = ButtonDefaults.buttonColors(containerColor = AviatorGreen, contentColor = Color.Black),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = Icons.Default.DoneAll,
              contentDescription = null,
              modifier = Modifier.padding(end = 6.dp)
            )
            Text("Approve All Pending (Pass: 1234)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }

        // Quick Add Pre-Approved ID Form
        Card(
          colors = CardDefaults.cardColors(containerColor = AviatorCardBg),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text(
              "Add Pre-Approved User ID & Assign Password:",
              style = MaterialTheme.typography.labelMedium.copy(color = AviatorGold, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              OutlinedTextField(
                value = newUserId,
                onValueChange = { newUserId = it },
                placeholder = { Text("User ID", fontSize = 11.sp, color = TextMuted) },
                singleLine = true,
                modifier = Modifier
                  .weight(1f)
                  .height(48.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = AviatorGold,
                  unfocusedBorderColor = AviatorCardBorder,
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary
                )
              )
              Spacer(modifier = Modifier.width(6.dp))
              OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholder = { Text("Pass", fontSize = 11.sp, color = TextMuted) },
                singleLine = true,
                modifier = Modifier
                  .width(80.dp)
                  .height(48.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = AviatorGold,
                  unfocusedBorderColor = AviatorCardBorder,
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary
                )
              )
              Spacer(modifier = Modifier.width(6.dp))
              Button(
                onClick = {
                  if (newUserId.isNotBlank()) {
                    onApprove(newUserId.trim(), newSiteName, if (newPassword.isBlank()) "1234" else newPassword.trim())
                    newUserId = ""
                  }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AviatorGold, contentColor = Color.Black),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(48.dp)
              ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Add")
              }
            }
          }
        }

        Text(
          "User Requests & Approvals:",
          style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold),
          modifier = Modifier.padding(bottom = 6.dp)
        )

        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 280.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(accounts, key = { "${it.bettingSite}_${it.userId}" }) { account ->
            UserAccountAdminItem(
              account = account,
              onApproveWithPass = { pass -> onApprove(account.userId, account.bettingSite, pass) },
              onReject = { onReject(account.userId, account.bettingSite) }
            )
          }
        }
      }
    }
  }
}

@Composable
private fun UserAccountAdminItem(
  account: UserAccountEntity,
  onApproveWithPass: (password: String) -> Unit,
  onReject: () -> Unit
) {
  val isApproved = account.status == UserAccountEntity.STATUS_APPROVED
  val isPending = account.status == UserAccountEntity.STATUS_PENDING

  var inputPass by remember { mutableStateOf(account.assignedPassword.ifBlank { "1234" }) }

  val statusColor = when (account.status) {
    UserAccountEntity.STATUS_APPROVED -> AviatorGreen
    UserAccountEntity.STATUS_PENDING -> AviatorOrange
    else -> AviatorRedPrimary
  }

  Card(
    colors = CardDefaults.cardColors(containerColor = AviatorCardBg),
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, AviatorCardBorder),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = account.bettingSite,
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = AviatorGold
              )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(statusColor.copy(alpha = 0.2f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = account.status,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor
              )
            }
          }
          Text(
            text = "User ID: ${account.userId}",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          )
          if (account.assignedPassword.isNotBlank()) {
            Text(
              text = "Assigned Password: ${account.assignedPassword}",
              fontSize = 11.sp,
              color = AviatorGreen,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          if (isPending || isApproved) {
            IconButton(onClick = onReject) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Reject",
                tint = AviatorRedPrimary
              )
            }
          }
        }
      }

      if (!isApproved) {
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = inputPass,
            onValueChange = { inputPass = it },
            placeholder = { Text("Assign Password", fontSize = 11.sp, color = TextMuted) },
            singleLine = true,
            modifier = Modifier
              .weight(1f)
              .height(46.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AviatorGold,
              unfocusedBorderColor = AviatorCardBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            )
          )
          Spacer(modifier = Modifier.width(6.dp))
          Button(
            onClick = {
              onApproveWithPass(inputPass.ifBlank { "1234" })
            },
            colors = ButtonDefaults.buttonColors(containerColor = AviatorGreen, contentColor = Color.Black),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(46.dp)
          ) {
            Text("Approve & Set Password", fontWeight = FontWeight.Bold, fontSize = 11.sp)
          }
        }
      }
    }
  }
}

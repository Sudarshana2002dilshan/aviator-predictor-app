package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BettingSite
import com.example.ui.theme.AviatorCardBg
import com.example.ui.theme.AviatorCardBorder
import com.example.ui.theme.AviatorDarkBg
import com.example.ui.theme.AviatorGold
import com.example.ui.theme.AviatorGreen
import com.example.ui.theme.AviatorOrange
import com.example.ui.theme.AviatorRedLight
import com.example.ui.theme.AviatorRedPrimary
import com.example.ui.theme.AviatorSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginScreen(
  selectedSite: BettingSite,
  userIdInput: String,
  isConnecting: Boolean,
  errorMessage: String?,
  onlineUsers: Int = 84,
  onSiteSelect: (BettingSite) -> Unit,
  onUserIdChange: (String) -> Unit,
  onLoginClick: () -> Unit
) {
  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(AviatorDarkBg)
      .testTag("login_screen")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Header Status Indicator
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(AviatorGreen)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "SYSTEM ONLINE • ⚡ $onlineUsers USERS ACTIVE NOW",
          style = MaterialTheme.typography.labelSmall.copy(
            color = AviatorGreen,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        )
      }

      // Hero Banner / Logo Header
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AviatorCardBorder),
        colors = CardDefaults.cardColors(containerColor = AviatorSurface)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Aviator Logo Icon with glowing circle
          Box(
            modifier = Modifier
              .size(86.dp)
              .clip(CircleShape)
              .background(
                Brush.radialGradient(
                  colors = listOf(AviatorRedLight, AviatorRedPrimary, AviatorDarkBg)
                )
              )
              .border(2.dp, AviatorGold, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.FlightTakeoff,
              contentDescription = "Aviator Logo",
              tint = Color.White,
              modifier = Modifier.size(50.dp)
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "AVIATOR PREDICTOR",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Black,
              color = TextPrimary,
              letterSpacing = 1.5.sp
            )
          )

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "PRO SIGNAL TOOL",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = AviatorGold,
                letterSpacing = 2.sp
              )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.Default.MilitaryTech,
              contentDescription = null,
              tint = AviatorGold,
              modifier = Modifier.size(16.dp)
            )
          }

          Text(
            text = "Enter your betting site User ID below to verify and unlock signal access.",
            style = MaterialTheme.typography.bodySmall.copy(
              color = TextSecondary,
              textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(top = 8.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // STEP 1: Select Betting Site
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(bottom = 8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(AviatorGold),
            contentAlignment = Alignment.Center
          ) {
            Text("1", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "SELECT BETTING SITE",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary,
              letterSpacing = 0.5.sp
            )
          )
        }

        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          BettingSite.SUPPORTED_SITES.forEach { site ->
            val isSelected = site.id == selectedSite.id
            val siteColor = Color(site.primaryColorHex)

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) siteColor.copy(alpha = 0.25f) else AviatorCardBg)
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) siteColor else AviatorCardBorder,
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable { onSiteSelect(site) }
                .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) siteColor else TextMuted)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = site.name,
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) TextPrimary else TextSecondary
                  )
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // STEP 2: Enter Betting User ID
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(bottom = 8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(AviatorGold),
            contentAlignment = Alignment.Center
          ) {
            Text("2", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "ENTER ${selectedSite.name.uppercase()} USER ID",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary,
              letterSpacing = 0.5.sp
            )
          )
        }

        OutlinedTextField(
          value = userIdInput,
          onValueChange = onUserIdChange,
          placeholder = { Text("e.g. 83910247", color = TextMuted) },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.AccountBalanceWallet,
              contentDescription = null,
              tint = AviatorGold
            )
          },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("user_id_input"),
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AviatorGold,
            unfocusedBorderColor = AviatorCardBorder,
            focusedContainerColor = AviatorSurface,
            unfocusedContainerColor = AviatorSurface,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )

        if (errorMessage != null) {
          Text(
            text = errorMessage,
            color = AviatorRedLight,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
          )
        }

        // Approval Requirement Notice
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AviatorCardBg)
            .padding(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = AviatorOrange,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Note: User ID verification is required to unlock Aviator Signals.",
            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // LOGIN / CONNECT BUTTON
      Button(
        onClick = onLoginClick,
        enabled = !isConnecting,
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp)
          .testTag("login_button"),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = AviatorRedPrimary,
          contentColor = Color.White
        )
      ) {
        if (isConnecting) {
          CircularProgressIndicator(
            color = Color.White,
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.dp
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            "CONNECTING SERVER...",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            letterSpacing = 1.sp
          )
        } else {
          Icon(
            imageVector = Icons.Default.Radar,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
          )
          Text(
            "LOGIN & VERIFY ID",
            fontWeight = FontWeight.Black,
            fontSize = 16.sp,
            letterSpacing = 1.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Bottom Server Status & Fluctuating Online Users Display
      Card(
        colors = CardDefaults.cardColors(containerColor = AviatorCardBg),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AviatorCardBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(AviatorGreen)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "SERVER: ONLINE (0.04ms)",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(AviatorGreen.copy(alpha = 0.15f))
              .border(1.dp, AviatorGreen, RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "⚡ $onlineUsers USERS ONLINE",
              fontSize = 11.sp,
              fontWeight = FontWeight.Black,
              color = AviatorGreen
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))
    }
  }
}

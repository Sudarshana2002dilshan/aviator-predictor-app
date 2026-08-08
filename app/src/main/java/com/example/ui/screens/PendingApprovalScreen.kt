package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun PendingApprovalScreen(
  userId: String,
  site: String,
  onlineUsers: Int = 84,
  onRefreshStatus: () -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.95f,
    targetValue = 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseScale"
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(AviatorDarkBg)
      .padding(20.dp)
      .testTag("pending_approval_screen"),
    contentAlignment = Alignment.Center
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      border = androidx.compose.foundation.BorderStroke(1.5.dp, AviatorOrange.copy(alpha = 0.6f)),
      colors = CardDefaults.cardColors(containerColor = AviatorSurface)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Hourglass Pulsing Lock Icon
        Box(
          modifier = Modifier
            .size(90.dp)
            .scale(pulseScale)
            .clip(CircleShape)
            .background(AviatorOrange.copy(alpha = 0.15f))
            .border(2.dp, AviatorOrange, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.HourglassEmpty,
            contentDescription = "Pending Approval",
            tint = AviatorOrange,
            modifier = Modifier.size(48.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "AWAITING ADMIN APPROVAL",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Black,
            color = AviatorOrange,
            letterSpacing = 1.sp
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Account Details Summary Badge
        Card(
          colors = CardDefaults.cardColors(containerColor = AviatorCardBg),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, AviatorCardBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Betting Platform:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
              Text(site, style = MaterialTheme.typography.bodySmall.copy(color = AviatorGold, fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Your User ID:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
              Text(userId, style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Signal Status:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
              Text("LOCKED (Pending)", style = MaterialTheme.typography.bodySmall.copy(color = AviatorOrange, fontWeight = FontWeight.Bold))
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Your User ID has been submitted to the Aviator Signal Administrator. Once approved, the high-accuracy prediction engine will activate automatically.",
          style = MaterialTheme.typography.bodyMedium.copy(
            color = TextSecondary,
            textAlign = TextAlign.Center
          )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // REFRESH / CHECK STATUS BUTTON
        Button(
          onClick = onRefreshStatus,
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("refresh_status_button"),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = AviatorGold,
            contentColor = Color.Black
          )
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
          )
          Text(
            "CHECK APPROVAL STATUS",
            fontWeight = FontWeight.Black,
            fontSize = 14.sp
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Live Online Users Status Badge
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AviatorCardBg)
            .border(1.dp, AviatorCardBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
          contentAlignment = Alignment.Center
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
              text = "⚡ $onlineUsers USERS ONLINE NOW",
              style = MaterialTheme.typography.labelSmall.copy(
                color = AviatorGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            )
          }
        }
      }
    }
  }
}
